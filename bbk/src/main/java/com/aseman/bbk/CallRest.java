package com.aseman.bbk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class CallRest {
	public static String Error="";
	// ---------------------------------------------
	public static boolean isConnectionAvailable(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected() && netInfo.isConnectedOrConnecting()
					&& netInfo.isAvailable()) {
				return true;
			}
		}
		return false;
	}

	// -----------------------------------------------
	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// --------------------------------------------------------
	public String CallPrice(String method) {
		try {
			System.out.println("guru");
			DefaultHttpClient httpClient = new DefaultHttpClient();
			method = method.replaceAll(" ", "%20");
			// HttpGet httpGet=new
			// HttpGet("http://10.0.2.2/wcfService/RestServiceImpl.svc/"+addr);
			HttpGet httpGet = new HttpGet("http://www.bbk-iran.com/widget/livestock.php" + method);
			// Get the response
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream stream = httpEntity.getContent();

			// Convert the stream to readable format
			String result = convertStreamToString(stream);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	// --------------------------------------------------------
	public String Call(String method) {
		try {
			System.out.println("guru");
			DefaultHttpClient httpClient = new DefaultHttpClient();
			method = method.replaceAll(" ", "%20");
			// HttpGet httpGet=new
			// HttpGet("http://10.0.2.2/wcfService/RestServiceImpl.svc/"+addr);
			HttpGet httpGet = new HttpGet("http://bbk-iran.com/webservice/" + method);
			// Get the response
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream stream = httpEntity.getContent();

			// Convert the stream to readable format
			String result = convertStreamToString(stream);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	/***************************************************************/
	public String CallAsync(String Method) throws Exception {

//	    final ProgressDialog Asycdialog = new ProgressDialog(contx);
		String bnds = new AsyncTask<String, Void, String>() {
			@Override
			protected void onPreExecute() {
//	            Asycdialog.setMessage("Loading...");
//	            Asycdialog.show();
			};

			@Override
			protected String doInBackground(String... params) {
				return Call(params[0]);
			}

			@Override
			protected void onPostExecute(String result) {
//	            Asycdialog.dismiss();
			}
		}.execute(Method).get();
		return bnds;
	}

	/*************************************** User Login  ***********************************/
	public String login(String username, String password) {

		try {

			// Create the POST object and add the parameters
			HttpPost httpPost = new HttpPost("http://bbk-iran.com/webservice/login");

			// JSONObject jsonObj = new JSONObject();
			// jsonObj.put("Username", username);
			// jsonObj.put("Password", password);
			// StringEntity entity = new
			// StringEntity(jsonObj.toString(), HTTP.UTF_8);
			// entity.setContentType("application/json");
			// httpPost.setEntity(entity);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream stream = httpEntity.getContent();

			// Convert the stream to readable format
			String result = convertStreamToString(stream);
			JSONObject jsonResponse = new JSONObject(result);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			/****** Get Object for each JSON node. ***********/
			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
			appVar.main.UserKey=jsonChildNode.optString("MDU").toString();
			return jsonChildNode.optString("uid").toString();
		} catch (Exception e) {
			return null;
		}
	}

	/************************************  User Register  *********************************/
	public Status Register(String username, String password) {

		try {

			// Create the POST object and add the parameters
			HttpPost httpPost = new HttpPost("http://bbk-iran.com/webservice/preRegister");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream stream = httpEntity.getContent();

			// Convert the stream to readable format
			String result = convertStreamToString(stream);
			JSONObject jsonResponse = new JSONObject(result);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			/****** Get Object for each JSON node. ***********/
			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
			Status status = new Status();
			status.Code = jsonChildNode.optString("status").toString();
			status.Message = jsonChildNode.optString("msg").toString();
			return status;
		} catch (Exception e) {
			return null;
		}
	}

	/******************************* Get Product List ********************************/
	public List<Product> GetProducts(String para)  {
		JSONObject jsonResponse;
		List<Product> products = new ArrayList<Product>();

		try {
			String Content = Call("products"+para);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
//			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
//			if (jsonChildNode.optString("status").toString().equals("0"))
//			{
//				Error=jsonChildNode.optString("msg").toString();
//				return  null;
//			}
			for (int i = 0; i < jsonMainNode.length(); i++) {
				Product product = new Product();
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				product.ID = jsonChildNode.optString("id").toString();
				product.Title = jsonChildNode.optString("title").toString();
				product.SectionID = jsonChildNode.optString("section_id").toString();
				product.Price = jsonChildNode.optString("price").toString();
				product.Currency = jsonChildNode.optString("currency").toString();
				product.Field1 = jsonChildNode.optString("field1").toString();
				product.Field2 = jsonChildNode.optString("field2").toString();
				product.Company_ID = jsonChildNode.optString("company_ID").toString();
				product.Company_Name = jsonChildNode.optString("company_Name").toString();
				product.Phones = jsonChildNode.optString("phones").toString();
				products.add(product);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return products;
	}

	public List<Request> GetRequests(String para)  {
		JSONObject jsonResponse;
		List<Request> requests = new ArrayList<Request>();

		try {
			String Content = Call("requests"+para);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
//			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
//			if (jsonChildNode.optString("status").toString().equals("0"))
//			{
//				Error=jsonChildNode.optString("msg").toString();
//				return  null;
//			}
			for (int i = 0; i < jsonMainNode.length(); i++) {
				Request request = new Request();
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				request.ID = jsonChildNode.optString("id").toString();
				request.Title = jsonChildNode.optString("field1").toString();
				request.CatID = jsonChildNode.optString("cat_id").toString();
				request.Content = jsonChildNode.optString("content").toString();
				request.UserID = jsonChildNode.optString("user_id").toString();
				request.UserName = jsonChildNode.optString("user_name").toString();
				request.Location = jsonChildNode.optString("location").toString();
				request.Activity = jsonChildNode.optString("activity").toString();
				request.Tel = jsonChildNode.optString("tel").toString();
				request.URL = jsonChildNode.optString("url").toString();
				request.Expired = jsonChildNode.optString("expired").toString();
				request.Field1 = jsonChildNode.optString("field1").toString();
				request.Field2 = jsonChildNode.optString("field2").toString();
				request.Field3 = jsonChildNode.optString("field3").toString();
				requests.add(request);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return requests;
	}

	public List<Category> GetCategory(String para)  {
		JSONObject jsonResponse;
		List<Category> categories = new ArrayList<Category>();

		try {
			String Content = Call("request_cats"+para);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
//			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
//			if (jsonChildNode.optString("status").toString().equals("0"))
//			{
//				Error=jsonChildNode.optString("msg").toString();
//				return  null;
//			}
			for (int i = 0; i < jsonMainNode.length(); i++) {
				Category category = new Category();
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				category.ID = jsonChildNode.optString("id").toString();
				category.Title = jsonChildNode.optString("title").toString();
				categories.add(category);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return categories;
	}

	public Category GetFields(String para)  {
		JSONObject jsonResponse;
		Category Fileds = new Category();
		try {
			String Content = Call("request_cats"+para);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
			for (int i = 0; i < jsonMainNode.length(); i++) {
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				Fileds.ID = jsonChildNode.optString("id").toString();
				Fileds.Title = jsonChildNode.optString("title").toString();
				if (jsonChildNode.optString("field1").length()>2) Fileds.Fields.add(jsonChildNode.optString("field1").toString());
				if (jsonChildNode.optString("field2").length()>2) Fileds.Fields.add(jsonChildNode.optString("field2").toString());
				if (jsonChildNode.optString("field3").length()>2) Fileds.Fields.add(jsonChildNode.optString("field3").toString());
				if (jsonChildNode.optString("field4").length()>2) Fileds.Fields.add(jsonChildNode.optString("field4").toString());
				if (jsonChildNode.optString("field5").length()>2) Fileds.Fields.add(jsonChildNode.optString("field5").toString());
				if (jsonChildNode.optString("field6").length()>2) Fileds.Fields.add(jsonChildNode.optString("field6").toString());
				if (jsonChildNode.optString("field7").length()>2) Fileds.Fields.add(jsonChildNode.optString("field7").toString());
				if (jsonChildNode.optString("field8").length()>2) Fileds.Fields.add(jsonChildNode.optString("field8").toString());
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return Fileds;
	}

	/******************************* Get Product ********************************/
	public ProductDetail GetProduct(String ID) {
		JSONObject jsonResponse;

		ProductDetail product = new ProductDetail();
		try {
			String Content = Call("product?id=" + ID);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			/****** Get Object for each JSON node. ***********/
			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
			product.ID = jsonChildNode.optString("id").toString();
			product.Title = jsonChildNode.optString("title").toString();
			product.SectionID = jsonChildNode.optString("section_id").toString();
			product.Price = jsonChildNode.optString("price").toString();
			product.Unit = jsonChildNode.optString("unit").toString();
			product.Discount = jsonChildNode.optString("discount").toString();
			product.Company_ID = jsonChildNode.optString("company_id").toString();
			product.Company_Name = jsonChildNode.optString("company_name").toString();
			product.Company_Joinery = jsonChildNode.optString("company_joinery").toString();
			product.Company_p1_Name = jsonChildNode.optString("company_p1_name").toString();
			product.Company_Number = jsonChildNode.optString("company_number").toString();
			product.Company_Year = jsonChildNode.optString("company_year").toString();
			product.Company_Website = jsonChildNode.optString("company_website").toString();
			product.Company_Phones = jsonChildNode.optString("company_phones").toString();
			product.Company_Fax = jsonChildNode.optString("company_fax").toString();
			product.User_ID = jsonChildNode.optString("user_id").toString();
			product.Tax = jsonChildNode.optString("tax").toString();
			product.Shoping_Cost = jsonChildNode.optString("shoping_cost").toString();
			product.Time_Delivery = jsonChildNode.optString("time_delivery").toString();
			product.After_Sales = jsonChildNode.optString("after_sales").toString();
			product.Catalog = jsonChildNode.optString("catalog").toString();
			product.Introduct = jsonChildNode.optString("introduct").toString();
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return product;
	}

	/****************************** Get Company List ********************************/
	public List<Companies> GetCompanies(String SID){
		JSONObject jsonResponse;
		List<Companies> brands = new ArrayList<Companies>();

		try {
			String Content = Call("companies?sid="+SID);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

				// جدا کننده سه رقم
				// DecimalFormat f = new DecimalFormat("#,###");
				// DecimalFormatSymbols fs = f.getDecimalFormatSymbols();
				// fs.setGroupingSeparator(',');
				// f.setDecimalFormatSymbols(fs);
				Companies company = new Companies();

				/******* Fetch node values **********/
				// String min =
				// f.format(Integer.parseInt(jsonChildNode.optString("min").toString()));
				// String max =
				// f.format(Integer.parseInt(jsonChildNode.optString("max").toString()));
				company.ID = jsonChildNode.optString("id").toString();
				company.Full_Name = jsonChildNode.optString("name").toString();
				company.Joinery = jsonChildNode.optString("joinery").toString();
				company.Activity = jsonChildNode.optString("activity").toString();
				company.Activity2 = jsonChildNode.optString("activity2").toString();
				brands.add(company);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return brands;
	}

	/******************************* Get Company With ID ********************************/
	public Company GetCompany(String ID) throws Exception {
		JSONObject jsonResponse;
		Company company = new Company();

		try {
			String Content = CallAsync("company?id=" + ID);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			/****** Get Object for each JSON node. ***********/
			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
			company.ID = jsonChildNode.optString("id").toString();
			company.Full_Name = jsonChildNode.optString("full_name").toString();
			company.P_Name = jsonChildNode.optString("p1_name").toString();
			company.Joinery = jsonChildNode.optString("joinery").toString();
			company.Activity = jsonChildNode.optString("activity").toString();
			company.Activity2 = jsonChildNode.optString("activity2").toString();
			company.Number = jsonChildNode.optString("number").toString();
			company.Year = jsonChildNode.optString("year").toString();
			company.Website = jsonChildNode.optString("website").toString();
			company.Phone = jsonChildNode.optString("phones").toString();
			company.Fax = jsonChildNode.optString("fax").toString();
			company.P_Count = jsonChildNode.optString("p_count").toString();
			company.Resume = jsonChildNode.optString("resume").toString();
			company.Medal = jsonChildNode.optString("medal").toString();

		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return company;
	}

	/******************************* Get Section List ********************************/
	public List<Section> GetSections(String parent)  {
		JSONObject jsonResponse;
		List<Section> sections = new ArrayList<Section>();

		try {
			String Content = Call("sections" + parent);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				Section section = new Section();
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				section.ID = jsonChildNode.optString("id").toString();
				section.Title = jsonChildNode.optString("title").toString();
				section.Parnet = jsonChildNode.optString("parent").toString();
				section.Type = jsonChildNode.optString("type").toString();
				section.Ordering = jsonChildNode.optString("ordering").toString();
				sections.add(section);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return sections;
	}

	/*************************************  Save User Profile ***********************************/
	public String SaveBanner(String name,String mode,String period,String activity,String location,
				String tel,String content,String category,String[] fields) throws Exception {

		String bnds = new AsyncTask<String, Void, String>() {
			@Override
			protected void onPreExecute() {
			};

			@Override
			protected String doInBackground(String... params) {
				try {

					// Create the POST object and add the parameters
					HttpPost httpPost = new HttpPost("http://bbk-iran.com/webservice/new_ad");

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
					nameValuePairs.add(new BasicNameValuePair("name", params[0]));
					nameValuePairs.add(new BasicNameValuePair("mode", params[1]));
					nameValuePairs.add(new BasicNameValuePair("period", params[2]));
					nameValuePairs.add(new BasicNameValuePair("activity", params[3]));
					nameValuePairs.add(new BasicNameValuePair("location", params[4]));
					nameValuePairs.add(new BasicNameValuePair("tel", params[5]));
					nameValuePairs.add(new BasicNameValuePair("content", params[6]));
					nameValuePairs.add(new BasicNameValuePair("category", params[7]));
					nameValuePairs.add(new BasicNameValuePair("field1", params[8]));
					nameValuePairs.add(new BasicNameValuePair("field2", params[9]));
					nameValuePairs.add(new BasicNameValuePair("field3", params[9]));
					nameValuePairs.add(new BasicNameValuePair("field4", params[9]));
					nameValuePairs.add(new BasicNameValuePair("field5", params[9]));
					nameValuePairs.add(new BasicNameValuePair("field6", params[9]));
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpClient httpClient = new DefaultHttpClient();
					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					InputStream stream = httpEntity.getContent();

					// Convert the stream to readable format
					String result = convertStreamToString(stream);
					JSONObject jsonResponse = new JSONObject(result);
					JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

					/****** Get Object for each JSON node. ***********/
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
					return jsonChildNode.optString("uid").toString();
				} catch (Exception e) {
					return null;
				}
			}

			@Override
			protected void onPostExecute(String result) {
			}
		}.execute(name,mode,period,activity,tel,location,tel,content,category,fields[1],
				fields[2],fields[3],fields[4],fields[5],fields[6]).get();
		return bnds;
	}




	/******************************* Get User Profile With ID ********************************/
	public UserProfile LoadProfile(String ID) throws Exception {
		JSONObject jsonResponse;
		UserProfile user = new UserProfile();

		try {
			String Content = Call("customer?id=" + ID +"&MDU="+appVar.main.UserKey);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			/****** Get Object for each JSON node. ***********/
			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
			user.ID = 		  jsonChildNode.optString("id").toString();
			user.Name = 	  jsonChildNode.optString("full_name").toString();
			user.Title = 	  jsonChildNode.optString("title").toString();
			user.eMail = 	  jsonChildNode.optString("email").toString();
			user.Mobile = 	  jsonChildNode.optString("mobile").toString();
			user.Tel = 		  jsonChildNode.optString("tel").toString();
			user.State = 	  jsonChildNode.optString("state").toString();
			user.City =   	  jsonChildNode.optString("city").toString();
			user.Address = 	  jsonChildNode.optString("address").toString();
			user.PCode =      jsonChildNode.optString("pcode").toString();
			user.NewsLetter = jsonChildNode.optString("newletter").toString();

		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return user;
	}

	/******************************* Get User Profile With ID ********************************/
	public UserProfile LoadProfile2(String ID) throws Exception {
		JSONObject jsonResponse;
		UserProfile user = new UserProfile();

		try {
			String Content = CallAsync("customer?id=" + ID +"&MDU="+appVar.main.UserKey);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			/****** Get Object for each JSON node. ***********/
			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
			user.ID = 		  jsonChildNode.optString("id").toString();
			user.Name = 	  jsonChildNode.optString("full_name").toString();
			user.Title = 	  jsonChildNode.optString("title").toString();
			user.eMail = 	  jsonChildNode.optString("email").toString();
			user.Mobile = 	  jsonChildNode.optString("mobile").toString();
			user.Tel = 		  jsonChildNode.optString("tel").toString();
			user.State = 	  jsonChildNode.optString("state").toString();
			user.City =   	  jsonChildNode.optString("city").toString();
			user.Address = 	  jsonChildNode.optString("address").toString();
			user.PCode =      jsonChildNode.optString("pcode").toString();
			user.NewsLetter = jsonChildNode.optString("newletter").toString();

		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return user;
	}

	/******************************* Get Sections of Company ********************************/
	public List<Section> GetSections(String CompId, String parent) throws Exception {
		JSONObject jsonResponse;
		List<Section> sections = new ArrayList<Section>();

		try {
			String Content = "";
			if (parent.length() > 0)
				Content = Call("sections?parent" + parent + "&cid=" + CompId);
			else
				Content = Call("sections?cid" + CompId);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				Section section = new Section();
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				section.ID = jsonChildNode.optString("id").toString();
				section.Title = jsonChildNode.optString("title").toString();
				sections.add(section);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return sections;
	}

	/******************************* Get Product List ********************************/
	public List<Product> OthersProducts(String SectionID,String ID) throws Exception {
		JSONObject jsonResponse;
		List<Product> products = new ArrayList<Product>();

		try {
			String Content = Call("products?sid=" + SectionID + "&id=" + ID);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				Product product = new Product();
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				products.add(product);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return products;
	}

	/******************************* Search ********************************/
	public List<Product> Search(String pro,String SID) throws Exception /**/{
		JSONObject jsonResponse;
		
		List<Product> prods = new ArrayList<Product>();

		try {
			String Content = Call("search?q=" + pro + "&sid=" + SID ); //+ "&page=1"
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				Product prod = new Product();
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
			prods.add(prod);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return prods;
	}

	/*************************************** Check Out  ***********************************/
	public String CheckOut(String address,String type,List<Item> items) {

		try {

			// Create the POST object and add the parameters
			HttpPost httpPost = new HttpPost("http://webservice.ferzmarket.com/checkout");

	        JSONArray jsonArray = new JSONArray();
	        for (int i = 0; i < items.size(); i++) {
				 JSONObject jsonObj = new JSONObject();
				 jsonObj.put("id", items.get(0).proID);
				 jsonObj.put("cnt", items.get(0).Count);
				 jsonArray.put(jsonObj);
	        }
//	        JSONObject output = new JSONObject();
//	        output.put("cid", appVar.main.UserID);
//	        output.put("address", address);
//	        output.put("method", type);
//	        output.put("tel", GetAdress.Info.Tel);
//	        output.put("company", GetAdress.Info.Shop);
//	        output.put("MDU", appVar.main.UserKey);
//	        output.put("order", jsonArray);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("cid", appVar.main.UserID));
			nameValuePairs.add(new BasicNameValuePair("address", address));
			nameValuePairs.add(new BasicNameValuePair("method", type));
//			nameValuePairs.add(new BasicNameValuePair("tel", GetAdress.Info.Tel));
//			nameValuePairs.add(new BasicNameValuePair("company", GetAdress.Info.Shop));
			nameValuePairs.add(new BasicNameValuePair("MDU", appVar.main.UserKey));
			nameValuePairs.add(new BasicNameValuePair("order", jsonArray.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
//			StringEntity entity = new StringEntity(output.toString(), HTTP.UTF_8);
//			entity.setContentType("application/json");
//			httpPost.setEntity(entity);

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream stream = httpEntity.getContent();

			// Convert the stream to readable format
			String result = convertStreamToString(stream);
			JSONObject jsonResponse = new JSONObject(result);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			/****** Get Object for each JSON node. ***********/
			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
			
			String status=jsonChildNode.optString("status").toString();
//			if (status.equals("true"))
//				return jsonChildNode.optString("tracking").toString();
//			else
				return jsonChildNode.optString("tracking").toString()+" : "+status;
			
		} catch (Exception e) {
			return null;
		}
	}

	
	
	// -------End Class
}
