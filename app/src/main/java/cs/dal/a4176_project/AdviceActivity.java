package cs.dal.a4176_project;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;

//it is for customize plan
public class AdviceActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDatePicker,confirm;//The buttons are for choosing one date and confirmation
    EditText txtDate,weight_input;//The chosen date can be saved o txtDate
    TextView advice;
    private int year, month, day;//for calculating the gap between selected date and current date
    DatabaseManager db;//connect with database

    String output;
    long numberofday = 0;
    double w = 0.0;
    double tw = 0.0;
    double result = 0.0;

    public AdviceActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);

        db = new DatabaseManager(getBaseContext());

        Cursor cursor = db.getAllProfile();
        if (cursor.moveToFirst()){
            w = Double.valueOf(cursor.getString(6));
        }

        //set connection with the elements of another pages
        btnDatePicker = findViewById(R.id.date_select);
        confirm = findViewById(R.id.confirm);
        txtDate = findViewById(R.id.date_input);
        weight_input = findViewById(R.id.weight_input);
        advice = findViewById(R.id.advice);

        btnDatePicker.setOnClickListener(this);

        confirm.setOnClickListener(this);

    }

    //according to user's input, giving the suitable plan
    @Override
    public void onClick(View v) {
        if (v == confirm) {
            if(weight_input.getText() != null){
                tw = Double.valueOf(weight_input.getText().toString());
                if (tw < w){
                    result = ((w - tw) / numberofday);
                    output = numberofday + " days left\nYour current weight is " + w + "\n You need to lose " + new DecimalFormat("##.##").format(result) + " Kg every day\n\n";
                    if(result < 0.5)
                        output += "Running 20 mins\nSwimming 50 mins\nPush up count 20\n";
                    else if (result >= 0.5 && result < 1){
                        output += "Running 40 mins\nSwimming 1.2 hours\nPush up count 25\nRope jumping count 40\n";
                    }
                    else
                        output += "You are so ambitious. Please set a smaller target.\n";


                }
                else
                    output = "target weight should be less than current weight";


                advice.setText(output);
            }
        }
        if (v == btnDatePicker) {

            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            //It is for choosing date and calculating the difference
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                    txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    Calendar targetDate = Calendar.getInstance();
                    targetDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    targetDate.set(Calendar.MONTH,monthOfYear); // 0-11 so 1 less
                    targetDate.set(Calendar.YEAR, year);

                    Calendar today = Calendar.getInstance();

                    long diff =  targetDate.getTimeInMillis()-today.getTimeInMillis();// change the time to millisecond
                    numberofday = diff / (24 * 60 * 60 * 1000);//the gap time change to date
                }
            }, year, month, day);
            datePickerDialog.show();
        }
    }
}
