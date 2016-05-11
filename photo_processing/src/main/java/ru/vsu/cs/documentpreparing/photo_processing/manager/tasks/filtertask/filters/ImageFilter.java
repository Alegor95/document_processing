/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import org.bytedeco.javacpp.opencv_core.IplImage;

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
    public abstract IplImage filterImage(IplImage image);
}
