/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import java.util.Queue;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;

/**
 *
 * @author aleksandr
 */
public class ImageProcessingManager {
    
    private final Logger log = Logger
            .getLogger(this.getClass().getCanonicalName());
    /**
     * 
     */
    
    /**
     * Worker count.
     */
    private final int workerCount;
    /**
     * Maximum task count.
     * If max task count equals -1, all tasks will be accepted
     */
    private final int maxTaskCount; 
    
    /**
     * Default worker count.
     */
    private static final int DEFAULT_WORKER_COUNT = 4;
    /**
     * Default maximum task count
     */
    private static final int DEFAULT_MAX_TASK_COUNT = -1;
    
    /**
     * List of workers.
     */
    private List<ImageProcessingWorker> workersList;
    /**
     * Queue of tasks
     */
    private Queue<ImageProcessingTask> tasksQueue;
    /**
     * Listeners for processing finish
     */
    private List<Runnable> finishListeners;
    
    /**
     * Get free worker for worker's list
     * @return 
     */
    private ImageProcessingWorker getFreeWorker(){
        ImageProcessingWorker currentWorker = null;
        for (ImageProcessingWorker worker : workersList){
            if (ImageProcessingWorker.Status.WAITING
                    .equals(worker.getStatus())){
                currentWorker = worker;
                break;
            }
        }
        return currentWorker;
    }
    
    public void addFinishListener(Runnable listener){
        this.finishListeners.add(listener);
    }
    
    /**
     * Set task to worker.
     * Method is synchronized to avoid race condition
     * @param task
     */
    public void assignmentTask(final ImageProcessingTask task){
        log.fine(String.format("Start task %d assigment", task.getId()));
        final ImageProcessingWorker currentWorker;
        //Looking for worker
        synchronized (this) {
            currentWorker = getFreeWorker();
            if (currentWorker == null){
                log.info(String.format("Task %d was added to queue", task.getId()));
                if (maxTaskCount != -1 &&
                        tasksQueue.size() >= maxTaskCount) {
                    throw new TaskQueueOverflowException();
                }
                this.tasksQueue.add(task);
                return;
            }
            //Process task in seprate thread
            currentWorker.bindTask(task);
        }
    }
    
    /**
     * Assign collection of tasks
     * @param tasks 
     */
    public void assignmentTasks(Collection<? extends ImageProcessingTask> tasks){
        for (ImageProcessingTask task:tasks){
            this.assignmentTask(task);
        }
    }
    
    /**
     * Run all workers
     */
    public void run(){
        for (ImageProcessingWorker worker:this.workersList){
            if (ImageProcessingWorker.Status.BINDED.equals(worker.getStatus())){
                worker.processTask();                
            }
        }
    }
    
    /**
     * Lookup task for worker
     * @param worker 
     */
    public void recieveTask(ImageProcessingWorker worker){
        final ImageProcessingTask newTask;
        synchronized (this){
            if (tasksQueue.size() > 0){
                newTask = tasksQueue.poll();
                worker.bindTask(newTask);
            } else {
                //No tasks - stop worker
                worker.stop();
                checkFinish();
            }
        }
        worker.processTask();
    }
    
    private void checkFinish(){
        for (ImageProcessingWorker worker:this.workersList){
            if (!ImageProcessingWorker.Status.STOPPED
                    .equals(worker.getStatus())){
                return;
            }
        }
        //All workers finished - stop manager
        for(Runnable listener:this.finishListeners){
            listener.run();
        }
    }
    
    public ImageProcessingManager(){
        this(DEFAULT_WORKER_COUNT, DEFAULT_MAX_TASK_COUNT);
    }
    
    public ImageProcessingManager(int workerCount, int maxTaskCount){
        this.workerCount = workerCount;
        workersList = new ArrayList<>(this.workerCount);
        //Fill up workers
        for (int i = 0; i < this.workerCount; i++){
            ImageProcessingWorker newWorker = new ImageProcessingWorker(this);
            workersList.add(newWorker);
        }
        //Create task queue
        this.maxTaskCount = maxTaskCount;
        tasksQueue = new LinkedList<>();
        this.finishListeners = new LinkedList<>();
    }
    
    @PreDestroy
    public void destroy(){
        for (ImageProcessingWorker worker:this.workersList) {
            worker.destroy();
        }
    }
    
    public class TaskQueueOverflowException extends RuntimeException {
        
        public TaskQueueOverflowException(){
            super(String.format(
                    "Task count reached maximum size - %d items",
                    maxTaskCount
            ));
        }
    }
}
