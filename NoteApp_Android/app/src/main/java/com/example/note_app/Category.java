package com.example.note_app;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Category implements Serializable {
    @SerializedName("id") private int id;
    @SerializedName("name") private String name;
    @SerializedName("description") private String description;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name == null ? "" : name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description == null ? "" : description; }
    public void setDescription(String description) { this.description = description; }

    @Override public String toString() { return getName(); }
}
