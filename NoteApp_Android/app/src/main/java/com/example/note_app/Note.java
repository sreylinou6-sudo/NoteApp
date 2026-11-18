package com.example.note_app;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Note implements Serializable {
    @SerializedName("id") private int id;
    @SerializedName("title") private String title;
    @SerializedName("content") private String content;
    @SerializedName("category_id") private Integer categoryId;
    @SerializedName("category_name") private String categoryName;
    @SerializedName("created_at") private String createdAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title == null ? "" : title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content == null ? "" : content; }
    public void setContent(String content) { this.content = content; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    /** "2026-11-11T19:22:00Z" -> "2026-11-11" */
    public String getDisplayDate() {
        if (createdAt == null) return "";
        return createdAt.length() >= 10 ? createdAt.substring(0, 10) : createdAt;
    }
}
