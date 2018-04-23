package cs.dal.a4176_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by tedzhao on 2017/11/6.
 */

public class AddRecordActivity extends AppCompatActivity {

    private Button add, cancel;
    private EditText sport_type, sport_title, sport_time, sport_planned,sport_weight;
    private EditText diet_type, diet_title, diet_amount;
    DatabaseManager databaseManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        setTitle("Add record");
        // create db object
        databaseManager = new DatabaseManager(this);
        // add record
        add = (Button) findViewById(R.id.add_record);
        // cancel record
        cancel = (Button) findViewById(R.id.add_cancel);
        // sport type
        sport_type = (EditText) findViewById(R.id.stype);
        // sport title
        sport_title = (EditText) findViewById(R.id.stitle);
        // sport time
        sport_time = (EditText) findViewById(R.id.stime);
        // sport plan time
        sport_planned = (EditText) findViewById(R.id.splanned);
        // user weight
        sport_weight = (EditText) findViewById(R.id.sport_weight);
        // diet type
        diet_type = (EditText) findViewById(R.id.dtype);
        // diet title
        diet_title = (EditText) findViewById(R.id.dtitle);
        // diet amount
        diet_amount = (EditText) findViewById(R.id.damount);

        //collect the info from the edit text
        final CharSequence[] s = new CharSequence[8];//It is convenient to add to database finally
        sport_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            //when the text changes, the edit text info is sent to list
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                s[0] = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sport_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                s[1] = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sport_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                s[2] = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sport_planned.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                s[3] = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        diet_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                s[4] = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        diet_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                s[5] = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        diet_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                s[6] = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sport_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                s[7] = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean emptyField = true;
                //Every text has been defined, on this part, they can be implemented
                if(s[0] != null && s[1] != null && s[2] != null && s[3] != null ) {
                    if (!s[0].toString().isEmpty() && !s[1].toString().isEmpty() && !s[2].toString().isEmpty() && !s[3].toString().isEmpty() && !s[7].toString().isEmpty()) {//send the info to database
                            databaseManager.addSportRecords(s[0].toString(), s[1].toString(), DairyActivity.CURRENT_DATE, s[2].toString(), Float.parseFloat(s[3].toString()), 0f, 0f, 0f, Float.parseFloat(s[7].toString()));
                        emptyField = false;
                    }
                }

                if(s[4] != null && s[5] != null && s[6] != null) {
                    if (!s[4].toString().isEmpty() && !s[5].toString().isEmpty() && !s[6].toString().isEmpty() ) {
                        databaseManager.addDietRecords(s[4].toString(), s[5].toString(), DairyActivity.CURRENT_DATE, Float.parseFloat(s[6].toString()), 1.5f * Float.parseFloat(s[6].toString()));
                        emptyField = false;
                    }
                }

                if (!emptyField) {
                    Toast.makeText(getApplicationContext(), "Plan added success!", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(), DairyActivity.class);
                    startActivity(intent1);
                }
                else
                    Toast.makeText(getApplicationContext(), "Empty field exists!", Toast.LENGTH_SHORT).show();
            }
        });

        //when clicking "cancel", change page to activity
        // cancel button clicked
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
