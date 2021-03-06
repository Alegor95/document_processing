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
public abstract class ImageFilter {
    
    /**
     * Filter image
     * @param image {@link IplImage}
     * @return {@link IplImage} 
     */
    public abstract Mat filterImage(Mat image);
}
