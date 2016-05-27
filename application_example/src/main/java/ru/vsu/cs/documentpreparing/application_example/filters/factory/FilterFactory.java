/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.documentpreparing.application_example.filters.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Logger;
import ru.vsu.cs.documentpreparing.photo_processing.manager.tasks.filtertask.filters.ImageFilter;

/**
 *
 * @author aleksandr
 * @param <T>
 */
public class FilterFactory<T extends ImageFilter> {
    
    private static int nextId = 0;
    
    private final Logger log = Logger
            .getLogger(getClass().getCanonicalName());
    
    private final int id = nextId++;
    
    private final String filterName;
    
    private final Class<T> filterClass;
    
    private final Parameter[] parameters;
    
    public int getId(){
        return id;
    }
    
    public String getFilterName(){
        return filterName;
    }
    
    public Class<T> getFilterClass(){
        return this.filterClass;
    }
    
    public Parameter[] getParameters(){
        return parameters;
    }
    
    public T createFilter(){
        try {
            //Get all parameter's types
            Object[] initArgs = new Object[parameters.length];
            Class[] parameterTypes = new Class[parameters.length];
            for (int i = 0; i < parameters.length; i++){
                initArgs[i] = parameters[i].getValue();
                parameterTypes[i] = parameters[i].getValueClass();
            }
            //Get constructor of type
            Constructor constructor = this.getFilterClass()
                    .getConstructor(parameterTypes);
            return (T)constructor.newInstance(initArgs);
        } catch (InstantiationException
                |IllegalAccessException
                |InvocationTargetException e){
            log.severe("Error on constructor calling");
            return null;
        } catch (NoSuchMethodException e){
            log.severe("Cannot find an appropriate constructor for class\n"
                    + e.getMessage());
            return null;            
        }
    }
    
    private <T> Constructor<T> getAppropriateConstructor(Class<T> c,
            Parameter[] initArgs){
        if(initArgs == null)
            initArgs = new Parameter[0];
        for(Constructor con : c.getDeclaredConstructors()){
            Class[] types = con.getParameterTypes();
            if(types.length!=initArgs.length)
                continue;
            boolean match = true;
            for(int i = 0; i < types.length; i++){
                Class need = types[i], got = initArgs[i].getValueClass();
                if(!need.isAssignableFrom(got)){
                    if(need.isPrimitive()){
                        match = (int.class.equals(need) && Integer.class.equals(got))
                        || (long.class.equals(need) && Long.class.equals(got))
                        || (char.class.equals(need) && Character.class.equals(got))
                        || (short.class.equals(need) && Short.class.equals(got))
                        || (boolean.class.equals(need) && Boolean.class.equals(got))
                        || (byte.class.equals(need) && Byte.class.equals(got));
                    }else{
                        match = false;
                    }
                }
                if(!match)
                    break;
            }
            if(match)
                return con;
        }
        throw new IllegalArgumentException("Cannot find an appropriate constructor for class " + c + " and arguments " + Arrays.toString(initArgs));
    }
    
    public FilterFactory(String filterName, Class<T> filterClass,
            Parameter[] parameters){
        this.filterName = filterName;
        this.parameters = parameters;
        this.filterClass = filterClass;
    }
    
    @Override
    public String toString(){
        return this.getFilterName();
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof FilterFactory){
            FilterFactory f = (FilterFactory)o;
            return this.getId() == f.getId();
        }
        return false;
    }
    
}
