package mad.citysimulator.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import mad.citysimulator.database.GameStateSchema.GameStateTable;
import mad.citysimulator.models.GameState;
import mad.citysimulator.models.Settings;

public class GameStateCursor extends CursorWrapper {

    public GameStateCursor(Cursor cursor) { super(cursor); }

    public GameState getGameState() {
        String saveName = getString(getColumnIndex(GameStateTable.Cols.SAVE_NAME));
        int mapWidth = getInt(getColumnIndex(GameStateTable.Cols.MAP_WIDTH));
        int mapHeight = getInt(getColumnIndex(GameStateTable.Cols.MAP_HEIGHT));
        int money = getInt(getColumnIndex(GameStateTable.Cols.MONEY));
        int initialMoney = getInt(getColumnIndex(GameStateTable.Cols.INITIAL_MONEY));
        int familySize = getInt(getColumnIndex(GameStateTable.Cols.FAMILY_SIZE));
        int shopSize = getInt(getColumnIndex(GameStateTable.Cols.SHOP_SIZE));
        int salary = getInt(getColumnIndex(GameStateTable.Cols.SALARY));
        double taxRate = getDouble(getColumnIndex(GameStateTable.Cols.TAX_RATE));
        int serviceCost = getInt(getColumnIndex(GameStateTable.Cols.SERVICE_COST));
        int houseBuildingCost = getInt(getColumnIndex(GameStateTable.Cols.HOUSE_BUILDING_COST));
        int commBuildingCost = getInt(getColumnIndex(GameStateTable.Cols.COMM_BUILDING_COST));
        int roadBuildingCost = getInt(getColumnIndex(GameStateTable.Cols.ROAD_BUILDING_COST));
        int gameTime = getInt(getColumnIndex(GameStateTable.Cols.GAME_TIME));

        Settings settings = new Settings(saveName, mapWidth, mapHeight, initialMoney, familySize, shopSize, salary,
                taxRate, serviceCost, houseBuildingCost, commBuildingCost, roadBuildingCost);
        return new GameState(settings, money, gameTime);
    }
}
