/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters;

import org.opencv.core.Mat;

/**
 *
 * @author aleksandr
 */
public class AddGaussianNoiseFilter extends ImageFilter{

    @Override
    public Mat filterImage(Mat image) {
        /*IplImage gaussianImage = IplImage.create(image.width(), image.height(),
                image.depth(), image.nChannels());
        Mat gaussianNoise = new Mat();*/
        return image;
    }
    
}
