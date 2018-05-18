package com.zyw.nnm.Activities.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zyw on 2017-10-30.
 */
public class GameViewState implements Serializable {

    private static GameViewState instance;

    private ArrayList<Checker> emptySpots = new ArrayList<>();
    private ArrayList<Checker> checkers = new ArrayList<>();
    private String fileName;




    private boolean remove;
    private int gameMode;

    public static GameViewState getInstance(){
        if(instance == null) {
            instance = new GameViewState();
        }
        return instance;
    }


    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }
    public ArrayList<Checker> getEmptySpots() {
        return emptySpots;
    }

    public void setEmptySpots(ArrayList<Checker> emptySpots) {
        this.emptySpots = emptySpots;
    }

    public ArrayList<Checker> getCheckers() {
        return checkers;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCheckers(ArrayList<Checker> checkers) {
        this.checkers = checkers;
    }

    public void  initBoard(float margin, int radius, Context context){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            gameMode = Integer.parseInt(sharedPreferences.getString("example_list", ""));
        }
        catch (Exception e){
            gameMode = 9;
        }

        Log.d("GaME", "initBoard: " + context);

        //Square 1

        emptySpots.add(new Checker(margin,margin, radius, 3, context,margin));
        emptySpots.add(new Checker(margin*4,margin, radius, 6, context,margin));
        emptySpots.add(new Checker(margin*7,margin, radius, 9, context,margin));
        emptySpots.add(new Checker(margin,margin*4, radius, 24, context,margin));
        emptySpots.add(new Checker(margin,margin*7, radius, 21, context,margin));
        emptySpots.add(new Checker(margin*4,margin*7, radius, 18, context,margin));
        emptySpots.add(new Checker(margin*7,margin*7, radius, 15, context,margin));
        emptySpots.add(new Checker(margin*7,margin*4, radius, 12, context,margin));

        // Square 2
        if(gameMode>3) {
            emptySpots.add(new Checker(margin * 2, margin * 2, radius, 2, context, margin));
            emptySpots.add(new Checker(margin * 4, margin * 2, radius, 5, context, margin));
            emptySpots.add(new Checker(margin * 6, margin * 2, radius, 8, context, margin));
            emptySpots.add(new Checker(margin * 2, margin * 4, radius, 23, context, margin));
            emptySpots.add(new Checker(margin * 6, margin * 4, radius, 11, context, margin));
            emptySpots.add(new Checker(margin * 6, margin * 6, radius, 14, context, margin));
            emptySpots.add(new Checker(margin * 2, margin * 6, radius, 20, context, margin));
            emptySpots.add(new Checker(margin * 4, margin * 6, radius, 17, context, margin));
        }


        // Square 3
        if(gameMode>6 ){
            emptySpots.add(new Checker(margin * 3, margin * 3, radius, 1, context, margin));
            emptySpots.add(new Checker(margin * 4, margin * 3, radius, 4, context, margin));
            emptySpots.add(new Checker(margin * 5, margin * 3, radius, 7, context, margin));
            emptySpots.add(new Checker(margin * 3, margin * 4, radius, 22, context, margin));
            emptySpots.add(new Checker(margin * 5, margin * 4, radius, 10, context, margin));
            emptySpots.add(new Checker(margin * 3, margin * 5, radius, 19, context, margin));
            emptySpots.add(new Checker(margin * 4, margin * 5, radius, 16, context, margin));
            emptySpots.add(new Checker(margin * 5, margin * 5, radius, 13, context, margin));
        }


    }

    public void saveInstance(Context context, String fileName) throws IOException{
        ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
        out.writeObject(GameViewState.getInstance());
        out.close();
    }

    public void loadInstance(Context fileContext, String fileName) throws IOException, ClassNotFoundException{
        ObjectInputStream in = new ObjectInputStream(fileContext.openFileInput(fileName));
        instance = (GameViewState) in.readObject();
        in.close();

        for (Checker checker:instance.getCheckers()
             ) {
            checker.loadDrawableMarker(fileContext);
        }
        for (Checker checker:instance.getEmptySpots()
                ) {
            checker.loadDrawableMarker(fileContext);
        }
    }

}
