package cs.dal.a4176_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;


public class CalendarFragment extends Fragment {
    public static String date;
    public static int YEAR, MONTH, DAY;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    static CalendarView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView= view.findViewById(R.id.calendarView);
        // calendar listener
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String mon ="";
                // switch number day to string
                switch (month+1) {
                    case 1:
                        mon = "January";
                        break;
                    case 2:
                        mon = "February";
                        break;
                    case 3:
                        mon = "March";
                        break;
                    case 4:
                        mon = "April";
                        break;
                    case 5:
                        mon = "May";
                        break;
                    case 6:
                        mon = "June";
                        break;
                    case 7:
                        mon = "July";
                        break;
                    case 8:
                        mon = "August";
                        break;
                    case 9:
                        mon = "September";
                        break;
                    case 10:
                        mon = "October";
                        break;
                    case 11:
                        mon = "November";
                        break;
                    case 12:
                        mon = "December";
                        break;

                }
                // date string in new format
                date = mon + " " + dayOfMonth + ", " + year;
                YEAR = year;
                MONTH = month;
                DAY = dayOfMonth;
                Intent intent1 = new Intent(getContext(),DairyActivity.class);
                startActivity(intent1);
            }
        });
        return view;
    }


}
