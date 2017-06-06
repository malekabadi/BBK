package com.aseman.bbk;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

	EditText phone, password, newpassword;
	TextView tv;
	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		/*************************************************** Set Custom ActionBar *****/
		getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.actionbar);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base_color));
		View mCustomView = getSupportActionBar().getCustomView();
		TextView title = (TextView) mCustomView.findViewById(R.id.title);
		title.setText("ایجاد حساب کاربری");

		sp = getSharedPreferences("share", 0);
	}

	// ثبت نام وبررسی درست وارد کردن اطلاعات
	public void onclick(View btn) {
		if (btn == findViewById(R.id.button1)) {
			phone = (EditText) findViewById(R.id.tel);
			password = (EditText) findViewById(R.id.pass);
			newpassword = (EditText) findViewById(R.id.npass);
			
			if (password.getText().toString().equalsIgnoreCase(newpassword.getText().toString().trim())) {
				tv = (TextView) findViewById(R.id.textView2);
				new Thread(new Runnable() {
					@Override
					public void run() {
						register();
					}
				}).start();
			} else {

				Status result = new Status();
				result.Code="0";
				result.Message="رمز ورود و تکرار با هم مغایریند";
				DisplayResult(result);
			}
		}
		// else if(btn == findViewById(R.id.Login_action_bar_back))
		// {
		// super.onBackPressed();
		// }
	}

	// وصل شدن به سرور
	public void register() {

		Status result = new Status();
		CallRest cs = new CallRest();
		result = cs.Register(phone.getText().toString() , password.getText().toString());
		DisplayResult(result);
	}

	// نمایش نتیجه
	public void DisplayResult(final Status result) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (result.Code.equals("1")) {
					Toast.makeText(getApplicationContext(), "خوش آمدید", Toast.LENGTH_SHORT).show();
					appVar.main.UserName = phone.getText().toString();
					//appVar.main.UserID = result.substring(2);
					Editor editor = sp.edit();
					editor.putString("phonekey", phone.getText().toString());
					// editor.apply();
					finish();
				} else {
					Toast.makeText(getApplicationContext(), result.Message ,
							Toast.LENGTH_SHORT).show();
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