package com.zyw.nnm.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.zyw.nnm.Activities.View.GameViewState;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SaveAndLoad {

    private static int gameMode;
    private static void loadSettings(Context mainActivity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        try {
            gameMode = Integer.parseInt(sharedPreferences.getString("example_list", ""));
        }
        catch (Exception e){
            gameMode = 9;
        }
       NineMenMorrisRules.getInstance().newGame(gameMode);
    }
    // Load the previous saved file. If gameplan does not exist, draw a new empty one.
    public static void load(Context mainActivity, String fileNameView) {

        loadSettings(mainActivity);
        //String fileNameView = mainActivity.getIntent().getStringExtra("fileName");

        // If no save exists
        if (mainActivity.fileList().length == 0) {
            Log.d("game", "load: No saved file");
            NineMenMorrisRules.getInstance().newGame(gameMode);
            return;
        }

        if(fileNameView == null)
            fileNameView = mainActivity.fileList()[mainActivity.fileList().length-2];

        Log.d("game", "load: " + fileNameView);
        String fileNameGame = "game_"+fileNameView.split("_")[1];
        Log.d("game", "load: " + fileNameGame);
        try {
            NineMenMorrisRules.getInstance().loadGame(mainActivity.getApplicationContext(),fileNameGame);
            Log.d("GAME", "loaded: ");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        // Repaint the game plan
        try {
            GameViewState.getInstance().loadInstance(mainActivity,fileNameView);

        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }



    }

    // Save the game layout to file
    public static  void save(Context mainActivity) {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
        Date today = new Date();
        String reportDate = df.format(today);

        String currentFileName = GameViewState.getInstance().getFileName();
        if( currentFileName != null) {
            if (!currentFileName.isEmpty()) {
                mainActivity.deleteFile("gameStateView_" + currentFileName);
                mainActivity.deleteFile("game_" + currentFileName);
            }
        }

        GameViewState.getInstance().setFileName(reportDate);

        try {
            GameViewState.getInstance().saveInstance(mainActivity,"gameStateView_"  + reportDate);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        try {
            NineMenMorrisRules.getInstance().saveGame(mainActivity.getApplicationContext(),"game_"  + reportDate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
