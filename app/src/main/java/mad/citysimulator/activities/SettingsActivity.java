package mad.citysimulator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import mad.citysimulator.R;
import mad.citysimulator.database.SettingsDbManager;
import mad.citysimulator.fragments.SettingsFragment;
import mad.citysimulator.interfaces.SettingClickListener;
import mad.citysimulator.models.GameData;
import mad.citysimulator.models.Settings;

public class SettingsActivity extends AppCompatActivity implements SettingClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Settings currentSettings = GameData.get().getSettings();

        initSettingsFragment(currentSettings);
    }

    private void initSettingsFragment(Settings currentSettings) {
        FragmentManager fm = getSupportFragmentManager();
        SettingsFragment settingsFragment = (SettingsFragment) fm.findFragmentById(R.id.settingsFragment);
        if(settingsFragment == null) {
            int curMapHeight = currentSettings.getMapHeight();
            int curMapWidth = currentSettings.getMapWidth();
            int curInitialMoney = currentSettings.getInitialMoney();
            settingsFragment = SettingsFragment.newInstance(curMapWidth, curMapHeight, curInitialMoney);
            settingsFragment.setSettingClickListener(this);
            fm.beginTransaction().add(R.id.settingsFragmentPane, settingsFragment).commit();
        }
    }

    @Override
    public void onSaveSettingClick(int width, int height, int initialMoney) {
        // Update current game data settings
        Settings settings = GameData.get().getSettings();
        settings.setMapWidth(width);
        settings.setMapHeight(height);
        settings.setInitialMoney(initialMoney);
        GameData.get().setGameState(settings);
        // Create intent for back to title screen
        Intent intent = new Intent(this, TitleActivity.class);
        startActivity(intent);
    }
}