package cs.dal.a4176_project;

/**
 * Created by Roxanne on 2017/11/30 0030.
 */

import java.util.ArrayList;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

//A view pager adapter for Welcome Activity
public class ViewPagerAdapter extends PagerAdapter {
    //view
    private ArrayList<View> views;

    public ViewPagerAdapter(ArrayList<View> views) {
        this.views = views;
    }

    //get number of viewa
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        } else return 0;
    }

    //check if view is from object
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    //destroy item from specific position
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }

    //initialize item from specific position
    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(views.get(position), 0);
        return views.get(position);
    }

}
