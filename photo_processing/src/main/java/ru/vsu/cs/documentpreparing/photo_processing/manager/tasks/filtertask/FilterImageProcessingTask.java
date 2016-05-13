/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask;

import java.util.Iterator;
import java.util.List;
import org.opencv.core.Mat;
import ru.vsu.cs.documentpreparing.photo_processing.manager.ImageProcessingTask;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class FilterImageProcessingTask extends ImageProcessingTask {
    
    /**
     * List of filters in task
     */
    List<ImageFilter> filters;
    
    /**
     * Current progress
     */
    float progress = 0;
    /**
     * Progress step for each filter
     */
    final float progressStep;

    @Override
    public void taskActionImpl() {
        //Perform chain filtering of image
        for (Iterator<ImageFilter> fIt = filters.iterator(); fIt.hasNext();){
            ImageFilter filter = fIt.next();
            image = filter.filterImage(image);
            progress += this.progressStep;
        }
    }

    @Override
    public float getProgress() {
        return progress;
    }
    
    public FilterImageProcessingTask(Mat img, List<ImageFilter> filters){
        this.initialImage = img;
        this.image = img.clone();
        this.filters = filters;
        this.progressStep = 1.f/filters.size();
    }
}
