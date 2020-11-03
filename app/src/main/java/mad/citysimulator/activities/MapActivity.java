package mad.citysimulator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mad.citysimulator.R;
import mad.citysimulator.fragments.MapFragment;
import mad.citysimulator.fragments.SelectorFragment;
import mad.citysimulator.fragments.StatusFragment;
import mad.citysimulator.interfaces.DetailsClickListener;
import mad.citysimulator.interfaces.MapClickListener;
import mad.citysimulator.models.GameData;

public class MapActivity extends AppCompatActivity implements View.OnClickListener, MapClickListener,
        DetailsClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Set activity as onclick listener for run day btn
        Button incTimeBtn = findViewById(R.id.incTimeBtn);
        incTimeBtn.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();

        initStatusFragment(fm);

        MapFragment mapFrag = initMapFragment(fm);
        mapFrag.addMapClickListener(this);
        mapFrag.addDetailsClickListener(this);

        SelectorFragment selectorFrag = initSelectorFragment(fm);

        mapFrag.setBuilderSelector(selectorFrag);
        selectorFrag.setMapFragment(mapFrag);
    }

    private void initStatusFragment(FragmentManager fm) {
        StatusFragment statusFrag = (StatusFragment) fm.findFragmentById(R.id.statusFragment);
        if(statusFrag == null) {
            int gameTime = GameData.get().getGameTime();
            int money = GameData.get().getMoney();
            int income = GameData.get().getRecentIncome();
            int population = GameData.get().getPopulation();
            double employmentRate = GameData.get().getEmploymentRate();
            double temp = GameData.get().getTemperature();
            String cityName = GameData.get().getCityName();

            statusFrag = StatusFragment.newInstance(gameTime, money, income, population,
                    employmentRate, temp, cityName);
            fm.beginTransaction().add(R.id.statusFragmentPane, statusFrag).commit();
        }
    }

    private MapFragment initMapFragment(FragmentManager fm) {
        MapFragment mapFrag = (MapFragment) fm.findFragmentById(R.id.mapRecyclerView);
        if(mapFrag == null) {
            mapFrag = new MapFragment();
            fm.beginTransaction().add(R.id.mapFragmentPane, mapFrag).commit();
        }
        return mapFrag;
    }

    private SelectorFragment initSelectorFragment(FragmentManager fm) {
        SelectorFragment selectorFrag = (SelectorFragment) fm.findFragmentById(R.id.selectorRecyclerView);
        if(selectorFrag == null) {
            selectorFrag = new SelectorFragment();
            fm.beginTransaction().add(R.id.selectorFragmentPane, selectorFrag).commit();
        }
        return selectorFrag;
    }

    @Override
    public void onClick(View view) {
        GameData.get().incGameTime();
        FragmentManager fm = getSupportFragmentManager();
        initStatusFragment(fm);
    }

    @Override
    public void onBuild() {
        FragmentManager fm = getSupportFragmentManager();
        initStatusFragment(fm);
    }

    @Override
    public void onDetailsClick(int row, int col) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("row", row);
        intent.putExtra("col", col);
        startActivity(intent);
    }
}