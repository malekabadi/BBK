package com.aseman.bbk;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	Button logbtn;
	EditText phone;
	EditText pass;
	SharedPreferences sp;
	ProgressDialog progressDialog;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		progressDialog = new ProgressDialog(Login.this);
		phone = (EditText) findViewById(R.id.tel);
		pass = (EditText) findViewById(R.id.pass);
		logbtn = (Button) findViewById(R.id.button1);
		sp = getSharedPreferences("share", 0);
		String user = sp.getString("share", "");
		Button Register = (Button) findViewById(R.id.Register);
		Register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Login.this, Register.class));
			}
		});

		if (!user.equals("")) // لاگین نگهداشتن کاربر
		{
			startActivity(new Intent(Login.this, MainActivity.class));
			finish();
		}
	}

	// لاگین کردن کاربر و بررسی اعتبار سنجی ورود اطلاعات
	@SuppressWarnings("static-access")
	public void onclick(View btn) {
		if (btn == findViewById(R.id.button1)) {
			phone = (EditText) findViewById(R.id.tel);
			pass = (EditText) findViewById(R.id.editText2);

			try {
				if (phone.getText().toString().length() != 0 & pass.getText().toString().length() != 0) {

					btn.setEnabled(false);
					new AsyncTask<Integer, Integer, Boolean>() {
						ProgressDialog progressDialog = null;

						@Override
						protected void onPreExecute() {
							progressDialog = ProgressDialog.show(Login.this, "", "در حال اتصال ...");
						}

						@Override
						protected Boolean doInBackground(Integer... params) {
							if (params == null) {
								return false;
							}
							try {

								Thread.sleep(2000);
								login();

							} catch (Exception e) {

								return false;
							}

							return true;
						}

						@Override
						protected void onPostExecute(Boolean result) {
							progressDialog.dismiss();
						}
					}.execute();
				}

				else {
					Toast.makeText(this, "شماره تلفن خود را وارد کنید", Toast.LENGTH_SHORT).show();

				}
			} catch (Exception e) {
				Toast.makeText(this, "مشکل در ارتباط با سرور", Toast.LENGTH_SHORT).show();
				handler.sendEmptyMessage(0);
				progressDialog.dismiss();
			}
		} else if (btn == findViewById(R.id.textView2)) {
			Toast.makeText(getApplicationContext(), "asasa", Toast.LENGTH_SHORT).show();
		} // else if (btn == findViewById(R.id.Login_action_bar_back)) {
			// super.onBackPressed();
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// }

	}

	// وصل شدن به سرور
	public void login() throws MalformedURLException, IOException {

		String result = "", lines = "";
		CallRest cr = new CallRest();
		// result=cr.ResiveList("Login?name="+phone.getText().toString()+
		// "&pass="+pass.getText().toString());
		try {
			result = cr.login(phone.getText().toString(), pass.getText().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DisplayResult(result);

	}

	// نمایش نتیجه و رفتن به صفحه کاربری
	public void DisplayResult(final String result) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				int uid=-1;
				try
				{
					uid = Integer.parseInt(result);
				}catch (Exception e) {
					uid=-1;
				}
				if (uid > 0) {
					Toast.makeText(getApplicationContext(), "خوش آمدید", Toast.LENGTH_SHORT).show();
					Editor editor = sp.edit();
					editor.putString("phonekey", phone.getText().toString());
					appVar.main.UserName = phone.getText().toString();
					appVar.main.UserID = result;
					// editor.apply();
					// startActivity(new
					// Intent(LoginActivity.this,MainActivity.class));
					logbtn.setEnabled(true);

					progressDialog.dismiss();
					handler.sendEmptyMessage(0);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "یوز یا پسورد درست وارد نشده است" + result,
							Toast.LENGTH_SHORT).show();
					if (progressDialog != null) {
						progressDialog.dismiss();
						progressDialog.cancel();
					}
					logbtn.setEnabled(true);
				}
			}
		});
	}
}