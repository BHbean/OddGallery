package com.example.oddmeseum;

import java.io.Serializable;

public class Museum implements Serializable {
    private String name;
    private String nation;

    Museum(String name, String nation) {
        this.name = name;
        this.nation = nation;
    }

    public String getName() {
        return name;
    }

    public String getNation() {
        return nation;
    }
}
