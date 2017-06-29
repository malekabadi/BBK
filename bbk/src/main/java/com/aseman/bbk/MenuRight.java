package com.aseman.bbk;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuRight extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static  List<Product> products=new ArrayList<Product>();
    ListAdapter listAdapter;
    GridView productsList;
    public static TextView name;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_right);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" بازار بزرگ کشاورزی ایران");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //---------------------------------------------- Check Login
        sp = getSharedPreferences("share", MODE_PRIVATE);
        appVar.main.Frist = sp.getString("Frist", "True");
        if (appVar.main.Frist.equals("True"))
        {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Frist", "False");
            editor.commit();
            Intent i=new Intent(MenuRight.this,HomeActivity.class);
            startActivity(i);
        }
        appVar.main.UserName = sp.getString("UserName", "");
        if (!appVar.main.UserName.equals(""))
        {
            SharedPreferences.Editor editor = sp.edit();
            appVar.main.UserID = sp.getString("UserID", "");
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        name = (TextView)header.findViewById(R.id.Name);
        if (! appVar.main.UserName.equals("")) {
            name.setText(appVar.main.UserName);
            navigationView.getMenu().findItem(R.id.nav_account).setEnabled(true);
        }
        else
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MenuRight.this,Login.class));
                }
            });


        new AsyncTask<Integer, Integer, Boolean>() {
            ProgressDialog progressDialog = null;
            Dialog dialog=null;

            @Override
            protected void onPreExecute() {
                //progressDialog = ProgressDialog.show(MenuRight.this, "", "در حال اتصال ...");
                dialog = new Dialog(MenuRight.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                dialog.show();
            }

            @Override
            protected Boolean doInBackground(Integer... params) {
                CallRest cr=new CallRest();
                products = cr.GetProducts("?last=1");
                return  true;
            }
            protected void onPostExecute(Boolean result) {
                //progressDialog.dismiss();
                dialog.dismiss();
            }
        }.execute();

//        try {
//            products = cr.GetProducts("?last=1");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        ;
        Toast.makeText(this, CallRest.Error, Toast.LENGTH_LONG).show();
        ImageView imageButton = (ImageView) findViewById(R.id.imageView);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Intent i = new Intent(MenuRight.this, HomeActivity.class);
//                startActivity(i);
                Notification.Builder mBuilder =  new Notification.Builder(MenuRight.this)
                        .setSmallIcon(R.drawable.ic_launcher) // notification icon
                        .setContentTitle("Notification!") // title for notification
                        .setContentText("kelidestan.com") // message for notification
                        .setAutoCancel(true); // clear notification after click
                Intent intent = new Intent(MenuRight.this, MenuRight.class);
                PendingIntent pi = PendingIntent.getActivity(MenuRight.this,0,intent,0);
                mBuilder.setContentIntent(pi);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }

        });

        ImageView sale=(ImageView) findViewById(R.id.tab1);
        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuRight.this, Products.class);
                startActivity(i);
            }
        });

        ImageView buy=(ImageView) findViewById(R.id.tab2);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuRight.this, Products.class);
                startActivity(i);
            }
        });

        ImageView prc=(ImageView) findViewById(R.id.tab3);
        prc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuRight.this, Price.class);
                startActivity(i);
            }
        });

        ImageView intro=(ImageView) findViewById(R.id.tab4);
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuRight.this,Introduction.class);
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
            desc.setText(products.get(position).Field1);
            String ID=products.get(position).ID;
            ImageView img = (ImageView) gridViewAndroid.findViewById(R.id.gridview_image);
            Picasso.with(mContext) //
                    .load("https://www.bbk-iran.com/userupload/images/product-"+ID+"-thumb") //
                    //.placeholder(R.drawable.i1) //
                    .error(R.drawable.ic_launcher) //
                    .fit() //
                    .tag(mContext) //
                    .into(img);

//            img.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    // TODO Auto-generated method stub
//                    Intent i= new Intent(MenuRight.this,Details.class);
//                    i.putExtra("PID", products.get(position).ID);
//                    startActivity(i);
//                }
//            });

            return gridViewAndroid;
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_request) {
            Intent i=new Intent(this,Banner.class);
            startActivity(i);

        } else if (id == R.id.nav_special) {

        } else if (id == R.id.nav_fav) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_request) {

        } else if (id == R.id.nav_exit) {
            appVar.main.UserName="";
            appVar.main.UserID="";
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
