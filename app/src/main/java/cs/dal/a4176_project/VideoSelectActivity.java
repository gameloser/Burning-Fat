package cs.dal.a4176_project;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Roxanne on 2017/11/28 0028.
 */

//A selecting page for users to select YouTube Video
public class VideoSelectActivity extends AppCompatActivity {
    private ImageButton arm, belly, leg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_select);

        //show the "return" button on the top action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //three selection
        arm = (ImageButton) findViewById(R.id.ib_arm);
        belly = (ImageButton) findViewById(R.id.ib_belly);
        leg = (ImageButton) findViewById(R.id.ib_leg);

        //arm workout video
        arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), VideoActivity.class);
                // Plays https://www.youtube.com/watch?v=7KlnteHnZaU
                //Save the link in the extra
                intent.putExtra("link", "7KlnteHnZaU");
                startActivity(intent);
            }
        });

        //belly workout video
        belly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), VideoActivity.class);
                // Plays https://www.youtube.com/watch?v=cYEKCIlobjE
                //Save the link in the extra
                intent.putExtra("link", "cYEKCIlobjE");
                startActivity(intent);
            }
        });

        //leg workout video
        leg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), VideoActivity.class);
                // Plays https://www.youtube.com/watch?v=bR3p1uVgCoo
                //Save the link in the extra
                intent.putExtra("link", "bR3p1uVgCoo");
                startActivity(intent);
            }
        });
    }

    //return button on the top action bar
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //back to the home page
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}
