package com.ocbiofuel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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



import java.util.ArrayList;
import java.util.List;

public class LookUpID extends Activity{

	DefaultHttpClient client = new DefaultHttpClient();
	 ProgressDialog pd;
	HttpEntity entty = null;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lookup);
		
		
	Bundle b = this.getIntent().getExtras();  
      String lookUpID = b.getString("lookUp");
		 
	       
		 
	        
		
	
		
		logonToSite(lookUpID);
        
        setlistener();

		
		
			
	}
	
		public void logonToSite(String LookUpID) {
			
			

		
		String LookUpInfo = LookUpID.toString();
    	String[] lookupdata = LookUpInfo.split(",");
		
    	if (isNetworkAvailable()) {
    	new LongOperationLookupID().execute(lookupdata);
		} else{
			Toast toast = Toast.makeText(this," Network Unavailable", Toast.LENGTH_LONG);
			toast.show();
		}
    	
    	
    	
        
		} 
           

       

        


        public void loadspinner(String sitestr) {
            if (sitestr != null) {

              try {
                  
                  sitestr = "\n\r Select Site ," + sitestr;   // make blank first line so user has to select something and fire event
                  String[] sites = sitestr.split(",");

                  // fill spinner from array
                  Spinner s1 = (Spinner) findViewById(R.id.siteSelect);
                  ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, sites);
                  spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                  s1.setAdapter(spinnerArrayAdapter);
                  }      catch (Exception e) {
                             e.printStackTrace();
                         }
             }
        }



        public void setlistener() {

            final int NO_OF_EVENTS = 1;


            Spinner s = (Spinner) findViewById(R.id.siteSelect);
            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                int m_intSpinnerInitiCount = 0;


                
                public void onItemSelected(AdapterView<?> adapter, View v, int i, long lng) {
                    String selItem = adapter.getItemAtPosition(i).toString();



                    if (m_intSpinnerInitiCount < NO_OF_EVENTS) {
                        m_intSpinnerInitiCount++;
                    } else {
                        //YOUR CODE HERE


                    String   result = selItem.replace("\r\n", "");



                   String siteID = result.substring(0,5).trim();

                    Bundle bun = new Bundle();
                    bun.putCharSequence("sidID", siteID);

                    Intent myIntent2 = new Intent(getBaseContext(), RegDataPost.class);
                    myIntent2.putExtras(bun);
                    startActivityForResult(myIntent2, 0);
                    }
                }

                
                public void onNothingSelected(AdapterView<?> arg0) {
//do something else
                }
            });
	}

        private class LongOperationLookupID extends AsyncTask<String, Void, String> {
			 
			  @Override
			  protected String doInBackground(String... params) {
			    // perform long running operation operation
				  
				  HttpEntity entty = null;
					String username, password;
					username="scott"; password="24251SGl";
					String sitestr = null;

					UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
					client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
				  
					 try {
				           
				           List<NameValuePair> params1 = new ArrayList<NameValuePair>();
				           params1.add(new BasicNameValuePair("siteName", params[0].toString()));
				            
				            
				           
				            
				            String postURL = "http://199.68.191.200/house/selectSiteID.htm";
				            HttpPost post = new HttpPost(postURL);
				            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params1,HTTP.UTF_8);
				            post.setEntity(ent);
				            HttpResponse responsePOST = client.execute(post);
				            HttpEntity resEntity = responsePOST.getEntity();

				           sitestr = EntityUtils.toString(resEntity);

				        } catch (Exception e) {
				        e.printStackTrace();
				    }
				            return sitestr;
					
			  }
			 
			 
			  @Override
		     protected void onPostExecute(String result) {
			    // execution of result of Long time consuming operation
				
				  loadspinner(result);
				pd.dismiss();
		}
			 
			 
			  @Override
			  protected void onPreExecute() {
			  // Things to be done before execution of long running operation. For example showing ProgessDialog
				  pd = ProgressDialog.show(LookUpID.this, "Getting list","please wait", true, false);
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


