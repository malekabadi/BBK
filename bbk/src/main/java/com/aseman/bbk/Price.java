package com.aseman.bbk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class Price extends AppCompatActivity {
	ListView list;
	CustomAdapter adapter;
	public  Price CustomListView = null;
    public  ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_price);
		CustomListView = this;
        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base_color));
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("لیست کالاها");

		if (!hasNetworkConnection())
		{
			AlertDialog alt = new AlertDialog.Builder(Price.this).create();
			alt.setTitle("وضعیت اتصال به اینترنت");
			alt.setMessage("دسترسی به اینترنت میسر نیست! لطفاً اینترنت دستگاه را بررسی نمایید");
			//alt.setIcon(R.drawable.bbklogo);
			alt.setButton("خروج از برنامه", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Price.this.finish();
				}
			});
			alt.show();
		}

		
		// WebServer Request URL
        String serverURL = "http://www.bbk-iran.com/widget/livestock.php";
         
        // Use AsyncTask execute Method To Prevent ANR Problem
        new LongOperation().execute(serverURL);
        
        Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//onPopupButtonClick(v);
			}
		});
				
	}
	
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	public void onPopupButtonClick(View button) {
//        PopupMenu popup = new PopupMenu(this, button);
//        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
//
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId())
//                {
//                    case R.id.exit:
//                    	customExit();
//                        return true;
//                    case R.id.contact:
//                		Intent about = new Intent(Main.this,About.class);
//                		startActivity(about);
//                        return true;
//                    case R.id.web:
//                        Intent browserIntenti = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bbk-iran.com"));
//                        startActivity(browserIntenti);
//                        return true;
//
//                }
//                return false;
//            }
//        });
//
//        popup.show();
//    }
	
    //	@Override
//    public void onBackPressed() {
//
//        //super.onBackPressed();
//		customExit();
//
//    }

    public void customExit(){
		AlertDialog alt = new AlertDialog.Builder(Price.this).create();
		alt.setTitle("خروج از برنامه");
		alt.setMessage("آیا می خواهید از برنامه خارج شوید؟");
		alt.setButton("بلی", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Price.this.finish();
			}
		});
		alt.setButton2("خیر",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//Main.this.
			}
		});
		alt.show();
	}
	
	private class LongOperation  extends AsyncTask<String, Void, Void> {
        
        // Required initialization
         
        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(Price.this);
        String data =""; 
        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            try {
                CallRest cr = new CallRest();
                Content = cr.CallPrice("");
            } catch (Exception ex) {
                Error = ex.getMessage();
            }
            /*****************************************************/
            return null;
        }
        
        
        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
              
            // Close progress dialog
            Dialog.dismiss();
              
            if (Error != null) {
                  
                //uiUpdate.setText("Output : "+Error);
                  
            } else {
               
                // Show Response Json On Screen (activity)
               // uiUpdate.setText( Content );
                 
             /****************** Start Parse Response JSON Data *************/
                 
                //String OutputData = "";
                JSONObject jsonResponse;
                       
                try {
                       
                     /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                     jsonResponse = new JSONObject(Content);
                       
                     /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                     /*******  Returns null otherwise.  *******/
                     JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
                       
                     /*********** Process each JSON Node ************/
   
                     int lengthJsonArr = jsonMainNode.length();  
                       
                     for(int i=0; i < lengthJsonArr; i++) 
                     {
                    	 final ListModel sched = new ListModel();
                    	 /****** Get Object for each JSON node.***********/
                         JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                           
                         //جدا کننده سه رقم
                         DecimalFormat f = new DecimalFormat("#,###");
                         DecimalFormatSymbols fs = f.getDecimalFormatSymbols();
                         fs.setGroupingSeparator(',');
                         f.setDecimalFormatSymbols(fs);
                         
                         /******* Fetch node values **********/
                         String name     = jsonChildNode.optString("name").toString();
                         String date     = jsonChildNode.optString("date").toString();
                         String min		 = f.format(Integer.parseInt(jsonChildNode.optString("min").toString()));
                         String max		 = f.format(Integer.parseInt(jsonChildNode.optString("max").toString()));
                         String avg_day  = jsonChildNode.optString("avg_day").toString();
                         String change	 = jsonChildNode.optString("change").toString();
                         String avg_month= jsonChildNode.optString("avg_month").toString();
                         String pid		 = jsonChildNode.optString("pid").toString();
                         String id		 = jsonChildNode.optString("id").toString();
                         		
                         //OutputData += name+"\n";
                         
                         /******* Firstly take data in model object ******/
                         sched.setName(name);
                         sched.setDate(date);
                         sched.setMin(min);
                         sched.setMax(max);
                         sched.setPId(pid);
                         sched.setId(id);
                          
                      /******** Take Model Object in ArrayList **********/
                      CustomListViewValuesArr.add( sched );
                         
                          
                    }
                 /****************** End Parse Response JSON Data *************/    
                    //Show Parsed Output on screen (activity)
                     Resources res =getResources();
                     list = ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )
                      
                     /**************** Create Custom Adapter *********/
                     adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
                     list.setAdapter( adapter );
                    //jsonParsed.setText( OutputData );
                      
                       
                 } catch (JSONException e) {
           
                     e.printStackTrace();
                 }
   
             }
        }
	}

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        ListModel tempValues = ( ListModel ) CustomListViewValuesArr.get(mPosition);

        Intent intent = new Intent(Price.this,Details.class);
        intent.putExtra("pId", tempValues.getPId());
        intent.putExtra("Id", tempValues.getId());
        startActivity(intent);
       // SHOW ALERT                  
      // Toast.makeText(CustomListView,""+tempValues.getAvgDay()+" Url:"+tempValues.getDate(), Toast.LENGTH_LONG).show();
    }
    
    protected boolean hasNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        try{
            if (!wifi.isConnected()){
                if(mobile==null || (mobile!=null && !mobile.isConnected())){
                    return false;
                }
            }
        }catch(Exception e){
            Log.e("AEP41-Has Network", ""+e.getStackTrace());
        }
        return true;
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
