package com.example.foodie_android.models;

public class CategoryItem {
    private String ID;
    private String name;
    private String imageUrl;

    public CategoryItem(String ID, String name, String imageUrl) {
        this.ID = ID;
        this.name = name;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
