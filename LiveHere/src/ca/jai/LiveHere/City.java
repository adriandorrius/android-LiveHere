package ca.jai.LiveHere;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class City extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		setupCitySpinner();
		setupGetStatButton();
	}
	
	private void setupCitySpinner() {
		final Spinner spinnerCity = (Spinner)findViewById(R.id.citySpinner);
	
		getData daData = new getData();
		daData.execute();

		spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int positon, long arg3) {
				spinnerCity.setSelection(spinnerCity.getSelectedItemPosition());
				SharedPreferences provinceSharedPref = getSharedPreferences("city", MODE_PRIVATE);
				SharedPreferences.Editor cityEditor = provinceSharedPref.edit();
				cityEditor.putString("city", (String)spinnerCity.getItemAtPosition(positon));
				cityEditor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	private void setupGetStatButton() {
		Button getStatButton = (Button)findViewById(R.id.getStatButton);
		getStatButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(City.this, Stat.class));
			}
		});
	}
	
	private class getData extends AsyncTask<String[], String, String> {

		@Override
		protected String doInBackground(String[] ...params) {
			// TODO Auto-generated method stub
			String output;
			try 
			{
				output= readJ.readURL("http://namara.io/api/v0/resources/1f1db6c6-15c9-" + "430a-bfe8-c3c2eddadb23/data/aggregate?api_key=e53683a95b14f134f71a5009a33d6b03c7ed5accb994c9434155c0a95c273bb2&aggregate=%7B%22column%22:1,%22operation%22:%22distinct%22");			
				Log.i("ca.jai.LiveHere", "nai");
			}
			catch(Exception E)
			{
				output = "n/a";
				Log.i("ca.jai.LiveHere", E.toString());
			}
			publishProgress(output);
			return output;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			SharedPreferences prov = getSharedPreferences("province", MODE_PRIVATE);
			String province = prov.getString("province", "n/a");
			Log.i("ca.jai.LiveHere", province);
			String[] cities = readJ.extractCities(values[0], province);
			final Spinner spinnerCity = (Spinner)findViewById(R.id.citySpinner);
			ArrayAdapter<String> citySpinnerArray = new ArrayAdapter<String>(City.this, android.R.layout.simple_spinner_dropdown_item, cities);
			spinnerCity.setAdapter(citySpinnerArray);
		}
	}

}
