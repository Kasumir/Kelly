package org.androidtown.image;

/**
 * Created by BJW on 2017-05-15.
 */

public class IvInfo {
    private float x, y;
    private int textSize;
    private int textColor;
    private int strokeWidth;
    private int strokeColor;
    private int tfNum;

    public void setX(float x){this.x = x;}

    public float getX(){return x;}

    public void setY(float y){this.y = y;}

    public float getY(){return y;}

    public void setTextSize(int ts){this.textSize = ts;}

    public int getTextSize(){return textSize;}

    public void setTextColor(int tc){this.textColor = tc;}

    public int getTextColor(){return textColor;}

    public void setStrokeWidth(int strokeWidth){this.strokeWidth = strokeWidth;}

    public int getStrokeWidth(){return strokeWidth;}

    public void setStrokeColor(int strokeColor){this.strokeColor = strokeColor;}

    public int getStrokeColor(){return strokeColor;}

    public void setTfNum(int num){this.tfNum = num;}

    public int getTfNum(){return tfNum;}
}
