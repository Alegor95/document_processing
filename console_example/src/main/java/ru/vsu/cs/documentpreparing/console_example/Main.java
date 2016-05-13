/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.console_example;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import ru.vsu.cs.documentpreparing.photo_processing.manager.ImageProcessingManager; 
import ru.vsu.cs.documentpreparing.photo_processing.manager.ImageProcessingTask;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.FilterImageProcessingTask;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.*;

/**
 *
 * @author aleksandr
 */
public class Main {
    
    static ImageProcessingManager manager;
    
    private static final String HOME_URI = "/home/aleksandr/";
    private static final String INPUT_URI = "input/";
    private static final String OUTPUT_URI = "output/";
    
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    /**
     * 
     * @param args 
     */
    public static void main(String[] args){
        //
        manager = new ImageProcessingManager();
        //Generate filters list
        List<ImageFilter> filtersList = new LinkedList<>();
        filtersList.add(new RotareFilter(-90));
        //Tasks list
        List<ImageProcessingTask> tasksList = new LinkedList<>();
        //Process files
        File inputDir = Paths.get(HOME_URI, INPUT_URI).toFile();
        for (File file:inputDir.listFiles()){            
            Mat image = Highgui.imread(file.getAbsolutePath());
            //Generate task
            FilterImageProcessingTask task = 
                    new FilterImageProcessingTask(image, filtersList);
            //Add finish listener
            task.addFinishListener((ActionEvent e) -> {
                //Try to get image
                Object source = e.getSource();
                if (source instanceof ImageProcessingTask) {
                    ImageProcessingTask task1 = (ImageProcessingTask)source;
                    Highgui.imwrite(HOME_URI+OUTPUT_URI+file.getName(), task1.getImage());
                }
            });
            tasksList.add(task);
            //Add to manager
            manager.addTask(task);
        }        
    }
}
