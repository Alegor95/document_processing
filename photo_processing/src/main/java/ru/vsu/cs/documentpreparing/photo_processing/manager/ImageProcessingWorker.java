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
public class ImageProcessingWorker {
    
    public enum Status {
        WORKING,
        WAITING
    }
    
    private Status status;
    
    public Status getStatus(){
        return status;
    }
    
    protected void setStatus(Status newStatus){
        this.status = newStatus;
    }
    
}
