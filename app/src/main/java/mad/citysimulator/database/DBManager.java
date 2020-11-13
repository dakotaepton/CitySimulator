package mad.citysimulator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import mad.citysimulator.database.DBSchema.BitmapImageTable;
import mad.citysimulator.models.GameState
;
import mad.citysimulator.database.DBSchema.GameStateTable;
import mad.citysimulator.models.ImageHolder;
import mad.citysimulator.models.MapElement;
import mad.citysimulator.models.Settings;
import mad.citysimulator.models.Structure;

public class DBManager {

    // Map of {SAVE_NAME, GameState} to ensure save name uniqueness before even touching the db
    private HashMap<String, GameState> allSavedGameState;
    private List<ImageHolder> allSavedImages;

    private SQLiteDatabase db;

    // Constructor
    public DBManager() {
        this.db = null;
        this.allSavedGameState = new HashMap<>();
        this.allSavedImages = new LinkedList<>();
    }

    public void load(Context context) {
        this.db = new DBHelper(context.getApplicationContext()).getWritableDatabase();
        // Load game state
        DBCursor stateCursor = new DBCursor(
            db.query(GameStateTable.NAME, null, null, null,
                    null, null, null)
        );
        try {
            stateCursor.moveToFirst();
            while(!stateCursor.isAfterLast()) {
                GameState temp = stateCursor.getGameState();
                allSavedGameState.put(temp.getSettings().getSaveName(), temp);
                stateCursor.moveToNext();
            }
        } finally {
            stateCursor.close();
        }

        // Load all images
        DBCursor imageCursor = new DBCursor(
                db.query(BitmapImageTable.NAME, null, null, null,
                        null, null, null)
        );
        try {
            imageCursor.moveToFirst();
            while(!imageCursor.isAfterLast()) {
                ImageHolder temp = imageCursor.getImageHolder();
                allSavedImages.add(temp);
                imageCursor.moveToNext();
            }
        } finally {
            imageCursor.close();
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

    public void addImageHolder(ImageHolder inHolder) {
        for(ImageHolder holder : allSavedImages) {
            // Remove existing entry if there is one for that location
            if(holder.getRow() == inHolder.getRow() && holder.getCol() == inHolder.getCol()) {
                allSavedImages.remove(holder);
            }
        }
        allSavedImages.add(inHolder);

        // Convert image bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        inHolder.getImage().compress(Bitmap.CompressFormat.JPEG, 0, stream);
        byte[] byteArray = stream.toByteArray();

        ContentValues cv = new ContentValues();
        cv.put(BitmapImageTable.Cols.ROW_NUM, inHolder.getRow());
        cv.put(BitmapImageTable.Cols.COL_NUM, inHolder.getCol());
        cv.put(BitmapImageTable.Cols.IMAGE_BLOB, byteArray);
        db.insert(BitmapImageTable.NAME, null, cv);
    }

    public void removeImageHolder(ImageHolder imageHolder) {
        allSavedGameState.remove(imageHolder);
        String[] values = { String.valueOf(imageHolder.getRow()), String.valueOf(imageHolder.getCol()) };

        db.delete(BitmapImageTable.NAME, BitmapImageTable.Cols.ROW_NUM + " = ? AND " +
               BitmapImageTable.Cols.COL_NUM + " = ?", values);
    }

    public void removeAllImages() {
        for(ImageHolder holder : allSavedImages) {
            removeImageHolder(holder);
        }
    }

    public ImageHolder getImageHolder(int row, int col) {
        ImageHolder found = null;
        for(ImageHolder holder : allSavedImages) {
            if(holder.getRow() == row && holder.getCol() == col) {
                found = holder;
            }
        }
        return found;
    }

    public List<ImageHolder> getAllImages() { return allSavedImages; }

    private ContentValues populateContentValues(GameState gameState) {
        // Convert map data to Json string for storage
        String mapJson = convertMapToJson(gameState.getMap());
        String structureJson = convertStructuresToJson(gameState.getStructures());
        Settings settings = gameState.getSettings();

        ContentValues cv = new ContentValues();
        cv.put(GameStateTable.Cols.SAVE_NAME, settings.getSaveName());
        cv.put(GameStateTable.Cols.CITY_NAME, settings.getCityName());
        cv.put(GameStateTable.Cols.MAP, mapJson);
        cv.put(GameStateTable.Cols.STRUCTURES, structureJson);
        cv.put(GameStateTable.Cols.MAP_WIDTH, settings.getMapWidth());
        cv.put(GameStateTable.Cols.MAP_HEIGHT, settings.getMapHeight());
        cv.put(GameStateTable.Cols.MONEY, gameState.getMoney());
        cv.put(GameStateTable.Cols.INITIAL_MONEY, settings.getInitialMoney());
        cv.put(GameStateTable.Cols.GAME_TIME, gameState.getGameTime());
        return cv;
    }

    private String convertStructuresToJson(ArrayList<Structure> structures) {
        Gson g = new Gson();
        Structure[] structuresArray = new Structure[structures.size()];
        structuresArray = structures.toArray(structuresArray);
        String roadJson = g.toJson(structuresArray);
        return roadJson;
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