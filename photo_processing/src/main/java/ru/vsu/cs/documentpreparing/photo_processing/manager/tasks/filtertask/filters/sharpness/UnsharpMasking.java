/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.sharpness;

import static org.opencv.core.Core.addWeighted;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class UnsharpMasking extends ImageFilter {

    private final Size maskSize;
    private final double sigma, summCoeff;
    
    @Override
    public Mat filterImage(Mat image) {
        Mat gaussianBlur = new Mat();
        GaussianBlur(image, gaussianBlur, maskSize, sigma, sigma);
        addWeighted(image, 1 + summCoeff, gaussianBlur, -summCoeff, 0, image);
        return image;
    }
    
    public UnsharpMasking(Integer size, Double sigma, Double k){
        this(size.intValue(), sigma.doubleValue(), k.doubleValue());
    }
    
    public UnsharpMasking(int gaussianSize, double sigma, double summCoeff){
        this.maskSize = new Size(gaussianSize, gaussianSize);
        this.sigma = sigma;
        this.summCoeff = summCoeff;
    }
}
