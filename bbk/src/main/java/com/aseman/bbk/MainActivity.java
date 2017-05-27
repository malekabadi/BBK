package com.aseman.bbk;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MenuRight {

    List<Product> products=new ArrayList<Product>();
    ListAdapter listAdapter;
    GridView productsList;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        CallRest cr = new CallRest();
        try {
            products = cr.GetProducts("?last=1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
        Toast.makeText(this, CallRest.Error, Toast.LENGTH_LONG).show();
        ImageView imageButton = (ImageView) findViewById(R.id.imageView);
        imageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Products.class);
                startActivity(i);
            }

        });

        productsList = (GridView) findViewById(R.id.gridView);
        listAdapter=new ListAdapter(this);
        productsList.setAdapter(listAdapter);

    }
    //------------------------------------------------------------------------
        public class ListAdapter extends BaseAdapter {

            private Context mContext;
            int Number=0;

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

                gridViewAndroid = new View(mContext);
                gridViewAndroid = inflater.inflate(R.layout.grid_product, null);
                TextView name = (TextView) gridViewAndroid.findViewById(R.id.gridview_text1);
                TextView price = (TextView) gridViewAndroid.findViewById(R.id.gridview_text2);
                TextView desc = (TextView) gridViewAndroid.findViewById(R.id.gridview_text3);
                name.setText(products.get(position).Title);
                if (products.get(position).Price.length() > 0)
                    price.setText(Utility.GetMoney(products.get(position).Price));
                desc.setText(products.get(position).Company_Name);
                String ID=products.get(position).ID;
                ImageView img = (ImageView) gridViewAndroid.findViewById(R.id.gridview_image);
                Picasso.with(mContext) //
                        .load("https://www.bbk-iran.com/userupload/images/product-"+ID+"-thumb") //
                        //.placeholder(R.drawable.i1) //
                        .error(R.drawable.ic_launcher) //
                        .fit() //
                        .tag(mContext) //
                        .into(img);

                img.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        Intent i= new Intent(MainActivity.this,Details.class);
                        i.putExtra("PID", products.get(position).ID);
                        startActivity(i);
                    }
                });

                return gridViewAndroid;
            }


        }


}
