package com.quayintech.roomdatabase;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

public class TestForecastService extends IntentService {

    Context context;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    final int SDK_INT = Build.VERSION.SDK_INT;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent alarm;

    public TestForecastService() {
        super("");
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onHandleIntent(Intent intent) {
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FCFCFCFC");

        wakeLock.acquire();
        sendDATA("");

    }

    private void sendDATA(String city) {
        try {

//

        } catch (Exception e) {
            Log.v("fserviceerror","erre");
        }

        reSETALARAM();
        wakeLock.release();
    }

    private void reSETALARAM() {
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm = new Intent(this,TestForecastService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarm, 0);

        if (SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000, pendingIntent);
            Log.d("lowerFS","hahah");
        }
        else if (Build.VERSION_CODES.KITKAT <= SDK_INT  && SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000,pendingIntent);
            Log.d("kitkatFS","hahah");
        }
        else if (SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000,pendingIntent);
            Log.d("marshmallowFS","hahah");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}