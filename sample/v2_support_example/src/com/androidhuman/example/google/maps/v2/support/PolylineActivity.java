/*
 * Copyright 2013 Taeho Kim (jyte82@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.androidhuman.example.google.maps.v2.support;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.androidhuman.google.maps.v2.support.NoPlayServicesFoundException;
import com.androidhuman.google.maps.v2.support.SupportGoogleMap;
import com.androidhuman.google.maps.v2.support.V2SupportUtils;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class PolylineActivity extends FragmentActivity {

	SupportGoogleMap mGoogleMap;
	long polylineId = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_map_base);
	    
	    try {
			mGoogleMap = SupportGoogleMap.newInstance(getApplicationContext(), 
					(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
		} catch (NoPlayServicesFoundException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), 
					"No Google Play services found on this device. Please download from Play store.", 
					Toast.LENGTH_SHORT).show();
			V2SupportUtils.moveToPlayServiceDownloadPage(this);
		}
	    
	    polylineId = mGoogleMap.addPolyline(Color.RED,
	    		new LatLng(37.527154,126.98204),
	    		new LatLng(37.516806,126.907883));
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_polyline, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_activity_polyline_add_polyline:
			LatLng newPoint = new LatLng(37.483032,127.019119);
			mGoogleMap.appendPolyline(polylineId, newPoint, true);
			mGoogleMap.animateTo(newPoint);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
