package com.ocbiofuel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class miuadj extends Activity{

	
	
	
	
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.miuadj);

		
		Button cb = (Button) findViewById(R.id.calcmiu);	
		
		
		
cb.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {

				double oilweight = 7.57;
				double miuweight = 8.34;
			
				EditText tMIU = (EditText) findViewById(R.id.txtMIU);
				EditText tGrossLbs = (EditText) findViewById(R.id.txtGrossOil);
			
				TextView  galsOil = (TextView ) findViewById(R.id.galOil);
				TextView galsMiu = (TextView) findViewById(R.id.miuGal);
				TextView netOIL = (TextView) findViewById(R.id.netOil);
				TextView wtMiu = (TextView) findViewById(R.id.grMIU);
				TextView wtOIL = (TextView) findViewById(R.id.grOil);
				
				//determine gallons from gross oil weight
				String sMIU = tMIU.getText().toString();
				
				float fmiu = Float.valueOf(sMIU);
				int grossPounds = Integer.parseInt(tGrossLbs.getText().toString());
				
				
				
				///calc net oil weight here and display in text field
				///calc gallons of oil weight
				
				// ((grossPounds*(1-miu)/oilweight+(grossPounds*(miu)/miuweight))*(1-miu))*oilweight
				// gallons of oil = (C5*(1-C7))/G5
				//gallons of MIU = (C5*C7)/G6
				double oilGallons = ((grossPounds * ( 1 - fmiu))/oilweight );
				
				double miuGallons  = ((grossPounds * fmiu)/miuweight) ;
				
				double grOilwt = (oilGallons * 7.57);
				double grMIUwt = (miuGallons * 8.34);
				
				double grossGallons = oilGallons + miuGallons;
				
				Double finalOilGallons = (Double) (grossGallons * (1-fmiu));
				Double finalmiuGallons = (Double) ( grossGallons - finalOilGallons);
				Double finalgrOilwt = (Double) (grOilwt);
				Double finalmiuwt = (Double) (grMIUwt);
				
				 
				int oilLbs = (int) (finalOilGallons * oilweight);
				int finalOil = finalOilGallons.intValue();
				int finalmiu = finalmiuGallons.intValue();
				int fnlgrOilwt = finalgrOilwt.intValue();
				int fnlgrMIUwt = finalmiuwt.intValue();
				
				
				
				galsOil.setText(Integer.toString(finalOil));
				galsMiu.setText(Integer.toString(finalmiu));
				netOIL.setText(Integer.toString(oilLbs));
				wtMiu.setText(Integer.toString(fnlgrMIUwt));
				wtOIL.setText(Integer.toString(fnlgrOilwt));
				
				
				
				
				
				
				
				
				
				
			}});
		
		
}
	
	
	
	
	
	
	
	
	
	
	
	
	
}