package es.unavarra.tlm.prueba.PantallaPrincipal;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by ibai on 11/4/17.
 */

public class Producto2 {

    private Bitmap bitmap;
    private ImageView image;
    private String description;
    private String location;

    public Producto2(Bitmap bitmap, String description, String location) {
        this.bitmap = bitmap;
        this.description = description;
        this.location = location;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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


