package com.example.abhishekaryan.shoppinglist.Infrastructure;

import java.util.HashMap;

public class ServiceResponse {

    private HashMap<String,String> propertyErrors;

    public ServiceResponse() {

        propertyErrors=new HashMap<>();
    }


    public void setPropertyErrors(String property,String error){

        propertyErrors.put(property,error);
    }

    public String getPropertyErrors(String property){
        return propertyErrors.get(property);
    }

    public Boolean didSucceed(){

        return (propertyErrors.size()==0);
    }
}
