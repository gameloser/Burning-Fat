package cs.dal.a4176_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by Roxanne on 2017/11/4 0013.
 */

public class ProfileFragment extends Fragment{

    private ImageView icon;
    private TextView name, age, gender, weight, height, medical;
    private TextView level;
    String s_name, s_gender, s_medical;
    private Button edit;
    DatabaseManager db;
    Toast toast;
    private ImageView lv;

    private static Integer[] levelimage = {
            R.drawable.lv1,
            R.drawable.lv2,
            R.drawable.lv3,
            R.drawable.lv4,
            R.drawable.lv5,
            R.drawable.lv6,
            R.drawable.lv7,
            R.drawable.lv8,
            R.drawable.lv9,
            R.drawable.lv10,
            R.drawable.lv11,
            R.drawable.lv12};

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        db = new DatabaseManager(getContext());//open database connection
        s_name = "";
        s_gender = "";
        s_medical = "";

        return view;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        icon = getActivity().findViewById(R.id.protrait_icon);//get the head photo from library
        name = getActivity().findViewById(R.id.ename);
        age = getActivity().findViewById(R.id.dage);
        gender = getActivity().findViewById(R.id.egender);
        height = getActivity().findViewById(R.id.eheight);
        weight = getActivity().findViewById(R.id.eweight);
        medical = getActivity().findViewById(R.id.emedicalhistory);


        level = getActivity().findViewById(R.id.level);
        lv = getActivity().findViewById(R.id.image_lv);


        if (getActivity().getIntent().hasExtra("pic")) {
            Bundle ex = getActivity().getIntent().getExtras();
            byte[] byteArray = ex.getByteArray("pic");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            icon.setImageBitmap(bmp);
        }


        Cursor cursor = db.getAllProfile();
        //get the profile information from database

        if (cursor.moveToFirst()) {
            //read from database and show it to user
            int i = cursor.getInt(1);
            level.setText(String.valueOf(i));
            if (i < 13)
                lv.setImageResource(levelimage[i]);
            name.setText(cursor.getString(2));
            age.setText(String.valueOf(cursor.getInt(3)));
            gender.setText(cursor.getString(4));
            height.setText(String.valueOf(cursor.getFloat(5)));
            weight.setText(String.valueOf(cursor.getFloat(6)));
            medical.setText(cursor.getString(7));
        } else
            Log.i("1", "empty cursor");

        edit = view.findViewById(R.id.FileEdit);
        //user click on edit to change profile information and move to the profile edit activity page.
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity().getBaseContext(), ProfileEditActivity.class);
                startActivity(intent1);
            }
        });

    }


}