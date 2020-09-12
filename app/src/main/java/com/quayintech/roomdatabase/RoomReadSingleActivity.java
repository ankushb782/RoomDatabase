package com.quayintech.roomdatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.quayintech.roomdatabase.model.ModelData;


public class RoomReadSingleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        EditText etNama = findViewById(R.id.et_name);
        EditText etMerk = findViewById(R.id.et_email);
        EditText etHarga = findViewById(R.id.et_extra);
        Button btSubmit = findViewById(R.id.bt_submit);

        etNama.setEnabled(false);
        etMerk.setEnabled(false);
        etHarga.setEnabled(false);
        btSubmit.setVisibility(View.GONE);

        ModelData modelData = (ModelData) getIntent().getSerializableExtra("data");
        if(modelData !=null){
            etNama.setText(modelData.getName());
            etMerk.setText(modelData.getEmail());
            etHarga.setText(modelData.getExtra());
        }

    }

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, RoomReadSingleActivity.class);
    }
}
