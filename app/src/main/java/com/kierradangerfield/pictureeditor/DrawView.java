package com.kierradangerfield.pictureeditor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kierra on 3/25/2018.
 */

public class DrawView extends View {

    private Paint linePaint;
    private int currentWidth;
    private int currentHeight;
    private Timer timer;

    public DrawView(Context context) {
        super(context);
        setup(null);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    private void setup(AttributeSet attrs){
        linePaint = new Paint();
        linePaint.setColor(Color.CYAN);
        linePaint.setStyle(Paint.Style.STROKE);

        int dpSize = 2; // want the line to be 2 dp wide
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
        linePaint.setStrokeWidth(strokeWidth);
        linePaint.setAntiAlias(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

      //  canvas.drawPaint(backgroundPaint);
        canvas.drawLine(0, 0, currentWidth - 1, currentHeight - 1, linePaint);
        canvas.drawLine(currentWidth - 1, 0, 0, currentHeight - 1, linePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        currentWidth = w;
        currentHeight = h;
    }

    /**
     * Source and explanation
     * http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation
     */
    @Override
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
    }

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

}
