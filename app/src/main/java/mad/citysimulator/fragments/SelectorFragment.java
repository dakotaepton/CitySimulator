package mad.citysimulator.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mad.citysimulator.R;
import mad.citysimulator.models.Structure;
import mad.citysimulator.models.StructureData;


public class SelectorFragment extends Fragment {

    private RecyclerView recycView;
    private RecyclerView.LayoutManager layoutManager;
    private int selectedPos;

    public SelectorFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selector, container, false);
        recycView = (RecyclerView) view.findViewById(R.id.selectorRecyclerView);
        layoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);

        recycView.setLayoutManager(layoutManager);
        SelectRecycAdaptor mAdaptor = new SelectRecycAdaptor();
        recycView.setAdapter(mAdaptor);

        return view;
    }

    public void setSelectedPos(int inPos) { this.selectedPos = inPos; }

    //=======================================================================================
    //ADAPTOR
    private class SelectRecycAdaptor extends RecyclerView.Adapter<SelectViewHolder> {

        public SelectRecycAdaptor() { }

        @Override
        public SelectorFragment.SelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new SelectorFragment.SelectViewHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(SelectorFragment.SelectViewHolder holder, int position) {
            Structure element = StructureData.get().getStructure(position);
            holder.bind(element);
        }

        @Override
        public int getItemCount() {
            return StructureData.get().getNumStructures();
        }
    }

    //=======================================================================================
    //VIEW HOLDER
    private class SelectViewHolder extends RecyclerView.ViewHolder {

        private ImageView listImage;
        private TextView listText;
        private Structure structure;

        public SelectViewHolder(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.fragment_selector_item, parent, false));
            listImage = itemView.findViewById(R.id.selectorItemImage);
            listText = itemView.findViewById(R.id.selectorItemText);
        }

        public void bind(Structure structure) {
            listImage.setImageResource(structure.getImageId());
            String structureName = structure.getStructureName();
            listText.setText(structureName);
            switch(structureName) {
                case "Residential":
                    listText.setTextColor(Color.parseColor("#FFFFFFFF"));
                    break;
                case "Commercial":
                    listText.setTextColor(Color.parseColor("#ff206e"));
                    break;
                case "Road":
                    listText.setTextColor(Color.parseColor("#fbff12"));
                    break;
            }
            this.structure = structure;
        }
    }
}