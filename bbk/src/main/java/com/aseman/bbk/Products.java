package com.aseman.bbk;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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

            img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
//                        Intent i= new Intent(ShowProducts.this,ShowDetails.class);
//                        i.putExtra("PID", products.get(position).ID);
//                        startActivity(i);
                }
            });

            return gridViewAndroid;
        }


    }

}