package mad.citysimulator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mad.citysimulator.models.Commercial;
import mad.citysimulator.models.GameState
;
import mad.citysimulator.database.GameStateSchema.GameStateTable;
import mad.citysimulator.models.MapElement;
import mad.citysimulator.models.Residential;
import mad.citysimulator.models.Road;
import mad.citysimulator.models.Settings;
import mad.citysimulator.models.Structure;

public class GameStateDbManager {

    // Map of {SAVE_NAME, GameState} to ensure save name uniqueness before even touching the db
    private HashMap<String, GameState> allSavedGameState;
;
    private SQLiteDatabase db;

    // Constructor
    public GameStateDbManager() {
        this.db = null;
        this.allSavedGameState = new HashMap<>();
    }

    public void load(Context context) {
        this.db = new GameStateDbHelper(context.getApplicationContext()).getWritableDatabase();
        GameStateCursor cursor = new GameStateCursor(
            db.query(GameStateTable.NAME, null, null, null,
                    null, null, null)
        );
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                GameState temp = cursor.getGameState();
                allSavedGameState.put(temp.getSettings().getSaveName(), temp);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    // Returns false if the proposed save name already exists in db
    public boolean addGameState(GameState gameState) {
        boolean success = false;
        if(!allSavedGameState.containsKey(gameState.getSettings().getSaveName())) {
            allSavedGameState.put(gameState.getSettings().getSaveName(), gameState);
            ContentValues cv = populateContentValues(gameState);
            db.insert(GameStateTable.NAME, null, cv);
            success = true;
        }
        return success;
    }

    public void updateGameState(GameState gameState) {
        allSavedGameState.put(gameState.getSettings().getSaveName(), gameState);
        ContentValues cv = populateContentValues(gameState);
        String[] whereValue = { String.valueOf(gameState.getSettings().getSaveName())};
        db.update(GameStateTable.NAME, cv, GameStateTable.Cols.SAVE_NAME + " = ?", whereValue);
    }

    public void removeGameState(GameState gameState) {
        allSavedGameState.remove(gameState.getSettings().getSaveName());
        String[] whereValue = { String.valueOf(gameState.getSettings().getSaveName())};
        db.delete(GameStateTable.NAME, GameStateTable.Cols.SAVE_NAME + " = ?", whereValue);
    }

    public GameState getSavedState(String saveName) { return allSavedGameState.get(saveName); }

    private ContentValues populateContentValues(GameState gameState) {
        // Convert map data to Json string for storage
        String mapJson = convertMapToJson(gameState.getMap());
        String roadJson = convertRoadsToJson(gameState.getRoads());
        String commJson = convertCommercialsToJson(gameState.getCommercials());
        String resiJson = convertResidentialsToJson(gameState.getResidentials());
        Settings settings = gameState.getSettings();

        ContentValues cv = new ContentValues();
        cv.put(GameStateTable.Cols.SAVE_NAME, settings.getSaveName());
        cv.put(GameStateTable.Cols.CITY_NAME, settings.getCityName());
        cv.put(GameStateTable.Cols.MAP, mapJson);
        cv.put(GameStateTable.Cols.ROADS, roadJson);
        cv.put(GameStateTable.Cols.COMMERCIALS, commJson);
        cv.put(GameStateTable.Cols.RESIDENTIALS, resiJson);
        cv.put(GameStateTable.Cols.MAP_WIDTH, settings.getMapWidth());
        cv.put(GameStateTable.Cols.MAP_HEIGHT, settings.getMapHeight());
        cv.put(GameStateTable.Cols.MONEY, gameState.getMoney());
        cv.put(GameStateTable.Cols.INITIAL_MONEY, settings.getInitialMoney());
        cv.put(GameStateTable.Cols.GAME_TIME, gameState.getGameTime());
        return cv;
    }

    private String convertRoadsToJson(ArrayList<Road> roads) {
        Gson g = new Gson();
        Road[] roadArray = new Road[roads.size()];
        roadArray = roads.toArray(roadArray);
        String roadJson = g.toJson(roadArray);
        return roadJson;
    }

    private String convertCommercialsToJson(ArrayList<Commercial> commercials) {
        Gson g = new Gson();
        Commercial[] commArray = new Commercial[commercials.size()];
        commArray = commercials.toArray(commArray);
        String commJson = g.toJson(commArray);
        return commJson;
    }

    private String convertResidentialsToJson(ArrayList<Residential> residentials) {
        Gson g = new Gson();
        Residential[] resiArray = new Residential[residentials.size()];
        resiArray = residentials.toArray(resiArray);
        String resiJson = g.toJson(resiArray);
        return resiJson;
    }

    private String convertMapToJson(MapElement[][] map) {
        Gson g = new Gson();
        String json = g.toJson(map);
        return json;
    }
}
/*      FULL SETTINGS
        cv.put(GameStateTable.Cols.FAMILY_SIZE, settings.getFamilySize());
        cv.put(GameStateTable.Cols.SHOP_SIZE, settings.getShopSize());
        cv.put(GameStateTable.Cols.SALARY, settings.getSalary());
        cv.put(GameStateTable.Cols.TAX_RATE, settings.getTaxRate());
        cv.put(GameStateTable.Cols.SERVICE_COST, settings.getServiceCost());
        cv.put(GameStateTable.Cols.HOUSE_BUILDING_COST, settings.getHouseBuildingCost());
        cv.put(GameStateTable.Cols.COMM_BUILDING_COST, settings.getCommBuildingCost());
        cv.put(GameStateTable.Cols.ROAD_BUILDING_COST, settings.getRoadBuildingCost());

 */