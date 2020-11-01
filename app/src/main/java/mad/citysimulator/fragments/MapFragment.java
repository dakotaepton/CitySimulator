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
    private RecyclerView.LayoutManager layoutManager;

    // Required empty public constructor
    public MapFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        recycView = (RecyclerView) view.findViewById(R.id.mapRecyclerView);
        this.gameData = GameData.get();
        layoutManager = new GridLayoutManager(
                getActivity(),
                gameData.getMapHeight(),
                GridLayoutManager.HORIZONTAL,
                false);

        recycView.setLayoutManager(layoutManager);
        MapRecycAdaptor mAdaptor = new MapRecycAdaptor();
        recycView.setAdapter(mAdaptor);

        return view;
    }

    //=======================================================================================
    //ADAPTOR
    private class MapRecycAdaptor extends RecyclerView.Adapter<MapViewHolder> {

        public MapRecycAdaptor() { }

        @Override
        public MapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_map_cell, parent,false);
            MapViewHolder vh = new MapViewHolder(cellView);
            return vh;
        }

        @Override
        public void onBindViewHolder(MapViewHolder holder, int position) {
            int row = position % gameData.getMapHeight();
            int col = position / gameData.getMapHeight();
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

        public MapViewHolder(View view) {
            super(view);
            int size = getView().getMeasuredHeight() / GameData.get().getMapHeight() + 1;
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