package cs.dal.a4176_project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by aimee on 17/11/22.
 */

//It is for receiving alarm
public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() { }

    //When receiver receives alarm, it will be triggered. New intent will be open --Ringtone Activity
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(this.getClass().getSimpleName(), "Service triggered");
        Intent secIntent = new Intent(context, RingtoneActivity.class);
        secIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(secIntent);

    }
}
