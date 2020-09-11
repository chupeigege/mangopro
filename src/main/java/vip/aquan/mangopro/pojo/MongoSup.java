package vip.aquan.mangopro.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "supplier")
public class MongoSup {
    private Location location;
    private String name;
    private String categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
