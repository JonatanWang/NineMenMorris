package com.zyw.nnm.Activities.View;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zyw.nnm.Activities.MainActivity;
import com.zyw.nnm.Model.NineMenMorrisRules;
import com.zyw.nnm.R;

/**
 * Created by zyw on 2017-10-30.
 */
public class GameView extends View {

    private static final String TAG = "Game";
    public static final int BLUE_MARKER = 4;
    public static final int RED_MARKER = 5;
    public static final int BLUE_MOVES = 1;
    public static final int RED_MOVES = 2;
    public static final int EMPTY_SPACE = 0;
    private int gameMode;
    private boolean darkTheme;
    private int currentTurn;
    private Drawable currentTurnDrawable;
    private int margin;
    private int radius;
    private MainActivity mainActivity;

    public GameView(Context context) {
        super(context);

        mainActivity = (MainActivity) context;
        loadSettings();


    }

    /**
     * Gets the setting that are used for the view
     * form shared prefrences
     */
    public void loadSettings(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        try {
            gameMode = Integer.parseInt(sharedPreferences.getString("example_list", ""));
            darkTheme = sharedPreferences.getBoolean("example_switch", true);
            Log.d(TAG, "loadSettings: " +darkTheme);
        }
        catch (Exception e){
            Log.d(TAG, "loadSettings: Exce" );
            darkTheme = false;
            gameMode = 9;
        }


    }


    /**
     * Draws all lines needed for a board on canvas
     * depending on the game mode ( 6 or 9 mens morris)
     * @param canvas
     */
    private void drawBoard(Canvas canvas) {
        // Draw 16 lines to make the board
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        if(darkTheme)
            paint.setColor(Color.WHITE);
        // square 1
        canvas.drawLine(margin, margin, margin * 7, margin, paint);
        canvas.drawLine(margin, margin, margin, margin * 7, paint);
        canvas.drawLine(margin, margin * 7, margin * 7, margin * 7, paint);
        canvas.drawLine(margin * 7, margin, margin * 7, margin * 7, paint);

        if(GameViewState.getInstance().getGameMode()>5) {
            // square 2
            canvas.drawLine(margin * 2, margin * 2, margin * 6, margin * 2, paint);
            canvas.drawLine(margin * 2, margin * 6, margin * 6, margin * 6, paint);
            canvas.drawLine(margin * 2, margin * 2, margin * 2, margin * 6, paint);
            canvas.drawLine(margin * 6, margin * 2, margin * 6, margin * 6, paint);
            //connections square 1 to 2
            canvas.drawLine(margin, margin * 4, margin * 2, margin * 4, paint);
            canvas.drawLine(margin * 4, margin, margin * 4, margin * 2, paint);
            canvas.drawLine(margin * 6, margin * 4, margin * 7, margin * 4, paint);
            canvas.drawLine(margin * 4, margin * 6, margin * 4, margin * 7, paint);
        }
        if(GameViewState.getInstance().getGameMode()>8) {

            // horizontal line 3
            canvas.drawLine(margin * 3, margin * 3, margin * 5, margin * 3, paint);
            canvas.drawLine(margin * 3, margin * 5, margin * 5, margin * 5, paint);
            canvas.drawLine(margin * 3, margin * 3, margin * 3, margin * 5, paint);
            canvas.drawLine(margin * 5, margin * 3, margin * 5, margin * 5, paint);
            //connections
            canvas.drawLine(margin * 4, margin * 2, margin * 4, margin * 3, paint);
            canvas.drawLine(margin * 2, margin * 4, margin * 3, margin * 4, paint);
            canvas.drawLine(margin * 5, margin * 4, margin * 6, margin * 4, paint);
            canvas.drawLine(margin * 4, margin * 5, margin * 4, margin * 6, paint);

        }

    }



    public boolean initialized;

