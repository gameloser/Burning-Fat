package cs.dal.a4176_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//It is showing the record of dairy
public class DairyActivity extends AppCompatActivity {

    DatabaseManager databaseManager;
    TextView textView;
    Toast toast;
    public static String CURRENT_DATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy);
        setTitle(CalendarFragment.date);
        textView = (TextView) findViewById(R.id.fetchedData);
        databaseManager = new DatabaseManager(this);
        fetchedRecords();

    }
    //it is for start activity
    public void start(View view) {
        Intent intent1 = new Intent(getBaseContext(), TypeSelectActivity.class);
        startActivity(intent1);
    }

    //This is for the adding button on the right top of page
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addrecord, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //It is for defining the event when the add button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.add) {
            Intent intent1 = new Intent(this, AddRecordActivity.class);
            startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }
    //Find the responding record of one date
    public void fetchedRecords() {
        Cursor cursor = databaseManager.getAllSportRecords();
        String displayedText = "", sportRecord = "", dietRecord = "";
        String fetchedDate = "";
        String currentClickedDate = "";
        currentClickedDate = CalendarFragment.YEAR + "-" + (CalendarFragment.MONTH+1) + "-" + CalendarFragment.DAY;
        CURRENT_DATE = currentClickedDate;

        if (cursor.moveToFirst()) {//moveToFirst is to find and get the first record of database
            do {
                fetchedDate = cursor.getString(3);
                if (fetchedDate.equals(currentClickedDate)) {
                    sportRecord += "Type:        " + cursor.getString(1) + "\n";
                    sportRecord += "Time:        " + cursor.getString(4) + "\n";
                    sportRecord += "Distance:    " + cursor.getFloat(5) + "\n";
                    sportRecord += "Weight:      " + cursor.getFloat(9) + "\n";
                    sportRecord += "Note:        " + cursor.getString(2) + "\n";
                    sportRecord += "----------------------------------------------------\n";
                }
            } while (cursor.moveToNext());
        }else
            Log.i("1", "empty cursor :"+CURRENT_DATE+" haha");

        if (!sportRecord.equals("")) {
            displayedText += "Sport Record\n\n";
            displayedText += "----------------------------------------------------\n";
            displayedText += sportRecord;
        }

        cursor = databaseManager.getAllDietRecords();

        if (cursor.moveToFirst()) {
            do {
                fetchedDate = cursor.getString(3);
                if (fetchedDate.equals(currentClickedDate)) {
                    dietRecord += "Type:      " + cursor.getString(1) + "\n";
                    dietRecord += "Amount:    " + cursor.getFloat(4) + "\n";
                    dietRecord += "Note:     " + cursor.getString(2) + "\n";
                    dietRecord += "----------------------------------------------------\n";
                    //dietRecord += cursor.getFloat(5) + "\n";
                }

            } while (cursor.moveToNext());

        }else
            Log.i("2", "empty cursor: "+CURRENT_DATE+" heihei");

        if (!dietRecord.equals("")) {
            displayedText += "\n\nDiet Record\n\n";
            displayedText += "----------------------------------------------------\n";
            displayedText += dietRecord;
        }

        textView.setText(displayedText);
    }

}
