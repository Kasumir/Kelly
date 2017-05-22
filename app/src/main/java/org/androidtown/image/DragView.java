package org.androidtown.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jongwon on 2017-05-23.
 */

public class DragView extends View{
    int color = Color.BLUE;
    Paint paint = new Paint();
    int width, height;

    public DragView(Context context){
        super(context);
        paint.setColor(color);
        paint.setAlpha(30);
    }
    public DragView(Context context, AttributeSet attrs){
        super(context, attrs);
        paint.setColor(color);
        paint.setAlpha(30);
    }
    public DragView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        paint.setColor(color);
        paint.setAlpha(30);
    }
    @Override
    public void onDraw(Canvas canvas){
        drawRect(canvas,width, height);
    }
    public void drawRect(Canvas canvas, int width, int height){
        canvas.drawRect(0,0,width,height, paint);
    }
    public void setWidth(int width){this.width = width;}
    public void setHeight(int height){this.height = height;}
}
