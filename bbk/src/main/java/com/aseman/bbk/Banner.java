package com.aseman.bbk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Banner extends AppCompatActivity {

    public ArrayList<ImageView> iv=new ArrayList<ImageView>();
    public ArrayList<String> items=new ArrayList<String>();
    List<ImageList> imgs=new ArrayList<ImageList>();
    List<Category> categories=new ArrayList<Category>();
    String[] edits=new String[8];
    Category fields=new Category();

    private RecyclerView recyclerView;
    private AdapterSections mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ListView inputList;
    ListAdapter listAdapter;
    spinnerAdapter adapter;
    final int KeyGallery = 100, ReadExternalRequestCode = 200; // کلید ها برای باز کردن گالری و دریافت دسترسی ها
    String url = "http://192.168.1.104/"; // آدرس وب سرورتون (چون من محلی تست کردم آی پی کامپیوترم هست.)

    int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner);

        //Utility.verifyStoragePermissions(Banner.this);

        /*************************************************** Set Custom ActionBar *****/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base_color));
        View mCustomView = getSupportActionBar().getCustomView();
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("ثبت آگهی");

        Button addpic = (Button) findViewById(R.id.addpic);
        addpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.verifyStoragePermissions(Banner.this);
                selectImage();
            }
        });

        Button btnReg = (Button) findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText _title=(EditText) findViewById(R.id.title);String title=_title.getText().toString();
                EditText _active=(EditText) findViewById(R.id.active);String active=_active.getText().toString();
                EditText _location=(EditText) findViewById(R.id.location);String location=_location.getText().toString();
                EditText _email=(EditText) findViewById(R.id.email);String email=_email.getText().toString();
                EditText _mobile=(EditText) findViewById(R.id.mobile);String mobile=_mobile.getText().toString();
                EditText _desc=(EditText) findViewById(R.id.desc);String desc=_desc.getText().toString();
                Spinner _cate=(Spinner) findViewById(R.id.txtState);String cate=_cate.getSelectedItem().toString();
                Spinner _type=(Spinner) findViewById(R.id.type);int i=_type.getSelectedItemPosition();String type= String.valueOf(i);
                String period="";
                try {
                    new CallRest().SaveBanner(title,type,period,active,location,mobile,desc,cate,edits);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        String[] Typeitems = new String[] {"رایگان", "ویژه"};
        Spinner type = (Spinner) findViewById(R.id.type);
        spinnerAdapter adapter1 = new spinnerAdapter(Banner.this, android.R.layout.simple_list_item_1);
        adapter1.addAll(Typeitems);
        type.setAdapter(adapter1);
        //type.setSelection(adapter1.getCount());


            final Spinner city = (Spinner) findViewById(R.id.txtState);
        new AsyncTask<Integer, Integer, Boolean>() {
            ProgressDialog progressDialog = null;
            Dialog dialog=null;

            @Override
            protected void onPreExecute() {
                //progressDialog = ProgressDialog.show(MenuRight.this, "", "در حال اتصال ...");
                dialog = new Dialog(Banner.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


                dialog.setContentView(R.layout.dialog);
                dialog.show();
            }

            @Override
            protected Boolean doInBackground(Integer... params) {
                CallRest cr=new CallRest();
                categories = cr.GetCategory("");
                return  true;
            }
            protected void onPostExecute(Boolean result) {
                //progressDialog.dismiss();
                dialog.dismiss();
                for(Category c:categories)
                    items.add(c.Title);
                adapter = new spinnerAdapter(Banner.this, android.R.layout.simple_list_item_1);
                adapter.addAll(items);
                adapter.add("دسته بندی");
                city.setAdapter(adapter);
                city.setSelection(adapter.getCount());
            }

        }.execute();

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               new LongOperation().execute("?id="+categories.get(position-1).ID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.hlistview);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AdapterSections(imgs);
        recyclerView.setAdapter(mAdapter);

        new LongOperation().execute("?id=1");
        inputList = (ListView) findViewById(R.id.listView2);
        listAdapter=new ListAdapter(this);
        inputList.setAdapter(listAdapter);

    }

    //------------------------------------------------------------------------
    private class LongOperation extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog progressDialog = null;
        Dialog dialog=null;

        @Override
        protected void onPreExecute() {
            //progressDialog = ProgressDialog.show(MenuRight.this, "", "در حال اتصال ...");
            dialog = new Dialog(Banner.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            CallRest cr=new CallRest();
            fields=cr.GetFields(params[0]);
            return  true;
        }
    protected void onPostExecute(Boolean result) {
        //progressDialog.dismiss();
        listAdapter.notifyDataSetChanged();
        Utility.setListViewHeightBasedOnChildren(inputList);
        dialog.dismiss();
    }
}

    //------------------------------------------------------------------------
    public class ListAdapter extends BaseAdapter {

        private Context mContext;
        int Number=0;

        public ListAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return fields.Fields.size();
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
            gridViewAndroid = inflater.inflate(R.layout.listview_input, null);
            EditText name = (EditText) gridViewAndroid.findViewById(R.id.editText);
            name.setHint(fields.Fields.get(position));
            name.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    edits[position]=arg0.toString();
                }
            });
            return gridViewAndroid;
        }


    }

    //--------------------------------------------------------------------------
    private void selectImage() {
        final CharSequence[] options = { "گرفتن عکس با دوربین", "انتخاب از گالری","انصراف" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Banner.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("گرفتن عکس با دوربین"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("انتخاب از گالری"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("انصراف")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //------------------------------------------------------------------------------
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }

                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    ImageView temp=new ImageView(this);iv.add(temp);
                    iv.get(index).setImageBitmap(bitmap);
                    index++;
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                ImageView temp=new ImageView(this);ImageList Temp=new ImageList();
                Bitmap selectedImage=null;
                try {
                    final Uri imageUri = data.getData();

                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(imageUri, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    Temp.Path=picturePath;


                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    selectedImage = BitmapFactory.decodeStream(imageStream);
                    Temp.iv=new ImageView(this);
                    Temp.iv.setImageBitmap(selectedImage);
                    //temp.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //iv.get(index).setImageBitmap(bitmap);
                imgs.add(Temp);
                //imgs.get(index).iv.setImageBitmap(selectedImage);
                mAdapter.notifyDataSetChanged();
                //hListView.invalidate();
                index++;
            }
        }
    }

    //------------------------------------------------------------------------------
    public void uploadImage(File file) {
        AsyncHttpClient myClient = new AsyncHttpClient();// یک کلاینت اچ تی تی پی ایجاد می کنیم.

        RequestParams params = new RequestParams(); // یک متغییر برای پارامتر های درخواست پست ایجاد می کنیم.
        try {
            params.put("file", file); // فایل را با اندیس file به پارامترها اضافه میکنیم.
        } catch (FileNotFoundException e) {
        }
        myClient.post(url + "android/upload.php", params, new AsyncHttpResponseHandler() { // درخواست را انجام می دهیم.
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) { // در صورت موفقیت
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody)); // آبجکت جیسون را از داده های دریافتی از سرور ایجاد می کنیم.
                    if (jsonObject.getString("status").equals("success")) { // در صورتی که اندیس status مقدار success داشته باشد یعنی داده با موفقیت آپلود شده است.
// در دستورهای زیر آدرس فایل را از جیسون با اندیس filename میگیریم به ایمیج ویو با کمک کتابخانه پیکاسو لود می کنیم.
//                        Picasso.with(getBaseContext())
//                                .load(url + "android/uploads/" + jsonObject.getString("filename"))
//                                .into(img);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
// در صورت موفق نبودن آپلود خطا را با تست به کاربر نشان میدهیم.
                Toast.makeText(getBaseContext(), new String(responseBody), Toast.LENGTH_LONG).show();
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
