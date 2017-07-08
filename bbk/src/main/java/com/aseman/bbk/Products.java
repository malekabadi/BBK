package com.aseman.bbk;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Products extends AppCompatActivity {

    List<Product> products=new ArrayList<Product>();
    List<Product> more=new ArrayList<Product>();
    ListAdapter listAdapter;
    ListView productsList;
    TextView sec;
    List<Section> sections = new ArrayList<Section>();
    List<Companies> comp=new ArrayList<Companies>();
    String SID="",Title="همه بخش ها",prevTitle="";
    int lastItemPosition=0,page=1;

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


        new LongOperation().execute("");

        productsList = (ListView) findViewById(R.id.listView);
        listAdapter=new ListAdapter(this);
        productsList.setAdapter(listAdapter);


        productsList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastItem = firstVisibleItem + visibleItemCount;
                if (listAdapter.getCount() >= 10 && lastItem > listAdapter.getCount() - 1) {
                    boolean isLoading = false;
                    if (!isLoading) {
                        // if(readNetworkStatus(context)){
                        if (lastItem > lastItemPosition) {
                            Toast.makeText(Products.this, "loading...", Toast.LENGTH_LONG).show();
                            lastItemPosition = listAdapter.getCount();
                            page++;
                            new LongOperation2().execute("?page="+String.valueOf(page));
                        }
                        // }
                        isLoading = true;
                    }
                }
            }
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

        });

        sec = (TextView) findViewById(R.id.section);
        sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(Products.this);
            }
        });

        ImageView buy=(ImageView) findViewById(R.id.tab2);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Products.this, ShowBanners.class);
                startActivity(i);
                finish();
            }
        });

        ImageView prc=(ImageView) findViewById(R.id.tab3);
        prc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Products.this, Price.class);
                startActivity(i);
                finish();
            }
        });

        ImageView intro=(ImageView) findViewById(R.id.tab4);
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Products.this,Introduction.class);
                startActivity(i);
                finish();
            }
        });

    }

    //----------------------------------------------------------
    private class LongOperation extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog progressDialog = null;
        Dialog dialog=null;

        @Override
        protected void onPreExecute() {
            //progressDialog = ProgressDialog.show(MenuRight.this, "", "در حال اتصال ...");
            dialog = new Dialog(Products.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


            dialog.setContentView(R.layout.dialog);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            CallRest cr=new CallRest();
            products = cr.GetProducts(params[0]);
            return  true;
        }
        protected void onPostExecute(Boolean result) {
            listAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }


    }
    //----------------------------------------------------------
    private class LongOperation2 extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog progressDialog = null;
        Dialog dialog=null;

        @Override
        protected void onPreExecute() {
            //progressDialog = ProgressDialog.show(MenuRight.this, "", "در حال اتصال ...");
            dialog = new Dialog(Products.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


            dialog.setContentView(R.layout.dialog);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            CallRest cr=new CallRest();
            more = cr.GetProducts(params[0]);
            return  true;
        }
        protected void onPostExecute(Boolean result) {
            products.addAll(more);
            listAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }


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
                        String phoneNumber=products.get(position).Phones;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                        intent.putExtra("sms_body", message);
                        startActivity(intent);
                    }
                });
                ImageView tel = (ImageView) gridViewAndroid.findViewById(R.id.b2);
                tel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri = "tel:" + products.get(position).Phones; ;
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

    //------------------------------------------------------------
    private void showPopup(final Activity context) {

//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup);
//        dialog.show();

        Display display = getWindowManager().getDefaultDisplay();
        int popupWidth = display.getWidth();//-(int)(display.getWidth()*0.2);
        int popupHeight = display.getHeight();//-(int)(display.getWidth()*0.6);

        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);



        final ArrayList<String> Category = new ArrayList<String>();
        for (Section i:Splash.sections)
            if (i.Parnet.equals("1")) {
                Category.add(i.Title);
                sections.add(i);
            }
        final ListView lv = (ListView) layout.findViewById(R.id.list_topic);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.listview_item_row, Category);
        lv.setAdapter(adapter);


        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        popup.setBackgroundDrawable(new ColorDrawable(0xa0000000));
        popup.getContentView().setBackgroundResource(android.R.color.transparent);

        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        Category.add(0,"همه بخش ها");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (position>0) {
                    SID = sections.get(position - 1).ID;
                    prevTitle = Title;
                    Title = sections.get(position - 1).Title;

                    Category.clear();
                    sections.clear();
                    Category.add("همه بخش ها");
                    for (Section i:Splash.sections)
                        if (i.Parnet.equals(SID)) {
                            Category.add(i.Title);
                            sections.add(i);
                        }
                    adapter.notifyDataSetChanged();
                }
                if (sections.size()<1 || position<1) {
                    popup.dismiss();
                    try {
                        new LongOperation().execute("?sid="+SID);
                        sec.setText(Title);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
