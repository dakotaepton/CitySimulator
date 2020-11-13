package mad.citysimulator.models;

import android.graphics.Bitmap;

// Holder class for storing user uploaded images in the database
public class ImageHolder {
    private int row;
    private int col;
    private Bitmap image;

    public ImageHolder(int row, int col, Bitmap image) {
        this.image = image;
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public Bitmap getImage() { return image; }

    public void setRow(int row) { this.row = row; }
    public void setCol(int col) { this.col = col; }
    public void setImage(Bitmap image) { this.image = image; }
}
