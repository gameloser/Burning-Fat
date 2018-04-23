package cs.dal.a4176_project;

/**
 * Created by Roxanne on 2017/11/30 0030.
 */

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

//A welcome activity shown when a first use user. There are four help layout to introduce main features
public class WelcomeActivity extends Activity implements OnClickListener, OnPageChangeListener {
    private ViewPager viewPager;
    private ViewPagerAdapter vpAdapter;
    private ArrayList<View> views;
    //Add all layout in an integer array
    private static final int[] pics = {R.layout.welcome_view1, R.layout.welcome_view2, R.layout.welcome_view3, R.layout.welcome_view4};
    private ImageView[] points;
    private int currentIndex;
    private Button startBtn;

    //Create both image and points
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomelayout);
        initView();
        initData();
    }

    //Initialize view
    private void initView() {
        views = new ArrayList<View>();
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);

            //Show enter button at the final page
            if (i == pics.length - 1) {
                startBtn = (Button) view.findViewById(R.id.btn_login);
                startBtn.setTag("enter");
                startBtn.setOnClickListener(this);
            }

            views.add(view);

        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        vpAdapter = new ViewPagerAdapter(views);

    }

    //Initialize the switching point
    private void initData() {
        //setData
        viewPager.setAdapter(vpAdapter);
        viewPager.setOnPageChangeListener(this);

        // initialize points on the buttom
        initPoint();
    }


    private void initPoint() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);
        points = new ImageView[pics.length];

        // get points in a loop
        for (int i = 0; i < pics.length; i++) {
            points[i] = (ImageView) linearLayout.getChildAt(i);
            points[i].setEnabled(true);
            points[i].setOnClickListener(this);
            points[i].setTag(i);
        }
        // set current index
        currentIndex = 0;
        points[currentIndex].setEnabled(false);
    }

    //onPageScrollStateChanged
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    //onPageScrolled
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    //onPageSelected
    @Override
    public void onPageSelected(int arg0) {
        // change point color
        setCurDot(arg0);
    }

    //activity for enter button
    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterMainActivity();
            return;
        }
        //get the tag of current position
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    //set current view of page
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    //set current view of point
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length - 1 || currentIndex == position) {
            return;
        }
        points[position].setEnabled(false);
        points[currentIndex].setEnabled(true);

        currentIndex = position;
    }

    //go to the main page
    private void enterMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
