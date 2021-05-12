package com.example.foodie_android.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Meal {
    private String ID;
    private String name;
    private String category;
    private String area;
    private String instructions;
    private String imageURL;
    private String tags;
    private String videoURL;
    private HashMap<String, String> ingredients;
    private String source;

    public Meal(String ID, String name, String category, String area, String instructions,
                String imageURL, String tags, String videoURL,
                HashMap<String, String> ingredients, String source) {
        this.ID = ID;
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.imageURL = imageURL;
        this.tags = tags;
        this.videoURL = videoURL;
        this.ingredients = ingredients;
        this.source = source;
    }

    public Meal(String ID, String name, String category, String area, String instructions,
                String tags, String videoURL, HashMap<String, String> ingredients,
                String source) {
        this.ID = ID;
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.tags = tags;
        this.videoURL = videoURL;
        this.ingredients = ingredients;
        this.source = source;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public HashMap<String, String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<String, String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
