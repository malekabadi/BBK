package com.aseman.bbk;

import android.content.Intent;
import android.os.Bundle;
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
        Picasso.with(this) //
                .load("https://www.bbk-iran.com/userupload/images/product-"+ID) //
                //.placeholder(R.drawable.i1) //
                .error(R.drawable.ic_launcher) //
                .fit() //
                .tag(this) //
                .into(img);
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
