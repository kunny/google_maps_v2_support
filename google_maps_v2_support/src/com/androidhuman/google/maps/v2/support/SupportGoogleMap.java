package com.androidhuman.google.maps.v2.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

/**
 * This is the 'wrapper' of the main class of the Google Maps Android API and is the entry point 
 * for all methods related to the map.
 * @author Taeho Kim
 *
 */
public class SupportGoogleMap implements OnCameraChangeListener{
	private static final String KEY_LAST_LATITUDE = "com_androidhuman_google_maps_v2_support_last_latitude";
	private static final String KEY_LAST_LONGITUDE = "com_androidhuman_google_maps_v2_support_last_longitude";
	private static final String KEY_LAST_ZOOM = "com_androidhuman_google_maps_v2_support_last_zoom";
	
	private static final int INVALID_VALUE = -1000;
	
	private boolean mRememberLastCamPosition = true;
	
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
	
	/**
	 * Adds an image to this map.
	 * @param options A ground-overlay options object that defines how to render the overlay. 
	 * Options must have an image (AnchoredBitmap) and position specified.
	 * @return The GroundOverlay that was added to the map.
	 * @throws IllegalArgumentException if either the image or the position is unspecified in the options.
	 */
	public long addGroundOverlay(GroundOverlayOptions options){
		// TODO
		return 0;
	}

	/**
	 * Adds a marker to this map.<p>
	 * The marker's icon is rendered on the map at the location Marker.position. 
	 * Clicking the marker centers the camera on the marker. 
	 * If Marker.title is defined, the map shows an info box with the marker's title and snippet. 
	 * If the marker is draggable, long-clicking and then dragging the marker moves it.
	 * @param options A marker options object that defines how to render the marker.
	 * @return The id of the marker that has added
	 */
	public long addMarker(MarkerOptions options){
		return mMarkerManager.add(options);
	}
	
	public void addMarker(long id, MarkerOptions options){
		mMarkerManager.add(id, options);
	}
	
	public long addPolygon(PolygonOptions options){
		// TODO
		return 0;
	}
	
	public long addPolyline(PolylineOptions options){
		// TODO
		return 0;
	}
	
	public long addTileOverlay(TileOverlayOptions options){
		// TODO
		return 0;
	}
	
	public void animateCamera(CameraUpdate update, int durationMs, GoogleMap.CancelableCallback callback){
		mGoogleMap.animateCamera(update, durationMs, callback);
	}
	
	public void animateCamera(CameraUpdate update, GoogleMap.CancelableCallback callback){
		mGoogleMap.animateCamera(update, callback);
	}
	
	public void animateCamera(CameraUpdate update){
		mGoogleMap.animateCamera(update);
	}
	
	public void clear(){
		mGoogleMap.clear();
		mMarkerManager.clear(false);
	}
	
	public CameraPosition getCameraPosition(){
		return mGoogleMap.getCameraPosition();
	}
	
	/**
	 * Gets the type of map that's currently displayed.
	 * @return
	 */
	public int getMapType(){
		return mGoogleMap.getMapType();
	}
	
	public float getMaxZoomLevel(){
		return mGoogleMap.getMaxZoomLevel();
	}
	
	public float getMinZoomLevel(){
		return mGoogleMap.getMinZoomLevel();
	}
	
	public Location getMyLocation(){
		return mGoogleMap.getMyLocation();
	}
	
	public Projection getProjection(){
		return mGoogleMap.getProjection();
	}
	
	public UiSettings getUiSettings(){
		return mGoogleMap.getUiSettings();
	}
	
	public boolean isIndoorEnabled(){
		return mGoogleMap.isIndoorEnabled();
	}
	
	public boolean isMyLocationEnabled(){
		return mGoogleMap.isMyLocationEnabled();
	}
	
	public boolean isTrafficEnabled(){
		return mGoogleMap.isTrafficEnabled();
	}
	
	public void moveCamera(CameraUpdate update){
		mGoogleMap.moveCamera(update);
	}
	
	/**
	 * Set whether SupportGoogleMap should remember and load last CameraPosition
	 * of the GoogleMap, which includes latitude, longitude, zoom level.
	 * Enabled by default.
	 * @param enabled true to use this feature, false to disable.
	 */
	public void rememberAndLoadLastCameraPosition(boolean enabled){
		this.mRememberLastCamPosition = enabled;
	}
	
	public void setIndoorEnabled(boolean enabled){
		mGoogleMap.setIndoorEnabled(enabled);
	}
	
	public void setInfoWindowAdapter(GoogleMap.InfoWindowAdapter adapter){
		mGoogleMap.setInfoWindowAdapter(adapter);
	}
	
	public void setLocationSource(LocationSource source){
		mGoogleMap.setLocationSource(source);
	}
	
	public void setMapType(int type){
		mGoogleMap.setMapType(type);
	}
	
	public void setMyLocationEnabled(boolean enabled){
		mGoogleMap.setMyLocationEnabled(enabled);
	}

	public void setGoogleMap(GoogleMap map) {
		if(map==null){
			throw new IllegalArgumentException("GoogleMap object is null");
		}
		mGoogleMap = map;
		mGoogleMap.setOnCameraChangeListener(this);
		
		mMarkerManager.setGoogleMap(map);
		
		if(mRememberLastCamPosition){
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
	}
	
	public MarkerManager getMarkerManager(){
		return mMarkerManager;
	}
	
	/**
	 * Sets a callback that's invoked when the camera changes.
	 * @param listener The callback that's invoked when the camera changes. To unset the callback, use null.
	 */
	public void setOnCameraChangeListener(OnCameraChangeListener listener){
		this.mCameraChangeListener = listener;
	}
	
	public void setOnInfoWindowClickListener(SupportOnInfoWindowClickListener listener){
		mMarkerManager.setOnInfoWindowClickListener(listener);
	}
	
	public void setOnMapClickListener(GoogleMap.OnMapClickListener listener){
		mGoogleMap.setOnMapClickListener(listener);
	}
	
	public void setOnMapLongClickListener(GoogleMap.OnMapLongClickListener listener){
		mGoogleMap.setOnMapLongClickListener(listener);
	}
	
	public void setOnMarkerClickListener(GoogleMap.OnMarkerClickListener listener){
		// TODO
	}
	
	public void setOnMarkerDragListener(GoogleMap.OnMarkerDragListener listener){
		// TODO
	}
	
	public void setTrafficEnabled(boolean enabled){
		mGoogleMap.setTrafficEnabled(enabled);
	}
	
	public void stopAnimation(){
		mGoogleMap.stopAnimation();
	}

	@Override
	public void onCameraChange(CameraPosition position) {
		if(mRememberLastCamPosition){
			mSharedPreferenceEditor.putFloat(KEY_LAST_LATITUDE, (float)position.target.latitude)
			.putFloat(KEY_LAST_LONGITUDE, (float)position.target.longitude)
			.putFloat(KEY_LAST_ZOOM, (float)position.zoom).commit();
		}
			
		if(mCameraChangeListener!=null){
			mCameraChangeListener.onCameraChange(position);
		}
	}
	
}
