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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    protected synchronized void assignmentTask(){
        log.fine("Start task assigment");
        //Looking for worker
        final ImageProcessingWorker currentWorker = getFreeWorker();
        if (currentWorker == null){
            log.fine("No free workers");
            return;
        }
        //Looking for task
        final ImageProcessingTask currentTask;
        if (tasksQueue.isEmpty()){
            log.fine("Task queue empty");
            return; //No work - no assignments
        }
        currentTask = tasksQueue.poll();
        //Process task in seprate thread
        currentWorker.bindTask(currentTask);
        Thread workerThread = new Thread(){            
            @Override
            public void run(){
                currentWorker.processTask();
                //Assign new task, when this will be complited                
                assignmentTask();
            }
        };
        log.fine("Successfull assignment");
        workerThread.start();
    }
    
    public ImageProcessingManager(){
        this(DEFAULT_WORKER_COUNT, DEFAULT_MAX_TASK_COUNT, true);
    }
    
    public ImageProcessingManager(int workerCount, int maxTaskCount,
            boolean async){
        this.workerCount = workerCount;
        workersList = new ArrayList<>(this.workerCount);
        //Fill up workers
        for (int i = 0; i < this.workerCount; i++){
            workersList.add(new ImageProcessingWorker());
        }
        //Create task queue
        this.maxTaskCount = maxTaskCount;
        tasksQueue = new LinkedList<>();
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
