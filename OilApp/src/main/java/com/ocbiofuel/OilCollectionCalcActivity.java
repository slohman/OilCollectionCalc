package com.ocbiofuel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class OilCollectionCalcActivity extends Activity {
	/** Called when the activity is first created. */

	// RadioButton rb1, rb2, rb3, rb4, rb5;
	double qty;
	double oildensity = 6.3588;
	double knottsdensity = 7.47;
	
	int pos, pounds;
	Spinner Spinner1;
	float depth = 0;

	public static final int ClearForm = 0;
	public static final int TankQty = 1;
	public static final int RatioCalc = 3;
	public static final int PostReg = 4;
	public static final int tablet = 7;
	public static final int miuadj = 8;
	
	String inputLine ;
	BufferedReader in = null;
	PrintWriter out =null;
	Socket soc = null;
	String Line = "";
	 ProgressDialog pd;
	

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final TextView oq = (TextView) findViewById(R.id.oilqty);
		final EditText odepth = (EditText) findViewById(R.id.oildepth);
		Button cb = (Button) findViewById(R.id.calcoilqty);

		addItemsOnSpinner1();

		// addListenerOnSpinnerItemSelection();
		Spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				EditText etW = (EditText) findViewById(R.id.etWidth);
				EditText etL = (EditText) findViewById(R.id.etLength);
				if (arg2 != 5) {
					etW.setVisibility(android.view.View.INVISIBLE);
					etL.setVisibility(android.view.View.INVISIBLE);
				} else {
					etW.setVisibility(android.view.View.VISIBLE);
					etL.setVisibility(android.view.View.VISIBLE);
				}
			}

			
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		cb.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {

				double oildensity = 6.3588;
				double wvodensity = 7.5;
				
				

				String SText = Spinner1.getSelectedItem().toString();

				if (SText == "Barrel") {
					// calc oilqty for barrel

					// r2xHxD = cuft x 6.82011
					// use 397.406* depth in inches/1728

					depth = Float.valueOf(odepth.getText().toString());

					qty = ((depth / 33) * 7.7) * oildensity;
					int dqty = (int) qty;
					pounds = (int) (dqty * wvodensity);
					String sqty = Double.toString(dqty);
					oq.setText(sqty + " / " + pounds);

				}
				if (SText == "Roller Bin") {
					// calc oilqty for Roller bin
					// 3x2x?

					float depth = Float.valueOf(odepth.getText().toString());

					qty = ((depth * 481.3125) / 231) * .85;
					int dqty = (int) qty;
					pounds = (int) (dqty * wvodensity);
					String sqty = Double.toString(dqty);
					oq.setText(sqty + " / " + pounds);

				}
				// small bin
				if (SText == "Small Bin") {
					// calc oilqty for 89 bin bin
					// 3x2x?

					float depth = Float.valueOf(odepth.getText().toString());

					qty = ((depth * 576) / 231) * .85;
					int dqty = (int) qty;

					pounds = (int) (dqty * wvodensity);
					String sqty = Double.toString(dqty);
					oq.setText(sqty + " / " + pounds);

				}
				// large bin
				if (SText == "Large Bin") {
					// calc oilqty for 89 bin bin
					// 3x2x?

					float depth = Float.valueOf(odepth.getText().toString());

					qty = ((depth * 864) / 231) * .85;
					int dqty = (int) qty;
					pounds = (int) (dqty * wvodensity);
					String sqty = Double.toString(dqty);
					oq.setText(sqty + " / " + pounds);

				}
				// other
				if (SText == "Other") {
					// calc oilqty non-standard bin
					// 3x2x?
					final EditText manwidth = (EditText) findViewById(R.id.etWidth);
					final EditText manlen = (EditText) findViewById(R.id.etLength);
					float mW = Float.valueOf(manwidth.getText().toString());
					float mL = Float.valueOf(manlen.getText().toString());
					float depth = Float.valueOf(odepth.getText().toString());

					qty = ((mW * mL * depth) / 231) * .85;
					int dqty = (int) qty;
					pounds = (int) (dqty * wvodensity);
					String sqty = Double.toString(dqty);
					oq.setText(sqty + " / " + pounds);

				}
				// Knotts Reg
				if (SText == "KnottsReg") {
					// calc oilqty for 89 bin bin
					// 3x2x?

					float depth = Float.valueOf(odepth.getText().toString());

					qty = ((depth * 1296) / 231) * .85;
					int dqty = (int) qty;
					pounds = (int) (dqty * wvodensity);
					String sqty = Double.toString(dqty);
					oq.setText(sqty + " / " + pounds);

				}

				// Knotts CDR
				if (SText == "KnottsCDR") {
					// calc oilqty for 89 bin bin
					// 3x2x?

					float depth = Float.valueOf(odepth.getText().toString());

					qty = ((depth * 1728) / 231) * .85	;
					int dqty = (int) qty;
					pounds = (int) (dqty * wvodensity);
					String sqty = Double.toString(dqty);
					oq.setText(sqty + " / " + pounds);

				}
				// Spectrum Bin
				if (SText == "Spectrum Bin") {
				

					float depth = Float.valueOf(odepth.getText().toString());

					qty = ((depth * 1440) / 231) * .85;
					int dqty = (int) qty;
					pounds = (int) (dqty * wvodensity);
					String sqty = Double.toString(dqty);
					oq.setText(sqty + " / " + pounds);

				}
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ClearForm, 1, "Clear Data");
		menu.add(0, TankQty, 2, "Tank QTY");
		menu.add(0, RatioCalc, 3, "Ratio Calc");
		menu.add(0, PostReg, 4, "Post Reg Acct");
		menu.add(0,miuadj,8,"MIU Calc");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ClearForm:

			final TextView oq = (TextView) findViewById(R.id.oilqty);
			final EditText odepth = (EditText) findViewById(R.id.oildepth);
			final EditText manwidth = (EditText) findViewById(R.id.etWidth);
			final EditText manlen = (EditText) findViewById(R.id.etLength);

			odepth.setText("");
			oq.setText("");
			manwidth.setText("");
			manlen.setText("");
			return true;

		case TankQty:
			
			 boolean NA = isNetworkAvailable();
			if (NA) {
				 new LongOperationGetVolume().execute();
			}else{
				Toast toast = Toast.makeText(this, "No Network", Toast.LENGTH_LONG);
				toast.show();
			}
			return true;
			
			
		case RatioCalc:
			
			Intent myIntent = new Intent(getBaseContext(), RatioCalc.class);
            startActivityForResult(myIntent, 0);
		return true;
		
		case PostReg:
			
			Intent myIntent1 = new Intent(getBaseContext(), RegDataPost.class);
            startActivityForResult(myIntent1, 0);
		    return true;
		    

		

	    case tablet :
	    
		    return true;	
	    case miuadj :
	    	
	    	Intent myIntent2 = new Intent(getBaseContext(), miuadj.class);
            startActivityForResult(myIntent2, 0);
		    return true;	
	    	
		
	}
		return false;

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

	public void addItemsOnSpinner1() {

		Spinner1 = (Spinner) findViewById(R.id.spinner1);
		List<String> list = new ArrayList<String>();
		list.add("Select Container");
		list.add("Barrel");
		list.add("Small Bin");
		list.add("Large Bin");
		list.add("Roller Bin");
		list.add("Other");
		list.add("KnottsReg");
		list.add("KnottsCDR");
		list.add("Spectrum Bin");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner1.setAdapter(dataAdapter);
	}

	public void alertuser(String inputLine) {
		TextView tankqty = (TextView)findViewById(R.id.oilqty);
		tankqty.setText(inputLine);
	}
	
	
	private class LongOperationGetVolume extends AsyncTask<String, Void, String> {
		 
		  @Override
		  protected String doInBackground(String... params) {
		    // perform long running operation operation
			  
			 
			 // Socket soc;
		    	Line = "";
		    	String s= "@63";;
		    	
				
				
				try {
					Socket soc1 = new Socket();
				
					soc1.connect(new InetSocketAddress("65.60.105.2", 81), 5000);
					
					//soc = new Socket("99.124.161.85", 81);
					
					
					BufferedReader	in = new BufferedReader(new	InputStreamReader(soc1.getInputStream()));
					PrintWriter	out = new PrintWriter( new BufferedWriter ( new OutputStreamWriter(soc1.getOutputStream())),true);
					out.println(s);
				    
					
					while ((inputLine = in.readLine()) != null) {
					 Line += inputLine.toString() + "\n";
					 }	
					 
			    
			    in.close();
			    out.close();
				soc1.close();
				
				return Line;
				
				} catch (UnknownHostException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
				
					e.printStackTrace();
				}
				
				return null;
				
		  }
		 
		 
		  @Override
	     protected void onPostExecute(String result) {
		    // execution of result of Long time consuming operation
			
			 TextView tankqty = (TextView)findViewById(R.id.oilqty);
			 TextView tankqty65 = (TextView)findViewById(R.id.oilqty65);
			 TextView P1qty = (TextView)findViewById(R.id.editText1);
			 TextView P2qty = (TextView)findViewById(R.id.txtGrossOil);
			 
			 String[] rsult = result.split(",");
			 String finish1 = rsult[0];
			 String P1 = rsult[1];
			 String P2 = rsult[2];
			 String finish2 = rsult[3];
			 
			 
				tankqty.setText(finish1);
				tankqty65.setText(finish2);
				P1qty.setText("Process 1 Qty: " + "  "+ P1);
				P2qty.setText("Process 2 Qty: " + "  "+ P2);
				pd.dismiss();
	}
		 
		 
		  @Override
		  protected void onPreExecute() {
		  // Things to be done before execution of long running operation. For example showing ProgessDialog
			  pd = ProgressDialog.show(OilCollectionCalcActivity.this, "Getting Tank Volume...","please wait", true, false);
		  }
		 
		  
		  @Override
		  protected void onProgressUpdate(Void... values) {
		      // Things to be done while execution of long running operation is in progress. For example updating ProgessDialog
		   }
		}


}
