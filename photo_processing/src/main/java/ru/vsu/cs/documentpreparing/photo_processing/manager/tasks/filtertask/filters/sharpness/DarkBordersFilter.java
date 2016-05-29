/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.sharpness;

import java.util.LinkedList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class DarkBordersFilter extends ImageFilter{

    private final double k;
    
    @Override
    public Mat filterImage(Mat image) {
        Mat borders = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
        List<Mat> channels = new LinkedList<>();
        Core.split(image, channels);
        for (int i = 0; i < 3; i++){
            Imgproc.adaptiveThreshold(channels.get(i), channels.get(i), 255 ,
                    Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY,
                    3, 1);
        }
        Core.merge(channels, image);
        return image;
    }
    
    public DarkBordersFilter(double k){
        this.k = k;
    }
}
