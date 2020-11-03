package mad.citysimulator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import mad.citysimulator.R;
import mad.citysimulator.models.GameData;
import mad.citysimulator.models.MapElement;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nameDetail;
    TextView rowColDetail;
    TextView typeDetail;
    TextView thumbnailLabel;
    ImageView thumbnailImage;
    Button saveDetailsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get Row and Col values
        int row = getIntent().getIntExtra("row", -1);
        int col = getIntent().getIntExtra("col", -1);
        // Get map element
        MapElement element = GameData.get().getElement(row, col);

        // Get view elements
        nameDetail = findViewById(R.id.nameDetail);
        rowColDetail = findViewById(R.id.rowColDetail);
        typeDetail = findViewById(R.id.typeDetail);
        thumbnailLabel = findViewById(R.id.thumbnailLabel);
        thumbnailImage = findViewById(R.id.thumbnailImage);
        saveDetailsBtn = findViewById(R.id.saveDetailsBtn);

        //Set onclick listeners
        thumbnailImage.setOnClickListener(this);
        thumbnailLabel.setOnClickListener(this);
        saveDetailsBtn.setOnClickListener(this);

        // Fill details
        nameDetail.setText(element.getOwnerName());
        rowColDetail.setText(Integer.toString(row) + ", " + Integer.toString(col));
        if(element.getImage() != null) {
            thumbnailImage.setImageBitmap(element.getImage());
        }
        else {
            thumbnailImage.setImageResource(element.getStructure().getImageId());
        }
        typeDetail.setText(element.getStructure().getStructureName());


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.thumbnailImage:
                System.out.println("LAUNCH CAMERA APP");
                break;
            case R.id.thumbnailLabel:
                System.out.println("LAUNCH CAMERA APP");
                break;
            case R.id.saveDetailsBtn:
                System.out.println("Save " + nameDetail.getText());
                break;
        }
    }
}