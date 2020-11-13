package mad.citysimulator.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mad.citysimulator.R;
import mad.citysimulator.database.DBManager;

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
    private DBManager dbManager;
    private Settings settings;
    private MapElement[][] map;
    private ArrayList<Structure> structures;
    private int money;
    private int gameTime;

    private static final int WATER = R.drawable.ic_water;
    private static final int[] GRASS = {R.drawable.ic_grass1, R.drawable.ic_grass2,
            R.drawable.ic_grass3, R.drawable.ic_grass4};

    private static final Random rng = new Random();

    // Default constructor
    protected GameData() {
        this.money = 0;
        this.gameTime = 0;
        this.map = null;
        this.structures = new ArrayList<>();
        this.settings = new Settings();
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
        updateSettings(gameState.getSettings());
        if(gameState.getMap() == null) {
            System.out.println(settings.getMapHeight() + "    " + settings.getMapWidth());
            this.map = generateMap(settings.getMapHeight(), settings.getMapWidth());
        }
        else {
            this.map = gameState.getMap();
        }
        if(gameState.getStructures() != null) {
            this.structures = gameState.getStructures();
            for(Structure structure : structures) {
                int row = structure.getRow();
                int col = structure.getCol();
                MapElement element = getElement(row, col);
                element.setStructure(structure);
                map[row][col] = element;
            }
        }
        this.money = gameState.getMoney();
        this.gameTime = gameState.getGameTime();
        saveGameState();
    }

    public void setImages(List<ImageHolder> images) {
        for(ImageHolder holder : images) {
            MapElement temp = map[holder.getRow()][holder.getCol()];
            temp.setImage(holder.getImage());
            map[holder.getRow()][holder.getCol()] = temp;
        }
    }

    public void setDbManager(DBManager dbManager) { this.dbManager = dbManager; }
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
    public int getPopulation() { return calcPopulation(); }
    public double getEmploymentRate() { return calcEmploymentRate()*100; }
    public int getRecentIncome() { return calcRecentIncome(); }
    public String getCityName() { return settings.getCityName(); }

    public int getNumResidential() {
        int numResidential = 0;
        for(Structure structure : structures) {
            if(structure.getStructureType() == StructureType.RESIDENTIAL) {
                numResidential++;
            }
        }
        return numResidential;
    }

    public int getNumCommercials() {
        int numCommercial = 0;
        for(Structure structure : structures) {
            if(structure.getStructureType() == StructureType.COMMERCIAL) {
                numCommercial++;
            }
        }
        return numCommercial;
    }

    public void incGameTime() {
        if(calcPopulation() > 0) {
            this.money += calcRecentIncome();
        }
        this.gameTime++;
        saveGameState();
    }

    public int buildStructure(MapElement element) {
        int shortChanged = 0;
        Structure structure = element.getStructure();
        switch (structure.getStructureType()) {
            case ROAD:
                shortChanged = settings.getRoadBuildingCost() - money;
                if(shortChanged <= 0){
                    map[structure.getRow()][structure.getCol()] = element;
                    this.money -= settings.getRoadBuildingCost();
                    this.structures.add(structure);
                    saveGameState();
                }
                break;
            case RESIDENTIAL:
                shortChanged = settings.getHouseBuildingCost() - money;
                if(shortChanged <= 0) {
                    map[structure.getRow()][structure.getCol()] = element;
                    this.money -= settings.getHouseBuildingCost();
                    this.structures.add(structure);
                    saveGameState();
                }
                break;
            case COMMERCIAL:
                shortChanged = settings.getCommBuildingCost() - money;
                if(shortChanged <= 0){
                    map[structure.getRow()][structure.getCol()] = element;
                    this.money -= settings.getCommBuildingCost();
                    this.structures.add(structure);
                    saveGameState();
                }
                break;
        }
        return shortChanged;
    }

    public void demolishStructure(MapElement element) {
        Structure structure = element.getStructure();
        structures.remove(structure);
        element.setStructure(null);
        map[structure.getRow()][structure.getCol()] = element;
        saveGameState();
    }

    public void updateSettings(Settings settings) {
        this.settings = settings;
        this.map = generateMap(settings.getMapHeight(), settings.getMapWidth());
        saveGameState();
    }

    public void updateElement(int row, int col, MapElement element) {
        if(this.map[row][col] != null) {
            this.map[row][col] = element;
        }
        // If element has an image save it.
        if(element.getImage() != null) {
            saveImage(element);
        }
        saveGameState();
    }

    public int calcPopulation() {
        int population = settings.getFamilySize() * getNumResidential();
        return population;
    }

    public double calcEmploymentRate() {
        double employmentRate = 0;
        if(calcPopulation() > 0) {
            employmentRate = Math.min(1.0, ((double) getNumCommercials() *
                    (double) settings.getShopSize()) / ((double) calcPopulation()));
        }
        return employmentRate;
    }

    public int calcRecentIncome() {
        int salary = settings.getSalary();
        double taxRate = settings.getTaxRate();
        int serviceCost = settings.getServiceCost();
        int income = (int) (getPopulation() * (calcEmploymentRate() * salary * taxRate - serviceCost));
        return income;
    }

    // Checks whether a given map location is adjacent to a road
    public boolean isAdjacentToRoad(int row, int col) {
        boolean isAdjacent = false;
        int rowAbove = row - 1;
        int rowBelow = row + 1;
        int colRight = col + 1;
        int colLeft = col - 1;

        for(Structure structure : structures) {
            if(structure.getStructureType() == StructureType.ROAD) {
                int structureCol = structure.getCol();
                int structureRow = structure.getRow();
                System.out.println(structureCol + " " + structureRow);
                // Above Case
                if(structureCol == col && structureRow == rowAbove) { isAdjacent = true; }
                // Below Case
                if(structureCol == col && structureRow == rowBelow) { isAdjacent = true; }
                // Right Case
                if(structureCol == colRight && structureRow == row) { isAdjacent = true; }
                // Left Case
                if(structureCol == colLeft && structureRow == row) { isAdjacent = true; }
            }
        }

        return isAdjacent;
    }

    public boolean isAdjacentToNonRoad(int row, int col) {
        boolean isAdjacent = false;
        int rowAbove = row - 1;
        int rowBelow = row + 1;
        int colRight = col + 1;
        int colLeft = col - 1;

        for(Structure structure : structures) {
            if(structure.getStructureType() == StructureType.COMMERCIAL ||
                    structure.getStructureType() == StructureType.RESIDENTIAL) {
                int structureCol = structure.getCol();
                int structureRow = structure.getRow();
                System.out.println(structureCol + " " + structureRow);
                // Above Case
                if(structureCol == col && structureRow == rowAbove) { isAdjacent = true; }
                // Below Case
                if(structureCol == col && structureRow == rowBelow) { isAdjacent = true; }
                // Right Case
                if(structureCol == colRight && structureRow == row) { isAdjacent = true; }
                // Left Case
                if(structureCol == colLeft && structureRow == row) { isAdjacent = true; }
            }
        }

        return isAdjacent;
    }

    private void saveGameState() {
        GameState gameState = new GameState(this.settings, this.money, this.gameTime, this.map,
                this.structures);
        dbManager.updateGameState(gameState);
    }

    private void saveImage(MapElement element) {
        Structure structure = element.getStructure();
        int row = structure.getRow();
        int col = structure.getCol();
        ImageHolder holder = new ImageHolder(row, col, element.getImage());
        dbManager.addImageHolder(holder);
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
