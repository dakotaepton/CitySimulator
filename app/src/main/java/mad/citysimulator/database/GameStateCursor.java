package mad.citysimulator.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import mad.citysimulator.database.GameStateSchema.GameStateTable;
import mad.citysimulator.models.Commercial;
import mad.citysimulator.models.GameState;
import mad.citysimulator.models.MapElement;
import mad.citysimulator.models.Residential;
import mad.citysimulator.models.Road;
import mad.citysimulator.models.Settings;
import mad.citysimulator.models.Structure;

public class GameStateCursor extends CursorWrapper {

    public GameStateCursor(Cursor cursor) { super(cursor); }

    public GameState getGameState() {
        String saveName = getString(getColumnIndex(GameStateTable.Cols.SAVE_NAME));
        String cityName = getString(getColumnIndex(GameStateTable.Cols.CITY_NAME));
        String mapJson = getString(getColumnIndex(GameStateTable.Cols.MAP));
        String roadJson = getString(getColumnIndex(GameStateTable.Cols.ROADS));
        String resiJson = getString(getColumnIndex(GameStateTable.Cols.RESIDENTIALS));
        String commJson = getString(getColumnIndex(GameStateTable.Cols.COMMERCIALS));
        int mapWidth = getInt(getColumnIndex(GameStateTable.Cols.MAP_WIDTH));
        int mapHeight = getInt(getColumnIndex(GameStateTable.Cols.MAP_HEIGHT));
        int money = getInt(getColumnIndex(GameStateTable.Cols.MONEY));
        int initialMoney = getInt(getColumnIndex(GameStateTable.Cols.INITIAL_MONEY));
        int gameTime = getInt(getColumnIndex(GameStateTable.Cols.GAME_TIME));

        Settings settings = new Settings(saveName, cityName, mapWidth, mapHeight, initialMoney);

        MapElement[][] map = convertJsonToMap(mapJson);
        ArrayList<Road> roads = convertJsonToRoads(roadJson);
        ArrayList<Residential> residentials = convertJsonToResidentials(resiJson);
        ArrayList<Commercial> commercials = convertJsonToCommercials(commJson);

        return new GameState(settings, money, gameTime, map, roads, residentials, commercials);
    }

    private MapElement[][] convertJsonToMap(String mapJson) {
        Gson g = new Gson();
        MapElement[][] newMap = g.fromJson(mapJson, MapElement[][].class);
        return newMap;
    }

    private ArrayList<Road> convertJsonToRoads(String roadJson) {
        ArrayList<Road> roads = new ArrayList<>();
        Gson g = new Gson();
        Road[] roadArray = g.fromJson(roadJson, Road[].class);
        if(roadArray != null) {
            roads.addAll(Arrays.asList(roadArray));
        }
        return roads;
    }

    private ArrayList<Residential> convertJsonToResidentials(String resiJson) {
        ArrayList<Residential> residentials = new ArrayList<>();
        Gson g = new Gson();
        Residential[] resiArray = g.fromJson(resiJson, Residential[].class);
        if(resiArray != null) {
            residentials.addAll(Arrays.asList(resiArray));
        }
        return residentials;
    }

    private ArrayList<Commercial> convertJsonToCommercials(String commJson) {
        ArrayList<Commercial> commercials = new ArrayList<>();
        Gson g = new Gson();
        Commercial[] commArray = g.fromJson(commJson, Commercial[].class);
        if(commArray != null) {
            commercials.addAll(Arrays.asList(commArray));
        }
        return commercials;
    }
}

/*         int familySize = getInt(getColumnIndex(GameStateTable.Cols.FAMILY_SIZE));
        int shopSize = getInt(getColumnIndex(GameStateTable.Cols.SHOP_SIZE));
        int salary = getInt(getColumnIndex(GameStateTable.Cols.SALARY));
        double taxRate = getDouble(getColumnIndex(GameStateTable.Cols.TAX_RATE));
        int serviceCost = getInt(getColumnIndex(GameStateTable.Cols.SERVICE_COST));
        int houseBuildingCost = getInt(getColumnIndex(GameStateTable.Cols.HOUSE_BUILDING_COST));
        int commBuildingCost = getInt(getColumnIndex(GameStateTable.Cols.COMM_BUILDING_COST));
        int roadBuildingCost = getInt(getColumnIndex(GameStateTable.Cols.ROAD_BUILDING_COST));

 */