package com.zyw.nnm.Activities.View;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zyw.nnm.R;

import java.io.Serializable;

/**
 * Created by zyw on 2017-10-30.
 */

public class Checker extends Drawable implements Serializable {

    private int radius;
    private int index;
    private float posX;
    private float posY;
    private float margin;
    private transient Drawable marker;

    private int typeOfMarker = 0;

    public Checker( int radius, Drawable marker) {
        this.radius = radius;
        this.marker = marker;
    }

    public void setDrawabe(Drawable marker){
        this.marker = marker;
    }

    public Checker(float posX, float posY, int radius, int index, Context context, float margin) {
        this.radius = radius;
        this.posX= posX;
        this.posY = posY;
        this.index = index;
        this.margin = margin;
        this.marker = context.getResources().getDrawable(R.drawable.redpiece, null);
        Rect rect = new Rect((int)posX-radius, (int)posY-radius, (int)posX+radius,(int)posY+radius);
        marker.setBounds(rect);
    }

    public void converToNewMargin(float m){
        setPos(posX/margin*m,posY/margin*m);
        margin=m;
    }

    public Checker(float posX, float posY, int radius, int index) {
        this.radius = radius;
        this.posX= posX;
        this.posY = posY;
        this.index = index;
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public int getTypeOfMarker() {
        return typeOfMarker;
    }

    public void setTypeOfMarker(int typeOfMarker) {
        this.typeOfMarker = typeOfMarker;
    }

    public void setPos(float x, float y){
        this.posX = x;
        this.posY = y;
        updateDrawable((int)x,(int)y);
    }

    public void updateDrawable(int x, int y){
        Rect rect = new Rect((int)posX-radius, (int)posY-radius, (int)posX+radius,(int)posY+radius);
        marker.setBounds(rect);
    }


    public void loadDrawableMarker(Context context){
        if(typeOfMarker == 0) {
            this.marker = context.getResources().getDrawable(R.drawable.redpiece, null);
            Rect rect = new Rect((int) posX - radius, (int) posY - radius, (int) posX + radius, (int) posY + radius);
            marker.setBounds(rect);
        }
        if(typeOfMarker == 1) {
            this.marker = context.getResources().getDrawable(R.drawable.chess_p1, null);
            Rect rect = new Rect((int) posX - radius, (int) posY - radius, (int) posX + radius, (int) posY + radius);
            marker.setBounds(rect);
        }
        if(typeOfMarker == 2) {
            this.marker = context.getResources().getDrawable(R.drawable.chess_p2, null);
            Rect rect = new Rect((int) posX - radius, (int) posY - radius, (int) posX + radius, (int) posY + radius);
            marker.setBounds(rect);
        }
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    // Check if a marker is within a destination checker, deviation is radius * 6
    public boolean isHit(int radius, float x, float y) {

        float rightX = posX + radius;
        float leftX = posX - radius;
        float downY = posY + radius;
        float upY = posY - radius;
        return (x <= rightX && x >= leftX && y <= downY && y >= upY);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if(marker != null)
            marker.draw(canvas);
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        marker.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
