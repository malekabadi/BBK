package com.aseman.bbk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {

    String ID="0";
    ProductDetail product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base_color));
        View mCustomView = getSupportActionBar().getCustomView();
        TextView Title = (TextView) mCustomView.findViewById(R.id.title);
        Title.setText("اطلاعات محصول");



        Bundle extra = getIntent().getExtras();
        if (extra != null)
            ID = extra.getString("PID");

        CallRest cr = new CallRest();
        try {
            product = cr.GetProduct(ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(product.Title);
        TextView price = (TextView) findViewById(R.id.price);
        price.setText(product.Price);
        TextView company = (TextView) findViewById(R.id.company);
        company.setText(product.Company_Name);
        ImageView img = (ImageView) findViewById(R.id.image);

        TextView text1 = (TextView) findViewById(R.id.text1);
        TextView text2 = (TextView) findViewById(R.id.text2);
        TextView text3 = (TextView) findViewById(R.id.text3);
        TextView text4 = (TextView) findViewById(R.id.text4);
        text1.setText(product.Company_Joinery);
        text2.setText(product.Time_Delivery);
        text3.setText(product.Company_Website);
        text4.setText(product.Introduct);

        Picasso.with(this) //
                .load("https://www.bbk-iran.com/userupload/images/product-"+ID) //
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
                if (ContextCompat.checkSelfPermission(Details.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Details.this, new String[]{Manifest.permission.CALL_PHONE},1);
                    if (ContextCompat.checkSelfPermission(Details.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                        startActivity(phoneIntent);
                }
                else
                {
                    startActivity(phoneIntent);
                }
            }
        });

        ImageView email = (ImageView) findViewById(R.id.b3);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Details.this,SendMail.class);
                startActivity(i);
            }
        });

    }


}
