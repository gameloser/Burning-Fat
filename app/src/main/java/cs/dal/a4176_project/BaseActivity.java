package cs.dal.a4176_project;

/**
 * Created by Roxanne on 2017/11/28 0028.
 */

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Window;


/**
 * A base class for all of our activities.
 * <p/>
 * Allows us to share some basic methods that all activities want.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    //get the feature of future transition
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        super.onCreate(savedInstanceState);
        styleRecentAppBar();
    }

    /**
     * Style the recent apps menu in Lollipop.
     */
    protected void styleRecentAppBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            String title = "Burning! fat";
            Bitmap icon = BitmapFactory.decodeResource(
                    getResources(), R.mipmap.ic_launcher);
            int color = getResources().getColor(R.color.primary_dark);

            setTaskDescription(new ActivityManager.TaskDescription(title, icon,
                    color));
        }
    }
    //Add an exit navigation button on top left of the screen
    protected void configureTransitions() {
        if (Build.VERSION.SDK_INT >= 21) {
            Transition fade = new Fade();
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setExitTransition(fade);
            getWindow().setEnterTransition(fade);
        }
    }
}