package cs.dal.a4176_project;

/**
 * Created by Roxanne on 2017/11/29 0029.
 */

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.util.Random;

public class RopeCounterService extends Service {
    private static final int SHAKE_THRESHOLD = 600;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();
    protected double exerciseCount = 0;
    protected long startTime = 0;
    protected long endTime = 0;
    protected SensorManager sensorManager;
    protected Sensor sensor;
    private Looper mServiceLooper;
    private long lastUpdate = 0;
    private ServiceHandler mServiceHandler;
    private float last_x, last_y, last_z;

    // Start up the thread running the service.
    @Override
    public void onCreate() {
        // separate thread due to service normally runs in the process' main thread

        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();

        // background priority so CPU-intensive work will not disrupt our UI.
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //method for clients
    public double getCurrentJumps() {
        return exerciseCount;
    }


    //Class used for the client Binder
    public class LocalBinder extends Binder {
        RopeCounterService getService() {
            // Return this instance of LocalService so clients can call public methods
            return RopeCounterService.this;
        }
    }


    private final class ServiceHandler extends Handler implements SensorEventListener {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }

        //a method detected the sensor changed
        @Override
        public void onSensorChanged(SensorEvent event) {
            Sensor mySensor = event.sensor;
            //use three direction to determine the movement of the sensor
            if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                long curTime = System.currentTimeMillis();

                //define the speed of accelerometer
                if ((curTime - lastUpdate) > 150) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    float speed =
                            Math.abs(x + y + z - last_x - last_y - last_z) /
                                    diffTime * 10000;

                    //if speed is large than the shake threshold, we will add 0.5 to the count
                    if (speed > SHAKE_THRESHOLD) {
                        exerciseCount = exerciseCount + 0.5;
                        if (exerciseCount == 1) {
                            startTime = java.lang.System.currentTimeMillis();
                        }
                        endTime = java.lang.System.currentTimeMillis();
                    }

                    //assign new location
                    last_x = x;
                    last_y = y;
                    last_z = z;
                }
            }
        }

        //we don't define accuracy changed at this time
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }
}
