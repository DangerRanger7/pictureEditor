package com.kierradangerfield.pictureeditor;
/*SOURCES: https://stackoverflow.com/questions/15704205/how-to-draw-line-on-imageview-along-with-finger-in-android*/
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.solver.widgets.Rectangle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;

public class MainActivity extends Activity {
    private int reqCode = 1;
    static final int TAKE_PICTURE = 2;
    ImageView imageView;

    private static int IMAGE = 1;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint linePaint;

    float xDown = 0, yDown = 0;
    float xUp, yUp;

    //String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DrawView drawView = new DrawView(this);

        final DrawView dv = findViewById(R.id.drawView);
        dv.setup();

 /*findViewById(R.id.rectangle_button).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              RectangleView rv = findViewById(R.id.drawView);
              rv.addRectangle();
          }
      });*/

        //open gallery
        /*findViewById(R.id.openGallery_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });*/

        //image
       /* this.findViewById(R.id.picture_imageView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

             / ImageView iv = findViewById(R.id.picture_imageView);
                Drawable drawable = iv.getDrawable();
                drawable.setBounds(20,30, drawable.getIntrinsicWidth() + 20, drawable.getIntrinsicHeight() +30);
                drawable.draw(canvas);*

                DrawView dv = findViewById(R.id.drawView);
                dv.setup();

                return false;
            }
        });*/


       /* findViewById(R.id.picture_imageView).setOnTouchListener(new View.OnTouchListener() {
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
                });*/

        /*
        * source
        * https://stackoverflow.com/questions/15704205/how-to-draw-line-on-imageview-along-with-finger-in-android*/

        //edit***************************************************************
        findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //editButtonClicked();
            }
        });


        /****************** SAVE BUTTON***************************************************************/
      /*  findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File file = new File(path);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                startActivity(intent);/
            }
        });*/

        /******TAKE PICTURE BUTTON*******************************************************************************************/
        findViewById(R.id.takePicture_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, TAKE_PICTURE);
                }
            }
        });

    }

    public void editButtonClicked() {
        //displays width and height
        Display display = getWindowManager().getDefaultDisplay();

        float displayWidth = display.getWidth();
        float displayHeight = display.getHeight();

        //ImageView imageView = findViewById(R.id.picture_imageView);
        bitmap = Bitmap.createBitmap((int) displayWidth, (int) displayHeight, Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bitmap);
        linePaint = new Paint();
        linePaint.setColor(Color.CYAN);
        imageView.setImageBitmap(bitmap);
        imageView.setOnTouchListener((View.OnTouchListener) this);
    }



    /*SOURCE TO GET IMAGE FROM GALLERY
    http://programmerguru.com/android-tutorial/how-to-pick-image-from-gallery/8
    * */

    //get picture from gallery
   /* private void getImage(){

        Intent getPicIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getPicIntent, reqCode );
    }*/
/*SELECT IMAGE*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //try {
            case 1:
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

               // ImageView imageView = findViewById(R.id.picture_imageView);
                //set image
                //imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                Bitmap bitmap2 = BitmapFactory.decodeFile(path);

               // DrawView drawView =findViewById(R.id.drawView);
               // canvas.drawBitmap(imageView.getWidth(), imageView.getHeight());

                //DrawView dv = findViewById(R.id.drawView);
               // drawView.start();
              //  imageView.setOnTouchListener((View.OnTouchListener) this);

            }
            break;

            //save image
            /*case 2:
                // if (requestCode == reqCode && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageView iv = findViewById(R.id.picture_imageView);
                iv.setImageBitmap(imageBitmap);
              /*  } else {
                    Toast.makeText(getApplicationContext(), "An image has not been picked!",
                            Toast.LENGTH_LONG).show();
                }*/
                //break;

            default:
                break;
       /* }catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }*/

        }
    }


    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        int touch = event.getAction();

        drawLines(imageView);
        switch (touch){
            case MotionEvent.ACTION_DOWN:
                xDown = event.getX();
                yDown = event.getY();
                break;

            case  MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                xUp = event.getX();
                yUp = event.getY();
                canvas.drawLine(xDown, yDown, xUp, yUp, linePaint);
                imageView.invalidate();
                break;

            default:
                break;
        }

        return super.onTouchEvent(event);

    }*/

    public void drawLines(ImageView imageView){
        bitmap = Bitmap.createBitmap((int) imageView.getWidth(), (int) imageView.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.FILL);
        //ImageView imageView = findViewById(R.id.drawView);
        imageView.setImageBitmap(bitmap);

      //  imageView.setOnTouchListener((View.OnTouchListener) this);
    }

    /*@Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int touch = motionEvent.getAction();

        switch (touch){
            case MotionEvent.ACTION_DOWN:
                xDown = motionEvent.getX();
                yDown = motionEvent.getY();
                break;

            case  MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                xUp = motionEvent.getX();
                yUp = motionEvent.getY();
                canvas.drawLine(xDown, yDown, xUp, yUp, linePaint);
                imageView.invalidate();
                break;

            default:
                break;
        }
        return false;
    }*/

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

   /* @Override
    protected void onResume() {
        super.onResume();

        DrawView dv = findViewById(R.id.drawView);
        dv.setup();
    }*/
/* @Override
    protected void onPause() {
        super.onPause();
        DrawView dv = findViewById(R.id.drawView);
        dv.stop();

    }*/
}
