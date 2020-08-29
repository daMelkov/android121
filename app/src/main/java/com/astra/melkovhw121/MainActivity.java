package com.astra.melkovhw121;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PERMISSION_READ_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_READ_STORAGE);
        }
    }

    private void init() {
        // init data in private external storage
        Data.writeToPrivateExternalStorage(
                MainActivity.this,
                "data.txt",
                getString(R.string.samples),
                false
        );

        initViews();
    }

    private void initViews() {
        List<String> samples = getData();

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, samples);

        ListView list = findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    private List<String> getData() {
        String samples = Data.readFromPrivateExternalStorage(
                MainActivity.this,
                "data.txt"
        );

        List<String> result = new ArrayList<>(Arrays.asList(samples.split(";")));
        return result;
    }


    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grants) {
        if (requestCode == REQUEST_CODE_PERMISSION_READ_STORAGE) {
            if (grants.length > 0 && grants[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                finish();
            }
        }
    }
}