package cs.dal.a4176_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class CcActivity extends AppCompatActivity {

    ArrayList items;
    ArrayList items2;
    ListView listView;
    ListView listView2;
    TextView calorie;
    TextView calorie2;
    EditText editText;
    EditText editText2;
    Button confirmButton;
    Button confirmButton2;
    int c_in =0;
    int c_out =0;
    private CustomAdapter adapter1;
    private CustomAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calorie = (TextView)findViewById(R.id.c_in);
        calorie2 = (TextView)findViewById(R.id.c_out);

        listView = (ListView) findViewById(R.id.ListView);
        listView2 = (ListView) findViewById(R.id.ListView2);

        editText = (EditText) findViewById(R.id.EditText);
        editText2 = (EditText) findViewById(R.id.EditText2);

        confirmButton = (Button) findViewById(R.id.Button);
        confirmButton2 = (Button) findViewById(R.id.Button2);

        calorie.setText("calorie input: "+c_in);
        calorie2.setText("calorie output: "+c_out);

        items = new ArrayList();
        items2 = new ArrayList();

        //add information to choice list
        items.add(new Item("Apple Pie", 1,false));
        items.add(new Item("Banana Bread", 2,false));
        items.add(new Item("Cupcake", 3,false));
        items.add(new Item("Donut", 4,false));
        items.add(new Item("Eclair", 5,false));
        items.add(new Item("Froyo", 6,false));
        items.add(new Item("Gingerbread", 7,false));
        items.add(new Item("Honeycomb", 8,false));
        items.add(new Item("Ice Cream Sandwich", 9,false));
        items.add(new Item("Jelly Bean", 10,false));
        items.add(new Item("Kitkat", 11,false));
        items.add(new Item("Lollipop", 12,false));
        items.add(new Item("Marshmallow", 13,false));
        items.add(new Item("Nougat", 14,false));

        items2.add(new Item("Running", 1,false));
        items2.add(new Item("Swimming", 2,false));
        items2.add(new Item("Sit-up", 3,false));
        items2.add(new Item("Push-up", 4,false));
        items2.add(new Item("Rope-skip", 5,false));
        items2.add(new Item("Jump", 6,false));
        items2.add(new Item("A", 7,false));

        //two adapters for listeners
        adapter1 = new CustomAdapter(items, getApplicationContext());
        adapter2 = new CustomAdapter(items2, getApplicationContext());

        //set adapter
        listView.setAdapter(adapter1);
        listView2.setAdapter(adapter2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                //calculate intake
                Item item= (Item) items.get(position);
                item.checked = !item.checked;
                if(item.checked){
                    c_in+=item.calorie;
                }else {
                    c_in-=item.calorie;
                }
                adapter1.notifyDataSetChanged();

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = editText.getText().toString();
                if(str.trim().equals("")){
                    str = "1";
                }
                calorie.setText("calorie input: "+c_in*Integer.parseInt(str)+" J");
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                //calculate the consumption
                Item item= (Item) items2.get(position);
                item.checked = !item.checked;
                if(item.checked){
                    c_out+=item.calorie;
                }else {
                    c_out-=item.calorie;
                }
                adapter2.notifyDataSetChanged();

            }
        });

        confirmButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = editText2.getText().toString();
                if(str.trim().equals("")){
                    str = "1";
                }
                calorie2.setText("calorie output: "+c_out*Integer.parseInt(str)+" J");
            }
        });

    }
    //for menu
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

