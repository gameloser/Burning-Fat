package cs.dal.a4176_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private ImageButton start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Burning! Fat");
        fragmentManager = getSupportFragmentManager();
        fragment = new CalendarFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, fragment).commit();


        start = (ImageButton)findViewById(R.id.ImageButton01);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),TypeSelectActivity.class);
                startActivity(intent);
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        DisableShiftMode.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_calender:
                                fragment = new CalendarFragment();
                                break;
                            case R.id.navigation_progress:
                                fragment = new ProgressFragment();
                                break;
                            case R.id.navigation_tools:
                                fragment = new ToolsFragment();
                                break;
                            case R.id.navigation_profile:
                                fragment = new ProfileFragment();
                                break;
                        }
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content, fragment).commit();
                        return true;
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.codeScanner:
                startActivity(new Intent(getBaseContext(), CodeScannerActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



//    public void start(View view) {
//        Intent intent1 = new Intent(getBaseContext(), TypeSelectActivity.class);
//        startActivity(intent1);
//    }
}
