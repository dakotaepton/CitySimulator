package mad.citysimulator.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

import mad.citysimulator.R;
import mad.citysimulator.interfaces.DetailsClickListener;
import mad.citysimulator.interfaces.MapClickListener;
import mad.citysimulator.models.GameData;
import mad.citysimulator.models.MapElement;
import mad.citysimulator.models.Road;
import mad.citysimulator.models.Structure;

public class MapFragment extends Fragment {

    private RecyclerView recycView;
    private RecyclerView.LayoutManager layoutManager;

    private SelectorFragment selector;
    private List<MapClickListener> mapClickListeners;
    private DetailsClickListener detailsClickListener;

    // Required empty public constructor
    public MapFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        recycView = (RecyclerView) view.findViewById(R.id.mapRecyclerView);
        layoutManager = new GridLayoutManager(
                getActivity(),
                GameData.get().getMapHeight(),
                GridLayoutManager.HORIZONTAL,
                false);

        recycView.setLayoutManager(layoutManager);
        MapRecycAdaptor mAdaptor = new MapRecycAdaptor();
        recycView.setAdapter(mAdaptor);

        return view;
    }

    public void setBuilderSelector(SelectorFragment selector) { this.selector = selector; }

    // Map click listener functions
    public void addMapClickListener(MapClickListener listener) {
        if(this.mapClickListeners == null) {
            this.mapClickListeners = new LinkedList<>();
        }
        this.mapClickListeners.add(listener);
    }
    public void removeMapClickListener(MapClickListener listener) { this.mapClickListeners.remove(listener); }
    public void notifyMapClickListeners() {
        for(int i=0; i<mapClickListeners.size(); i++) {
            mapClickListeners.get(i).onBuild();
        }
    }

    // Details click listener
    public void addDetailsClickListener(DetailsClickListener listener) {this.detailsClickListener = listener; }
    public void removeDetailsClickListener() { this.detailsClickListener = null; }


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
            int row = position % GameData.get().getMapHeight();
            int col = position / GameData.get().getMapHeight();
            MapElement element = GameData.get().getElement(row,col);
            holder.bind(element);
        }

        @Override
        public int getItemCount() {
            return GameData.get().getMapHeight() * GameData.get().getMapWidth();
        }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public int getItemViewType(int position) { return position; }
    }

    //=======================================================================================
    //VIEW HOLDER
    private class MapViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView topLeft;
        private ImageView topRight;
        private ImageView bottomLeft;
        private ImageView bottomRight;
        private ImageView fullCell;
        private MapElement element;
        private RecyclerView.Adapter adapter;

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
            this.element = element;
            this.adapter = getBindingAdapter();
            if(element.getStructure() != null) {
                if(element.getImage() != null) {
                    fullCell.setImageBitmap(element.getImage());
                }
                else {
                    fullCell.setImageResource(element.getStructure().getImageId());
                }
            }
            topLeft.setImageResource(element.getNorthWest());
            topRight.setImageResource(element.getNorthEast());
            bottomLeft.setImageResource(element.getSouthWest());
            bottomRight.setImageResource(element.getSouthEast());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Structure currStructure = element.getStructure();
            Structure selectedStructure = selector.getSelectedStructure();

            // If user has a currently selected structure to build
            if(selectedStructure != null) {
                // Get element position info
                int position = getBindingAdapterPosition();
                int row = position % GameData.get().getMapHeight();
                int col = position / GameData.get().getMapHeight();
                // Check if structure already exists at that spot or if the map element is not buildable
                if(currStructure != null || !element.isBuildable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(),
                            R.style.AlertDialogCustom));
                    builder.setTitle("Uh oh!")
                            .setMessage("You can't build there.")
                            .setPositiveButton("OK", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(!GameData.get().isAdjacentToRoad(row, col) &&
                        (selectedStructure.getStructureType() != "Road")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(),
                            R.style.AlertDialogCustom));
                    builder.setTitle("Uh oh!")
                            .setMessage("You gotta build a road next to that first.")
                            .setPositiveButton("OK", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    MapElement tempElement = this.element;
                    Structure tempStructure = selectedStructure;
                    tempStructure.setRow(row);
                    tempStructure.setCol(col);
                    tempElement.setStructure(selectedStructure);
                    // Returns 0 or greater if the player can afford
                    int shortChanged = GameData.get().buildStructure(tempElement);
                    if(shortChanged <= 0) {
                        this.element = tempElement;
                        adapter.notifyItemChanged(getBindingAdapterPosition());
                        notifyMapClickListeners();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(),
                                R.style.AlertDialogCustom));
                        builder.setTitle("Uh oh!")
                                .setMessage("You don't have enough money to build that.\n(Need +$" + shortChanged + ")")
                                .setPositiveButton("OK", null);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
            else {
                if(currStructure != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(),
                            R.style.AlertDialogCustom));
                    builder.setTitle("OPTIONS")
                            .setPositiveButton("See Details", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    detailsClickListener.onDetailsClick(currStructure.getRow(),
                                            currStructure.getCol());
                                }
                            })
                            .setNeutralButton("Demolish", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    GameData.get().demolishStructure(element);
                                    element.setStructure(null);
                                    adapter.notifyItemChanged(getBindingAdapterPosition());
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
    }
}