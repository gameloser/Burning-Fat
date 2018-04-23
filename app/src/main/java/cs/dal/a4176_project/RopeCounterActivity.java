package cs.dal.a4176_project;

/**
 * Created by Roxanne on 2017/11/28 0028.
 */


import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;


public final class RopeCounterActivity extends CounterActivity {

    private static final int SHAKE_THRESHOLD = 600;//set the threshold for sersor to sense an activity
    RopeCounterService mService;
    boolean mBound = false;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, RopeCounterService.class);
        startService(intent);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //keep screen on in this page
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toast.makeText(getApplicationContext(), "To start counting just put your phone in your pocket and start jumping.", Toast.LENGTH_LONG).show();
        //message that tell user how to start exercise
        sensor = sensorManager.getDefaultSensor(
                Sensor.TYPE_LINEAR_ACCELERATION);

    }


    public void updatePauseState() {
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = event.values[0];//these three variable form a 3-dimension change of the accelerometer
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 150) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) /
                        diffTime * 10000;//use a method to calculate the speed of sensed activity

                if (speed > SHAKE_THRESHOLD) {//if the speed that accelerometer sense is larger than the threshold,
                                              // count as half activity as this is a rope skiping exercise
                    exerciseCount = exerciseCount + 0.5;
                    if (exerciseCount == 1) {//when the count is larger than one which means a full activity is sensed, start timer
                        startTime = java.lang.System.currentTimeMillis();
                    }
                    endTime = java.lang.System.currentTimeMillis();//end timer
                    updateExerciseCount();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {//when user press home button, notify user to quit or cancel.
            onBackPressed();
            promptDiscard();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}