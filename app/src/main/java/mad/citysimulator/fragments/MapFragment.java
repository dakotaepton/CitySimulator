package mad.citysimulator.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import mad.citysimulator.R;
import mad.citysimulator.interfaces.SettingClickListener;
import mad.citysimulator.models.GameData;
import mad.citysimulator.models.MapElement;

public class MapFragment extends Fragment {


    private GameData gameData;

    private RecyclerView recycView;
    private RecyclerView.Adapter mAdaptor;
    private RecyclerView.LayoutManager layoutManager;

    public MapFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.gameData = GameData.get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {

        View view = inflater.inflate(R.layout.fragment_map, ui, false);
        recycView = (RecyclerView) view.findViewById(R.id.mapRecyclerView);
        layoutManager = new GridLayoutManager(
                getActivity(),
                gameData.getMapHeight(),
                GridLayoutManager.HORIZONTAL,
                false);

        recycView.setLayoutManager(layoutManager);
        mAdaptor = new MapRecycAdaptor();
        recycView.setAdapter(mAdaptor);

        return view;
    }

    //=======================================================================================
    //ADAPTOR
    private class MapRecycAdaptor extends RecyclerView.Adapter<MapViewHolder> {

        public MapRecycAdaptor() {}

        @Override
        public MapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(getActivity()); // <-- Fragment method
            return new MapViewHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(MapViewHolder holder, int position) {
            int row = position % GameData.get().getMapHeight();
            int col = position / GameData.get().getMapHeight();
            MapElement element = gameData.getElement(row,col);
            holder.bind(element);
        }

        @Override
        public int getItemCount() {
            return gameData.getMapHeight() * gameData.getMapWidth();
        }
    }

    //=======================================================================================
    //VIEW HOLDER
    private class MapViewHolder extends RecyclerView.ViewHolder {

        private ImageView topLeft;
        private ImageView topRight;
        private ImageView bottomLeft;
        private ImageView bottomRight;
        private ImageView fullCell;
        private MapElement element;

        public MapViewHolder(LayoutInflater li, ViewGroup parent) {
            super( li.inflate(R.layout.fragment_map_cell, parent, false) );

            int size = parent.getMeasuredHeight() / GameData.get().getMapHeight() + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;
            topLeft = itemView.findViewById(R.id.topLeft);
            topRight = itemView.findViewById(R.id.topRight);
            bottomLeft = itemView.findViewById(R.id.bottomLeft);
            bottomRight = itemView.findViewById(R.id.bottomRight);
            fullCell = itemView.findViewById(R.id.fullCell);
        }

        public void bind(MapElement element)
        {
            topLeft.setImageResource(element.getNorthWest());
            topRight.setImageResource(element.getNorthEast());
            bottomLeft.setImageResource(element.getSouthWest());
            bottomRight.setImageResource(element.getSouthEast());
            if(element.getStructure() != null)
            {
                fullCell.setImageResource(element.getStructure().getImageId());
            }
            this.element = element;
        }
    }
}