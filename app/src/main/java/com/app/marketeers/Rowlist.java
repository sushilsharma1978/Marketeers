package com.app.marketeers;

public class Rowlist
{
    String id,title,content,image;


    public Rowlist(String id, String title, String content, String image) {
        this.setId(id);
        this.setTitle(title);
        this.setContent(content);
        this.setImage(image);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
