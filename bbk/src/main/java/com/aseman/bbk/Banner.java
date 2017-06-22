package com.aseman.bbk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Banner extends AppCompatActivity {

    public ArrayList<ImageView> iv=new ArrayList<ImageView>();
    public ArrayList<String> Files=new ArrayList<String>();
    List<ImageList> imgs=new ArrayList<ImageList>();

    private RecyclerView recyclerView;
    private AdapterSections mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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


        String[] items = new String[] {"One", "Two", "Three"};
        Spinner city = (Spinner) findViewById(R.id.txtCity);

        spinnerAdapter adapter = new spinnerAdapter(Banner.this, android.R.layout.simple_list_item_1);
        adapter.addAll(items);
        adapter.add("شهر");
        city.setAdapter(adapter);
        city.setSelection(adapter.getCount());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.hlistview);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AdapterSections(imgs);
        recyclerView.setAdapter(mAdapter);

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
