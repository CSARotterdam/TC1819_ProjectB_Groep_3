package groep3.hr.nl.techlabhr;

public class Product {
    String id,manufacturer,category,name;
    int stock,broken;

    public Product(String id,String manufacturer,String category,String name, int stock, int broken) {
        if (broken > stock) {
            broken = stock;
        }
        this.id = id;
        this.manufacturer = manufacturer;
        this.category = category;
        this.name = name;
        this.stock = stock;
        this.broken = broken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getBroken() {
        return broken;
    }

    public void setBroken(int broken) {
        this.broken = broken;
    }
}
