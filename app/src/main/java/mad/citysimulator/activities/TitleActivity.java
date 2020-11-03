package mad.citysimulator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mad.citysimulator.R;
import mad.citysimulator.database.GameStateDbManager;
import mad.citysimulator.models.GameData;
import mad.citysimulator.models.GameState;
import mad.citysimulator.models.Settings;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        Button mapBtn = findViewById(R.id.mapBtn);
        Button settingsBtn = findViewById(R.id.settingBtn);
        mapBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);


        // Load DB Manager
        GameStateDbManager.get().load(getApplicationContext());
        GameState gameState = GameStateDbManager.get().getSavedState("DEFAULT");

        // If null no default save stored so make one
        if(gameState == null) {
            gameState = new GameState();
            GameStateDbManager.get().addGameState(gameState);
        }
        GameData.get().setGameState(gameState);
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.mapBtn:
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.settingBtn:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
    }
}