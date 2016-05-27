/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.application_example.filters;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.vsu.cs.documentpreparing.application_example.filters.factory.FilterFactory;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 */
public class FiltersRepository {
    
    /**
     * List of filter factories
     */
    ObservableList<FilterFactory> factories;
    
    /**
     * List of active factories
     */
    ObservableList<FilterFactory> activeFactories;
    
    /**
     * Get copy list of avaliable factories
     * @return 
     */
    public ObservableList<FilterFactory> getFactories(){
        return factories;
    }
    
    /**
     * Get list of active factories
     * @return 
     */
    public ObservableList<FilterFactory> getActiveFactories(){
        return activeFactories;
    }

    /**
     * Get filters list created by current factories list
     * @return 
     */
    public List<ImageFilter> getFilters(){
        return this.getFilters(factories);
    }
    
    public List<ImageFilter> getFilters(Collection<FilterFactory> factList){        
        List filters = new LinkedList<>();
        for (FilterFactory factory : factList){
            filters.add(factory.createFilter());
        }
        return filters;
    }

    private FiltersRepository(){
        this.factories = FXCollections
                .observableList(new LinkedList<FilterFactory>());
    }
     
     //Singleton methods
    private final static FiltersRepository instance = new FiltersRepository();
     
    public static FiltersRepository getInstance(){
        return instance;
    }
}
