package com.astra.melkovhw121;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
        // init data in private external storage (in first run)
        Data.writeToPrivateExternalStorage(
                MainActivity.this,
                "data.txt",
                getString(R.string.samples),
                false
        );

        initViews();
    }

    private void initViews() {
        // ArrayAdapter
        final List<String> samples = getDataFromExternalStorage();
        final ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, samples);

        // ListView
        ListView list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // remove from list
                samples.remove(position);

                // remove from private external file
                setDataToExternalStorage(samples);

                // update
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setDataToExternalStorage(List<String> samples) {
        StringBuilder newData = new StringBuilder();
        for (String item : samples) {
           newData.append(item).append(";");
        }

        Data.writeToPrivateExternalStorage(
                MainActivity.this,
                "data.txt",
                newData.toString(),
                true
        );
    }

    private List<String> getDataFromExternalStorage() {
        String samples = Data.readFromPrivateExternalStorage(
                MainActivity.this,
                "data.txt"
        );

        List<String> result = new ArrayList<>();

        // avoid empty elements (between ';' after deleting)
        for (String data: Arrays.asList(samples.split(";"))) {
            if(data.length() != 0) {
                result.add(data);
            }
        }
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