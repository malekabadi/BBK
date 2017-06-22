package com.aseman.bbk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Introduction extends AppCompatActivity {

    List<Companies> comp=new ArrayList<Companies>();
    ListAdapter listAdapter;
    GridView productsList;
    List<Section> sections = new ArrayList<Section>();
    String SID="",Title="همه بخش ها",prevTitle="";
    TextView sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base_color));
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("لیست شرکت ها");


        CallRest cr = new CallRest();
        try {
            comp = cr.GetCompanies("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        productsList = (GridView) findViewById(R.id.companyList);
        listAdapter=new ListAdapter(this);
        productsList.setAdapter(listAdapter);
        sec = (TextView) findViewById(R.id.section);
        sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(Introduction.this);
            }
        });

        Utility.setGridViewHeightBasedOnChildren(productsList,3);
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



        CallRest cr = new CallRest();
        try {
            sections = cr.GetSections("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ArrayList<String> Category = new ArrayList<String>();
        for (Section i:sections)
            Category.add(i.Title);
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
                CallRest cr = new CallRest();
                if (position>0) {
                    SID = sections.get(position - 1).ID;
                    prevTitle = Title;
                    Title = sections.get(position - 1).Title;
                    try {
                        sections = cr.GetSections(SID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Category.clear();
                    Category.add("همه بخش ها");
                    for (Section i : sections)
                        Category.add(i.Title);
                    adapter.notifyDataSetChanged();
                }
                if (sections.size()<1 || position<1) {
                    popup.dismiss();
                    try {
                        comp = cr.GetCompanies(SID);
                        listAdapter.notifyDataSetChanged();
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

//------------------ End Of Activity
}
