package com.ocbiofuel;


import java.util.ArrayList;
import java.util.List;

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

public class CollectionPost extends Activity{

	
	public static final int backToCalc= 0;
	DefaultHttpClient client = new DefaultHttpClient();
	 ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.knottscollect);
		
		Button post = (Button)findViewById(R.id.postcollection);
		
		  Bundle b = this.getIntent().getExtras();  
	        String poundsToPost = b.getString("pounds");
	        EditText pnds = (EditText)findViewById(R.id.lbsCollected);
	        pnds.setText(poundsToPost);
	        
		
	
		post.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
		logonToSite();
		clearform();
		
			}});
	}
	
		public void logonToSite() {
			
	
			if (isNetworkAvailable()) {
	
				
				
	
            EditText sid = (EditText)findViewById(R.id.siteNum);
        	EditText tDate = (EditText)findViewById(R.id.dateCollected);
        	EditText pnds = (EditText)findViewById(R.id.lbsCollected);
        	EditText comm = (EditText)findViewById(R.id.comments);
        	EditText miu = (EditText)findViewById(R.id.miu);
        	
        	String dropData = sid.getText().toString() + "," + tDate.getText().toString() + "," + pnds.getText().toString() + "," + miu.getText().toString() + "," + comm.getText().toString();
        	
        	String[] formData = dropData.split(",");
        	
        	new LongOperationPostKnotts().execute(formData);
        	
        } else{
				Toast toast = Toast.makeText(this," Network Unavailable", Toast.LENGTH_LONG);
				toast.show();
			}
	}

		public void clearform() {
			
			
			EditText sid = (EditText)findViewById(R.id.siteNum);
			EditText tDate = (EditText)findViewById(R.id.dateCollected);
			EditText pnds = (EditText)findViewById(R.id.lbsCollected);
			EditText comm = (EditText)findViewById(R.id.comments);
			EditText miu = (EditText)findViewById(R.id.miu);
		
			sid.setText("");
			tDate.setText("");
			pnds.setText("");
			comm.setText("");
			miu.setText("");



		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			menu.add(0, backToCalc, 1, "Back To Calc");
			
			
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case backToCalc:
				Intent myIntent = new Intent(getBaseContext(), RatioCalc.class);
	  	 		startActivityForResult(myIntent, 0);
				return true;
			
			}
			return false;

		}
		
		private class LongOperationPostKnotts extends AsyncTask<String, Void, String> {
			 
			  @Override
			  protected String doInBackground(String... params) {
			    // perform long running operation operation
				  

				  String username, password;
					username="scott"; password="24251SGl";
					

					UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
					client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);

			        String postURL = "http://www.ocbiofuel.com/knotts/dooildropmobile.htm";
			        HttpPost post = new HttpPost(postURL);
			        List<NameValuePair> postData = new ArrayList<NameValuePair>();
			        postData.add(new BasicNameValuePair("ID", params[0]));
			        postData.add(new BasicNameValuePair("TranDate", params[1]));
			        postData.add(new BasicNameValuePair("Pounds", params[2]));
			        postData.add(new BasicNameValuePair("MIU", params[3]));
			        postData.add(new BasicNameValuePair("Comments", params[4]));
			            
			        	
			        	try {
			            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(postData,HTTP.UTF_8);
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
	                    Toast toast = Toast.makeText(CollectionPost.this,result,Toast.LENGTH_LONG);
	                    toast.show();
	                } else {
	                	 Toast toast = Toast.makeText(CollectionPost.this,result,Toast.LENGTH_LONG);
		                    toast.show();
	                	pd.dismiss();
	                }
				
		}
			 
			 
			  @Override
			  protected void onPreExecute() {
			  // Things to be done before execution of long running operation. For example showing ProgessDialog
				  pd = ProgressDialog.show(CollectionPost.this, "Posting Collection","please wait", true, false);
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
