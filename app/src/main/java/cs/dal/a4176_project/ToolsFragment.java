package cs.dal.a4176_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

//a tool selection fragment
public class ToolsFragment extends Fragment {
    public ToolsFragment() {

    }

    //setting the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tools, container, false);
        int level = 0;
        DatabaseManager db;
        db = new DatabaseManager(getContext());
        ImageButton alarm;
        ImageButton cc;
        ImageButton bluetooth;
        ImageButton video;
        ImageButton advice;

        //reading users level from database, some tools is unblocked when users is up to specific level
        Cursor cursor = db.getAllProfile();
        if (cursor.moveToFirst()) {
            level = cursor.getInt(1);
            Log.e("level", "" + cursor.getInt(1));
            db.close();
        }

        //set all buttons, textview
        TextView lock_alarm, lock_cc, lock_video;
        cc = (ImageButton)view.findViewById(R.id.imageButton2);
        alarm = (ImageButton)view.findViewById(R.id.imageButton);
        bluetooth = view.findViewById(R.id.imageButton3);
        video = view.findViewById(R.id.imageButton4);
        lock_alarm = view.findViewById(R.id.lock_alarm);
        lock_cc = view.findViewById(R.id.lock_cc);
        lock_video = view.findViewById(R.id.lock_video);
        advice = view.findViewById(R.id.imageButton5);

        //alarm is for 1+ level users
        if (level > 0) {
            lock_alarm.setVisibility(View.INVISIBLE);
            alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AlarmActivity.class);
                    startActivity(intent);
                }
            });

            //video is for 1+ level users
            lock_video.setVisibility(View.INVISIBLE);
            video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), VideoSelectActivity.class);
                    startActivity(intent);
                }
            });
        }

        //calorie calculation is for 2+ level users
        if (level > 1) {
            lock_cc.setVisibility(View.INVISIBLE);
            cc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(getContext(), CcActivity.class);
                    startActivity(intent2);
                }
            });

        }

        //leads bluetooth activity
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BluetoothActivity.class);
                startActivity(intent);
            }
        });


        //leads advice activity
        advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdviceActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}