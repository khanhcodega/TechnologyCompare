package com.example.technologycompare.Sp;

public class Device {
    private String imageUrl;
    private String name;
    private String price;
    private String ram;
    private String storage;
    private String battery;
    private String chip;
    private String camera;
    private String screen;
    private String link;
    private  String id;
    public Device(String imageUrl, String name, String price, String ram, String storage, String battery, String chip, String camera, String screen,String link,String id) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.ram = ram;
        this.storage = storage;
        this.battery = battery;
        this.chip = chip;
        this.camera = camera;
        this.screen = screen;
        this.link = link;
        this.id = id;
    }

    // Các phương thức getter và setter cho từng thuộc tính
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getChip() {
        return chip;
    }

    public void setChip(String chip) {
        this.chip = chip;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }
}
