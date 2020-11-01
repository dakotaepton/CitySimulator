package mad.citysimulator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import mad.citysimulator.R;
import mad.citysimulator.fragments.MapFragment;
import mad.citysimulator.fragments.SelectorFragment;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        FragmentManager fm = getSupportFragmentManager();

        MapFragment mapFrag = initMapFragment(fm);
        SelectorFragment selectorFrag = initSelectorFragment(fm);

        mapFrag.setBuilderSelector(selectorFrag);
        selectorFrag.setMapFragment(mapFrag);
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
}