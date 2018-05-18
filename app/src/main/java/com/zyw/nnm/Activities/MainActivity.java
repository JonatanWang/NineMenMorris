package com.zyw.nnm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zyw.nnm.Activities.View.GameView;
import com.zyw.nnm.R;
import com.zyw.nnm.Model.SaveAndLoad;

/**
 * Created by zyw on 2017-10-30.
 */
public class MainActivity extends AppCompatActivity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameView = new GameView(this);
        setContentView(gameView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SaveAndLoad.load(this, getIntent().getStringExtra("fileName"));
    }
    @Override
    protected void onResume() {
        super.onResume();
        SaveAndLoad.load(this, getIntent().getStringExtra("fileName"));
        gameView.loadSettings();

    }

    @Override
    protected void onPause() {
        SaveAndLoad.save(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.new_game:
                //newGame();
                gameView.resetView();
                Toast.makeText(MainActivity.this,"Reset, start new game", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.quit:
                //quitGame();
                SaveAndLoad.save(this);
                finish();
                System.exit(0);
                Toast.makeText(MainActivity.this,"Quit game", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.load_game:
                SaveAndLoad.save(this);
                Intent intentLoad = new Intent(this, LoadActivity.class);
                startActivity(intentLoad);
                return true;

            case R.id.setting:
                SaveAndLoad.save(this);
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
