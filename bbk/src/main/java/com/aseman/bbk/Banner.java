package com.aseman.bbk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

public class Banner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner);

        String[] items = new String[] {"One", "Two", "Three"};
        Spinner city = (Spinner) findViewById(R.id.txtCity);

        spinnerAdapter adapter = new spinnerAdapter(Banner.this, android.R.layout.simple_list_item_1);
        adapter.addAll(items);
        adapter.add("شهر");
        city.setAdapter(adapter);
        city.setSelection(adapter.getCount());

    }
}
