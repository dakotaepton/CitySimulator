package mad.citysimulator.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mad.citysimulator.database.GameStateDbManager;

public class GameState {

    // All Game State Data
    private Settings settings;
    private int money;
    private int gameTime;
    private MapElement[][] map;
    private ArrayList<Road> roads;
    private ArrayList<Residential> residentials;
    private ArrayList<Commercial> commercials;

    // Default constructor
    public GameState() {
        this.settings = new Settings();
        this.money = this.settings.getInitialMoney();
        this.gameTime = 0;
        this.map = null;
        this.roads = new ArrayList<>();
        this.residentials = new ArrayList<>();
        this.commercials = new ArrayList<>();
    }

    // Alternate constructor
    public GameState(Settings settings, int money, int gameTime, MapElement[][] map,
                     ArrayList<Road> roads, ArrayList<Residential> residentials,
                     ArrayList<Commercial> commercials) {
        this.settings = settings;
        this.money = money;
        this.gameTime = gameTime;
        this.map = map;
        this.roads = roads;
        this.residentials = residentials;
        this.commercials = commercials;
    }

    // Getters
    public Settings getSettings() { return settings; }
    public int getMoney() { return money; }
    public int getGameTime() { return gameTime; }
    public MapElement[][] getMap() { return map; }
    public ArrayList<Road> getRoads() { return roads; }
    public ArrayList<Residential> getResidentials() { return residentials; }
    public ArrayList<Commercial> getCommercials() { return commercials; }

    // Setters
    public void setSettings(Settings settings) { this.settings = settings; }
    public void setMoney(int money) { this.money = money; }
    public void setGameTime(int gameTime) { this.gameTime = gameTime; }
    public void setMap(MapElement[][] map) { this.map = map; }
    public void setRoads(ArrayList<Road> roads) { this.roads = roads; }
    public void setResidentials(ArrayList<Residential> residentials) { this.residentials = residentials; }
    public void setCommercials(ArrayList<Commercial> commercials) { this.commercials = commercials; }
}
