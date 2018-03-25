package com.kierradangerfield.pictureeditor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URI;

public class MainActivity extends Activity {
private int reqCode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open gallery
        findViewById(R.id.openGallery_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getImage();
            }
        });

        //image
        findViewById(R.id.picture_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    /*SOURCE TO GET IMAGE FROM GALLERY
    http://programmerguru.com/android-tutorial/how-to-pick-image-from-gallery/
    * */

    //get picture from gallery
    private void getImage(){

        Intent getPicIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getPicIntent, reqCode );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == reqCode && resultCode == RESULT_OK && null != data) {
                Uri selectPic = data.getData();
                String[] image = {MediaStore.Images.Media.DATA};

                //cursor
                Cursor cursor = getContentResolver().query(selectPic, image, null, null, null);
                //move cursor to 1st row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(image[0]);
                String path = cursor.getString(columnIndex);
                cursor.close();

                ImageView imageView = findViewById(R.id.picture_imageView);
                //set image
                imageView.setImageBitmap(BitmapFactory.decodeFile(path));
            } else {
                Toast.makeText(getApplicationContext(), "An image has not been picked!",
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
}
