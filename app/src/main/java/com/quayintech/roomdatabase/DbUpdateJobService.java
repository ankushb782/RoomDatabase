package com.quayintech.roomdatabase;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;

import org.json.JSONException;

public class DbUpdateJobService extends JobService{
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        System.out.println("onStartJob");
        DbToRoomDataUpdateTask dbUpdateTask = new DbToRoomDataUpdateTask();
        try {
            System.out.println("onStartJob");
            dbUpdateTask.getCouponsFromFirebaseUpdateLocalDb(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jobFinished(jobParameters,true);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void scheduleJobFirebaseToRoomDataUpdate(){
        JobScheduler jobScheduler = (JobScheduler)getApplicationContext()
                .getSystemService(JOB_SCHEDULER_SERVICE);

        ComponentName componentName = new ComponentName(this,
                DbUpdateJobService.class);


        // PersistableBundle bundle = new PersistableBundle();
        //  bundle.putString("name", json);
        //  bundle.putString("email", json);
        JobInfo jobInfo = new JobInfo.Builder(100, componentName)
                .setPeriodic(15 * 60 * 1000).setRequiredNetworkType(
                        JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                //.setRequiresBatteryNotLow(true)
                //  .setRequiresCharging(true)
                // .setExtras(bundle)
                .build();
        int result= jobScheduler.schedule(jobInfo);
        if(result==JobScheduler.RESULT_SUCCESS){
            System.out.println("Job Scheduled");
        }else{
            System.out.println("Job Scheduled Failed");
        }

    }
}