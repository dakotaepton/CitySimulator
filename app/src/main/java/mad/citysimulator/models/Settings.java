package mad.citysimulator.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import mad.citysimulator.database.SettingsDbHelper;

public class Settings {

    // Game Setting Data
    private String saveName;
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
    private int money;
    private int gameTime;

    // Constructor W/ Default Settings
    public Settings() {
        this.saveName = "DEFAULT";
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
        this.money = this.initialMoney;
        this.gameTime = 0;
    }

    // Alternate Constructor
    public Settings(String saveName, int mapWidth, int mapHeight, int money, int initialMoney, int familySize,
                    int shopSize, int salary, double taxRate, int serviceCost, int houseBuildingCost, int commBuildingCost,
                    int roadBuildingCost, int gameTime) {
        this.saveName = saveName;
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
        this.money = money;
        this.gameTime = gameTime;
    }

    // Accessors
    public String getSaveName() { return saveName; }
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
    public int getMoney() { return money; }
    public int getGameTime() { return gameTime; }

    // Mutator
    public void setSaveName(String saveName) { this.saveName = saveName; }
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
    public void setMoney(int money) { this.money = money; }
    public void setGameTime(int gameTime) { this.gameTime = gameTime ; }
}
