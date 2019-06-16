package groep3.hr.nl.techlabhr;

import android.graphics.Bitmap;
import android.util.Log;

public class Product {
    String id,name,stock,broken;
    Bitmap icon;



    public Product(String id, String name, String stock, Bitmap icon) {

        this.id = id;
        this.name = name;
        this.stock = stock;
        this.icon = icon;

    }

    public Product(String id, String name, String stock, Bitmap icon, String broken) {

        this.id = id;
        this.name = name;
        this.stock = stock;
        this.icon = icon;
        this.broken = broken;
        Log.d("Product", "Executed right constructor");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStock() {
        return stock;
    }

    public String getBroken() {
        Log.d("Product", "Returned broken");
        return broken;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
