package mad.citysimulator.models;

import mad.citysimulator.database.GameStateDbManager;

public class GameState {

    // All Game State Data
    private Settings settings;
    private int money;
    private int gameTime;


    // Default constructor
    public GameState() {
        this.settings = new Settings();
        this.money = this.settings.getInitialMoney();
        this.gameTime = 0;
    }

    // Alternate constructor
    public GameState(Settings settings, int money, int gameTime) {
        this.settings = settings;
        this.money = money;
        this.gameTime = gameTime;
    }

    // Getters
    public Settings getSettings() { return settings; }
    public int getMoney() { return money; }
    public int getGameTime() { return gameTime; }

    // Setters
    public void setSettings(Settings settings) { this.settings = settings; }
    public void setMoney(int money) { this.money = money; }
    public void setGameTime(int gameTime) { this.gameTime = gameTime; }
}
