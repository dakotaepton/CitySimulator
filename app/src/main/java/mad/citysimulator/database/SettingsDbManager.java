package mad.citysimulator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import mad.citysimulator.models.Settings;
import mad.citysimulator.database.SettingsSchema.SettingsTable;

public class SettingsDbManager {

    // instance
    private static SettingsDbManager instance = null;

    // Map of {SAVE_NAME, Settings} to ensure save name uniqueness before even touching the db
    private HashMap<String, Settings> allSavedSettings;
    private SQLiteDatabase db;

    // Constructor
    protected SettingsDbManager() {
        this.db = null;
        this.allSavedSettings = new HashMap<>();
    }

    public static SettingsDbManager get() {
        if(instance == null) {
            instance = new SettingsDbManager();
        }
        return instance;
    }

    public void load(Context context) {
        this.db = new SettingsDbHelper(context.getApplicationContext()).getWritableDatabase();
        SettingsCursor cursor = new SettingsCursor(
            db.query(SettingsTable.NAME, null, null, null,
                    null, null, null)
        );
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Settings temp = cursor.getSettings();
                allSavedSettings.put(temp.getSaveName(), temp);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }

    // Returns false if the proposed save name already exists in db
    public boolean addSettings(Settings settings) {
        boolean success = false;
        if(!allSavedSettings.containsKey(settings.getSaveName())) {
            allSavedSettings.put(settings.getSaveName(), settings);
            ContentValues cv = populateContentValues(settings);
            db.insert(SettingsTable.NAME, null, cv);
            success = true;
        }
        return success;
    }

    public void updateSettings(Settings settings) {
        allSavedSettings.put(settings.getSaveName(), settings);
        ContentValues cv = populateContentValues(settings);
        String[] whereValue = { String.valueOf(settings.getSaveName())};
        db.update(SettingsTable.NAME, cv, SettingsTable.Cols.SAVE_NAME + " = ?", whereValue);
    }

    public void removeSettings(Settings settings) {
        allSavedSettings.remove(settings.getSaveName());
        String[] whereValue = { String.valueOf(settings.getSaveName())};
        db.delete(SettingsTable.NAME, SettingsTable.Cols.SAVE_NAME + " = ?", whereValue);
    }

    public Settings getSavedSetting(String saveName) { return allSavedSettings.get(saveName); }

    private ContentValues populateContentValues(Settings settings) {
        ContentValues cv = new ContentValues();
        cv.put(SettingsTable.Cols.SAVE_NAME, settings.getSaveName());
        cv.put(SettingsTable.Cols.MAP_WIDTH, settings.getMapWidth());
        cv.put(SettingsTable.Cols.MAP_HEIGHT, settings.getMapHeight());
        cv.put(SettingsTable.Cols.MONEY, settings.getMoney());
        cv.put(SettingsTable.Cols.INITIAL_MONEY, settings.getInitialMoney());
        cv.put(SettingsTable.Cols.FAMILY_SIZE, settings.getFamilySize());
        cv.put(SettingsTable.Cols.SHOP_SIZE, settings.getShopSize());
        cv.put(SettingsTable.Cols.SALARY, settings.getSalary());
        cv.put(SettingsTable.Cols.TAX_RATE, settings.getTaxRate());
        cv.put(SettingsTable.Cols.SERVICE_COST, settings.getServiceCost());
        cv.put(SettingsTable.Cols.HOUSE_BUILDING_COST, settings.getHouseBuildingCost());
        cv.put(SettingsTable.Cols.COMM_BUILDING_COST, settings.getCommBuildingCost());
        cv.put(SettingsTable.Cols.ROAD_BUILDING_COST, settings.getRoadBuildingCost());
        cv.put(SettingsTable.Cols.GAME_TIME, settings.getGameTime());
        return cv;
    }

}
