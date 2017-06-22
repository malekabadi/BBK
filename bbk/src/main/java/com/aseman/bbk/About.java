package com.aseman.bbk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class About extends AppCompatActivity {

    Company comp = new Company();
    String ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base_color));
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText(" مشخصات فروشگاه");


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

        ImageView sms = (ImageView) findViewById(R.id.b1);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message="";
                String phoneNumber="0915";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                intent.putExtra("sms_body", message);
                startActivity(intent);
            }
        });
        ImageView tel = (ImageView) findViewById(R.id.b2);
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + "0915" ;
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse(uri));
//                try {
//                    startActivity(phoneIntent);
//                    finish();
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(About.this,
//                            "امکان برقرار تماس میسر نمی باشد", Toast.LENGTH_SHORT).show();
//                }
                if (ContextCompat.checkSelfPermission(About.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(About.this, new String[]{Manifest.permission.CALL_PHONE},1);
                    if (ContextCompat.checkSelfPermission(About.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                        startActivity(phoneIntent);
                }
                else
                {
                    startActivity(phoneIntent);
                }                //startActivity(phoneIntent);
            }
        });
        ImageView email = (ImageView) findViewById(R.id.b3);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(About.this, SendMail.class);
                startActivity(i);
            }
        });


    }

    //------------------------------------------------------ Action Bar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action, menu);
        return true;
    }

    //-----------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.arrow_back:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    // -----------------------

}
