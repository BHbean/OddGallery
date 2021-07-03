package com.example.oddmeseum;

import java.io.Serializable;

public class Author implements Serializable {
    private String name;
    private String country;

    Author(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}

