package cs.dal.a4176_project;

/**
 * Created by Roxanne on 2017/11/28 0028.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public abstract class CounterActivity extends AppCompatActivity implements SensorEventListener {
    private static final String STATE_EXERCISE_COUNT = "exercise_count";
    private static final String STATE_PAUSED = "counter_paused";

    protected SensorManager sensorManager;
    protected Sensor sensor;

    protected boolean paused = false;
    protected double exerciseCount = 0;
    protected double exerciseMax;
    protected long startTime = 0;
    protected long endTime = 0;

    protected View readyPrompt, resetButton;
    protected Button counterCircle;
    protected ViewGroup pausedControlLayout, resumedControlLayout;

    protected Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check the state of new counter
        if (savedInstanceState != null) {
            exerciseCount = savedInstanceState.getDouble(STATE_EXERCISE_COUNT);
            paused = savedInstanceState.getBoolean(STATE_PAUSED);
        }
        setContentView(R.layout.activity_counter);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        exerciseMax = 0;//Can use this variable to set a maximum count time

        counterCircle = (Button) findViewById(R.id.counter_circle);

        readyPrompt = findViewById(R.id.counter_ready_prompt);//ready to go function
        resetButton = findViewById(R.id.button_reset);//reset function
        pausedControlLayout = (ViewGroup) findViewById(R.id.paused_control_layout);
        resumedControlLayout = (ViewGroup) findViewById(R.id.resumed_control_layout);//change layout between paused and resumed

        if (Build.VERSION.SDK_INT >= 21) {
            counterCircle.setTransitionName("transition_id_counter_circle");
        }


    }

    //count the exercise time to change layout.
    public void updateExerciseCount() {
        //default layout
        if (exerciseCount < 0) exerciseCount = 0;
        String out = String.valueOf((int) exerciseCount);
        counterCircle.setText(out);

        //resumed layout
        if ((int) exerciseCount == 0) {
            readyPrompt.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.INVISIBLE);

            paused = false;
            updatePauseState();
        } else {
            //paused layout
            readyPrompt.setVisibility(View.INVISIBLE);
            resetButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Ensure everything matches the pause state.
     */
    protected abstract void updatePauseState();

    @Override
    //on pause function
    protected void onPause() {
        super.onPause();//state is pause
        sensorManager.unregisterListener(this);
    }

    @Override
    //resume function
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        updatePauseState();//change state to resume
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putDouble(STATE_EXERCISE_COUNT, exerciseCount);
        savedInstanceState.putBoolean(STATE_PAUSED, paused);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    //users select whether they want to quit or not
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://user choose to return to home
                promptDiscard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //user choose to quit and discard, return to previous page
    public void promptDiscard() {
        Callback positiveCallback = new Callback() {
            @Override
            public void fire() {
                finish();
            }
        };//return to previous page
        promptDiscard(positiveCallback);
    }
    //user click on quit button a dialog shows to user
    public void promptDiscard(final Callback positiveCallback) {
        //user choose to quit
        DialogInterface.OnClickListener positiveListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        positiveCallback.fire();
                    }
                };
        //user choose to cancel the dialog
        DialogInterface.OnClickListener negativeListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                };
        //UI dialog to ask user if user want to quit or not
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Quit??!!!")
                .setMessage("Are you sure you want to quit?")
                .setIcon(R.drawable.doge)
                .setPositiveButton("Quit", positiveListener)
                .setNegativeButton("Cancel", negativeListener)
                .setCancelable(true)
                .create();
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        promptDiscard();
    }

    //count exercise time
    public void incrementCount(View view) {
        exerciseCount++;
        updateExerciseCount();
    }
    //view button to decrease count time
    public void decrementCount(View view) {
        exerciseCount--;
        updateExerciseCount();
    }

    public void togglePause(View view) {
        // Cannot be paused if exerciseCount == 0
        paused = exerciseCount > 0 && !paused;
        updatePauseState();
    }
    //reset the count to zero.
    public void resetCount(View view) {
        // FUTURE: animate this action
        // Maybe a circular reveal.
        exerciseCount = 0;
        updateExerciseCount();
    }

}