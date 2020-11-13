package mad.citysimulator.models;

import java.util.ArrayList;
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
    private GameStateDbManager dbManager;
    private Settings settings;
    private MapElement[][] map;
    private ArrayList<Road> roads;
    private ArrayList<Residential> residentials;
    private ArrayList<Commercial> commercials;
    private int money;
    private int recentIncome;
    private int gameTime;
    private int population;
    private double employmentRate;

    private static final int WATER = R.drawable.ic_water;
    private static final int[] GRASS = {R.drawable.ic_grass1, R.drawable.ic_grass2,
            R.drawable.ic_grass3, R.drawable.ic_grass4};

    private static final Random rng = new Random();

    // Default constructor
    protected GameData() {
        this.money = 0;
        this.gameTime = 0;
        this.recentIncome = 0;
        this.employmentRate = 0;
        this.population = 0;
        this.map = null;
        this.roads = new ArrayList<>();
        this.residentials = new ArrayList<>();
        this.commercials = new ArrayList<>();
    }

    public static GameData get() {
        if(instance == null) {
            // Create new Game Data
            instance = new GameData();
        }
        return instance;
    }

    // Setters
    public void setGameState(GameState gameState) {
        this.settings = gameState.getSettings();
        if(gameState.getMap() == null) {
            this.map = generateMap(settings.getMapHeight(), settings.getMapWidth());
        }
        else {
            this.map = gameState.getMap();
        }
        if(gameState.getRoads() != null) {
            this.roads = gameState.getRoads();
            for(Road road : roads) {
                int row = road.getRow();
                int col = road.getCol();
                MapElement element = getElement(row, col);
                element.setStructure(road);
                map[row][col] = element;
            }
        }
        if(gameState.getCommercials() != null) {
            this.commercials = gameState.getCommercials();
            for(Commercial comm : commercials) {
                int row = comm.getRow();
                int col = comm.getCol();
                MapElement element = getElement(row, col);
                element.setStructure(comm);
                map[row][col] = element;
            }
        }
        if(gameState.getResidentials() != null) {
            this.residentials = gameState.getResidentials();
            for(Residential resi : residentials) {
                int row = resi.getRow();
                int col = resi.getCol();
                MapElement element = getElement(row, col);
                element.setStructure(resi);
                map[row][col] = element;
            }
        }
        this.money = gameState.getMoney();
        this.gameTime = gameState.getGameTime();
        saveGameState();
    }

    public void setDbManager(GameStateDbManager dbManager) { this.dbManager = dbManager; }
    public void setMoney(int money) { this.money = money; }
    public void setGameTime(int gameTime) { this.gameTime = gameTime; }
    public void regenerateMap(int height, int width)  { this.map = generateMap(height, width); }
    public void setMap(MapElement[][] map) { this.map = map; }

    // Getters
    public int getMoney() { return money; }
    public int getGameTime() { return gameTime; }
    public Settings getSettings() { return settings; }
    public MapElement getElement(int i, int j) { return map[i][j]; }
    public int getMapHeight() { return settings.getMapHeight(); }
    public int getMapWidth() { return settings.getMapWidth(); }
    public int getPopulation() { return population; }
    public double getEmploymentRate() { return employmentRate; }
    public int getRecentIncome() { return recentIncome; }
    public String getCityName() { return settings.getCityName(); }
    public int getNumResidential() { return residentials.size(); }
    public int getNumCommercials() { return commercials.size(); }

    public void incGameTime() {
        this.population = calcPopulation();
        this.employmentRate = 0;
        if(population > 0) {
            this.employmentRate = calcEmploymentRate();
        }
        int salary = settings.getSalary();
        double taxRate = settings.getTaxRate();
        int serviceCost = settings.getServiceCost();
        this.recentIncome = (int) (population * (employmentRate * salary * taxRate - serviceCost));
        this.money += recentIncome;
        this.gameTime++;
    }

    public int buildStructure(MapElement element) {
        int shortChanged = 0;
        Structure structure = element.getStructure();
        if(structure instanceof Residential) {
            shortChanged = settings.getHouseBuildingCost() - money;
            if(shortChanged <= 0) {
                map[structure.getRow()][structure.getCol()] = element;
                this.money -= settings.getHouseBuildingCost();
                this.residentials.add((Residential) structure);
                saveGameState();
            }
        }
        else if(structure instanceof Commercial) {
            shortChanged = settings.getCommBuildingCost() - money;
            if(shortChanged <= 0){
                map[structure.getRow()][structure.getCol()] = element;
                this.money -= settings.getCommBuildingCost();
                this.commercials.add((Commercial) structure);
                saveGameState();
            }
        }
        else if(structure instanceof Road) {
            shortChanged = settings.getRoadBuildingCost() - money;
            if(shortChanged <= 0){
                map[structure.getRow()][structure.getCol()] = element;
                this.money -= settings.getRoadBuildingCost();
                this.roads.add((Road) structure);
                saveGameState();
            }
        }
        return shortChanged;
    }

    public void demolishStructure(MapElement element) {
        Structure structure = element.getStructure();
        if(structure instanceof Residential) {
            residentials.remove(structure);
        }
        else if (structure instanceof Commercial) {
            commercials.remove(structure);
        }
        else if (structure instanceof Road) {
            roads.remove(structure);
        }
        map[structure.getRow()][structure.getCol()] = element;
        saveGameState();
    }

    public void updateSettings(Settings settings) {
        this.settings = settings;
        saveGameState();
    }

    public void updateElement(int row, int col, MapElement element) {
        if(this.map[row][col] != null) {
            this.map[row][col] = element;
        }
        saveGameState();
    }

    public int calcPopulation() {
        int population = settings.getFamilySize() * getNumResidential();
        return population;
    }

    public double calcEmploymentRate() {
        int employmentRate = 0;
        if(calcPopulation() > 0) {
            employmentRate = Math.min(1, getNumCommercials() * settings.getShopSize() / calcPopulation());
        }
        return employmentRate;
    }

    // Checks whether a given map location is adjacent to a road
    public boolean isAdjacentToRoad(int row, int col) {
        boolean isAdjacent = false;
        int rowAbove = row - 1;
        int rowBelow = row + 1;
        int colRight = col + 1;
        int colLeft = col - 1;

        for(Road road : roads) {
            int roadCol = road.getCol();
            int roadRow = road.getRow();
            System.out.println(roadCol + " " + roadRow);
            // Above Case
            if(roadCol == col && roadRow == rowAbove) { isAdjacent = true; }
            // Below Case
            if(roadCol == col && roadRow == rowBelow) { isAdjacent = true; }
            // Right Case
            if(roadCol == colRight && roadRow == row) { isAdjacent = true; }
            // Left Case
            if(roadCol == colLeft && roadRow == row) { isAdjacent = true; }
        }

        return isAdjacent;
    }

    private void saveGameState() {
        GameState gameState = new GameState(this.settings, this.money, this.gameTime, this.map,
                this.roads, this.residentials, this.commercials);
        dbManager.updateGameState(gameState);
    }

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
                                    R.drawable.ic_coast_southeast, R.drawable.ic_coast_southeast_concave)
                    );
                }
                else
                {
                    map[i][j] = new MapElement(
                            false, WATER, WATER, WATER, WATER);
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

}
