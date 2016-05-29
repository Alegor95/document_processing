/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.application_example.manager;

import ru.vsu.cs.documentpreparing.photo_processing.manager.ImageProcessingManager;

/**
 *
 * @author aleksandr
 */
public class ManagerFactory {
    
    private int defaultWorkerCount;
    
    private int defaultMaxTaskCount;
    
    /**
     * Get default worker count
     * @return 
     */
    public int getDefaultWorkerCount(){
        return defaultWorkerCount;
    }
    
    /**
     * Set default worker count
     * @param workerCount 
     */
    public void setDefaultWorkerCount(int workerCount){
        this.defaultWorkerCount = workerCount;
    }
    
    /**
     * Get default maximal tasks count.
     * If value = -1, task count is unlimited
     * @return 
     */
    public int getDefaultMaxTaskCount(){
        return defaultMaxTaskCount;
    }
    
    /**
     * Set default maximal tasks count.
     * If value = -1, task count is unlimited
     * @param value 
     */
    public void setDefaultMaxTaskCount(int value){
        this.defaultMaxTaskCount = value;
    }

    /**
     * Create instance of {@link ImageProcessingManager}
     * with default factory settings
     * @return {@link ImageProcessingManager}
     */
    public ImageProcessingManager createManager(){
        return new ImageProcessingManager(
                this.getDefaultWorkerCount(),
                this.getDefaultMaxTaskCount()
        );
    }
    
    //Singleton methods
    private final static ManagerFactory instance = new ManagerFactory();
    
    /**
     * Get manger factory instance
     * @return 
     */
    public static ManagerFactory getInstance(){
        return instance;
    }
    
    private ManagerFactory(){
        this.defaultWorkerCount = 4;
        this.defaultMaxTaskCount = -1;
    }
}
