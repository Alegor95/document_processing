/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.sharpness;

import static org.opencv.core.CvType.CV_32FC1;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import static org.opencv.imgproc.Imgproc.filter2D;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class ConvolutionSharpnessFilter extends ImageFilter {

    final Mat kernel;
    final Point anchor;
    final int size = 3;
    
    @Override
    public Mat filterImage(Mat image) {
        filter2D(image, image, -1, kernel, anchor, 0);
        return image;
    }
    
    public ConvolutionSharpnessFilter( double centralValue){
        double border = (centralValue - 1)/4;
        double[][] kernelArr = new double[][]{
            { 0,-border, 0},
            {-border, centralValue,-border},
            { 0,-border, 0} 
        };
        kernel = new Mat(size, size, CV_32FC1);
        //Set up kernel
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                kernel.put(i, j, kernelArr[i][j]);
            }
        }
        this.anchor = new Point(-1, -1);
    }
    
}
