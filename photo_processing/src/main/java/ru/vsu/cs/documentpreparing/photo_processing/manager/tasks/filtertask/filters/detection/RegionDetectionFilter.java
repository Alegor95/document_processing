/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.detection;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.adaptiveThreshold;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class RegionDetectionFilter extends ImageFilter {

    /**
    * Maximal image size
    */
    private final int processSize = 300;
    
    /**
     * Gaussian blur properties
     */
    private final int blurSize = 5;
    private final double sigmaX = 10;
    
    private Mat getBestChannel(Mat image){
        //Count brightness channel
        Mat brightness = new Mat(), result;
        int code = 0, channelCnt = 0;
        switch(image.channels()){
            case 4: {
                code = Imgproc.COLOR_BGRA2GRAY;
                channelCnt = 3;
            } break;
            default: {
                code = Imgproc.COLOR_BGRA2GRAY;
                channelCnt = image.channels();
            } break;
        }
        Imgproc.cvtColor(image, brightness, code);
        MatOfDouble brightnessMean = new MatOfDouble(), brightnessDev = new MatOfDouble();
        Core.meanStdDev(brightness, brightnessMean, brightnessDev);
        result = brightness;
        //Check channels
        List<Mat> channels = new LinkedList<>();
        Core.split(image, channels);
        for (int i = 0; i < channelCnt; i++){
            Mat channel = channels.get(i);
            //Count mean and dev
            MatOfDouble mean = new MatOfDouble(), dev = new MatOfDouble();
            Core.meanStdDev(channel, mean, dev);
            if (dev.get(0, 0)[0] > brightnessDev.get(0, 0)[0]){
                result = channel;
                break;
            }
        }
        return result;
    }
    
    @Override
    public Mat filterImage(Mat image) {
        //Get most informative channel
        Mat channel = getBestChannel(image);
        //Resize and blur channel
        double resizeK = 1;
        while (channel.cols() > processSize || channel.rows() > processSize){
            Size newSize = new Size(channel.cols()/2, channel.rows()/2);
            Imgproc.resize(channel, channel, newSize);
            resizeK *= 2;
        }
        //Blur and binarization
        Imgproc.GaussianBlur(channel, channel,
                new Size(blurSize, blurSize), this.sigmaX);
        Imgproc.threshold(channel, channel, 0, 255, Imgproc.THRESH_OTSU);
        //Canny detector
        Imgproc.Canny(channel, channel, 1, 2);
        //Loocking for maximal contour
        Mat lines = new Mat();
        Imgproc.HoughLines(channel, lines,
                5, 10*Math.PI/2,
                Math.min(channel.rows(), channel.cols())/8);
        //Draw lines
        //channel.convertTo(channel, CvType.CV_8UC3);
        for( int i = 0; i < lines.rows(); i++ )
        {
            double rho = lines.get(i, 0)[0], theta = lines.get(i, 0)[1];
            Point pt1 = new Point(), pt2 = new Point();
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            pt1.x = Math.round(x0 + 1000*(-b));
            pt1.y = Math.round(y0 + 1000*(a));
            pt2.x = Math.round(x0 - 1000*(-b));
            pt2.y = Math.round(y0 - 1000*(a));
            Imgproc.line( channel, pt1, pt2, new Scalar(128, 255, 128), 1);
        }
        return channel;
    }
    
}
