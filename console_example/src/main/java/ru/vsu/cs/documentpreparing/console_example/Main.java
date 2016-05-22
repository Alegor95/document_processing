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
import ru.vsu.cs.documentpreparing.photo_processing.manager.ImageProcessingManager; 
import ru.vsu.cs.documentpreparing.photo_processing.manager.ImageProcessingTask;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.FilterImageProcessingTask;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.*;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.light.AutoRetinexFilter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.light.CLAHEFilter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.light.RetinexFilter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.noise.BilateralNoiseFilter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.sharpness.ConvolutionSharpnessFilter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.sharpness.UnsharpMasking;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.support.ThresholdResizeFilter;

/**
 *
 * @author aleksandr
 */
public class Main {
    
    static ImageProcessingManager manager;
    
    private static final String HOME_URI = "/home/aleksandr/";
    private static final String INPUT_URI = "input/";
    private static final String OUTPUT_URI = "output/";
    
    private static int MAX_SIZE = 1000;
    
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    /**
     * 
     * @param args 
     */
    public static void main(String[] args){
        //Create manager
        manager = new ImageProcessingManager(4, -1);
        //Generate filters list
        List<ImageFilter> filtersList = new LinkedList<>();
        //Resize image, if it more than threshold
        filtersList.add(new ThresholdResizeFilter(1000));
        //Noise filtering
        filtersList.add(new BilateralNoiseFilter(3, 196, 3));
        //Histogram filtering
        filtersList.add(new AutoRetinexFilter());
        //Increase sharpness
        filtersList.add(new UnsharpMasking(3, 128, 3));
        //filtersList.add(new ConvolutionSharpnessFilter(5));
        //Tasks list
        List<ImageProcessingTask> tasksList = new LinkedList<>();
        //Process filesизо
        File inputDir = Paths.get(HOME_URI, INPUT_URI).toFile();
        for (File file:inputDir.listFiles()){
            if (!file.isFile()) continue;
            Mat image = imread(file.getAbsolutePath());
            //Generate task
            FilterImageProcessingTask task = 
                    new FilterImageProcessingTask(image, filtersList);
            //Add finish listener
            task.addFinishListener((ActionEvent e) -> {
                //Try to get image
                Object source = e.getSource();
                if (source instanceof ImageProcessingTask) {
                    ImageProcessingTask task1 = (ImageProcessingTask)source;
                    imwrite(HOME_URI+OUTPUT_URI+file.getName(), task1.getImage());
                }
            });
            tasksList.add(task);
            //Add to manager
            manager.addTask(task);
        }        
    }
}
