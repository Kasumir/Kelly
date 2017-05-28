package org.androidtown.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jongwon on 2017-05-28.
 */

public class Polygon extends View{
    int color = Color.BLUE;
    Paint paint = new Paint();
    int width, height;
    int shape;
    int strokeWidth = 10;
    final int RECT = 0;
    final int ROUND = 1;
    public Polygon(Context context){
        super(context);
    }
    public Polygon(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public Polygon(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        setMeasuredDimension(width + 2 * strokeWidth , height + 2 *strokeWidth);
    }
    @Override
    public void onDraw(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        if(shape == RECT)
        drawRect(canvas, width, height);
        else if(shape == ROUND)
            drawRound(canvas, width, height);
    }
    public void drawRect(Canvas canvas, int width, int height){
        requestLayout();
        canvas.drawRect(strokeWidth / (float)2, strokeWidth / (float)2, width + strokeWidth / (float)2, height + strokeWidth /(float)2, paint);
    }
    public void drawRound(Canvas canvas, int width, int height){
        requestLayout();
        canvas.drawOval(strokeWidth / (float)2,strokeWidth / (float)2,width+strokeWidth / (float)2,height+strokeWidth / (float)2,paint);
    }

    public void setWidth(int width){this.width = width;}
    public void setHeight(int height){this.height = height;}
    public void setColor(int c){paint.setColor(c);}
    public void setShape(int q){
        if(q == RECT || q == ROUND)
            shape = q;
    }
}
