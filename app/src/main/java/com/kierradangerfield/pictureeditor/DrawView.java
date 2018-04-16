package com.kierradangerfield.pictureeditor;
/*sources:
* https://stackoverflow.com/questions/15704205/how-to-draw-line-on-imageview-along-with-finger-in-android
* https://stackoverflow.com/questions/21864863/android-canvas-clear-with-transparency
* https://stackoverflow.com/questions/6956838/how-to-erase-previous-drawing-on-canvas*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kierra on 3/25/2018.
 */

public class DrawView extends View {

   int drawingSize; // = MainActivity.size;
    private static int androidDIP = 50;

    private Paint linePaint;
    private Paint bitmapPaint;

    private int currentWidth;
    private int currentHeight;

    private Paint recPaint;

   public static Bitmap bitmap;
    private Canvas canvas;
    Path path;

    float x, y;

    int color2 = MainActivity.color;
    int width = MainActivity.size;

  //  private ImageView imageView;

    public DrawView(Context context) {
        super(context);
         setup();
        rectangleSetUp();

    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
       rectangleSetUp();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
        rectangleSetUp();
    }

    public void setup(){

            linePaint = new Paint();
            linePaint.setAntiAlias(true);
            linePaint.setColor(color2);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(width);
            drawingSize = 50;

            path = new Path();

            loadBitmap();
    }

    public void rectangleSetUp(){

        x = y = 0;

        recPaint = new Paint();
        recPaint.setColor(color2);
        recPaint.setStyle(Paint.Style.STROKE);
        recPaint.setStrokeWidth(width);

    }

    private void loadBitmap(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int pixelSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, drawingSize, dm);
        bitmap = Bitmap.createBitmap(pixelSize, pixelSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        /*FOR RECTANGLE */
        currentWidth = w;
        currentHeight = h;

        /****FOR LINES*****************************************************************/
        bitmap = Bitmap.createBitmap(currentWidth, currentHeight, Bitmap.Config.ARGB_8888);
      canvas = new Canvas(bitmap);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.drawPath(path, linePaint);

    }

    public void changeColor(int color){

        linePaint.setColor(color);
        recPaint.setColor(color);

    }

    public void changeSize(int size){
        linePaint.setStrokeWidth((float) size);
        recPaint.setStrokeWidth((float) size);
    }

    public void clearCanvas(){
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void changeStyle(Paint.Style style){
        linePaint.setStyle(style);
        recPaint.setStyle(style);
    }

    protected void save(Bitmap bitmap){
        String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root + "saved_images");
        dir.mkdirs();
        Random r = new Random();
        int n = 1000;
        n = r.nextInt(n);
        String fname = "Image-" + n + "jpg";
        File file = new File(dir, fname);
        if (file.exists()) {
            file.delete();
        }

        Log.i("LOAD", root + fname);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(this.getContext(), new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
        Toast.makeText(this.getContext(),"Your image is saved", Toast.LENGTH_LONG).show();

    }

    /*protected void mediaScan() {
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse(fileUri.toString())));
    }*/

    private float downX, downY;
    private static final float TOUCH_TOL = 4;
    private void touchDown(float x, float y){

        path.moveTo(x,y);

        downX = x;
        downY = y;
    }

    private void touchMove(float x, float y){
        float moveX = Math.abs(x - downX);
        float moveY = Math.abs(y - downY);

        if (moveX >= TOUCH_TOL || moveY >= TOUCH_TOL){
            path.quadTo(downX, downY, (x + downX)/2, (y + downY)/2);
            downX = x;
            downY = y;
        }
    }

    private void touchUp(){
        int tool = MainActivity.tools;

        path.lineTo(downX, downY);

        if (tool == 1) {
            canvas.drawPath(path, linePaint);
        }else if (tool == 0) {
            canvas.drawRect(downX, downY, x, y, recPaint);
        }
        path.reset();
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();


        if (MainActivity.recButton.isChecked()){
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                   /* downX = x;
                    downY = y;*/
                   touchDown(x, y);
                   invalidate();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    touchMove(x, y);
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    touchUp();
                    invalidate();
                    break;

                default:
                    return false;

            }
            postInvalidate();
            return true;

        }else  {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchDown(x, y);
                    invalidate();
                    break;

                case MotionEvent.ACTION_MOVE:
                    touchMove(x, y);
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    touchUp();
                    invalidate();
                    break;

                default:
                    break;
            }
        }

        return true;
    }

}

