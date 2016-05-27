/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.application_example;

import java.util.List;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.vsu.cs.documentpreparing.application_example.components.main.MainPanel;
import ru.vsu.cs.documentpreparing.application_example.components.settings.FilterSettingsPanel;
import ru.vsu.cs.documentpreparing.application_example.components.settings.ManagerSettingsPanel;
import ru.vsu.cs.documentpreparing.application_example.filters.FiltersRepository;
import ru.vsu.cs.documentpreparing.application_example.filters.factory.FilterFactory;
import ru.vsu.cs.documentpreparing.application_example.filters.factory.Parameter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.noise.BilateralNoiseFilter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.noise.GaussianNoiseFilter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.noise.MedianNoiseFilter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.support.AddGaussianNoiseFilter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.support.ThresholdResizeFilter;

/**
 *
 * @author aleksandr
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Обработка фотографий");
        Group root = new Group();
        Scene scene = new Scene(root, 800, 500, Color.WHITE);

        TabPane tabPane = new TabPane();

        BorderPane borderPane = new BorderPane();
        //Add main panel
        Tab mainTab = new Tab();
        mainTab.setClosable(false);
        mainTab.setText("Обработка");
        mainTab.setContent(new MainPanel());
        tabPane.getTabs().add(mainTab);
        //Add filters settings panel
        Tab filtersSettingsTab = new Tab();
        filtersSettingsTab.setClosable(false);
        filtersSettingsTab.setText("Фильтры");
        filtersSettingsTab.setContent(new FilterSettingsPanel());
        tabPane.getTabs().add(filtersSettingsTab);
        //Add manager setting panel        
        Tab managerSettingsTab = new Tab();
        managerSettingsTab.setClosable(false);
        managerSettingsTab.setText("Настройки");
        managerSettingsTab.setContent(new ManagerSettingsPanel());
        tabPane.getTabs().add(managerSettingsTab);
        // bind to take available space
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        
        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static int fillFiltersRepository(){
        FiltersRepository rep = FiltersRepository.getInstance();
        //Add utility filters
        {
            Parameter[] parameters = {
                new Parameter<Integer>("Макс. размер", 1000)
            };
            FilterFactory<ThresholdResizeFilter> resizeFactory = new FilterFactory<>(
                    "Изменить размер изображения",
                    ThresholdResizeFilter.class,
                    parameters
            );        
            rep.getFactories().add(resizeFactory);
        }
        {
            Parameter[] parameters = {
                new Parameter<Double>("Значение", 128.),
                new Parameter<Double>("Девиация", 64.)
            };
            FilterFactory<AddGaussianNoiseFilter> noiseFactory = new FilterFactory<>(
                    "Добавить белый шум",
                    AddGaussianNoiseFilter.class,
                    parameters
            );        
            rep.getFactories().add(noiseFactory);
        }      
        //Add noise filters
        {
            Parameter[] parameters = {
                new Parameter<Integer>("Размер маски", 3)
            };
            FilterFactory<GaussianNoiseFilter> lowFilterFactory = new FilterFactory<>(
                    "Фильтр нижних частот",
                    GaussianNoiseFilter.class,
                    parameters
            );        
            rep.getFactories().add(lowFilterFactory);
        }
        {
            Parameter[] parameters = {
                new Parameter<Integer>("Размер маски", 3)
            };
            FilterFactory<MedianNoiseFilter> medianFilterFactory = new FilterFactory<>(
                    "Медианный фильтр",
                    MedianNoiseFilter.class,
                    parameters
            );        
            rep.getFactories().add(medianFilterFactory);
        }
        {
            Parameter[] parameters = {
                new Parameter<Integer>("Размер маски", 3),
                new Parameter<Double>("Девиация цвета", 0.),
                new Parameter<Double>("Девиация пространства", 0.)
            };
            FilterFactory<BilateralNoiseFilter> bilateralFilterFactory = new FilterFactory<>(
                    "Билатеральный фильтр",
                    BilateralNoiseFilter.class,
                    parameters
            );        
            rep.getFactories().add(bilateralFilterFactory);
        }
        //
        List<? extends ImageFilter> filters = rep.getFilters();
        int size = filters.size();
        return size;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Fill repository
        fillFiltersRepository();
        //Lauch form
        launch(args);
    }
    
}
