package com.aseman.bbk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Introduction extends AppCompatActivity {

    List<Companies> comp=new ArrayList<Companies>();
    ListAdapter listAdapter;
    GridView productsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        CallRest cr = new CallRest();
        try {
            comp = cr.GetCompanies();
        } catch (Exception e) {
            e.printStackTrace();
        }

        productsList = (GridView) findViewById(R.id.companyList);
        listAdapter=new ListAdapter(this);
        productsList.setAdapter(listAdapter);

        Utility.setGridViewHeightBasedOnChildren(productsList,2);
//        Float density = this.getResources().getDisplayMetrics().density;
//        ViewGroup.LayoutParams params = productsList.getLayoutParams();
//        params.height = (int) (170 * density * listAdapter.getCount());
//        productsList.setLayoutParams(params);

    }

    //--------------------------------------------------------------------------------------
    public class ListAdapter extends BaseAdapter {

        private Context mContext;
        int Number=0;
        int select=-1;
        public ListAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return comp.size();
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
            gridViewAndroid = inflater.inflate(R.layout.gridview_company, null);
            TextView name = (TextView) gridViewAndroid.findViewById(R.id.gridview_text1);
            TextView desc = (TextView) gridViewAndroid.findViewById(R.id.gridview_text2);
            name.setText(comp.get(position).Full_Name);
            desc.setText(comp.get(position).Activity);
            String ID = comp.get(position).ID;
            ImageView img = (ImageView) gridViewAndroid.findViewById(R.id.gridview_image);
            final ProgressBar pb = (ProgressBar) gridViewAndroid.findViewById(R.id.progressBar1);
            pb.setVisibility(View.VISIBLE);
            Picasso.with(mContext) //
                    .load("https://www.bbk-iran.com/userupload/images/company-" + ID + "-thumb") //
                    //.placeholder(R.drawable.i1) //
                    .error(R.drawable.ic_launcher) //
                    .fit() //
                    .tag(mContext) //
                    .into(img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            pb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            pb.setVisibility(View.GONE);
                        }
                    });

            img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent i= new Intent(Introduction.this,About.class);
                    i.putExtra("ID", comp.get(position).ID);
                    startActivity(i);
                }
            });

            return gridViewAndroid;
        }
    }
//------------------ End Of Activity
}
