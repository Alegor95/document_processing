/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.application_example.components.main;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 *
 * @author aleksandr
 */
public class MainPanel extends HBox {
    
    private Label testLabel;
    
    public MainPanel(){
        testLabel = new Label("Main panel");
        this.getChildren().add(testLabel);
    }
    
}
