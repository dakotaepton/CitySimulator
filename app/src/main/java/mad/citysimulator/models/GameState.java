package mad.citysimulator.models;

import java.util.LinkedList;
import java.util.List;

import mad.citysimulator.database.GameStateDbManager;

public class GameState {

    // All Game State Data
    private Settings settings;
    private int money;
    private int gameTime;
    private MapElement[][] map;


    // Default constructor
    public GameState() {
        this.settings = new Settings();
        this.money = this.settings.getInitialMoney();
        this.gameTime = 0;
        this.map = null;
    }

    // Alternate constructor
    public GameState(Settings settings, int money, int gameTime, MapElement[][] map) {
        this.settings = settings;
        this.money = money;
        this.gameTime = gameTime;
        this.map = map;
    }

    // Getters
    public Settings getSettings() { return settings; }
    public int getMoney() { return money; }
    public int getGameTime() { return gameTime; }
    public MapElement[][] getMap() { return map; }


    // Setters
    public void setSettings(Settings settings) { this.settings = settings; }
    public void setMoney(int money) { this.money = money; }
    public void setGameTime(int gameTime) { this.gameTime = gameTime; }
    public void setMap(MapElement[][] map) { this.map = map; }
}
