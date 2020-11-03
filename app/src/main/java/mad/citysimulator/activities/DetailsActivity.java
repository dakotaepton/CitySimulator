package mad.citysimulator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import mad.citysimulator.R;
import mad.citysimulator.models.GameData;
import mad.citysimulator.models.MapElement;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    // Camera app intent variables
    private static final int REQUEST_THUMBNAIL = 1;
    private Intent thumbnailPhotoIntent;

    // Activity view elements
    private EditText nameDetail;
    private TextView rowColDetail;
    private TextView typeDetail;
    private TextView thumbnailLabel;
    private ImageView thumbnailImage;
    private Button saveDetailsBtn;

    // Map element info
    private MapElement element;
    private int row;
    private int col;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Set Row and Col values from intent
        this.row = getIntent().getIntExtra("row", -1);
        this.col = getIntent().getIntExtra("col", -1);

        // Get map element at that location
        this.element = GameData.get().getElement(row, col);

        // Get view elements
        this.nameDetail = findViewById(R.id.nameDetail);
        this.rowColDetail = findViewById(R.id.rowColDetail);
        this.typeDetail = findViewById(R.id.typeDetail);
        this.thumbnailLabel = findViewById(R.id.thumbnailLabel);
        this.thumbnailImage = findViewById(R.id.thumbnailImage);
        this.saveDetailsBtn = findViewById(R.id.saveDetailsBtn);

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

        // Set up thumbnail camera intent
        this.thumbnailPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    @Override
    public void onClick(View view) {
        int clickedId = view.getId();
        if (clickedId == R.id.thumbnailImage || clickedId == R.id.thumbnailLabel) {
            startActivityForResult(thumbnailPhotoIntent, REQUEST_THUMBNAIL);
        } else if (clickedId == R.id.saveDetailsBtn) {
            element.setOwnerName(nameDetail.getText().toString());
            GameData.get().updateElement(row, col, element);
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_THUMBNAIL) {
            Bitmap thumbnail = (Bitmap) resultIntent.getExtras().get("data");
            element.setImage(thumbnail);
            thumbnailImage.setImageBitmap(thumbnail);
        }
    }

}