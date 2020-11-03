package mad.citysimulator.models;

public class Settings {

    // Game Setting Data
    private String saveName;
    private String cityName;
    private int mapWidth;
    private int mapHeight;
    private int initialMoney;
    private int familySize;
    private int shopSize;
    private int salary;
    private double taxRate;
    private int serviceCost;
    private int houseBuildingCost;
    private int commBuildingCost;
    private int roadBuildingCost;

    // Constructor W/ Default Settings
    public Settings() {
        this.saveName = "DEFAULT";
        this.cityName = "Perth";
        this.mapWidth = 50;
        this.mapHeight = 10;
        this.initialMoney = 1000;
        this.familySize = 4;
        this.shopSize = 6;
        this.salary = 10;
        this.taxRate = 0.3;
        this.serviceCost = 2;
        this.houseBuildingCost = 100;
        this.commBuildingCost = 500;
        this.roadBuildingCost = 20;
    }

    // Alternate Constructor For Resume
    public Settings(String saveName, String cityName, int mapWidth, int mapHeight, int initialMoney) {
        this.saveName = saveName;
        this.cityName = cityName;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.initialMoney = initialMoney;
        this.familySize = 4;
        this.shopSize = 6;
        this.salary = 10;
        this.taxRate = 0.3;
        this.serviceCost = 2;
        this.houseBuildingCost = 100;
        this.commBuildingCost = 500;
        this.roadBuildingCost = 20;
    }

    // Full Alternate Constructor
    public Settings(String saveName, String cityName, int mapWidth, int mapHeight, int initialMoney, int familySize,
                    int shopSize, int salary, double taxRate, int serviceCost, int houseBuildingCost, int commBuildingCost,
                    int roadBuildingCost) {
        this.saveName = saveName;
        this.cityName = cityName;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.familySize = familySize;
        this.shopSize = shopSize;
        this.salary = salary;
        this.taxRate = taxRate;
        this.serviceCost = serviceCost;
        this.houseBuildingCost = houseBuildingCost;
        this.commBuildingCost = commBuildingCost;
        this.roadBuildingCost = roadBuildingCost;
        this.initialMoney = initialMoney;
    }

    // Accessors
    public String getSaveName() { return saveName; }
    public String getCityName() { return cityName; }
    public int getMapWidth() { return mapWidth; }
    public int getMapHeight() { return mapHeight; }
    public int getInitialMoney() { return initialMoney; }
    public int getFamilySize() { return familySize; }
    public int getShopSize() { return shopSize; }
    public int getSalary() { return salary; }
    public double getTaxRate() { return taxRate; }
    public int getServiceCost() { return serviceCost; }
    public int getHouseBuildingCost() { return houseBuildingCost; }
    public int getCommBuildingCost() { return commBuildingCost; }
    public int getRoadBuildingCost() { return roadBuildingCost; }

    // Mutator
    public void setSaveName(String saveName) { this.saveName = saveName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    public void setMapWidth(int mapWidth) { this.mapWidth = mapWidth; }
    public void setMapHeight(int mapHeight) { this.mapHeight = mapHeight; }
    public void setInitialMoney(int initialMoney) { this.initialMoney = initialMoney; }
    public void setFamilySize(int familySize) { this.familySize = familySize; }
    public void setShopSize(int shopSize) { this.shopSize = shopSize; }
    public void setSalary(int salary) { this.salary = salary; }
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }
    public void setServiceCost(int serviceCost) { this.serviceCost = serviceCost; }
    public void setHouseBuildingCost(int houseBuildingCost) { this.houseBuildingCost = houseBuildingCost; }
    public void setCommBuildingCost(int commBuildingCost) { this.commBuildingCost = commBuildingCost; }
    public void setRoadBuildingCost(int roadBuildingCost) { this.roadBuildingCost = roadBuildingCost; }
}
