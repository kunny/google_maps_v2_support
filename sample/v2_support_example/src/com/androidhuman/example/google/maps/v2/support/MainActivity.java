package com.androidhuman.example.google.maps.v2.support;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.androidhuman.google.maps.v2.support.SupportGoogleMap;
import com.androidhuman.google.maps.v2.support.SupportOnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends Activity{

	private GoogleMap mGoogleMap;
	private SupportGoogleMap mSuppMap;
	long markerId;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mGoogleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		mSuppMap = new SupportGoogleMap(this, mGoogleMap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_add_marker:
			mGoogleMap.addMarker(
					new MarkerOptions()
						.position(new LatLng(37.418000, -122.080000))
						.title("Marker")
						.snippet("Marker added by GoogleMap"));
			return true;
			
		case R.id.menu_add_polyline:
			mGoogleMap.addPolyline(new PolylineOptions()
			.color(Color.GREEN)
			.add(new LatLng(37.422006, -122.084095), new LatLng(37.418006, -122.080095), new LatLng(37.420006, -122.084000)));
			return true;
			
		case R.id.menu_support_add:
			markerId = mSuppMap.addMarker(new MarkerOptions()
				.position(new LatLng(37.410006, -122.080095))
				.title("Marker(supplib)")
				.snippet("Marker added by supplib"));
			return true;
			
		case R.id.menu_support_update:
			mSuppMap.updateMarker(markerId, "Title changed!");
			return true;
		}
			
		//
		return super.onOptionsItemSelected(item);
	}

}
