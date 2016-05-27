/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.light;

import java.util.LinkedList;
import java.util.List;
import static org.opencv.core.Core.merge;
import static org.opencv.core.Core.split;
import org.opencv.core.Mat;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2YCrCb;
import static org.opencv.imgproc.Imgproc.COLOR_YCrCb2BGR;
import static org.opencv.imgproc.Imgproc.cvtColor;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.equalizeHist;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 *
 * @author aleksandr
 */
public class HistEqualizationFilter extends ImageFilter{

    private double sigma;
    
    private double gaussian(double x, double y, double sigma){
        return Math.exp(-(x*x+y*y)/(2*sigma*sigma));
    }
    
    @Override
    public Mat filterImage(Mat image) {
        double[] colors;
        //Format to YCrCb color scheme, and process only brightness
        Mat ycrcb = new Mat();
        cvtColor(image,ycrcb, COLOR_BGR2YCrCb);
        List<Mat> channels = new LinkedList<>();
        split(ycrcb,channels);
        //Hist
        equalizeHist(channels.get(0), channels.get(0));
        //Convert back
        Mat result = new Mat();
        merge(channels,ycrcb);
        cvtColor(ycrcb,result,COLOR_YCrCb2BGR);
        return result;
    }
    
    public HistEqualizationFilter(Double sigma){
        this(sigma.doubleValue());
    }
    
    public HistEqualizationFilter(double sigma){
        this.sigma = sigma;
    }
    
}
