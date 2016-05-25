/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.photo_processing.manager;


import java.util.ArrayList;
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
     * Add task to queue
     * @param newTask 
     */
    public void addTask(ImageProcessingTask newTask){
        if (maxTaskCount != -1 &&
                tasksQueue.size() >= maxTaskCount) {
            throw new TaskQueueOverflowException();
        }
        log.fine("New task added");
        tasksQueue.add(newTask);
        //assigment task to workers
        log.fine("Assign task");
        assignmentTask();
    }
    
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
    
    /**
     * Set task to worker.
     * Method is synchronized to avoid race condition
     */
    protected void assignmentTask(){
        log.fine("Start task assigment");
        final ImageProcessingWorker currentWorker;        
        final ImageProcessingTask currentTask;
        //Looking for worker
        synchronized (this) {
            currentWorker = getFreeWorker();
            if (currentWorker == null){
                log.fine("No free workers");
                return;
            }
            //Looking for task
            if (tasksQueue.isEmpty()){
                log.fine("Task queue empty");
                //If have no tasks for workers - stop free workers
                currentWorker.stop();
                return;
            }
            currentTask = tasksQueue.poll();
            //Process task in seprate thread
            currentWorker.bindTask(currentTask);
        }        
        currentWorker.processTask();
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
            newWorker.addFinishListener(() -> {
                newWorker.getManager().assignmentTask();
            });
            workersList.add(newWorker);
        }
        //Create task queue
        this.maxTaskCount = maxTaskCount;
        tasksQueue = new LinkedList<>();
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
