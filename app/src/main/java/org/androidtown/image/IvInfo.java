package org.androidtown.image;

/**
 * Created by BJW on 2017-05-15.
 */

public class IvInfo {
    private float x, y;
    private float textSize;
    private int textColor;
    private int strokeWidth;
    private int strokeColor;
    private int tfNum;
    private boolean underline;
    private float skew;

    public IvInfo()
    {
        textSize = MainActivity.userTextSize;
        textColor = MainActivity.userTextColor;
        strokeWidth = MainActivity.userStrokeWidth;
        strokeColor = MainActivity.userStrokeColor;
        tfNum = MainActivity.userfont;
        underline = MainActivity.userUnderline;
        skew = MainActivity.userSkew;
    }
    public void setX(float x){this.x = x;}

    public float getX(){return x;}

    public void setY(float y){this.y = y;}

    public float getY(){return y;}

    public void setTextSize(float ts){this.textSize = ts;}

    public float getTextSize(){return textSize;}

    public void setTextColor(int tc){this.textColor = tc;}

    public int getTextColor(){return textColor;}

    public void setUnderline(boolean a){this.underline = a;}

    public boolean getUnderline(){return underline;}

    public void setSkew(float sk){this.skew = sk;}

    public float getSkew(){return skew;}

    public void setStrokeWidth(int strokeWidth){this.strokeWidth = strokeWidth;}

    public int getStrokeWidth(){return strokeWidth;}

    public void setStrokeColor(int strokeColor){this.strokeColor = strokeColor;}

    public int getStrokeColor(){return strokeColor;}

    public void setTfNum(int num){this.tfNum = num;}

    public int getTfNum(){return tfNum;}

    public void updateUserInput(){
        textSize = MainActivity.userTextSize;
        textColor = MainActivity.userTextColor;
        strokeWidth = MainActivity.userStrokeWidth;
        strokeColor = MainActivity.userStrokeColor;
        tfNum = MainActivity.userfont;
    }
}
