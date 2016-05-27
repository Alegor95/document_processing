/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.application_example.components.settings;

import java.util.LinkedList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.vsu.cs.documentpreparing.application_example.filters.FiltersRepository;
import ru.vsu.cs.documentpreparing.application_example.filters.factory.FilterFactory;
import ru.vsu.cs.documentpreparing.application_example.filters.factory.Parameter;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class FilterSettingsPanel extends HBox {
    
    private final double LABEL_WIDTH = 100,
            INPUT_WIDTH = 100,
            FIELD_HEIGHT = 20;
    private final int FORM_PADDING = 10;
    
    private ListView activeListView;
    private ObservableList<FilterFactory> activeListCollection;
    
    private VBox listButtons;
    
    private ListView fullListView;
    private ObservableList<FilterFactory> fullListCollection;
    
    private VBox selectedFilterSettings;
    
    private FilterFactory selectedFactory;
    
    private FiltersRepository rep = FiltersRepository.getInstance();
    
    private void updateSelectedFactory(){
        if (selectedFilterSettings == null){
            selectedFilterSettings = new VBox(FORM_PADDING);
        }
        selectedFilterSettings.getChildren().clear();
        selectedFilterSettings.getChildren().add(
                new Label("Опции выбранного фильтра")
        );
        Parameter[] parameters = selectedFactory.getParameters();
        //Generate label and input for each factory
        for (final Parameter parameter:parameters){
            HBox field = new HBox(FIELD_HEIGHT);
            Label fieldLabel = new Label(parameter.getName());
            fieldLabel.setPrefSize(LABEL_WIDTH, FIELD_HEIGHT);
            field.getChildren().add(fieldLabel);
            //Generate inputs
            if (Boolean.class.equals(parameter.getValueClass())){
                final CheckBox checkbox = new CheckBox();
                checkbox.setPrefSize(INPUT_WIDTH, FIELD_HEIGHT);
                checkbox.setSelected((Boolean)parameter.getValue());
                checkbox.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        parameter.setValue(checkbox.isSelected());
                    }
                });
                field.getChildren().add(checkbox);
            }
            if (Integer.class.equals(parameter.getValueClass())){
                final TextField textField = new TextField();
                textField.setPrefSize(INPUT_WIDTH, FIELD_HEIGHT);
                textField.setText(
                        parameter.getValue() != null?
                                parameter.getValue().toString():""
                );
                textField.textProperty().addListener(new ChangeListener<String>(){
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        Integer value= Integer.valueOf(0);
                        try {
                            value = Integer.valueOf(newValue);
                            parameter.setValue(value);
                        } catch (NumberFormatException e){
                            Alert message = new Alert(
                                    AlertType.ERROR,
                                    "Параметр \""+parameter.getName()
                                            +"\" задан некорректно" 
                            );
                            message.showAndWait();
                        }
                    }
                });
                field.getChildren().add(textField);
            }
            if (Double.class.equals(parameter.getValueClass())){
                final TextField textField = new TextField();
                textField.setPrefSize(INPUT_WIDTH, FIELD_HEIGHT);
                textField.setText(
                        parameter.getValue() != null?
                                parameter.getValue().toString():""
                );
                textField.textProperty().addListener(new ChangeListener<String>(){
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        Double value= Double.valueOf(0);
                        if (newValue == null || newValue.isEmpty())
                        try {
                            value = Double.valueOf(newValue);
                            parameter.setValue(value);
                        } catch (NumberFormatException e){
                            Alert message = new Alert(
                                    AlertType.ERROR,
                                    "Параметр \""+parameter.getName()
                                            +"\" задан некорректно" 
                            );
                            message.showAndWait();
                        }
                    }
                });
                field.getChildren().add(textField);
            }
            //
            selectedFilterSettings.getChildren().add(field);
        }
    }
    
    public FilterSettingsPanel(){
        this.setPadding(new Insets(FORM_PADDING, FORM_PADDING,
                FORM_PADDING, FORM_PADDING));
        this.setSpacing(this.FORM_PADDING);
        EventHandler selectHandler = new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if (event.getSource() instanceof ListView){
                    ListView list = ((ListView)event.getSource());
                    int index = list.getSelectionModel().getSelectedIndex();
                    if (index < 0) return;
                    Object currentItem = list.getItems().get(index);
                    if (currentItem instanceof FilterFactory){
                        selectedFactory = (FilterFactory)currentItem;
                        updateSelectedFactory();
                    }
                }
            }
        };
        //Add active list
        VBox activeListContainer = new VBox();
        Label activeListLabel = new Label("Активные фильтры");
        activeListView = new ListView();
        activeListView.prefHeightProperty().bind(this.heightProperty());
        activeListCollection = rep.getActiveFactories();
        activeListView.setItems(activeListCollection);
        activeListView.setOnMouseClicked(selectHandler);
        activeListContainer.getChildren()
                .addAll(activeListLabel, activeListView);
        //Add lists controls
        listButtons = new VBox();
        Button activateButton = new Button("<-");
        activateButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if (selectedFactory != null){
                    activeListCollection.add(selectedFactory);
                }
            }
        });
        Button deactivateButton = new Button("->");
        deactivateButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if (selectedFactory != null){
                    activeListCollection.remove(selectedFactory);
                }
            }
        });
        Button orderUpButton = new Button("/\\");
        orderUpButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if (selectedFactory != null){                    
                    final int index = activeListCollection
                            .indexOf(selectedFactory);
                    if (index <= 0){
                        return;
                    }
                    activeListCollection.remove(index);
                    activeListCollection.add(index - 1, selectedFactory);
                    activeListView.getSelectionModel().select(index - 1);
                }
            }
        });
        Button orderDownButton = new Button("\\/");
        orderDownButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if (selectedFactory != null){                    
                    final int index = activeListCollection
                            .indexOf(selectedFactory);
                    if (index < 0 || index == activeListCollection.size() - 1){
                        return;
                    }
                    activeListCollection.remove(index);
                    activeListCollection.add(index + 1, selectedFactory);
                    activeListView.getSelectionModel().select(index + 1);
                }
            }
        });
        listButtons.setAlignment(Pos.CENTER);
        listButtons.getChildren().addAll(activateButton, deactivateButton,
                orderUpButton, orderDownButton);
        //Add full list
        VBox fullListContainer = new VBox();
        Label fullListLabel = new Label("Доступные фильтры");
        fullListView = new ListView();
        fullListView.prefHeightProperty().bind(this.heightProperty());
        fullListCollection = rep.getFactories();
        fullListView.setItems(fullListCollection);
        fullListView.setOnMouseClicked(selectHandler);
        fullListContainer.getChildren().addAll(fullListLabel, fullListView);
        //Add filter options panel
        selectedFilterSettings = new VBox(this.FORM_PADDING);
        //Add childrens
        this.getChildren()
                .addAll(activeListContainer, listButtons,
                        fullListContainer, selectedFilterSettings);
    }
}