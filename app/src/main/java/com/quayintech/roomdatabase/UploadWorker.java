package com.quayintech.roomdatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;

public class UploadWorker extends Worker {
   public UploadWorker(
       @NonNull Context context,
       @NonNull WorkerParameters params) {
       super(context, params);
   }

   @Override
   public Result doWork() {

       DbToRoomDataUpdateTask dbUpdateTask = new DbToRoomDataUpdateTask();
       try {
           System.out.println("onStartJob");
           dbUpdateTask.getCouponsFromFirebaseUpdateLocalDb(getApplicationContext());
       } catch (JSONException e) {
           e.printStackTrace();
       }


     return Result.success();
   }
}
