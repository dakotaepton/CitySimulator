package mad.citysimulator.models;

import java.util.ArrayList;

public class GameState {

    // All Game State Data
    private Settings settings;
    private int money;
    private int gameTime;
    private MapElement[][] map;
    private ArrayList<Structure> structures;


    // Default constructor
    public GameState() {
        this.settings = new Settings();
        this.money = this.settings.getInitialMoney();
        this.gameTime = 0;
        this.map = null;
        this.structures = new ArrayList<>();
    }

    // Alternate constructor
    public GameState(Settings settings, int money, int gameTime, MapElement[][] map,
                     ArrayList<Structure> structures) {
        this.settings = settings;
        this.money = money;
        this.gameTime = gameTime;
        this.map = map;
        this.structures = structures;
    }

    // Getters
    public Settings getSettings() { return settings; }
    public int getMoney() { return money; }
    public int getGameTime() { return gameTime; }
    public MapElement[][] getMap() { return map; }
    public ArrayList<Structure> getStructures() { return structures; }

    // Setters
    public void setSettings(Settings settings) { this.settings = settings; }
    public void setMoney(int money) { this.money = money; }
    public void setGameTime(int gameTime) { this.gameTime = gameTime; }
    public void setMap(MapElement[][] map) { this.map = map; }
    public void setStructures(ArrayList<Structure> structures) { this.structures = structures; }
}
