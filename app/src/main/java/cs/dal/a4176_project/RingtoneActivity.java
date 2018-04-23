package cs.dal.a4176_project;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

//It is for playing sound and having the stop button
public class RingtoneActivity extends AppCompatActivity {

    Button stop;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone);
        stop = findViewById(R.id.stop);
        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mediaPlayer.start();

        stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });

    }
    //set a button for stopping sound
    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

}
