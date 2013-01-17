package com.androidhuman.google.maps.v2.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class SupportGoogleMap implements OnCameraChangeListener{
	private static final String KEY_LAST_LATITUDE = "com_androidhuman_google_maps_v2_support_last_latitude";
	private static final String KEY_LAST_LONGITUDE = "com_androidhuman_google_maps_v2_support_last_longitude";
	private static final String KEY_LAST_ZOOM = "com_androidhuman_google_maps_v2_support_last_zoom";
	
	private static final int INVALID_VALUE = -1000;
	
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mSharedPreferenceEditor;
	
	private GoogleMap mGoogleMap;
	private MarkerManager mMarkerManager;
	
	private OnCameraChangeListener mCameraChangeListener;
	
	public SupportGoogleMap(Context context){
		mMarkerManager = new MarkerManager();
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		mSharedPreferenceEditor = mSharedPreferences.edit();
	}
	
	public SupportGoogleMap(Context context, GoogleMap map){
		this(context);
		setGoogleMap(map);
	}

	public GoogleMap getGoogleMap() {
		return mGoogleMap;
	}

	public void setGoogleMap(GoogleMap map) {
		if(map==null){
			throw new IllegalArgumentException("GoogleMap object is null");
		}
		mGoogleMap = map;
		mGoogleMap.setOnCameraChangeListener(this);
		
		mMarkerManager.setGoogleMap(map);
		
		float lastLatitude = mSharedPreferences.getFloat(KEY_LAST_LATITUDE, INVALID_VALUE);
		float lastLongitude = mSharedPreferences.getFloat(KEY_LAST_LONGITUDE, INVALID_VALUE);
		float lastZoom = mSharedPreferences.getFloat(KEY_LAST_ZOOM, INVALID_VALUE);
		
		if(lastLatitude!=INVALID_VALUE && lastLongitude!=INVALID_VALUE){
			mGoogleMap.moveCamera(
					CameraUpdateFactory.newLatLng(
							new LatLng(lastLatitude, lastLongitude)));
		}
		
		if(lastZoom!=INVALID_VALUE){
			mGoogleMap.moveCamera(
					CameraUpdateFactory.zoomTo(lastZoom));
		}
	}
	
	public MarkerManager getMarkerManager(){
		return mMarkerManager;
	}

	@Override
	public void onCameraChange(CameraPosition position) {
		mSharedPreferenceEditor.putFloat(KEY_LAST_LATITUDE, (float)position.target.latitude)
			.putFloat(KEY_LAST_LONGITUDE, (float)position.target.longitude)
			.putFloat(KEY_LAST_ZOOM, (float)position.zoom).commit();
			
		if(mCameraChangeListener!=null){
			mCameraChangeListener.onCameraChange(position);
		}
	}
	
}
