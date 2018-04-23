package cs.dal.a4176_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

/**
 * Created by Roxanne on 2017/11/4 0013.
 */

//An activity to select exercise type
public class TypeSelectActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private ImageButton run;
    private ImageButton swim;
    private ImageButton rope_skip;
    private ImageButton sit_up;
    private ImageButton push_up;
    private ImageButton bicycle;
    private int level;
    static int TYPE = -1;
    private String s_type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_select);

        //a string used for displaying exercise page
        s_type = "";
        //get users level from database
        level = 0;
        DatabaseManager db;
        db = new DatabaseManager(this);
        Cursor cursor = db.getAllProfile();
        //read from profile table in the database
        if (cursor.moveToFirst()) {
            level = cursor.getInt(1);
            Log.e("level", "" + cursor.getInt(1));
            db.close();
        }

        //users select to run, leads to a map page
        run = (ImageButton)findViewById(R.id.run_type);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TYPE = 0;
                Intent intent = new Intent(getBaseContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        //users select to run, leads to a timer page
        swim = (ImageButton)findViewById(R.id.swim_type);
        swim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ExerciseActivity.class);
                s_type = "Swim";
                intent.putExtra("type", "Swim");
                startActivity(intent);
            }
        });

        //users select to rope skipping, pops up a menu, users can choose a timer or an accelerometer to count
        rope_skip = (ImageButton)findViewById(R.id.rope_type);
        rope_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_type = "Rope Skip";
                PopupMenu popupMenu = new PopupMenu(TypeSelectActivity.this, v);
                popupMenu.setOnMenuItemClickListener(TypeSelectActivity.this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();

            }
        });

        //users select to sit up, pops up a menu, users can choose a timer or an accelerometer to count
        sit_up = (ImageButton)findViewById(R.id.situp_type);
        sit_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_type = "Sit Up";
                PopupMenu popupMenu = new PopupMenu(TypeSelectActivity.this, v);
                popupMenu.setOnMenuItemClickListener(TypeSelectActivity.this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();
            }
        });

        //users select to push up, pops up a menu, users can choose a timer or an accelerometer to count
        push_up = (ImageButton)findViewById(R.id.pushup_type);
        push_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_type = "Push Up";
                PopupMenu popupMenu = new PopupMenu(TypeSelectActivity.this, v);
                popupMenu.setOnMenuItemClickListener(TypeSelectActivity.this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();
            }
        });

        //users select to bicycle, leads to a map page
        bicycle = (ImageButton)findViewById(R.id.bicycle_type);
        bicycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TYPE = 1;
                Intent intent = new Intent(getBaseContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        //shows return button on the top action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //shows return button on the top action bar
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    //pop up menu setting
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;

        //view setting
        switch (item.getItemId()) {
            case R.id.timer:
                intent = new Intent(getBaseContext(), ExerciseActivity.class);
                intent.putExtra("type", s_type);
                startActivity(intent);
                return true;
            case R.id.accelerometer:
                if (level == 0) {
                    item.setEnabled(false);
                    Toast.makeText(this, "Accelerometer is unlock when you are level 2", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(getBaseContext(), RopeCounterActivity.class);
                    startActivity(intent);
                }
                return true;
            default:
                return false;
        }
    }
}

