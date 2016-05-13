/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import org.opencv.core.Mat;

/**
 *
 * @author aleksandr
 */
public abstract class ImageProcessingTask {
    
    /**
     * Task status class
     */
    public enum Status {
        WAITING,
        WORKING,
        FINISHED
    }
    
    /**
     * Common id sequence
     */
    private static long nextId = 0;    
    /**
     * Task Id
     */
    private long id;    
    /**
     * Task status
     */
    protected Status status = Status.WAITING;    
    /**
     * Get task id
     * @return 
     */
    public long getId(){
        return id;
    }    
    /**
     * Finish listeners list
     */
    private List<ActionListener> finishListeners = new LinkedList<>();
    
    
    /**
     * Initial and processed image
     */
    protected Mat initialImage, image;
    /**
     * Add listener on task finish
     * @param listener 
     */
    public void addFinishListener(ActionListener listener){
        finishListeners.add(listener);
    }
    
    /**
     * Get task status
     * @return 
     */
    public Status getStatus(){
        return status;
    }
    
    /**
     * Set task status
     * @param newStatus 
     */
    protected void setStatus(Status newStatus){
        this.status = newStatus;
    }
    
    /**
     * Get initial image
     * @return {@link IplImage}
     */
    public Mat getInitialImage(){
        return initialImage;
    }
    
    /**
     * Get processed image
     * @return {@link IplImage}
     */
    public Mat getImage(){
        return image;
    }
    
    public abstract void taskActionImpl();
    
    public void taskAction(){
        this.setStatus(Status.WORKING);
        this.taskActionImpl();
        this.setStatus(Status.FINISHED);
        //Process all hooks
        ActionEvent e = new ActionEvent(this, (int)id, "finish");
        for (ActionListener listener:this.finishListeners){
            listener.actionPerformed(e);
        }
    }
    
    public abstract float getProgress();
    
    public ImageProcessingTask(){
        this.id = nextId++;
    }
}
