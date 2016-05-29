/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.application_example.components.main;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import ru.vsu.cs.documentpreparing.application_example.factory.ManagerFactory;
import ru.vsu.cs.documentpreparing.application_example.filters.FiltersRepository;
import ru.vsu.cs.documentpreparing.photo_processing.manager.ImageProcessingManager;
import ru.vsu.cs.documentpreparing.photo_processing.manager.ImageProcessingTask;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.FilterImageProcessingTask;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class MainPanel extends HBox {
    
    private final static double CONTROL_HEIGHT = 20;    
    private final static int FORM_PADDING = 10;
    
    private ListView imageList;
    private ObservableList<ImageContainer> images;
    
    private ImageView initialImage, processedImage;
    private Map<String, ImageContainer> processedFile;
    
    private List<ImageProcessingTask> tasks;
    
    private FiltersRepository fRepository = FiltersRepository.getInstance();
    private ManagerFactory mFactory = ManagerFactory.getInstance();
    
    public MainPanel(){
        processedFile = new HashMap<>();
        tasks = new LinkedList<>();
        this.setPadding(new Insets(FORM_PADDING,FORM_PADDING,
                FORM_PADDING,FORM_PADDING));
        this.setSpacing(FORM_PADDING);
        final VBox imageListContainer = new VBox(FORM_PADDING);
        imageListContainer.prefWidthProperty()
                .bind(this.widthProperty().multiply(0.25));
        imageListContainer.prefHeightProperty()
                .bind(this.heightProperty());
        Label imageListLabel = new Label("Список изображений");
        imageList = new ListView();
        imageList.prefHeightProperty().bind(
                imageListContainer.heightProperty()
        );
        images = FXCollections.observableList(new LinkedList<ImageContainer>());
        imageList.setItems(images);
        imageList.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                ImageContainer source = (ImageContainer) imageList
                        .getSelectionModel().getSelectedItem();
                initialImage.setImage(source.getSceneImage());
                if (processedFile.containsKey(source.getFileName())){
                    ImageContainer res =processedFile.get(source.getFileName());
                    processedImage.setImage(res.getSceneImage());
                }
            }
        });
        //Add controls
        Button loadButton = new Button("Загрузить");
        loadButton.setPrefHeight(CONTROL_HEIGHT);
        loadButton.prefWidthProperty().bind(this.widthProperty());
        loadButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Выберите файлы");
                List<File> files = fileChooser.showOpenMultipleDialog(
                        imageListContainer.getScene().getWindow()
                );                
                //Add images to list
                for (File file:files){
                    if (file.isFile()){
                        try {
                            ImageContainer container = new ImageContainer(
                                file.getName(),
                                new Image(Files.newInputStream(file.toPath()))
                            );
                            images.add(container);
                        } catch (IOException ex) {
                            Logger.getLogger(MainPanel.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            
        });
        Button removeButton = new Button("Удалить");
        removeButton.setPrefHeight(CONTROL_HEIGHT);
        removeButton.prefWidthProperty().bind(this.widthProperty());
        removeButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                ImageContainer removeObj = (ImageContainer) imageList
                        .getSelectionModel().getSelectedItem();
                images.remove(removeObj);
                //Remove from processed
                if (processedFile.containsKey(removeObj.getFileName())){
                    processedFile.remove(removeObj.getFileName());
                }
            }
        });
        imageListContainer.getChildren().addAll(imageListLabel, imageList,
                loadButton, removeButton);
        //Right panel
        VBox rightPanelContainer = new VBox(FORM_PADDING);
        rightPanelContainer.prefWidthProperty()
                .bind(this.widthProperty().multiply(0.75));
        rightPanelContainer.prefHeightProperty().bind(this.heightProperty());
        //Controls panel        
        final HBox controlsContainer = new HBox(FORM_PADDING);
        Button saveButton = new Button("Сохранить");
        saveButton.setPrefHeight(CONTROL_HEIGHT);
        saveButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Выберите файл  для сохранения");
                File file = chooser.showOpenDialog(
                        imageListContainer.getScene().getWindow()
                );
                //Get selected file and save
                ImageContainer origContainer = (ImageContainer) imageList
                        .getSelectionModel().getSelectedItem();                
                ImageContainer container;
                if (processedFile.containsKey(origContainer.getFileName())){
                    container = processedFile.get(origContainer.getFileName());
                } else {
                    container = origContainer;
                }
                BufferedImage writable = SwingFXUtils
                        .fromFXImage(container.getSceneImage(), null);
                try {
                    ImageIO.write(writable, "png", file);
                } catch (IOException ex) {
                    Logger.getLogger(MainPanel.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
                Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Сохранение завершено"
                );
                alert.show();
            }
            
        });
        Button saveAllButton = new Button("Сохранить все");
        saveAllButton.setPrefHeight(CONTROL_HEIGHT);
        saveAllButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Выберите директорию для сохрнения файлов");
                File dir = chooser.showDialog(
                        imageListContainer.getScene().getWindow()
                );
                if (dir.isDirectory()){
                    for (ImageContainer container:processedFile.values()){
                        File target = new File(dir, container.getFileName());
                        BufferedImage writable = SwingFXUtils
                                .fromFXImage(container.getSceneImage(), null);
                        try {
                            ImageIO.write(writable, "png", target);
                        } catch (IOException ex) {
                            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Сохранение завершено"
                );
                alert.show();
            }
            
        });
        Button processButton = new Button("Обработать");
        processButton.setPrefHeight(CONTROL_HEIGHT);
        processButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                //Get manager
                ImageProcessingManager manager = mFactory.createManager();
                //Get filters
                List<ImageFilter> filters = fRepository.getActiveFilters();
                //Add tasks
                tasks = new LinkedList<>();
                for (final ImageContainer container:images){
                    final FilterImageProcessingTask newTask = new FilterImageProcessingTask(
                            container.getOpenCVImage(),
                            filters
                    );
                    newTask.addFinishListener(new ActionListener(){
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            processedFile.put(
                                    container.getFileName(),
                                    new ImageContainer(
                                            container.getFileName(),
                                            newTask.getImage()
                                    )
                            );
                        }
                    });
                    tasks.add(newTask);
                    manager.assignmentTask(newTask);
                }
                manager.addFinishListener(new Runnable(){
                    @Override
                    public void run() {
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                Alert alert = new Alert(
                                        AlertType.INFORMATION,
                                        "Все задачи выполнены"
                                );
                                alert.show();
                            }
                        });
                    }
                });
            }
            
        });
        controlsContainer.setPrefHeight(CONTROL_HEIGHT);
        controlsContainer.getChildren()
                .addAll(saveButton, saveAllButton, processButton);
        //Images
        HBox imageContainer = new HBox(FORM_PADDING);
        ScrollPane initialScrollContainer = new ScrollPane();
        initialScrollContainer.prefViewportWidthProperty()
                .bind(imageContainer.widthProperty().divide(2));
        initialScrollContainer.prefViewportHeightProperty()
                .bind(imageContainer.heightProperty());
        VBox initialBox = new VBox();
        initialImage = new ImageView();
        initialBox.getChildren().add(initialImage);
        initialScrollContainer.setContent(initialBox);
        ScrollPane processedScrollContainer = new ScrollPane();
        VBox processedBox = new VBox();
        processedImage = new ImageView();
        processedBox.getChildren().add(processedImage);
        processedScrollContainer.setContent(processedBox);
        processedScrollContainer.prefViewportWidthProperty()
                .bind(imageContainer.widthProperty().divide(2));
        processedScrollContainer.prefViewportHeightProperty()
                .bind(imageContainer.heightProperty());
        imageContainer.getChildren()
                .addAll(initialScrollContainer, processedScrollContainer);
        imageContainer.prefHeightProperty()
                .bind(rightPanelContainer.heightProperty());
        imageContainer.prefWidthProperty()
                .bind(rightPanelContainer.widthProperty());
        rightPanelContainer.getChildren()
                .addAll(controlsContainer, imageContainer);
        this.getChildren().addAll(imageListContainer, rightPanelContainer);        
    }
    
}