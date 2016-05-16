/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.noise;

import org.opencv.core.Mat;
import static org.opencv.imgproc.Imgproc.medianBlur;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class MedianNoiseFilter extends ImageFilter{

    int apertureSize;
    
    @Override
    public Mat filterImage(Mat image) {
        medianBlur(image, image, apertureSize);
        return image;
    }
    
    public MedianNoiseFilter(int size){
        this.apertureSize = size;
    }
    
}
