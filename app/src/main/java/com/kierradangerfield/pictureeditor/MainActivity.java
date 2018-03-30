package com.kierradangerfield.pictureeditor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;

public class MainActivity extends Activity {
private int reqCode = 1;
static final int TAKE_PICTURE = 1;
ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint linePaint;
String path;
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
        findViewById(R.id.picture_imageView).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        //DrawView drawView = findViewById(R.id.picture_imageView);
                        //Canvas canvas = new Canvas();
                        // DrawView drawView = DrawView;
                        float actionDown_x, actionDown_y;
                        float actionUp_x, actionUp_y;

                        int action = motionEvent.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                actionDown_x = motionEvent.getX();
                                actionDown_y = motionEvent.getY();
                                break;

                            case MotionEvent.ACTION_UP:
                                actionUp_x = motionEvent.getX();
                                actionUp_y = motionEvent.getY();
                               //canvas.drawLine(actionDown_x, actionDown_y, actionUp_x, actionUp_y);
                                break;

                            default:
                                 break;
                        }
                        return false;
                    }
                });

        /*
        * source
        * https://stackoverflow.com/questions/15704205/how-to-draw-line-on-imageview-along-with-finger-in-android*/

        //edit
        findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //ImageView imageView =  findViewById(R.id.picture_imageView);
                bitmap = Bitmap.createBitmap((int) imageView.getWidth(), (int) imageView.getHeight(), Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
               linePaint = new Paint();
               linePaint.setColor(Color.BLACK);
               linePaint.setStyle(Paint.Style.FILL);
               //ImageView imageView = findViewById(R.id.drawView);
               imageView.setImageBitmap(bitmap);
               //imageView.setOnTouchListener(this);

            }
        });

        /****************** SAVE BUTTON***************************************************************/
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File file = new File(path);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                startActivity(intent);*/
            }
        });

        /******TAKE PICTURE BUTTON*******************************************************************************************/
        findViewById(R.id.takePicture_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, TAKE_PICTURE);
                }
            }
        });
    }



    /*SOURCE TO GET IMAGE FROM GALLERY
    http://programmerguru.com/android-tutorial/how-to-pick-image-from-gallery/8
    * */

    //get picture from gallery
    private void getImage(){

        Intent getPicIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getPicIntent, reqCode );
    }
/*SELECT IMAGE*/
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
                 path = cursor.getString(columnIndex);
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
