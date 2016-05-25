/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.application_example.components.settings;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.vsu.cs.documentpreparing.application_example.factory.ManagerFactory;

/**
 *
 * @author aleksandr
 */
public class ManagerSettingsPanel extends VBox {
    
    private final double LABEL_WIDTH = 200,
            INPUT_WIDTH = 400,
            FIELD_HEIGHT = 20;
    private final int FORM_PADDING = 10;
    
    private ManagerFactory factory = ManagerFactory.getInstance();
    
    private HBox workersBox;
    
    private TextField workersInput;
    
    protected HBox getWorkersBox(){
        return workersBox;
    }
    
    protected TextField getWorkersInput(){
        return workersInput;
    }
    
    private HBox tasksBox;
    
    private TextField tasksInput;
    
    private CheckBox limitedTasksInput;
    
    protected HBox getTasksBox(){
        return tasksBox;
    }
    
    protected TextField getTasksInput(){
        return tasksInput;
    }
    
    protected CheckBox getLimitedTasksInput(){
        return limitedTasksInput;
    }
    
    private HBox actionsBox;
    
    private HBox createWorkersBox(){
        workersBox = new HBox(FORM_PADDING);
        //Input label
        Label workersText = new Label("Количество работников");
        workersText.setPrefWidth(LABEL_WIDTH);
        workersText.setPrefHeight(FIELD_HEIGHT);
        workersBox.getChildren().add(workersText);
        //Input text
        workersInput = new TextField();
        workersInput.setPrefWidth(INPUT_WIDTH);
        workersInput.setPrefHeight(FIELD_HEIGHT);
        workersBox.getChildren().add(workersInput);
        return workersBox;
    }
    
    private HBox createTasksBox(){
        tasksBox = new HBox(FORM_PADDING);
        //Input label
        Label tasksText = new Label("Количество задач в очереди");
        tasksText.setPrefSize(LABEL_WIDTH, FIELD_HEIGHT);
        tasksBox.getChildren().add(tasksText);
        //Input text
        tasksInput = new TextField();
        tasksInput.setPrefSize(INPUT_WIDTH/2, FIELD_HEIGHT);
        tasksBox.getChildren().add(tasksInput);
        //Limited input checkbox
        limitedTasksInput = new CheckBox("Ограничить");
        limitedTasksInput.setPrefSize(INPUT_WIDTH/2, FIELD_HEIGHT);
        limitedTasksInput.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if (limitedTasksInput.isSelected()){
                    tasksInput.setDisable(false);
                } else {
                    tasksInput.setDisable(true);
                }
            }            
        });
        tasksBox.getChildren().add(limitedTasksInput);
        return tasksBox;
    }
    
    public void restoreDefault(){
        this.getWorkersInput().setText(factory.getDefaultWorkerCount() + "");
        if (factory.getDefaultMaxTaskCount() >= 0){
            this.getTasksInput().setText(factory.getDefaultMaxTaskCount() + "");
            this.getLimitedTasksInput().setSelected(true);
            this.getTasksInput().setDisable(false);
        } else {
            this.getLimitedTasksInput().setSelected(false);
            this.getTasksInput().setDisable(true);
        }
        
    }
    
    private HBox createActionBox(){
        actionsBox = new HBox(FORM_PADDING);
        //Add save button
        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                save();
            }
        });
        actionsBox.getChildren().add(saveButton);
        //Add restore button
        Button restoreButton = new Button("Восстановить");
        restoreButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                restoreDefault();
            }
        });
        actionsBox.getChildren().add(restoreButton);
        return actionsBox;
    }
    
    public void save(){
        //Get workers count from text
        int workersCount = 0;
        try {
            workersCount = Integer.valueOf(this.getWorkersInput().getText());
        } catch (NumberFormatException e){
            Alert alert = new Alert(
                    AlertType.ERROR,
                    "Количество работников должно быть числом");
            alert.showAndWait();
            return;
        }
        //Get tasks count
        int tasksCount = 0;
        if (this.getLimitedTasksInput().isSelected()){
            try {
                tasksCount = Integer.valueOf(this.getTasksInput().getText());
            } catch (NumberFormatException e){
                Alert alert = new Alert(
                        AlertType.ERROR,
                        "Максимальное количество задач должно быть числом");
                alert.showAndWait();
                return;
            }
        } else {
            tasksCount = -1;
        }
        factory.setDefaultMaxTaskCount(tasksCount);
        factory.setDefaultWorkerCount(workersCount);
        Alert alert = new Alert(
                        AlertType.INFORMATION,
                        "Настройки успешно сохранены");
                alert.showAndWait();
    }
    
        
    public ManagerSettingsPanel(){
        this.setSpacing(FORM_PADDING);
        this.setPadding(new Insets(FORM_PADDING, FORM_PADDING, FORM_PADDING, FORM_PADDING));
        this.getChildren().add(createWorkersBox());
        this.getChildren().add(createTasksBox());
        //Add Filter settings
        //
        this.getChildren().add(createActionBox());
        this.restoreDefault();
    }
}
