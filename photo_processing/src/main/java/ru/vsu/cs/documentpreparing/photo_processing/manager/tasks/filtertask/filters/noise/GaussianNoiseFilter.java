/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.noise;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class GaussianNoiseFilter extends ImageFilter {

    Size apertureSize;
    
    @Override
    public Mat filterImage(Mat image) {
        GaussianBlur(image, image, apertureSize, 0, 0);
        return image;
    }
    
    public GaussianNoiseFilter(int size){
        if (size % 2 == 0) size++;
        this.apertureSize = new Size(size, size);
    }
    
}
