/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.application_example;

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
        mainTab.setText("Обработка");
        mainTab.setContent(new MainPanel());
        tabPane.getTabs().add(mainTab);
        //Add filters settings panel
        Tab filtersSettingsTab = new Tab();
        filtersSettingsTab.setText("Фильтры");
        filtersSettingsTab.setContent(new FilterSettingsPanel());
        tabPane.getTabs().add(filtersSettingsTab);
        //Add manager setting panel        
        Tab managerSettingsTab = new Tab();
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
