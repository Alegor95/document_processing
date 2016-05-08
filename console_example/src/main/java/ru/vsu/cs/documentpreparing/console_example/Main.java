/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.console_example;

import ru.vsu.cs.documentpreparing.photo_processing.manager.ImageProcessingManager;
import ru.vsu.cs.documentpreparing.photo_processing.manager.ImageProcessingTask;

/**
 *
 * @author aleksandr
 */
public class Main {
    
    static ImageProcessingManager manager;
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args){
        int taskCount = 10;
        manager = new ImageProcessingManager(2, taskCount);
        for (int i = 0; i < taskCount; i++){
            manager.addTask(new ImageProcessingTask());
        }
    }
}
