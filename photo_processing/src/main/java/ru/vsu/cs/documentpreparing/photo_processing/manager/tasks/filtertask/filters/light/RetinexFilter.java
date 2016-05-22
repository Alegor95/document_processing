/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.light;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Scalar;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class RetinexFilter extends ImageFilter {

    private final int level;
    
    protected int getLevel(){
        return this.level;
    }
    
    public void normalize(Mat src, Mat upd, Mat dst){
        if (src.channels() == 1) dst.assignTo(src);
        //Count mean and deviation
        MatOfDouble meanSrc = new MatOfDouble(), devSrc = new MatOfDouble(),
                meanUpd = new MatOfDouble(), devUpd = new MatOfDouble();
        Core.meanStdDev(upd, meanUpd, devUpd);
        Core.meanStdDev(src, meanSrc, devSrc);
        //Do normalization
        double a = devSrc.get(0, 0)[0] / devUpd.get(0, 0)[0],
                b = meanSrc.get(0, 0)[0] - a * meanUpd.get(0, 0)[0];
        Core.multiply(upd, new Scalar(a), dst);
        Core.add(dst, new Scalar(b), dst);
    }
    
    public void laplassianTreshold(Mat src, Mat dst, int lvl){
        if (src.channels() != 1) dst.assignTo(src);
        float diff, outValue;
        int pixelCnt = src.rows()*src.cols()*src.channels(),
                cols = src.cols(), rows = src.rows();
        float values[] = new float[pixelCnt], result[] = new float[pixelCnt];
        src.get(0, 0, values);
        //Array pointers
        int p_c = 0;
        int p_px = p_c + 1, p_nx = p_c - 1,
                p_py = p_c + src.cols(), p_ny = p_c - src.cols();
        for (int j = 0; j < rows; j++){
            for (int i = 0; i < cols; i++){
                outValue = 0;
                //Positive Y difference
                if (j < rows-1){
                    diff = (values[p_c] - values[p_py]);
                    if (Math.abs(diff) > lvl){
                        outValue += diff;
                    }
                }
                //Negative Y difference
                if (j > 0){
                    diff = (values[p_c] - values[p_ny]);
                    if (Math.abs(diff) > lvl){
                        outValue += diff;
                    }
                }
                //Positive X difference
                if (i < cols-1){
                    diff = (values[p_c] - values[p_px]);
                    if (Math.abs(diff) > lvl){
                        outValue += diff;
                    }
                }
                //Negative X difference
                if (i > 0){
                    diff = (values[p_c] - values[p_nx]);
                    if (Math.abs(diff) > lvl){
                        outValue += diff;
                    }
                }
                result[p_c] = outValue;
                //Increase pointers
                p_nx++; p_px++; p_ny++; p_py++; p_c++;
            }
        }
        dst.put(0, 0, result);        
    }
    
    public void retinexPoissonDct(Mat src, Mat dst){
        int pixelCount = src.cols()*src.rows(),
                rows = src.rows(), cols = src.cols();
        double m = 1/(2.*pixelCount);
        double cosi[] = new double[cols], cosj[] = new double[rows];
        //Count cosinus tables
        for (int i = 0; i < cols; i++) {
            cosi[i] = Math.cos(i*Math.PI/cols);
        }
        for (int j = 0; j < rows; j++) {
            cosj[j] = Math.cos(j*Math.PI/rows);
        }
        //Process data
        float valuesSrc[] = new float[pixelCount],
                resultSrc[] = new float[pixelCount];
        src.get(0, 0, valuesSrc);
        int data_p = 0, cosj_p, cosi_p;
        double k;
        for (cosj_p = 0; cosj_p < rows; cosj_p++){            
            for (cosi_p = 0; cosi_p < cols; cosi_p++){
                if (cosj_p == 0 && cosi_p == 0) {
                    resultSrc[0] = 0;
                } else {
                    k = m / (2 - cosi[cosi_p] - cosj[cosj_p]);
                    resultSrc[data_p] = (float)(valuesSrc[data_p]*k);
                }
                data_p++;
            }
        }
        dst.put(0, 0, resultSrc);
    }
    
    @Override
    public Mat filterImage(Mat image) {
        Mat result = new Mat();
        List<Mat> channels = new LinkedList<>();
        Core.split(image, channels);
        int notAlphaCnt = channels.size() == 1? 1 : 3;
        //Process each channel separate
        for (int i = 0; i < notAlphaCnt; i++){
            Mat channel = channels.get(i);            
            //Convert image to float 64
            int original = channel.type();
            channel.convertTo(channel, CvType.CV_32FC(channel.channels()));
            //
            Mat processedChannel = new Mat();
            channel.copyTo(processedChannel);
            this.laplassianTreshold(channel, processedChannel, this.getLevel());
            //Perform DCT
            Core.dct(processedChannel, processedChannel);
            Core.multiply(processedChannel,
                    new Scalar(4 * Math.sqrt(processedChannel.rows()/2)
                            * Math.sqrt(processedChannel.cols()/2)),
                    processedChannel);
            Core.multiply(processedChannel.row(0),
                    new Scalar(Math.sqrt(2)),
                    processedChannel.row(0));
            Core.multiply(processedChannel.col(0),
                    new Scalar(Math.sqrt(2)),
                    processedChannel.col(0));
            //Retinex
            this.retinexPoissonDct(processedChannel, processedChannel);
            //Reverse transform
            Core.divide(processedChannel,
                    new Scalar(4 * Math.sqrt(processedChannel.rows()/2)
                            * Math.sqrt(processedChannel.cols()/2)),
                    processedChannel);
            Core.divide(processedChannel.row(0),
                    new Scalar(Math.sqrt(2)),
                    processedChannel.row(0));
            Core.divide(processedChannel.col(0),
                    new Scalar(Math.sqrt(2)),
                    processedChannel.col(0));
            Core.idct(processedChannel, processedChannel);
            //Normalization
            this.normalize(channel, processedChannel, channel);
            //Convert back
            channel.convertTo(channel, original);
        }
        Core.merge(channels, result);
        return result;
    }
    
    public RetinexFilter(int level){
        this.level = level;
    }
}