    @Override
    public void onDraw(Canvas canvas){
        if(darkTheme)
            canvas.drawColor(Color.BLACK,PorterDuff.Mode.CLEAR);
        if(!initialized){
            initialized = true;
            Log.d(TAG, "Initializing: ");
            margin = Math.min(getWidth(), getHeight())/8;
            radius = Math.min(getWidth(), getHeight())/32;
            for (Checker c : GameViewState.getInstance().getEmptySpots()) {
                c.converToNewMargin(margin);
            }
            for (Checker c: GameViewState.getInstance().getCheckers()) {
                c.converToNewMargin(margin);
            }
            if(GameViewState.getInstance().getEmptySpots().size()==0)
                GameViewState.getInstance().initBoard(margin,radius, mainActivity);
        }
        updateText(canvas);
        drawBoard(canvas);
            int i = 0;
        for (Checker c : GameViewState.getInstance().getEmptySpots()) {
            c.draw(canvas);
        }
        for (Checker c: GameViewState.getInstance().getCheckers()) {

            c.draw(canvas);
        }
    }

    private Checker selectedChecker = null;
    float oldX = 0;
    float oldY = 0;

    /**
     * On a touch even a move is tried
     * based on where the screen was pressed.
     *
     * @param e
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        
        switch (e.getAction()) {

            case MotionEvent.ACTION_UP:
                for (Checker emptySpot : GameViewState.getInstance().getEmptySpots()) {
                    boolean match = emptySpot.isHit(radius, x, y);
                    if(match) {

                        int from=0;
                        Log.d(TAG, "onTouchEvent: "  + emptySpot.getIndex());
                        if(selectedChecker != null) {

                            from = selectedChecker.getIndex();
                        }

                        if(move(from, emptySpot.getIndex())){
                            if(selectedChecker != null){
                                selectedChecker.setPos(emptySpot.getPosX(), emptySpot.getPosY());
                                selectedChecker.setIndex(emptySpot.getIndex());
                                selectedChecker = null;
                            }
                            else {
                                Checker checker = new Checker(radius, currentTurnDrawable);
                                checker.setPos(emptySpot.getPosX(), emptySpot.getPosY());
                                checker.setIndex(emptySpot.getIndex());
                                checker.setTypeOfMarker(currentTurn);
                                checker.setMargin(margin);
                                GameViewState.getInstance().getCheckers().add(checker);
                            }
                        }
                        break;
                    }
                }
                //If move was invalid and but a checker tried to move, then move it back
                if(selectedChecker != null){
                    animateSelectedChecker();
                    selectedChecker = null;
                }
                invalidate();
                return true;


            case MotionEvent.ACTION_DOWN:
                for (Checker c : GameViewState.getInstance().getCheckers()) {
                    boolean match = c.isHit(radius, x, y);
                    if (match &&  NineMenMorrisRules.getInstance().getMarker( NineMenMorrisRules.getInstance().getTurn())==0 || match && GameViewState.getInstance().isRemove()  ) {
                        selectedChecker = c;
                        oldX = c.getPosX();
                        oldY = c.getPosY();
                        break;
                    }
                }
            case MotionEvent.ACTION_MOVE:
                if(selectedChecker != null && !GameViewState.getInstance().isRemove() ) {
                    selectedChecker.setPos(x, y);
                    invalidate();
                }
        }
        return true;

    }


    /**
     * Animates between a Pos(x,y) and a second Pos(x,y) in 500ms
     */
    private void animateSelectedChecker(){
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x",selectedChecker.getPosX(), oldX);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y",selectedChecker.getPosY(), oldY);
        ValueAnimator vaX = ValueAnimator.ofPropertyValuesHolder(pvhX,pvhY);
        int mDuration = 500; //in millis
        vaX.setDuration(mDuration);
        vaX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            Checker temp = selectedChecker;
            public void onAnimationUpdate(ValueAnimator animation) {
                Float x = (Float) animation.getAnimatedValue("x");
                Float y = (Float) animation.getAnimatedValue("y");
                temp.setPos(x,y);
                invalidate();
            }
        });
        vaX.start();
    }

    /**
     * Checks for currents game state and
     * updates the displayed text accordingly
      */
    private void updateText(Canvas canvas) {

        Paint paint = new Paint();
        paint.setTextSize(margin/2);
        // First Prio: check who is the winner
        if ( NineMenMorrisRules.getInstance().win(RED_MARKER)) {
            paint.setColor(Color.RED);
            canvas.drawText("Potatoes wins!", margin * 1.5f, (float) (margin * 7.8), paint);
            //ameOver = true;
            return;
        }

        if ( NineMenMorrisRules.getInstance().win(BLUE_MARKER)) {
            paint.setColor(Color.BLUE);
            canvas.drawText("Flowers wins!", margin * 1.5f, (float) (margin * 7.8), paint);
            //gameOver = true;
            return;
        }

        // Second Prio: check mills
        if (GameViewState.getInstance().isRemove() ) {
            if ( NineMenMorrisRules.getInstance().getTurn() == RED_MOVES) {
                paint.setColor(Color.BLUE);
                canvas.drawText("Flowers removes one!", margin * 1.5f, (float) (margin * 7.8), paint);
            } else if ( NineMenMorrisRules.getInstance().getTurn() == BLUE_MOVES) {
                paint.setColor(Color.RED);
                canvas.drawText("Potatoes removes one!", margin * 1.5f, (float) (margin * 7.8), paint);
            } else {
            }
            return;
        }

        // Third Prio: check deadlock
        if (! NineMenMorrisRules.getInstance().moveLeft(RED_MARKER)) {
            paint.setColor(Color.BLUE);
            canvas.drawText("Flowers Wins. Red can not move!", margin * 1.5f, (float) (margin * 8), paint);
            //gameOver = true;
            return;
        }

        if (! NineMenMorrisRules.getInstance().moveLeft(BLUE_MARKER)) {
            paint.setColor(Color.RED);
            canvas.drawText("Potatoes Wins. Blue can not move!", margin * 1.5f, (float) (margin * 7.8), paint);
            //gameOver = true;
            return;
        }

        // Forth prio: check turns
        if ( NineMenMorrisRules.getInstance().getTurn() == RED_MOVES) {
            paint.setColor(Color.RED);
            canvas.drawText("Potatoes turn!", margin * 1.5f, (float) (margin * 7.8), paint);
        } else if ( NineMenMorrisRules.getInstance().getTurn() == BLUE_MOVES) {
            paint.setColor(Color.BLUE);
            canvas.drawText("Flowers turn!", margin * 1.5f, (float) (margin * 7.8), paint);
        } else {}
    }


    /**
     * Checl if move was valid
     * @param from
     * @param to
     * @return
     */
    public boolean move(int from, int to) {

        boolean validMove;

        int turn =  NineMenMorrisRules.getInstance().getTurn();
        int currentMarker;
        if(turn == BLUE_MOVES){
            currentTurn = 1;
            currentTurnDrawable = getResources().getDrawable(R.drawable.chess_p1,null);
            currentMarker = BLUE_MARKER;
        }else {
            currentTurn= 2;
            currentTurnDrawable = getResources().getDrawable(R.drawable.chess_p2,null);
            currentMarker = RED_MARKER;
        }

        if (GameViewState.getInstance().isRemove()){
            if( NineMenMorrisRules.getInstance().remove(from, currentMarker)) {
                GameViewState.getInstance().getCheckers().remove(selectedChecker);
                GameViewState.getInstance().setRemove(false);
                return true;
            }
            return false;
        }
        validMove =  NineMenMorrisRules.getInstance().legalMove(to,from,turn);
        if(validMove)
            GameViewState.getInstance().setRemove( NineMenMorrisRules.getInstance().remove(to));

        return validMove;
    }



    public void resetView() {
        loadSettings();
        selectedChecker = null;
        GameViewState.getInstance().setRemove(false);
        GameViewState.getInstance().setFileName("");
        //gameOver = false;
        NineMenMorrisRules.getInstance().newGame(gameMode);
        GameViewState.getInstance().getEmptySpots().clear();
        GameViewState.getInstance().getCheckers().clear();
        initialized =false;
        NineMenMorrisRules.getInstance().newGame(gameMode);
        invalidate();
    }

}
