package mad.citysimulator.models;

import java.util.Random;

import mad.citysimulator.R;
import mad.citysimulator.database.GameStateDbManager;

/**
 * Represents the overall map, and contains a map of MapElement objects (accessible using the
 * get(row, col) method). The two static constants width and height indicate the size of the map.
 *
 * There is a static get() method to be used to obtain an instance (rather than calling the
 * constructor directly).
 *
 * There is also a regenerate() method. The map is randomly-generated, and this method will invoke
 * the algorithm again to replace all the map data with a new randomly-generated map.
 */
public class GameData
{
    private static GameData instance = null;
    private Settings settings;
    private MapElement[][] map;
    private int money;
    private int recentIncome;
    private int population;
    private int gameTime;
    private double employmentRate;
    private String cityName = "Perth";
    private double temperature = 32.0;
    private int nResidential = 0;
    private int nCommercial = 0;

    private static final int WATER = R.drawable.ic_water;
    private static final int[] GRASS = {R.drawable.ic_grass1, R.drawable.ic_grass2,
            R.drawable.ic_grass3, R.drawable.ic_grass4};

    private static final Random rng = new Random();

    protected GameData(GameState gameState) {
        setGameState(gameState);
    }

    public static GameData get() {
        if(instance == null)
        {
            // Create new Game Data with default game state
            instance = new GameData(new GameState());
        }
        return instance;
    }

    // Setters
    public void setGameState(GameState gameState) {
        this.settings = gameState.getSettings();
        this.map = generateMap(settings.getMapHeight(), settings.getMapWidth());
        this.money = gameState.getMoney();
        this.gameTime = gameState.getGameTime();
        saveGameState();
    }

    public void setMoney(int money) { this.money = money; }
    public void setGameTime(int gameTime) { this.gameTime = gameTime; }
    public void regenerateMap(int height, int width)  { this.map = generateMap(height, width); }
    public void setMap(MapElement[][] map) { this.map = map; }

    public void setElement(int row, int col, MapElement element) {
        map[row][col] = element;
        saveGameState();
    }

    // Getters
    public int getMoney() { return money; }
    public int getGameTime() { return gameTime; }
    public Settings getSettings() { return settings; }
    public MapElement getElement(int i, int j) { return map[i][j]; }
    public int getMapHeight() { return settings.getMapHeight(); }
    public int getMapWidth() { return settings.getMapWidth(); }
    public int getPopulation() {
        this.population = settings.getFamilySize() * this.nResidential;
        return population;
    }
    public int getRecentIncome() { return recentIncome; }
    public double getEmploymentRate() {
        //this.employmentRate = Math.min(1, nCommercial * settings.getShopSize() / getPopulation());
        return employmentRate;
    }
    public String getCityName() { return cityName; }
    public double getTemperature() { return temperature; }

    // Map stuff
    private static MapElement[][] generateMap(int height, int width)
    {
        final int HEIGHT_RANGE = 256;
        final int WATER_LEVEL = 112;
        final int INLAND_BIAS = 24;
        final int AREA_SIZE = 1;
        final int SMOOTHING_ITERATIONS = 2;

        int[][] heightField = new int[height][width];
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                heightField[i][j] =
                        rng.nextInt(HEIGHT_RANGE)
                                + INLAND_BIAS * (
                                Math.min(Math.min(i, j), Math.min(height - i - 1, width - j - 1)) -
                                        Math.min(height, width) / 4);
            }
        }

        int[][] newHf = new int[height][width];
        for(int s = 0; s < SMOOTHING_ITERATIONS; s++)
        {
            for(int i = 0; i < height; i++)
            {
                for(int j = 0; j < width; j++)
                {
                    int areaSize = 0;
                    int heightSum = 0;

                    for(int areaI = Math.max(0, i - AREA_SIZE);
                        areaI < Math.min(height, i + AREA_SIZE + 1);
                        areaI++)
                    {
                        for(int areaJ = Math.max(0, j - AREA_SIZE);
                            areaJ < Math.min(width, j + AREA_SIZE + 1);
                            areaJ++)
                        {
                            areaSize++;
                            heightSum += heightField[areaI][areaJ];
                        }
                    }

                    newHf[i][j] = heightSum / areaSize;
                }
            }

            int[][] tmpHf = heightField;
            heightField = newHf;
            newHf = tmpHf;
        }

        MapElement[][] map = new MapElement[height][width];
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                MapElement element;

                if(heightField[i][j] >= WATER_LEVEL)
                {
                    boolean waterN = (i == 0)          || (heightField[i - 1][j] < WATER_LEVEL);
                    boolean waterE = (j == width - 1)  || (heightField[i][j + 1] < WATER_LEVEL);
                    boolean waterS = (i == height - 1) || (heightField[i + 1][j] < WATER_LEVEL);
                    boolean waterW = (j == 0)          || (heightField[i][j - 1] < WATER_LEVEL);

                    boolean waterNW = (i == 0) ||          (j == 0) ||         (heightField[i - 1][j - 1] < WATER_LEVEL);
                    boolean waterNE = (i == 0) ||          (j == width - 1) || (heightField[i - 1][j + 1] < WATER_LEVEL);
                    boolean waterSW = (i == height - 1) || (j == 0) ||         (heightField[i + 1][j - 1] < WATER_LEVEL);
                    boolean waterSE = (i == height - 1) || (j == width - 1) || (heightField[i + 1][j + 1] < WATER_LEVEL);

                    boolean coast = waterN || waterE || waterS || waterW ||
                            waterNW || waterNE || waterSW || waterSE;

                    map[i][j] = new MapElement(
                            !coast,
                            choose(waterN, waterW, waterNW,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_northwest, R.drawable.ic_coast_northwest_concave),
                            choose(waterN, waterE, waterNE,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_northeast, R.drawable.ic_coast_northeast_concave),
                            choose(waterS, waterW, waterSW,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_southwest, R.drawable.ic_coast_southwest_concave),
                            choose(waterS, waterE, waterSE,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_southeast, R.drawable.ic_coast_southeast_concave),
                            null);
                }
                else
                {
                    map[i][j] = new MapElement(
                            false, WATER, WATER, WATER, WATER, null);
                }
            }
        }
        return map;
    }

    private static int choose(boolean nsWater, boolean ewWater, boolean diagWater,
                              int nsCoastId, int ewCoastId, int convexCoastId, int concaveCoastId)
    {
        int id;
        if(nsWater)
        {
            if(ewWater)
            {
                id = convexCoastId;
            }
            else
            {
                id = nsCoastId;
            }
        }
        else
        {
            if(ewWater)
            {
                id = ewCoastId;
            }
            else if(diagWater)
            {
                id = concaveCoastId;
            }
            else
            {
                id = GRASS[rng.nextInt(GRASS.length)];
            }
        }
        return id;
    }

    private void saveGameState() {
        GameState gameState = new GameState(this.settings, this.money, this.gameTime);
        GameStateDbManager.get().updateGameState(gameState);
    }

    public void updateSettings(Settings settings) {
        this.settings = settings;
        saveGameState();
    }
}
