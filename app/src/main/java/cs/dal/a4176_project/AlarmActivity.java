package cs.dal.a4176_project;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

//The activity is for setting alarm （It is a repeating alarm)
public class AlarmActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private TextView textView;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    //This is for setting alarm, triggering alarm, and canceling alarm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        textView = findViewById(R.id.textView);
        textView.setText("0:0");
        timePicker.setIs24HourView(true);
        final Calendar calendar = Calendar.getInstance();//This is for transferring date

        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                textView.setText("Time selected: "+hourOfDay+":"+minute);
                //set calendar time
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, alarmIntent);
                textView.setText("ALARM SET");
            }
        });//setting alarm

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmMgr!= null) {
                    alarmMgr.cancel(alarmIntent);
                }
                textView.setText("ALARM CANCELED");
            }
        });//delete the alarm
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}

