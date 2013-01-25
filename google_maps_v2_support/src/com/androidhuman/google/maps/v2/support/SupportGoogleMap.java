/*
 * Copyright 2013 Taeho Kim
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
 */
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
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
	private boolean mIsInitialPositionRequested = false;
	private boolean mIsInitialZoomLevelRequested = false;
	
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mSharedPreferenceEditor;
	
	private GoogleMap mGoogleMap;
	private MarkerManager mMarkerManager;
	private PolylineManager mPolylineManager;
	
	private OnCameraChangeListener mCameraChangeListener;
	
	private SupportGoogleMap(Context context){
		mMarkerManager = new MarkerManager();
		mPolylineManager = new PolylineManager();
		
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		mSharedPreferenceEditor = mSharedPreferences.edit();
	}
	
	private SupportGoogleMap(Context context, GoogleMap map){
		this(context);
		setGoogleMap(map);
	}
	
	/**
	 * Adds an image to this map.
	 * @param options A ground-overlay options object that defines how to render the overlay. 
	 * Options must have an image (AnchoredBitmap) and position specified.
	 * @throws IllegalArgumentException if either the image or the position is unspecified in the options.
	 */
	public void addGroundOverlay(GroundOverlayOptions options){
		mGoogleMap.addGroundOverlay(options);
	}

	/**
	 * Adds a marker to this map.<p>
	 * The marker's icon is rendered on the map at the location Marker.position. 
	 * Clicking the marker centers the camera on the marker. 
	 * If Marker.title is defined, the map shows an info box with the marker's title and snippet. 
	 * If the marker is draggable, long-clicking and then dragging the marker moves it.
	 * @param options A marker options object that defines how to render the marker.
	 * @return The id of the marker that was added to map.
	 */
	public long addMarker(MarkerOptions options){
		return mMarkerManager.add(options);
	}
	
	/**
	 * Adds a marker to map with auto-generated marker id.
	 * @param title A title of the marker
	 * @param position Marker's position
	 * @return The id of the marker that has added
	 */
	public long addMarker(String title, LatLng position){
		return mMarkerManager.add(title, position);
	}
	
	/**
	 * Adds a marker to map with auto-generated marker id.
	 * @param title A title of the marker
	 * @param snippet A snippet of the marker
	 * @param position Marker's position
	 * @return The id of the marker that has added
	 */
	public long addMarker(String title, String snippet, LatLng position){
		return mMarkerManager.add(title, snippet, position);
	}
	
	/**
	 * Adds a marker to map with given marker id.
	 * @param id Marker's id
	 * @param options A marker options object that defines how to render the marker.
	 */
	public void addMarker(long id, MarkerOptions options){
		mMarkerManager.add(id, options);
	}
	
	public void addPolygon(PolygonOptions options){
		mGoogleMap.addPolygon(options);
	}
	
	/**
	 * Adds a polyline to this map.
	 * @param options A polyline options object that defines how to render the Polyline.
	 * @return The id of the polyline that was added to map.
	 */
	public long addPolyline(PolylineOptions options){
		return mPolylineManager.add(options);
	}
	
	/**
	 * Adds a polyline to this map.
	 * @param points Array of positions composing polyline
	 * @return The id of the polyline that was added to map.
	 */
	public long addPolyline(LatLng... points){
		return mPolylineManager.add(points);
	}
	
	/**
	 * Adds a polyline to this map with given polyline id.
	 * @param id Polyline's id
	 * @param options A polyline options object that defines how to render the Polyline.
	 */
	public void addPolyline(long id, PolylineOptions options){
		mPolylineManager.add(id, options);
	}
	
	/**
	 * Adds a tile overlay to this map. See TileOverlay for more information.<p>
	 * Note that unlike other overlays, if the map is recreated, tile overlays are not 
	 * automatically restored and must be re-added manually.
	 * @param options A tile-overlay options object that defines how to render the overlay. 
	 * Options must have a TileProvider specified, otherwise an IllegalArgumentException 
	 * will be thrown.
	 * @returns The TileOverlay that was added to the map.
	 */
	public TileOverlay addTileOverlay(TileOverlayOptions options){
		return mGoogleMap.addTileOverlay(options);
	}
	
	/**
	 * Moves the map according to the update with an animation over a specified duration, 
	 * and calls an optional callback on completion. See CameraUpdateFactory for a set of updates.
	 * If getCameraPosition() is called during the animation, it will return the current 
	 * location of the camera in flight.
	 * @param update The change that should be applied to the camera.
	 * @param durationMs The duration of the animation in milliseconds. 
	 * This must be strictly positive, otherwise an IllegalArgumentException will be thrown.
	 * @param callback An optional callback to be notified from the main thread 
	 * when the animation stops. If the animation stops due to its natural completion, 
	 * the callback will be notified with onFinish(). 
	 * If the animation stops due to interruption by a later camera movement or a user gesture, 
	 * onCancel() will be called. The callback should not attempt to move or animate the camera 
	 * in its cancellation method.
	 */
	public void animateCamera(CameraUpdate update, int durationMs, GoogleMap.CancelableCallback callback){
		mGoogleMap.animateCamera(update, durationMs, callback);
	}
	
	/**
	 * Animates the movement of the camera from the current position to the position defined 
	 * in the update and calls an optional callback on completion. 
	 * See CameraUpdateFactory for a set of updates.<p>
	 * During the animation, a call to getCameraPosition() returns an intermediate location 
	 * of the camera.
	 * @param update The change that should be applied to the camera.
	 * @param callback The callback to invoke from the main thread when the animation stops. 
	 * If the animation completes normally, onFinish() is called; otherwise, onCancel() is called. 
	 * Do not update or animate the camera from within onCancel().
	 */
	public void animateCamera(CameraUpdate update, GoogleMap.CancelableCallback callback){
		mGoogleMap.animateCamera(update, callback);
	}
	
	/**
	 * Animates the movement of the camera from the current position to the position defined 
	 * in the update. During the animation, a call to getCameraPosition() returns an intermediate 
	 * location of the camera. <p>See CameraUpdateFactory for a set of updates.
	 * @param update The change that should be applied to the camera.
	 */
	public void animateCamera(CameraUpdate update){
		mGoogleMap.animateCamera(update);
	}
	
	/**
	 * Removes all markers, overlays, and polylines from the map.
	 */
	public void clear(){
		mGoogleMap.clear();
		mMarkerManager.clear(false);
	}
	
	/**
	 * Gets the current position of the camera.<p>
	 * The CameraPosition returned is a snapshot of the current position, 
	 * and will not automatically update when the camera moves.
	 * @return The current position of the Camera.
	 */
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
	
	public Marker getMarker(long markerId){
		return mMarkerManager.getMarker(markerId);
	}
	
	/**
	 * Returns the maximum zoom level for the current camera position. 
	 * This takes into account what map type is currently being used, e.g., satellite or 
	 * terrain may have a lower max zoom level than the base map tiles.
	 * @return The maximum zoom level available at the current camera position.
	 */
	public float getMaxZoomLevel(){
		return mGoogleMap.getMaxZoomLevel();
	}
	
	/**
	 * Returns the minimum zoom level. This is the same for every location 
	 * (unlike the maximum zoom level) but may vary between devices and map sizes.
	 * @return The minimum zoom level available.
	 */
	public float getMinZoomLevel(){
		return mGoogleMap.getMinZoomLevel();
	}
	
	/**
	 * Returns the currently displayed user location, or null if there is no location data available.
	 * @return The currently displayed user location.
	 */
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
	
	public static SupportGoogleMap newInstance(Context context, MapFragment mapFragment){
		SupportGoogleMap map = new SupportGoogleMap(context, mapFragment.getMap());
		return map;
	}
	
	public static SupportGoogleMap newInstance(Context context, SupportMapFragment mapFragment){
		SupportGoogleMap map = new SupportGoogleMap(context, mapFragment.getMap());
		return map;
	}
	
	public static SupportGoogleMap newInstance(Context context, GoogleMap map){
		SupportGoogleMap mapInstance = new SupportGoogleMap(context, map);
		return mapInstance;
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
	
	public void setInitialCameraPosition(LatLng position){
		mIsInitialPositionRequested = true;
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
	}
	
	public void setInitialCameraPosition(LatLng posotion, float zoomLevel){
		mIsInitialPositionRequested = true;
		mIsInitialZoomLevelRequested = true;
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posotion, zoomLevel));
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
		mPolylineManager.setGoogleMap(map);
		
		if(mRememberLastCamPosition){
			float lastLatitude = mSharedPreferences.getFloat(KEY_LAST_LATITUDE, INVALID_VALUE);
			float lastLongitude = mSharedPreferences.getFloat(KEY_LAST_LONGITUDE, INVALID_VALUE);
			float lastZoom = mSharedPreferences.getFloat(KEY_LAST_ZOOM, INVALID_VALUE);
			
			if(!mIsInitialPositionRequested && 
					lastLatitude!=INVALID_VALUE && lastLongitude!=INVALID_VALUE){
				mGoogleMap.moveCamera(
						CameraUpdateFactory.newLatLng(
								new LatLng(lastLatitude, lastLongitude)));
			}
			
			if(!mIsInitialZoomLevelRequested && lastZoom!=INVALID_VALUE){
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
	
	public void setOnMarkerClickListener(SupportGoogleMap.SupportOnMarkerClickListener listener){
		mMarkerManager.setOnMarkerClickListener(listener);
	}
	
	public void setOnMarkerDragListener(SupportGoogleMap.SupportOnMarkerDragListener listener){
		mMarkerManager.setOnMarkerDragListener(listener);
	}
	
	public void setTrafficEnabled(boolean enabled){
		mGoogleMap.setTrafficEnabled(enabled);
	}
	
	public void stopAnimation(){
		mGoogleMap.stopAnimation();
	}
	
	public void updateMarker(long id, MarkerOptions options){
		mMarkerManager.update(id, options);
	}
	
	public void updateMarker(long id, LatLng position){
		mMarkerManager.update(id, position);
	}
	
	public void updateMarker(long id, String title){
		mMarkerManager.update(id, title);
	}
	
	public void updateMarker(long id, String title, String snippet){
		mMarkerManager.update(id, title, snippet);
	}
	
	public void removeMarker(long id){
		mMarkerManager.remove(id);
	}
	
	public void removeMarker(Marker marker){
		mMarkerManager.remove(marker);
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
	
	/**
	 * Callback interface for click/tap events on a marker's info window.
	 * @author Taeho Kim
	 */
	public interface SupportOnInfoWindowClickListener {
		
		/**
		 * Called when the marker's info window is clicked.
		 * @param id Marker's id
		 * @param marker The marker of the info window that was clicked.
		 */
		public void onInfoWindowClicked(long id, Marker marker);
	}
	
	public interface SupportOnMarkerClickListener {
		public boolean onMarkerClick(long id, Marker marker);
	}
	
	public interface SupportOnMarkerDragListener{
		/**
		 * Called repeatedly while a marker is being dragged. 
		 * The marker's location can be accessed via getPosition().
		 * @param id
		 * @param marker The marker being dragged.
		 */
		public void onMarkerDrag(long id, Marker marker);
		
		/**
		 * Called when a marker has finished being dragged. 
		 * The marker's location can be accessed via getPosition().
		 * @param id
		 * @param marker The marker being dragged.
		 */
		public void onMarkerDragEnd(long id, Marker marker);
		
		/**
		 * Called when a marker starts being dragged. 
		 * The marker's location can be accessed via getPosition(); 
		 * this position may be different to the position prior to the 
		 * start of the drag because the marker is popped up above the touch point.
		 * @param id
		 * @param marker THe marker being dragged.
		 */
		public void onMarkerDragStart(long id, Marker marker);
	}
	
}
