package com.example.technologycompare.helper;

public class Device {
    private int IdDevice;
    private String ImageDevice;
    private String NameDevice;
    private String LinkDevice;
    private String ScreenDevice;
    private String SystemDevice;
    private String ProcessorDevice;
    private String MemoryDevice;
    private String RamDevice;
    private String SimDevice;
    private String BatteryDevice;
    private String CameraDevice;
    private String PriceDevice;
    private String KpiScreen;
    private String KpiProcessor;
    private String KpiCamera;

    public Device(int idDevice, String imageDevice, String nameDevice, String linkDevice, String screenDevice, String systemDevice, String processorDevice,
                  String memoryDevice, String ramDevice, String simDevice, String batteryDevice, String cameraDevice, String priceDevice,String kpi_screen,String kpi_processor,String kpi_camera) {
        IdDevice = idDevice;
        ImageDevice = imageDevice;
        NameDevice = nameDevice;
        LinkDevice = linkDevice;
        ScreenDevice = screenDevice;
        SystemDevice = systemDevice;
        ProcessorDevice = processorDevice;
        MemoryDevice = memoryDevice;
        RamDevice = ramDevice;
        SimDevice = simDevice;
        BatteryDevice = batteryDevice;
        CameraDevice = cameraDevice;
        PriceDevice = priceDevice;
        KpiScreen =kpi_screen;
        KpiCamera = kpi_camera;
        KpiProcessor = kpi_processor;
    }


    public int getIdDevice() {
        return IdDevice;
    }

    public void setIdDevice(int idDevice) {
        IdDevice = idDevice;
    }

    public String getImageDevice() {
        return ImageDevice;
    }

    public void setImageDevice(String imageDevice) {
        ImageDevice = imageDevice;
    }

    public String getNameDevice() {
        return NameDevice;
    }

    public void setNameDevice(String nameDevice) {
        NameDevice = nameDevice;
    }

    public String getLinkDevice() {
        return LinkDevice;
    }

    public void setLinkDevice(String linkDevice) {
        LinkDevice = linkDevice;
    }

    public String getScreenDevice() {
        return ScreenDevice;
    }

    public void setScreenDevice(String screenDevice) {
        ScreenDevice = screenDevice;
    }

    public String getSystemDevice() {
        return SystemDevice;
    }

    public void setSystemDevice(String systemDevice) {
        SystemDevice = systemDevice;
    }

    public String getProcessorDevice() {
        return ProcessorDevice;
    }

    public void setProcessorDevice(String processorDevice) {
        ProcessorDevice = processorDevice;
    }

    public String getMemoryDevice() {
        return MemoryDevice;
    }

    public void setMemoryDevice(String memoryDevice) {
        MemoryDevice = memoryDevice;
    }

    public String getRamDevice() {
        return RamDevice;
    }

    public void setRamDevice(String ramDevice) {
        RamDevice = ramDevice;
    }

    public String getSimDevice() {
        return SimDevice;
    }

    public void setSimDevice(String simDevice) {
        SimDevice = simDevice;
    }

    public String getBatteryDevice() {
        return BatteryDevice;
    }

    public void setBatteryDevice(String batteryDevice) {
        BatteryDevice = batteryDevice;
    }

    public String getCameraDevice() {
        return CameraDevice;
    }

    public void setCameraDevice(String cameraDevice) {
        CameraDevice = cameraDevice;
    }

    public String getPriceDevice() {
        return PriceDevice;
    }

    public void setPriceDevice(String priceDevice) {
        PriceDevice = priceDevice;
    }

    public String getKpiScreen() {
        return KpiScreen;
    }

    public void setKpiScreen(String kpiScreen) {
        KpiScreen = kpiScreen;
    }

    public String getKpiProcessor() {
        return KpiProcessor;
    }

    public void setKpiProcessor(String kpiProcessor) {
        KpiProcessor = kpiProcessor;
    }

    public String getKpiCamera() {
        return KpiCamera;
    }

    public void setKpiCamera(String kpiCamera) {
        KpiCamera = kpiCamera;
    }
}
