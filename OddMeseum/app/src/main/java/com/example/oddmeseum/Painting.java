package com.example.oddmeseum;

import android.graphics.Bitmap;
import android.graphics.Typeface;

import java.io.Serializable;

public class Painting implements Serializable {
    private String name;
    private Author author;
    private Museum museum;
    private String imageResource;
    private String description;
    private Bitmap imageBitmap = null;
    public static String authorTag;
    public static String locationTag;
    public static String descriptionTag;
    public static String comma;
    public static Typeface tf;

    Painting(String name, Author author, Museum museum, String imageResource, String description) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.museum = museum;
        this.imageResource = imageResource;
    }

    Painting(String name, String authorName, String authorNation, String description,
             String museumName, String museumNation, String imageResource) {
        this.name = name;
        this.description = description;
        this.author = new Author(authorName, authorNation);
        this.museum = new Museum(museumName, museumNation);
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public Author getAuthor() {
        return author;
    }

    public Museum getMuseum() {
        return museum;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getDescription() {
        return description;
    }
}
