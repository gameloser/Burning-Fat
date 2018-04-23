package cs.dal.a4176_project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Roxanne on 2017/11/30 0030.
 */

//a splash activity for non-first-user
public class SplashActivity extends Activity {
    private DatabaseManager db;
    private String name;
    private TextView user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseManager(this);

        // check if it is a new user (first use)
        boolean isFirstOpen = true;
        Cursor cursor = db.getAllProfile();
        // check whether there is a profile record in the database
        if (cursor.moveToFirst()) {
            isFirstOpen = false;
            name = cursor.getString(2);
            db.close();
        }


        // if first time --> help guide
        if (isFirstOpen) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // if not first --> splash page
        setContentView(R.layout.splashlayout);
        user = findViewById(R.id.user_name);
        user.setText(name);

        //splash page will show in 3s
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 3000);
    }

    //after splash page, user will enter to the main page
    private void enterHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
