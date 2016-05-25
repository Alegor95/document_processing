/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;

/**
 *
 * @author aleksandr
 */
public class ImageProcessingWorker {
    
    private final Logger log = Logger
            .getLogger(getClass().getCanonicalName());
    
    private static long nextId = 0;
    
    public enum Status {
        BINDED,
        WORKING,
        WAITING,
        STOPPED
    }
    
    private long id;
    
    private Status status = Status.WAITING;
    
    private ImageProcessingTask task;
    
    private WorkerThread thread;
    
    private List<Runnable> finishListeners;
    
    private ImageProcessingManager manager;
    
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
    
    protected WorkerThread getThread(){
        return this.thread;
    }
    
    public synchronized void bindTask(ImageProcessingTask newTask){
        if (!Status.WAITING.equals(this.getStatus())){
            throw new WorkerAlreadyBindedException();
        }
        this.setStatus(Status.BINDED);
        this.setTask(newTask);
        log.info(String.format("Worker %d binded to task %d",
                this.getId(), this.getTask().getId()));
    }
    
    public void processTask(){
        synchronized (this){
            if (this.getThread().isAlive()){
                this.notify();
            }
        }
    }
    
    protected List<Runnable> getFinishListeners(){
        return this.finishListeners;
    }
    
    public void addFinishListener(Runnable listener){
        this.getFinishListeners().add(listener);
    }
    
    public ImageProcessingManager getManager(){
        return this.manager;
    }
    
    public void stop(){
        this.setStatus(Status.STOPPED);
        log.info(String.format("Worker %d is stopped", this.getId()));
    }
    
    public ImageProcessingWorker(ImageProcessingManager manager){
        this.id = nextId++;
        this.status = Status.WAITING;
        this.finishListeners = new LinkedList<>();
        this.manager = manager;
        this.thread = new WorkerThread(this);
        this.thread.start();
    }
    
    @PreDestroy
    public void destroy(){
        this.getThread().interrupt();
    }
    
    protected class WorkerThread extends Thread {
        
        protected final ImageProcessingWorker parent;
        
        private boolean finished = false;
        
        public boolean isFinished(){
            return finished;
        }
        
        public void setFinished(boolean value){
            this.finished = value;
        }
        
        @Override
        public void run(){
            while (!Status.STOPPED.equals(parent.getStatus())){
                synchronized (this.parent){                
                    try {
                        while (!Status.BINDED.equals(parent.getStatus())){
                            this.parent.wait();
                        }
                        parent.setStatus(Status.WORKING);
                        try {
                            log.info(String.format(
                                    "Worker %d in thread %d start task %d",
                                    parent.getId(), this.getId(), 
                                    parent.getTask().getId()));
                            parent.getTask().taskAction();
                            log.info(String.format(
                                    "Worker %d in thread %d finish task %d",
                                    parent.getId(), this.getId(), 
                                    parent.getTask().getId()));
                        } catch (Exception e){
                            log.severe(String.format(
                                    "Worker %d have failed with task %d, error: %s",
                                    parent.getId(), parent.getTask().getId(),
                                    e.getClass().toString()+ ":" + e.getMessage()
                            ));
                        } finally {
                            parent.setStatus(Status.WAITING);
                            parent.setTask(null);
                            //Alert listeners
                            for (Runnable listener:parent.getFinishListeners()){
                                listener.run();
                            }
                        }                    
                    } catch (InterruptedException e){
                        log.severe(String.format(
                                "Worker %d has interrupted thread",
                                parent.getId()
                        ));
                    }
                }
            }
        }
        
        public WorkerThread(ImageProcessingWorker parent){
            this.parent = parent;
        }
    }
    
    public class WorkerAlreadyBindedException extends RuntimeException{
        public WorkerAlreadyBindedException(){
            super(String.format("Worker %d is already working", getId()));
        }
    }
    
}
