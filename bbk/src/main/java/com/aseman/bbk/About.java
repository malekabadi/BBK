package com.aseman.bbk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class About extends AppCompatActivity {

    Company comp=new Company();
    String ID="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            ID = extra.getString("ID");
        } else {
            ID = "0";
        }

        CallRest cr = new CallRest();
        try {
            comp = cr.GetCompany(ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView name = (TextView) findViewById(R.id.Name);
        name.setText(comp.Joinery);
        TextView number = (TextView) findViewById(R.id.Number);
        number.setText(comp.Number);
        TextView act = (TextView) findViewById(R.id.Act);
        act.setText(comp.Activity);
        TextView manager = (TextView) findViewById(R.id.Manager);
        manager.setText(comp.P_Name);
        TextView year = (TextView) findViewById(R.id.Year);
        year.setText(comp.Year);
        TextView www = (TextView) findViewById(R.id.WWW);
        www.setText(comp.Website);
        TextView resume = (TextView) findViewById(R.id.Resume);
        resume.setText(comp.Resume);

        ImageView img = (ImageView) findViewById(R.id.logo);
        Picasso.with(this) //
                .load("https://www.bbk-iran.com/userupload/images/company-" + ID + "-thumb") //
                //.placeholder(R.drawable.i1) //
                .error(R.drawable.ic_launcher) //
                .fit() //
                .tag(this) //
                .into(img);

    }
}
