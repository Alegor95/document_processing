/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.noise;

import org.opencv.core.Mat;
import static org.opencv.imgproc.Imgproc.bilateralFilter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class BilateralNoiseFilter extends ImageFilter {

    private int aprtureSize;
    private double colorDeviation, spaceDeviation;
    
    @Override
    public Mat filterImage(Mat image) {
        Mat result = new Mat(image.rows(), image.cols(), image.type());
        bilateralFilter(image, result,
                aprtureSize, colorDeviation, spaceDeviation);
        return result;
    }
    
    public BilateralNoiseFilter(int size,
            double colorDeviation, double spaceDeviation){
        this.aprtureSize = size;
        this.colorDeviation = colorDeviation;
        this.spaceDeviation = spaceDeviation;
    }
    
}
