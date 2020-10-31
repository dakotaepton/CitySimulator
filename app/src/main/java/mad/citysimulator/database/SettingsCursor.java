package mad.citysimulator.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import mad.citysimulator.database.SettingsSchema.SettingsTable;
import mad.citysimulator.models.Settings;

public class SettingsCursor extends CursorWrapper {

    public SettingsCursor(Cursor cursor) { super(cursor); }

    public Settings getSettings() {
        String saveName = getString(getColumnIndex(SettingsTable.Cols.SAVE_NAME));
        int mapWidth = getInt(getColumnIndex(SettingsTable.Cols.MAP_WIDTH));
        int mapHeight = getInt(getColumnIndex(SettingsTable.Cols.MAP_HEIGHT));
        int money = getInt(getColumnIndex(SettingsTable.Cols.MONEY));
        int initialMoney = getInt(getColumnIndex(SettingsTable.Cols.INITIAL_MONEY));
        int familySize = getInt(getColumnIndex(SettingsTable.Cols.FAMILY_SIZE));
        int shopSize = getInt(getColumnIndex(SettingsTable.Cols.SHOP_SIZE));
        int salary = getInt(getColumnIndex(SettingsTable.Cols.SALARY));
        double taxRate = getDouble(getColumnIndex(SettingsTable.Cols.TAX_RATE));
        int serviceCost = getInt(getColumnIndex(SettingsTable.Cols.SERVICE_COST));
        int houseBuildingCost = getInt(getColumnIndex(SettingsTable.Cols.HOUSE_BUILDING_COST));
        int commBuildingCost = getInt(getColumnIndex(SettingsTable.Cols.COMM_BUILDING_COST));
        int roadBuildingCost = getInt(getColumnIndex(SettingsTable.Cols.ROAD_BUILDING_COST));
        int gameTime = getInt(getColumnIndex(SettingsTable.Cols.GAME_TIME));
        return new Settings(saveName, mapWidth, mapHeight, money, initialMoney, familySize, shopSize, salary,
                taxRate, serviceCost, houseBuildingCost, commBuildingCost, roadBuildingCost, gameTime);
    }
}
