package com.delaroystudios.scanner.model;

public class SpinnerModel {

    String text;

    Integer imageId;

    public SpinnerModel(String text, Integer imageId){
        this.text=text;
        this.imageId=imageId;
    }

    public String getText(){
        return text;
    }

    public Integer getImageId(){
        return imageId;
    }
}
