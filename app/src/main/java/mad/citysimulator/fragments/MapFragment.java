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

import mad.citysimulator.R;
import mad.citysimulator.interfaces.MapClickListener;
import mad.citysimulator.models.GameData;
import mad.citysimulator.models.MapElement;
import mad.citysimulator.models.Structure;

public class MapFragment extends Fragment {



    private RecyclerView recycView;
    private RecyclerView.LayoutManager layoutManager;

    private SelectorFragment selector;
    private MapClickListener selectorListener;
    private MapClickListener dataListener;

    // Required empty public constructor
    public MapFragment() {}

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
    public void setSelectorMapListener(MapClickListener listener) { this.selectorListener = listener; }
    public void setDataMapListener(MapClickListener listener) { this.dataListener = listener; }
    public void removeSelectorMapListener() { this.selectorListener = null; }

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
                fullCell.setImageResource(element.getStructure().getImageId());
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
                if(currStructure != null || !element.isBuildable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(),
                            R.style.AlertDialogCustom));
                    builder.setTitle("Uh oh!")
                            .setMessage("You can't build there.")
                            .setPositiveButton("OK", null);

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(selectedStructure != null) {
                    int position = getBindingAdapterPosition();
                    int row = position % GameData.get().getMapHeight();
                    int col = position / GameData.get().getMapHeight();
                    this.element.setStructure(selectedStructure);
                    GameData.get().setElement(row, col, this.element);
                    adapter.notifyItemChanged(getBindingAdapterPosition());
                    //dataListener.onMapClick();
                    selectorListener.onBuild();
                }
            }
            else {
                if(currStructure != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(),
                            R.style.AlertDialogCustom));
                    builder.setTitle("OPTIONS")
                            .setPositiveButton("See Details", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .setNeutralButton("Demolish", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
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