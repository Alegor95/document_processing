/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.support;

import static org.opencv.core.Core.*;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class AddGaussianNoiseFilter extends ImageFilter{

    private final double mean, deviation;
    
    @Override
    public Mat filterImage(Mat image) {
        Mat gaussianNoise = new Mat(image.rows(), image.cols(), image.type());
        randn(gaussianNoise, mean, deviation);
        add(gaussianNoise, new Scalar(-deviation/2), gaussianNoise);
        Mat result = new Mat();
        add(image, gaussianNoise, result);
        return result;
    }
    
    public AddGaussianNoiseFilter(double mean, double deviation){
        this.mean = mean;
        this.deviation = deviation;
    }
}
