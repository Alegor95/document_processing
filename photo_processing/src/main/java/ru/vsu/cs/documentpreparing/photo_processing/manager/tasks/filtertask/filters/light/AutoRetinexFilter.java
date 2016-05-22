/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.light;

import org.opencv.core.Mat;

/**
 *
 * @author aleksandr
 */
public class AutoRetinexFilter extends RetinexFilter{
    
    private int adaptiveLevel;
    
    @Override
    protected int getLevel(){
        return adaptiveLevel;
    }
    
    protected void setAdaptiveLevel(int value){
        this.adaptiveLevel = value;
    }
    
    @Override
    public Mat filterImage(Mat image){
        this.setAdaptiveLevel((image.cols() + image.rows())/200);
        return super.filterImage(image);
    }
    
    public AutoRetinexFilter() {
        super(0);
    }
    
}
