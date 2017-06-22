package com.aseman.bbk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Products extends AppCompatActivity {

    List<Product> products=new ArrayList<Product>();
    ListAdapter listAdapter;
    ListView productsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base_color));
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("لیست کالاها");


        CallRest cr = new CallRest();
        try {
            products = cr.GetProducts("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        productsList = (ListView) findViewById(R.id.listView);
        listAdapter=new ListAdapter(this);
        productsList.setAdapter(listAdapter);

    }
//----------------------------------------------------------
    public class ListAdapter extends BaseAdapter {

        private Context mContext;
        int Number=0;
        int select=-1;
        boolean open=false;
        ProductDetail product;
        public ListAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return products.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View gridViewAndroid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {;
            } else {
                gridViewAndroid = (View) convertView;
            }

            if (position == select) {
                //open=true;
                gridViewAndroid = inflater.inflate(R.layout.grid_product2, null);
                TextView name = (TextView) gridViewAndroid.findViewById(R.id.gridview_text1);
                TextView price = (TextView) gridViewAndroid.findViewById(R.id.gridview_text2);
                TextView desc = (TextView) gridViewAndroid.findViewById(R.id.gridview_text3);
                name.setText(products.get(position).Title);
                if (products.get(position).Price.length() > 0)
                    price.setText(Utility.GetMoney(products.get(position).Price));
                desc.setText(products.get(position).Company_Name);
                String ID=products.get(position).ID;
                TextView text1 = (TextView) gridViewAndroid.findViewById(R.id.text1);
                TextView text2 = (TextView) gridViewAndroid.findViewById(R.id.text2);
                TextView text3 = (TextView) gridViewAndroid.findViewById(R.id.text3);
                TextView text4 = (TextView) gridViewAndroid.findViewById(R.id.text4);
                text1.setText(products.get(position).Company_Name);
                text2.setText(products.get(position).Field1);
                text3.setText(products.get(position).Field2);
                text4.setText(products.get(position).Phones);

                ImageView img = (ImageView) gridViewAndroid.findViewById(R.id.gridview_image);
                Picasso.with(mContext) //
                        .load("https://www.bbk-iran.com/userupload/images/product-"+ID+"-thumb") //
                        //.placeholder(R.drawable.i1) //
                        .error(R.drawable.ic_launcher) //
                        .fit() //
                        .tag(mContext) //
                        .into(img);
                img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        select = -1;
                        notifyDataSetChanged();
                    }
                });

                ImageView sms = (ImageView) gridViewAndroid.findViewById(R.id.b1);
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
                ImageView tel = (ImageView) gridViewAndroid.findViewById(R.id.b2);
                tel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri = "tel:" + "0915" ;
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        phoneIntent.setData(Uri.parse(uri));
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Products.this, new String[]{Manifest.permission.CALL_PHONE},1);
                            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                                startActivity(phoneIntent);
                        }
                        else
                        {
                            startActivity(phoneIntent);
                        }
                    }
                });
                ImageView email = (ImageView) gridViewAndroid.findViewById(R.id.b3);
                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(mContext,SendMail.class);
                        startActivity(i);
                    }
                });


            } else {
                //open=false;
                gridViewAndroid = inflater.inflate(R.layout.grid_product, null);
                LinearLayout heg = (LinearLayout) gridViewAndroid.findViewById(R.id.heg);
//                LinearLayout.LayoutParams params = heg.getLayoutParams();
//                params.height = 400;
//                params.width = 100;
//                gridViewAndroid.setLayoutParams(params);
                //gridViewAndroid.setLayoutParams();
//                heg.setLayoutParams(new LinearLayout.LayoutParams(100,400));
//            }
                TextView name = (TextView) gridViewAndroid.findViewById(R.id.gridview_text1);
                TextView price = (TextView) gridViewAndroid.findViewById(R.id.gridview_text2);
                TextView desc = (TextView) gridViewAndroid.findViewById(R.id.gridview_text3);
                name.setText(products.get(position).Title);
                if (products.get(position).Price.length() > 0)
                    price.setText(Utility.GetMoney(products.get(position).Price));
                desc.setText(products.get(position).Field1);
                String ID = products.get(position).ID;
                ImageView img = (ImageView) gridViewAndroid.findViewById(R.id.gridview_image);
                Picasso.with(mContext) //
                        .load("https://www.bbk-iran.com/userupload/images/product-" + ID + "-thumb") //
                        //.placeholder(R.drawable.i1) //
                        .error(R.drawable.ic_launcher) //
                        .fit() //
                        .tag(mContext) //
                        .into(img);

                img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
//                        Intent i= new Intent(ShowProducts.this,ShowDetails.class);
//                        i.putExtra("PID", products.get(position).ID);
//                        startActivity(i);
                        select = position;
                        notifyDataSetChanged();
                    }
                });
            }
            return gridViewAndroid;
        }


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
