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
			String Content = Call("products"+"");
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
	public List<Section> GetSections(String parent) throws Exception {
		JSONObject jsonResponse;
		List<Section> sections = new ArrayList<Section>();

		try {
			String Content = CallAsync("sections?parent=" + parent);
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
	public String SaveBanner(String name, String price, String type, String email, String mobile, String location,String content) {

		try {

			// Create the POST object and add the parameters
			HttpPost httpPost = new HttpPost("http://webservice.ferzmarket.com/Register");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("name", name));
			nameValuePairs.add(new BasicNameValuePair("price", price));
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("tel", mobile));
			nameValuePairs.add(new BasicNameValuePair("location", location));
			nameValuePairs.add(new BasicNameValuePair("type", type));
			nameValuePairs.add(new BasicNameValuePair("content", content));
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




	/*************************************  Save User Profile ***********************************/
	public String SaveProfile(String name,String pass,String email,String mobile,String tel,
				String gender,String state,String city,String address,String pcode,String newsletter) throws Exception {

		String bnds = new AsyncTask<String, Void, String>() {
			@Override
			protected void onPreExecute() {
			};

			@Override
			protected String doInBackground(String... params) {
				try {

					// Create the POST object and add the parameters
					HttpPost httpPost = new HttpPost("http://webservice.ferzmarket.com/Register");

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
					nameValuePairs.add(new BasicNameValuePair("full_name", params[0]));
					nameValuePairs.add(new BasicNameValuePair("password", params[1]));
					nameValuePairs.add(new BasicNameValuePair("email", params[2]));
					nameValuePairs.add(new BasicNameValuePair("mobile", params[3]));
					nameValuePairs.add(new BasicNameValuePair("tel", params[4]));
					nameValuePairs.add(new BasicNameValuePair("gender", params[5]));
					nameValuePairs.add(new BasicNameValuePair("state", params[6]));
					nameValuePairs.add(new BasicNameValuePair("city", params[7]));
					nameValuePairs.add(new BasicNameValuePair("pcode", params[8]));
					nameValuePairs.add(new BasicNameValuePair("newsletter", params[9]));
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
		}.execute(name,pass,email,mobile,tel,gender,state,city,pcode,newsletter).get();
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

	/******************************* Get Order List Of Company ********************************/
	public List<Order> GetOrders(String CID) throws Exception {
		JSONObject jsonResponse;
		List<Order> orders = new ArrayList<Order>();

		try {
			String Content = Call("orders?cid=" + CID + "&MDU=" + appVar.main.UserKey);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				Order order = new Order();
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				order.ID = jsonChildNode.optString("id").toString();
				order.OrderDate = jsonChildNode.optString("order_date").toString();
				order.TrackNum = jsonChildNode.optString("tracking_number").toString();
				order.InvoiceNum = jsonChildNode.optString("invoice_number").toString();
				order.Status = jsonChildNode.optString("status").toString();
				order.Total = jsonChildNode.optString("total_price").toString();
				orders.add(order);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return orders;
	}

	/******************************* Get Order List Of Company ********************************/
	public List<OrderDetail> GetOrderDetail(String CID,String ID) throws Exception {
		JSONObject jsonResponse;
		List<OrderDetail> orders = new ArrayList<OrderDetail>();

		try {
			String Content = CallAsync("order?cid=" + CID + "&id=" + ID + "&MDU=" + appVar.main.UserKey);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				OrderDetail order = new OrderDetail();
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				order.ProID = jsonChildNode.optString("product_id").toString();
				order.ProName = jsonChildNode.optString("product_name").toString();
				order.InvNo = jsonChildNode.optString("product_sku").toString();
				order.Count = jsonChildNode.optString("product_quantity").toString();
				order.Price = jsonChildNode.optString("product_price").toString();
				order.Total = jsonChildNode.optString("final_price").toString();
				orders.add(order);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return orders;
	}

	/******************************* Get Invoice List Of Company ********************************/
	public List<Invoice> GetInvoices(String CID) throws Exception {
		JSONObject jsonResponse;
		List<Invoice> invoices = new ArrayList<Invoice>();

		try {
			String Content = Call("invoices?cid=" + CID + "&MDU=" + appVar.main.UserKey);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				Invoice invoice = new Invoice();
				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				invoice.ID = jsonChildNode.optString("id").toString();
				invoice.InvoiceDate = jsonChildNode.optString("invoice_date").toString();
				invoice.Amount = jsonChildNode.optString("amount").toString();
				invoice.InvoiceNum = jsonChildNode.optString("invoice_number").toString();
				invoice.Status = jsonChildNode.optString("status").toString();
				invoice.OrderID = jsonChildNode.optString("order_id").toString();
				invoice.TransactionDate = jsonChildNode.optString("transaction_date").toString();
				invoice.Tref = jsonChildNode.optString("tref").toString();
				invoices.add(invoice);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return invoices;
	}

	/******************************* Get Invoice Of ID ********************************/
	public Invoice GetInvoice(String ID) throws Exception {
		JSONObject jsonResponse;
		
		Invoice invoice = new Invoice();

		try {
			String Content = Call("invoices?id=" + ID + "&MDU=" + appVar.main.UserKey);
			jsonResponse = new JSONObject(Content);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

				/****** Get Object for each JSON node. ***********/
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
				invoice.ID = jsonChildNode.optString("id").toString();
				invoice.InvoiceDate = jsonChildNode.optString("invoice_date").toString();
				invoice.Amount = jsonChildNode.optString("amount").toString();
				invoice.InvoiceNum = jsonChildNode.optString("invoice_number").toString();
				invoice.Status = jsonChildNode.optString("status").toString();
				invoice.OrderID = jsonChildNode.optString("order_id").toString();
				
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
		return invoice;
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
