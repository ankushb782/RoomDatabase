package com.quayintech.roomdatabase;

import android.content.Context;
import android.os.AsyncTask;


import androidx.room.Room;

import com.quayintech.roomdatabase.data.factory.AppDatabase;
import com.quayintech.roomdatabase.model.ModelData;
import com.quayintech.roomdatabase.model.LoginData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import simplifiedcoding.net.kotlinretrofittutorial.api.RetrofitClient;

public class DbToRoomDataUpdateTask {

    private AppDatabase db;

   // private TaskExecutor taskExecutor;

    public DbToRoomDataUpdateTask(){

      //  taskExecutor = new TaskExecutor();
    }
    public void getCouponsFromFirebaseUpdateLocalDb(final Context ctx) throws JSONException {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db = Room.databaseBuilder(ctx,
                        AppDatabase.class, "barangdb").build();
                List<ModelData> arang =Arrays.asList(db.dataDAO().selectAllBarangs());

                for (int i=0;i< arang.size();i++){

                    try {
                        getData(arang.get(i).name,arang.get(i).getEmail(),arang.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


     //   taskExecutor.execute(new RoomUpdateTask("","", ctx));



    }
   /* public class TaskExecutor implements Executor {
        @Override
        public void execute(@NonNull Runnable runnable) {
           Thread t =  new Thread(runnable);
           t.start();
        }
    }
    public class RoomUpdateTask implements Runnable{
        String sName,sEmail;
        private Context context;
        public RoomUpdateTask(String name,String email, Context ctx){
            sName=name;
            sEmail=email;
            context = ctx;
        }
        @Override
        public void run() {
            insertLatestCouponsIntoLocalDb(sName,sEmail, context);
        }
    }
    private void insertLatestCouponsIntoLocalDb(String name,String email, Context ctx){
        db = Room.databaseBuilder(ctx,
                AppDatabase.class, "barangdb").build();

        //insert new coupons
        db.barangDAO().insertBarang(barang);

        //delete expired coupons
        couponsDb.CouponsDb().deleteCoupons(getTodaysDate());

    }
*/

   private void getData(final String name, final String email, final ModelData modelData) throws JSONException {
       JSONObject json=new JSONObject();
       json.put("name",name);
       json.put("email",email);
       RetrofitClient.INSTANCE.getInstance().getData(new LoginData(name,email)).enqueue(new Callback<JSONObject>() {
           @Override
           public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

               // delete data from db
               if(response.isSuccessful()) {

                   AsyncTask.execute(new Runnable() {
                       @Override
                       public void run() {

                           db.dataDAO().deleteBarang(modelData);
                       }
                   });


               }else{

               }
           }

           @Override
           public void onFailure(Call<JSONObject> call, Throwable t) {
           }


       });
   }

}