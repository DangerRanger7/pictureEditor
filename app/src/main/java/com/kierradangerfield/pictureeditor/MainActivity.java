package com.kierradangerfield.pictureeditor;
/*SOURCES: https://stackoverflow.com/questions/15704205/how-to-draw-line-on-imageview-along-with-finger-in-android*/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

/*FEATURES
1. CHANGE CANVAS BACKGROUND
2. CHANGE BACKGROUND LAYOUT-> DAY/LIGHT MODE
3.
*
* */

public class MainActivity extends Activity {

    public static int tools = 1;
   public static int size = 50;
   public static int color = Color.BLACK;

   public static RadioButton recButton;
    boolean isDayLight = true;
    boolean canvasLight = true;

    public static Paint.Style style = Paint.Style.STROKE;

   // String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawView dv = findViewById(R.id.drawView);
        dv.setup();

        RadioButton rb;
        recButton = findViewById(R.id.rec_radioButton);

        rb = findViewById(R.id.line_radioButton);
        rb.setChecked(true);


        //tools radio group
        //RadioGroup radioGroup = findViewById(R.id.tools
       RadioGroup rg =  findViewById(R.id.tools_radioGroup);
       rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup radioGroup, int id) {
               DrawView dv = findViewById(R.id.drawView);
               if (id == R.id.rec_radioButton){
                   tools = 0;

               }else if (id == R.id.line_radioButton){
                   tools = 1;
               }

           }
       });

        rb = findViewById(R.id.small_radioButton);
        rb.setChecked(true);
       findViewById(R.id.Medium_radioButton);
       findViewById(R.id.large_radioButton);

     rg =  findViewById(R.id.size_radioGroup);
     rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(RadioGroup radioGroup, int id) {
            // RadioButton rb = id;
             DrawView dv = findViewById(R.id.drawView);

             if (id == R.id.small_radioButton){
                 size = 25;
             }else if (id == R.id.Medium_radioButton){
                 size = 50;
             }else if (id == R.id.large_radioButton){
                 size = 75;
             }

             dv.changeSize(size);
         }
     });

    RadioButton color_rb = findViewById(R.id.black_radioButton);
    color_rb.setChecked(true);
   // color_rb = findViewById(R.id.cyan_radioButton);
        findViewById(R.id.cyan_radioButton);
       // color_rb.setChecked(true);
    findViewById(R.id.red_radioButton);

    RadioGroup color_radioGroup = findViewById(R.id.color_radioGroup);
    color_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
           int selected = radioGroup.getCheckedRadioButtonId();
           DrawView dv = findViewById(R.id.drawView);

            if (selected == R.id.black_radioButton){

                color = Color.BLACK;
            }else if (selected == R.id.cyan_radioButton){
                color = Color.CYAN;
            }else if (selected == R.id.red_radioButton){
                color = Color.RED;
            }
            dv.changeColor(color);
        }
    });

        //change canvas color----------->>>>FEATURE**********************************************************************
  final Button cb = findViewById(R.id.canvasBackgroundColor_button);

  cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawView dv = findViewById(R.id.drawView);
                int canvasColor;

                if (canvasLight){
                    canvasColor = Color.BLACK;
                    dv.setBackgroundColor(canvasColor);
                    dv.invalidate();
                    cb.setText("White Background");
                    canvasLight = false;
                }else if (canvasLight == false){
                    canvasColor = Color.WHITE;
                    dv.setBackgroundColor(canvasColor);
                    dv.invalidate();
                    cb.setText("Black Background");
                    canvasLight = true;
                }

            }
        });

        //day or night mode--->>>>>FEATURE**********************************************************************
      final Button b = findViewById(R.id.layoutMode_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ConstraintLayout layout = findViewById(R.id.layout);

                if (isDayLight){

                    layout.setBackgroundColor(Color.rgb(105,105,105));
                    changeTextColorDay();
                    b.setText("Day Mode");
                    isDayLight = false;

                }else if(isDayLight == false){

                    layout.setBackgroundColor(Color.rgb(255,255,240));
                    changeTextColorNight();
                    b.setText("Night Mode");
                    isDayLight = true;
                }
            }
        });

        //Clear Canvas---------->FEATURE**********************************************************************
        findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dv.clearCanvas();
            }
        });

        rb =  findViewById(R.id.stroke_radioButton);
        rb.setChecked(true);
        findViewById(R.id.fill_radioButton);
        findViewById(R.id.strokeFill_radioButton);
        rg = findViewById(R.id.style_radioGroup);
        //change style---------->FEATURE**********************************************************************
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.stroke_radioButton){
                    style = Paint.Style.STROKE;
                }else if (i == R.id.fill_radioButton){
                    style = Paint.Style.FILL;
                }else if (i == R.id.strokeFill_radioButton){
                    style = Paint.Style.FILL_AND_STROKE;
                }

                DrawView dv = findViewById(R.id.drawView);
                dv.changeStyle(style);
            }
        });

        /*
        * source
        * https://stackoverflow.com/questions/15704205/how-to-draw-line-on-imageview-along-with-finger-in-android*/

        /****************** SAVE BUTTON***************************************************************/
       findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DrawView dv = new DrawView(MainActivity.this);
                //dv.setDrawingCacheEnabled(true);
                save();
            }
        });


    }
    int[] ids = {R.id.rec_radioButton, R.id.line_radioButton, R.id.small_radioButton,
    R.id.Medium_radioButton, R.id.large_radioButton, R.id.black_radioButton, R.id.cyan_radioButton,
    R.id.red_radioButton, R.id.stroke_radioButton, R.id.fill_radioButton, R.id.strokeFill_radioButton};

    public void changeTextColorDay(){
        TextView tv;

        tv = findViewById(R.id.tools_textview);
        tv.setTextColor(Color.WHITE);
        tv = findViewById(R.id.size_textView);
        tv.setTextColor(Color.WHITE);
        tv = findViewById(R.id.color_textview);
        tv.setTextColor(Color.WHITE);
        tv = findViewById(R.id.style_textView);
        tv.setTextColor(Color.WHITE);


        for (int i = 0; i < ids.length; i++){
         //   int b = i;
            RadioButton rb = findViewById(ids[i]);
            rb.setTextColor(Color.WHITE);
        }
    }

    public void changeTextColorNight(){
        TextView tv;

        tv = findViewById(R.id.tools_textview);
        tv.setTextColor(Color.BLACK);
        tv = findViewById(R.id.size_textView);
        tv.setTextColor(Color.BLACK);
        tv = findViewById(R.id.color_textview);
        tv.setTextColor(Color.BLACK);
        tv = findViewById(R.id.style_textView);
        tv.setTextColor(Color.BLACK);

        for (int i = 0; i < ids.length; i++){
            //   int b = i;
            RadioButton rb = findViewById(ids[i]);
            rb.setTextColor(Color.BLACK);
        }
    }

public  void save() {
DrawView dv = findViewById(R.id.drawView);
    try {
        Bitmap bitmap = dv.getDrawingCache();
        File f = null;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), "TTImages_cache");
            if (!file.exists()) {
                file.mkdirs();
            }
            f = new File(file.getAbsolutePath() + file.separator + "filename" + ".png");
        }
        FileOutputStream fos = null;
        fos = new FileOutputStream(f);
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, fos);
        fos.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}

