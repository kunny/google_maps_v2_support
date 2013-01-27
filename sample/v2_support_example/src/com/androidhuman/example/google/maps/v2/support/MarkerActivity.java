package com.androidhuman.example.google.maps.v2.support;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.androidhuman.google.maps.v2.support.SupportGoogleMap;
import com.androidhuman.google.maps.v2.support.model.SupportMarker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MarkerActivity extends FragmentActivity {

	SupportGoogleMap mGoogleMap;
	SupportMarker mMarker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_map_base);
	    
	    // Get an instance of the SupportGoogleMap
	    mGoogleMap = SupportGoogleMap.newInstance(getApplicationContext(), 
	    		(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_marker, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_activity_marker_add:
			// Add a marker to Map
			mMarker = mGoogleMap.addMarker("Marker title!", new LatLng(37.483032,127.019119));
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mMarker.getMarker().getPosition()));
			return true;
			
		case R.id.menu_activity_marker_update_title:
			// Update marker's title
			mGoogleMap.updateMarker(mMarker, "new title");
			return true;
			
		case R.id.menu_activity_marker_update_position:
			mGoogleMap.updateMarker(mMarker, new LatLng(37.503189,126.906509));
			mGoogleMap.animateTo(new LatLng(37.503189,126.906509));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
