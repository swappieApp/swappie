package es.unavarra.tlm.prueba.model;

import android.widget.ImageView;

public class Producto {

    private int drawableId;
    private ImageView image;
    private String description;
    private String location;

    public Producto(int drawableId, String description, String location) {
        this.drawableId = drawableId;
        this.description = description;
        this.location = location;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}


