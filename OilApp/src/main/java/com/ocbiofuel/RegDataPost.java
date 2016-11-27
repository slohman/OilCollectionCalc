package com.ocbiofuel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegDataPost extends Activity{

	
	public static final int backToCalc= 0;
	public static final int lookUp= 1;
	DefaultHttpClient client = new DefaultHttpClient();
	 ProgressDialog pd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regdatapost);
       
		
		Button post = (Button)findViewById(R.id.postcollection);
        EditText sid= (EditText)findViewById(R.id.siteID);
        EditText today = (EditText)findViewById(R.id.dateCollected);
        EditText driverid= (EditText)findViewById(R.id.driverID);




        Boolean bol = this.getIntent().hasExtra("sidID");

        if (bol){
            Bundle b = this.getIntent().getExtras();
           final String lookUpID = b.getString("sidID");
           sid.setText(lookUpID);

            Calendar c = Calendar.getInstance();
            
            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
           
			String formattedDate = df.format(c.getTime()).toString();
			today.setText(formattedDate);
         }







	
		post.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {



        logonToSite();
		clearform();
		
			}});
	}
	
		public void logonToSite() {


			EditText tDate = (EditText)findViewById(R.id.dateCollected);
        	EditText pounds = (EditText)findViewById(R.id.galsCollected);
        	EditText sid= (EditText)findViewById(R.id.siteID);
        	EditText driverid= (EditText)findViewById(R.id.driverID);
       
        	String siteDropInfo = sid.getText().toString() + "," + tDate.getText().toString() + "," + pounds.getText().toString() + ","  + driverid.getText().toString();
        	String[] dropdata = siteDropInfo.split(",");
        	
       
			if (isNetworkAvailable()) {
          
            new LongOperationPostReg().execute(dropdata);
            
			} else{
				Toast toast = Toast.makeText(this," Network Unavailable", Toast.LENGTH_LONG);
				toast.show();
			}
			
			
            
          
}

		public void clearform() {
			
			
			EditText tDate = (EditText)findViewById(R.id.dateCollected);
        	EditText gals = (EditText)findViewById(R.id.galsCollected);
        	EditText sid= (EditText)findViewById(R.id.siteID);
        	EditText driverid= (EditText)findViewById(R.id.driverID);
			sid.setText("");
			tDate.setText("");
			gals.setText("");
			driverid.setText("");
			



		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			menu.add(0, backToCalc, 1, "Back To Calc");
			menu.add(0, lookUp, 1, "Look Up");
			
			
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case backToCalc:
				Intent myIntent = new Intent(getBaseContext(), OilCollectionCalcActivity.class);
	  	 		startActivityForResult(myIntent, 0);
				return true;
		
			case lookUp: 
			    
				EditText sid = (EditText)findViewById(R.id.sitelookupID);
				String SID = sid.getText().toString();
				Bundle bun = new Bundle();
				bun.putCharSequence("lookUp", SID);
				
		 		Intent myIntent2 = new Intent(getBaseContext(), LookUpID.class);
		 		myIntent2.putExtras(bun);
		 		startActivityForResult(myIntent2, 0);
		    return true;
		}
			
			
			return false;

		}

		private class LongOperationPostReg extends AsyncTask<String, Void, String> {
			 
			  @Override
			  protected String doInBackground(String... params) {
			    // perform long running operation operation
				  

					String username, password;
					username="scott"; password="24251SGl";
					

					UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
					client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
				  
				  try {
					  
					  
					  List<NameValuePair> LogonData = new ArrayList<NameValuePair>();
			           
			        	LogonData.add(new BasicNameValuePair("SiteID", params[0].toString()));
			        	LogonData.add(new BasicNameValuePair("DropDate", params[1].toString()));
			        	LogonData.add(new BasicNameValuePair("pounds", params[2].toString()));
			        	LogonData.add(new BasicNameValuePair("DriverID", params[3].toString()));
			        	LogonData.add(new BasicNameValuePair("CollectorID", "10"));
			        	
					  String postURL = "http://199.68.191.200/collect/dooildropmobile.htm";
					  HttpPost post = new HttpPost(postURL);
		                 UrlEncodedFormEntity ent = new UrlEncodedFormEntity(LogonData,HTTP.UTF_8);
		                 post.setEntity(ent);
		                 HttpResponse responsePOST = client.execute(post);
		                 HttpEntity resEntity = responsePOST.getEntity();
		                 String data = EntityUtils.toString(resEntity).replace("\r\n","");
		                 resEntity.consumeContent();
		                 return  data;

		                 } catch (Exception e) {
		                             e.printStackTrace();
		                     }
				    String badResult = " No Post";
					return badResult;
					
			  }
			 
			 
			  @Override
		     protected void onPostExecute(String result) {
			    // execution of result of Long time consuming operation
				
				 
				  if (result != "No Post") {
	                   
	                    // kills all the CR,LF from the http page
					  pd.dismiss();
	                    Toast toast = Toast.makeText(RegDataPost.this,result,Toast.LENGTH_LONG);
	                    toast.show();
	                } else {
	                	 Toast toast = Toast.makeText(RegDataPost.this,result,Toast.LENGTH_LONG);
		                    toast.show();
		                    pd.dismiss();
	                	
	                }
				
		}
			 
			 
			  @Override
			  protected void onPreExecute() {
			  // Things to be done before execution of long running operation. For example showing ProgessDialog
				  pd = ProgressDialog.show(RegDataPost.this, "Posting Collection","please wait", true, false);
			  }
			 
			  
			  @Override
			  protected void onProgressUpdate(Void... values) {
			      // Things to be done while execution of long running operation is in progress. For example updating ProgessDialog
			   }
			}


		public boolean isNetworkAvailable() {
		    ConnectivityManager cm = (ConnectivityManager) 
		    	getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		    // if no network is available networkInfo will be null
		    // otherwise check if we are connected
		    if (networkInfo != null && networkInfo.isConnected()) {
		        return true;
		    }
		    return false;
		} 	 


} // end of class


