/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.support;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class ThresholdResizeFilter extends ImageFilter {

    private int threshold;
    
    @Override
    public Mat filterImage(Mat image) {
        //Count size
        double k = image.cols()/(double)image.rows();
        if (image.cols() > image.rows()){
            if (image.cols() > threshold){
                Size size = new Size(threshold, threshold/k);
                //Resize
                Imgproc.resize(image, image, size);
            }
        } else {
            if (image.rows() > threshold){
                Size size = new Size(threshold*k, threshold);
                //Resize
                Imgproc.resize(image, image, size);
            }
        }
        return image;
    }
    
    public ThresholdResizeFilter(int threshold){
        this.threshold = threshold;
    }
    
}
