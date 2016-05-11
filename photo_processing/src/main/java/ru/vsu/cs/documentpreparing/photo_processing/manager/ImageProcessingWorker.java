/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aleksandr
 */
public class ImageProcessingWorker {
    
    private final Logger log = Logger
            .getLogger(getClass().getCanonicalName());
    
    private static long nextId = 0;
    
    public enum Status {
        WORKING,
        WAITING
    }
    
    private long id;
    
    private Status status = Status.WAITING;
    
    private ImageProcessingTask task;
    
    public long getId(){
        return id;
    }
    
    protected void setId(long newId){
        this.id = newId;
    }
    
    public Status getStatus(){
        return status;
    }
    
    protected void setStatus(Status newStatus){
        this.status = newStatus;
    }
    
    protected ImageProcessingTask getTask(){
        return task;
    }
    
    protected void setTask(ImageProcessingTask newTask){
        this.task = newTask;
    }
    
    public synchronized void bindTask(ImageProcessingTask newTask){
        if (Status.WORKING.equals(this.getStatus())){
            throw new WorkerAlreadyBindedException();
        }
        this.setStatus(Status.WORKING);
        this.setTask(newTask);
        log.info(String.format("Worker %d binded to task %d",
                this.getId(), this.getTask().getId()));
    }
    
    public void processTask(){
        log.info(String.format("Worker %d start task %d",
                this.getId(), this.getTask().getId()));
        this.getTask().taskAction();
        log.info(String.format("Worker %d finish task %d",
                this.getId(), this.getTask().getId()));
        //Task finished
        this.setStatus(Status.WAITING);
    }
    
    public ImageProcessingWorker(){
        this.id = nextId++;
    }
    
    public class WorkerAlreadyBindedException extends RuntimeException{
        public WorkerAlreadyBindedException(){
            super(String.format("Worker # %d is already working", getId()));
        }
    }
    
}
