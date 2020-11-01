package mad.citysimulator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import mad.citysimulator.models.GameState
;
import mad.citysimulator.database.GameStateSchema.GameStateTable;
import mad.citysimulator.models.Settings;

public class GameStateDbManager {

    // instance
    private static GameStateDbManager instance = null;

    // Map of {SAVE_NAME, GameState} to ensure save name uniqueness before even touching the db
    private HashMap<String, GameState> allSavedGameState;
;
    private SQLiteDatabase db;

    // Constructor
    protected GameStateDbManager() {
        this.db = null;
        this.allSavedGameState = new HashMap<>();
    }

    public static GameStateDbManager get() {
        if(instance == null) {
            instance = new GameStateDbManager();
        }
        return instance;
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
    public boolean addGameState(GameState gameState
) {
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
        Settings settings = gameState.getSettings();
        ContentValues cv = new ContentValues();
        cv.put(GameStateTable.Cols.SAVE_NAME, settings.getSaveName());
        cv.put(GameStateTable.Cols.MAP_WIDTH, settings.getMapWidth());
        cv.put(GameStateTable.Cols.MAP_HEIGHT, settings.getMapHeight());
        cv.put(GameStateTable.Cols.MONEY, gameState.getMoney());
        cv.put(GameStateTable.Cols.INITIAL_MONEY, settings.getInitialMoney());
        cv.put(GameStateTable.Cols.FAMILY_SIZE, settings.getFamilySize());
        cv.put(GameStateTable.Cols.SHOP_SIZE, settings.getShopSize());
        cv.put(GameStateTable.Cols.SALARY, settings.getSalary());
        cv.put(GameStateTable.Cols.TAX_RATE, settings.getTaxRate());
        cv.put(GameStateTable.Cols.SERVICE_COST, settings.getServiceCost());
        cv.put(GameStateTable.Cols.HOUSE_BUILDING_COST, settings.getHouseBuildingCost());
        cv.put(GameStateTable.Cols.COMM_BUILDING_COST, settings.getCommBuildingCost());
        cv.put(GameStateTable.Cols.ROAD_BUILDING_COST, settings.getRoadBuildingCost());
        cv.put(GameStateTable.Cols.GAME_TIME, gameState.getGameTime());
        return cv;
    }

}
