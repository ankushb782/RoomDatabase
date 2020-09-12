package com.quayintech.roomdatabase;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.quayintech.roomdatabase.data.factory.AppDatabase;
import com.quayintech.roomdatabase.model.ModelData;
import com.quayintech.roomdatabase.model.LoginData;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import simplifiedcoding.net.kotlinretrofittutorial.api.RetrofitClient;


public class RoomCreateActivity extends AppCompatActivity {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private AppDatabase db;
    EditText etName, etEmail, etExtra;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "barangdb").build();

        setWorkSchedule();

        etName = findViewById(R.id.et_name);
         etEmail = findViewById(R.id.et_email);
         etExtra = findViewById(R.id.et_extra);
        Button btSubmit = findViewById(R.id.bt_submit);

        final ModelData modelData = (ModelData) getIntent().getSerializableExtra("data");

        if(modelData !=null){
            etName.setText(modelData.getName());
            etEmail.setText(modelData.getEmail());
            etExtra.setText(modelData.getExtra());
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modelData.setName(etName.getText().toString());
                    modelData.setEmail(etEmail.getText().toString());
                    modelData.setExtra(etExtra.getText().toString());

                    updateBarang(modelData);
                }
            });
        }else{
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        if(!etName.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty()){
                            getData(etName.getText().toString(), etEmail.getText().toString());
                        }else{
                            Toast.makeText(RoomCreateActivity.this, "Please enter name and email", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        ModelData b = new ModelData();
                        b.setExtra(etExtra.getText().toString());
                        b.setEmail(etEmail.getText().toString());
                        b.setName(etName.getText().toString());
                        insertData(b);
                    }

                }
            });
        }
        Button btView = findViewById(R.id.bt_viewdata);
        btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RoomReadActivity.getActIntent(RoomCreateActivity.this));
            }
        });
    }

    private void setWorkSchedule() {
        Constraints constraints=new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(true).
                        build();
        WorkRequest uploadWorkRequest =
                new PeriodicWorkRequest.Builder(UploadWorker.class,60, TimeUnit.SECONDS)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(getApplicationContext()).enqueue(uploadWorkRequest);
    }

    private void updateBarang(final ModelData modelData){
        new AsyncTask<Void, Void, Long>(){
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.dataDAO().updateBarang(modelData);
                return status;
            }

            @Override
            protected void onPostExecute(Long status) {
                Toast.makeText(RoomCreateActivity.this, "status row "+status, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void insertData(final ModelData modelData){

        new AsyncTask<Void, Void, Long>(){
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.dataDAO().insertBarang(modelData);
                return status;
            }

            @Override
            protected void onPostExecute(Long status) {
                Toast.makeText(RoomCreateActivity.this, "Data Saved "+status, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, RoomCreateActivity.class);
    }




    private void getData(String name,String email){
        JSONObject json=new JSONObject();


        RetrofitClient.INSTANCE.getInstance().getData(new LoginData(name,email)).enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                // success
                if(!response.isSuccessful()){
                    ModelData b = new ModelData();
                    b.setExtra(etExtra.getText().toString());
                    b.setEmail(etEmail.getText().toString());
                    b.setName(etName.getText().toString());
                    insertData(b);
                }else{
                    Toast.makeText(RoomCreateActivity.this, "Data Saved to Server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                ModelData b = new ModelData();
                b.setExtra(etExtra.getText().toString());
                b.setEmail(etEmail.getText().toString());
                b.setName(etName.getText().toString());
                insertData(b);
            }
        });

    }
}
