package groep3.hr.nl.techlabhr;

import android.graphics.Bitmap;

public class Product {
    String id,name,stock;
    Bitmap icon;



    public Product(String id, String name, String stock, Bitmap icon) {

        this.id = id;
        this.name = name;
        this.stock = stock;
        this.icon = icon;
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
