package com.aseman.bbk;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.IntToString;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter extends BaseAdapter   implements OnClickListener {
         
	private final int[] bgColors = new int[] { R.color.list_bg_1, R.color.list_bg_2 };     
	/*********** Declare Used Variables *********/
         private Activity activity;
         private ArrayList data;
         private static LayoutInflater inflater=null;
         public Resources res;
         ListModel tempValues=null;
         int i=0;
          
         /*************  CustomAdapter Constructor *****************/
         public CustomAdapter(Activity a, ArrayList d,Resources resLocal) {
              
                /********** Take passed values **********/
                 activity = a;
                 data=d;
                 res = resLocal;
              
                 /***********  Layout inflator to call external xml layout () ***********/
                  inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              
         }
      
         /******** What is the size of Passed Arraylist Size ************/
         public int getCount() {
              
             if(data.size()<=0)
                 return 1;
             return data.size();
         }
      
         public Object getItem(int position) {
             return position;
         }
      
         public long getItemId(int position) {
             return position;
         }
          
         /********* Create a holder Class to contain inflated xml file elements *********/
         public static class ViewHolder{
              
             public TextView name;
             public TextView date;
             public TextView min;
             public TextView max;
             public TextView pId;
      
         }
      
         /****** Depends upon data size called for each row , Create each ListView row *****/
         public View getView(int position, View convertView, ViewGroup parent) {
              
             View vi = convertView;
             ViewHolder holder;
              
             if(convertView==null){
                  
                 /****** Inflate .xml file for each row ( Defined below ) *******/
                 vi = inflater.inflate(R.layout.list_single, null);
                  
                 /****** View Holder Object to contain tabitem.xml file elements ******/
 
                 holder = new ViewHolder();
                 holder.name = (TextView) vi.findViewById(R.id.name);
                 holder.date=(TextView)vi.findViewById(R.id.date);
                 holder.min=(TextView)vi.findViewById(R.id.min);
                 holder.max=(TextView)vi.findViewById(R.id.max);
                  
                /************  Set holder with LayoutInflater ************/
                 vi.setTag( holder );
             }
             else 
                 holder=(ViewHolder)vi.getTag();
              
             if(data.size()<=0)
             {
                 holder.name.setText("No Data");
                  
             }
             else
             {
	             int colorPosition = position % bgColors.length;
	             
	             vi.setBackgroundResource(bgColors[colorPosition]);

            	 /***** Get each Model object from Arraylist ********/
                 tempValues=null;
                 tempValues = ( ListModel ) data.get( position );
                  
                 /************  Set Model values in Holder elements ***********/
 
                  holder.name.setText( tempValues.getName() );
                  holder.date.setText( tempValues.getDate() );
                  holder.min.setText( tempValues.getMin() );
                  holder.max.setText( tempValues.getMax() );
            	  
                  /******** Set Item Click Listner for LayoutInflater for each row *******/
 
                  vi.setOnClickListener(new OnItemClickListener( position ));
             }
             return vi;
         }
          
         @Override
         public void onClick(View v) {
                 Log.v("CustomAdapter", "=====Row button clicked=====");
         }
          
         /********* Called when Item click in ListView ************/
         private class OnItemClickListener  implements OnClickListener{           
             private int mPosition;
              
             OnItemClickListener(int position){
                  mPosition = position;
             }
              
             @Override
             public void onClick(View arg0) {
                 Price sct = (Price)activity;
               /****  Call  onItemClick Method inside Main Class ( See Below )****/
               sct.onItemClick(mPosition);
             }               
         }   
     }
