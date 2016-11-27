package com.ocbiofuel;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RatioCalc extends Activity {
	
	
	
	
	public static final int ClearForm = 0;
	public static final int Post = 1;
	public static final int Measure = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ratiocalc);
	

	
	
	
	Button calc = (Button)findViewById(R.id.calcratio);
	
	
	
	calc.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View view) {   
 
		//calculate ratio for oil logging
			
			final EditText tgc = (EditText)findViewById(R.id.totalgallons);
			final EditText tp = (EditText)findViewById(R.id.totalpounds);
			final EditText tlg = (EditText)findViewById(R.id.locationgallons);
			
			final TextView locationRatioPounds = (TextView)findViewById(R.id.locationpounds);
			
			//turn this locations gallons into pounds and then divide by the total pounds to
			// get a ratio. Then mult ratio times total pounds collected to get location pounds
		String stotalGallons = tgc.getText().toString();	
		String sTotLocationGallons = tlg.getText().toString();
		String stotLoadPounds = tp.getText().toString();
		
		
		int igalForLoad = Integer.parseInt(stotalGallons);
		double itotalLocationGallons = Integer.parseInt(sTotLocationGallons);
		double itotalPounds = Integer.parseInt(stotLoadPounds);
		
		double dGalratio = (itotalLocationGallons/igalForLoad);
		
		int inetPounds = (int) (dGalratio * itotalPounds );
		String sNetLBSLocation = Integer.toString(inetPounds);
		
		locationRatioPounds.setText(sNetLBSLocation);
		
		
		
		};
	

	});

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ClearForm, 1, "Clear Data");
		menu.add(0, Post, 2, "Post");
		menu.add(0, Measure, 3, "Measure Oil");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ClearForm:

			final TextView lp = (TextView) findViewById(R.id.locationpounds);
			final EditText tp = (EditText) findViewById(R.id.totalpounds);
			final EditText tg = (EditText) findViewById(R.id.totalgallons);
			final EditText lg = (EditText) findViewById(R.id.locationgallons);

			lp.setText("");
			lg.setText("");
			tg.setText("");
			tp.setText("");
			
			
			return true;

		
		
		case Post:
			
			final TextView lp1 = (TextView) findViewById(R.id.locationpounds);
			String poundsToPost = lp1.getText().toString();
  	 		Bundle bun = new Bundle();
    			bun.putCharSequence("pounds", poundsToPost);
  	 		Intent myIntent = new Intent(getBaseContext(), CollectionPost.class);
  	 		myIntent.putExtras(bun);
	 			
            startActivityForResult(myIntent, 0);
		return true;
		
		case Measure:
			Intent myIntent1 = new Intent(getBaseContext(), OilCollectionCalcActivity.class);
  	 		startActivityForResult(myIntent1, 0);
			
			
		}
		return false;

	}
	

}	
	
