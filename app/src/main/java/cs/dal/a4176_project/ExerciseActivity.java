package cs.dal.a4176_project;



import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {
    private int minutes = 0,seconds = 0;
    private int max = 10;
    private TextView type, time, percent;
    private Spinner second,min;
    private ProgressBar progressBar;
    MyCountDownTimer myCountDownTimer;
    private boolean select;
    private int status;
    private int remain;
    private int length;
    private MediaPlayer start, medium, end, song;
    private AlertDialog dialog;
    private Bundle ex;
    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        final Context context = getApplicationContext();
        db = new DatabaseManager(context);


        //initialization
        time = (TextView) findViewById(R.id.Time);
        percent = (TextView) findViewById(R.id.percent);
        type = (TextView) findViewById(R.id.type);
        String s_type = "";
        if (getIntent().getExtras() != null)//get the type user select and create the new activity
            s_type = getIntent().getStringExtra("type");
        type.setText(s_type);//set the type

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        select = false;
        status = 0;
        remain = 0;
        length = 0;

        //check if users select items in dropdown list
        addItemsOnMin();
        addItemsOnHour();

        //media file
        start = MediaPlayer.create(this, R.raw.start);
        medium = MediaPlayer.create(this, R.raw.almostdone);
        end = MediaPlayer.create(this, R.raw.goodjob);
        song = MediaPlayer.create(this, R.raw.walkinginthesun);
        song.setVolume(20, 20);

        //actions for buttons
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.exercise_control);
        DisableShiftMode.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.start://user press start

                                if (status == 1) {//user choose time
                                    break;
                                }

                                if (!select) {//user didn't choose time
                                    Toast toast = Toast.makeText(context, "Please select time first", Toast.LENGTH_SHORT);
                                    //toast message to show user to select time before start
                                    toast.show();
                                    break;
                                }
                                max = (minutes * 60 + seconds) * 10;
                                progressBar.setMax(max);

                                //resume after pause
                                if (status == 2) {
                                    //Log.e("stop", max+";" + remain);
                                    song.seekTo(length);//background music start playing until timer ends
                                    song.start();
                                    myCountDownTimer = new MyCountDownTimer(remain * 1000, 1000);
                                    myCountDownTimer.start();//count down starts
                                    status = 1;

                                } else {
                                    //start
                                    //Log.e("start", max+";" + remain);
                                    start.start();
                                    Handler h = new Handler();
                                    h.postDelayed(new Runnable() {
                                        public void run() {
                                            song.start();
                                        }
                                    }, 1200);

                                    myCountDownTimer = new MyCountDownTimer((minutes * 60 + seconds) * 10000, 1000);
                                    myCountDownTimer.start();
                                    status = 1;
                                }
                                break;

                            case R.id.reset://reset the timer and progress bar
                                myCountDownTimer.cancel();
                                myCountDownTimer = new MyCountDownTimer(0,0);
                                status = 0;
                                time.setText("Time");
                                percent.setText("0%");
                                progressBar.setProgress(0);
                                song.pause();
                                remain = max;
                                break;

                            case R.id.stop://timer stops
                                if (status != 1) {//can not stop before start
                                    Toast toast = Toast.makeText(context, "Please start first", Toast.LENGTH_SHORT);
                                    toast.show();
                                    break;
                                }
                                myCountDownTimer.cancel();
                                status = 2;
                                song.pause();
                                length = song.getCurrentPosition();
                                break;
                        }
                    return true;
                    }
                });

    }
    //add timer's minutes
    public void addItemsOnMin() {

        min = (Spinner) findViewById(R.id.min_spinner);
        ArrayList<String> list = new ArrayList();
        list.add("0 min");
        list.add("10 min");
        list.add("20 min");
        list.add("30 min");
        list.add("40 min");
        list.add("50 min");
        list.add("60 min");
        list.add("70 min");
        list.add("80 min");
        list.add("90 min");
        list.add("100 min");
        list.add("110 min");
        list.add("120 min");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //dropdown list for user to choose
        min.setAdapter(dataAdapter);

        min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get the selected text from spinner
                String spinnerText = min.getSelectedItem().toString();


                //if the value is not "Select your time" then send them to another activity with intent
                if (!spinnerText.contentEquals("0 min")) {
                    minutes = spinnerText.charAt(0) - '0';
                    select = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //Add timer's seconds
    public void addItemsOnHour() {
        second = (Spinner) findViewById(R.id.sec_spinner);
        ArrayList<String> list = new ArrayList();
        list.add("0 s");
        list.add("10 s");
        list.add("20 s");
        list.add("30 s");
        list.add("40 s");
        list.add("50 s");
        list.add("60 s");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //spinner for user to drag down and view the list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //dropdown list for user to choose time
        second.setAdapter(dataAdapter);
        second.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //get the selected text from spinner
                String spinnerText = second.getSelectedItem().toString();
                    //if the value is not "Select your time" then send them to another activity with intent
                if (!spinnerText.contentEquals("0 s")) {
                    seconds = spinnerText.charAt(0) - '0';
                    select = true;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {

                    }
        });
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        //count down process
        public void onTick(long millisUntilFinished) {

            int progress = (int) (millisUntilFinished/1000);
            time.setText("" + (int) (progress / 60) + " MIN " + progress % 60 + " SEC");
            //calculate the remaining proportion of the progress bar
            progressBar.setProgress(progressBar.getMax() - progress);
            percent.setText("" + (int)((double)(max-progress)/max*100) + "%");
            remain = progress;
            if (remain == 0.2 * max)
                medium.start();

            //Log.e("start", "progress1" + progress+";" + max);

        }

        @Override
        //timer finished
        public void onFinish() {
            song.pause();
            length = song.getCurrentPosition();//song stops
            time.setText("Good Job!");//textview show good job
            progressBar.setProgress(progressBar.getMax());
            percent.setText("100%");//progress bar show 100%
            end.start();
            showDialog();
            Cursor cursor = db.getAllProfile();//get user's profile from the database
            if (cursor.moveToFirst()) {
                int i = cursor.getInt(1);//get user's current level
                i = i + 1;//level up 1
                db.updateLevel(1, i);//update the database
                Log.e("updated!", "" + cursor.getInt(1));
                db.close();//close database connection
            }


        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialoglayout);
        builder.show();
    }

}
