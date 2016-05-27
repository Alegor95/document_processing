/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.application_example.filters.factory;

import java.lang.reflect.ParameterizedType;

/**
 *
 * @author aleksandr
 */
public class Parameter<T>{
        
        private final String name;
        
        private T value;
        
        private Class<T> valueClass;
        
        public String getName(){
            return name;
        }
        
        public T getValue(){
            return value;
        }
        
        public void setValue(T value){
            this.value = value;
        }
        
        public Class<T> getValueClass(){
            return valueClass;
        }
        
        public Parameter(String name, T defaultValue){
            this.name = name;
            this.value = defaultValue;
            this.valueClass = (Class<T>) defaultValue.getClass();            
        }
        
        @Override
        public int hashCode(){
            return this.getName().hashCode();
        }
        
    }
