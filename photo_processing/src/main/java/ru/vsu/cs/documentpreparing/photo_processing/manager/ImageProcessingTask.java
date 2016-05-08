/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager;

/**
 *
 * @author aleksandr
 */
public class ImageProcessingTask {
    /**
     * Common id sequence
     */
    private static long nextId = 0;
    
    private long id;
    
    public long getId(){
        return id;
    }
    
    public ImageProcessingTask(){
        this.id = nextId++;
    }
}
