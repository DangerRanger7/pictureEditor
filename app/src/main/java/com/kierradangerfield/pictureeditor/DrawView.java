package com.kierradangerfield.pictureeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kierra on 3/25/2018.
 */

public class DrawView extends View {

    private static int lineSize = 100;
    private static int lineDIP = 5;
    private static int androidDIP = 50;

    private static int rectangleSizeDip = 50;
    private static int rectangleStepDip = 5;

    private Paint linePaint;
    private Paint recPaint;
    private Paint bitmapPaint;
    private Random random;

    private int currentWidth;
    private int currentHeight;

    private ArrayList<Rectangle> rectangles;

    private Bitmap bitmap;
    private Canvas canvas;
    Path path;

    public DrawView(Context context) {
        super(context);
         setup();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public void setup(){
      linePaint = new Paint();
      linePaint.setAntiAlias(true);
      linePaint.setColor(Color.BLACK);
      linePaint.setStyle(Paint.Style.STROKE);

      path = new Path();
      bitmapPaint = new Paint();
      bitmapPaint.setColor(Color.GREEN);

     /* /*RECTANGLE *
        random = new Random();
        recPaint = new Paint();
        recPaint.setStyle(Paint.Style.FILL);
        rectangles = new ArrayList<>();
        setUpBitmap();*/
       // setBackground(getBackground(R.id.picture_imageView));

      /*  random = new Random();

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);

        rectangles = new ArrayList<>();

        setUpBitmap();*/
      /*  linePaint = new Paint();
        linePaint.setColor(Color.CYAN);
        linePaint.setStyle(Paint.Style.STROKE);

        int dpSize = 2; // want the line to be 2 dp wide
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
        linePaint.setStrokeWidth(strokeWidth);
        linePaint.setAntiAlias(true);*/
    }

    private void setUpBitmap(){

        ImageView imageView = findViewById(R.id.picture_imageView);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int pixelSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, androidDIP, dm);

        bitmap = Bitmap.createBitmap(pixelSize, pixelSize, Bitmap.Config.ARGB_8888);

        //make the drawable
       /*Canvas canvas = new Canvas(bitmap);
        imageView.setAdjustViewBounds(true);
        imageView.getAdjustViewBounds();
        imageView.draw(canvas);*/
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

    public void addRectangle(){
        Rectangle r = new Rectangle(rectangleSizeDip, rectangleStepDip);
        rectangles.add(r);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.drawPath(path, linePaint);

        /*FOR RECTANGLE*/

/*        int size = rectangles.size();

        for (int i = 0; i < size; i++){
           Rectangle r = rectangles.get(i);


            canvas.drawBitmap(bitmap, r.x, r.y, linePaint);
        }*/



      //  canvas.drawPaint(backgroundPaint);
      /*  canvas.drawLine(0, 0, currentWidth - 1, currentHeight - 1, linePaint);
        canvas.drawLine(currentWidth - 1, 0, 0, currentHeight - 1, linePaint);*/
    }

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
        path.lineTo(downX, downY);

        canvas.drawPath(path, linePaint);

        path.reset();
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchDown(x,y);
                invalidate();
                break;

            case  MotionEvent.ACTION_MOVE:
                touchMove(x,y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;

            default:
                break;
        }

        return true;
    }

    /**
     * Source and explanation
     * http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation
     */
  /*  @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }*/

   /* public void start() {
        if (timer == null) {
            Random r = new Random();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (backgroundPaint.getColor() == Color.RED) {
                        backgroundPaint.setColor(Color.BLUE);
                    } else {
                        backgroundPaint.setColor(Color.RED);
                    }
                    postInvalidate();
                }
            };

            timer = new Timer();
            timer.schedule(task, 0, r.nextInt(2000) + 1000);
        }
    }*/

  /*  public void stop() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }*/

  class Rectangle{

      int x, y;
      //float pixelSize, xStep

      Rectangle(int lineSizeDP, int stepSizeDP){

      }
  }
}
