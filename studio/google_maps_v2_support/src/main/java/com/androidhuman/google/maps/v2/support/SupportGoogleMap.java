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
package com.androidhuman.google.maps.v2.support;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.androidhuman.google.maps.v2.support.model.SupportMarker;
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
import com.google.android.gms.maps.model.Polyline;
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
	
	private SupportGoogleMap(Context context, GoogleMap map) throws NoPlayServicesFoundException{
		this(context);
		setGoogleMap(map);
	}

    public static SupportGoogleMap newInstance(Context context, MapFragment mapFragment) throws NoPlayServicesFoundException{
        SupportGoogleMap map = new SupportGoogleMap(context, mapFragment.getMap());
        return map;
    }

    public static SupportGoogleMap newInstance(Context context, SupportMapFragment mapFragment) throws NoPlayServicesFoundException{
        SupportGoogleMap map = new SupportGoogleMap(context, mapFragment.getMap());
        return map;
    }

    public static SupportGoogleMap newInstance(Context context, GoogleMap map) throws NoPlayServicesFoundException{
        SupportGoogleMap mapInstance = new SupportGoogleMap(context, map);
        return mapInstance;
    }

    public GoogleMap getGoogleMap() throws IllegalStateException {
        if(mGoogleMap==null){
            throw new IllegalStateException("Google map is null");
        }
        return mGoogleMap;
    }

	/**
	 * Adds a marker to this map.<p>
	 * The marker's icon is rendered on the map at the location Marker.position. 
	 * Clicking the marker centers the camera on the marker. 
	 * If Marker.title is defined, the map shows an info box with the marker's title and snippet. 
	 * If the marker is draggable, long-clicking and then dragging the marker moves it.
	 * @param options A marker options object that defines how to render the marker.
	 * @return The {@link SupportMarker} object that was added
	 */
	public SupportMarker addMarker(MarkerOptions options){
		return mMarkerManager.add(options);
	}
	
	/**
	 * Adds a marker to map with auto-generated marker id.
	 * @param title A title of the marker
	 * @param position Marker's position
	 * @return The {@link SupportMarker} object that was added
	 */
	public SupportMarker addMarker(String title, LatLng position){
		return mMarkerManager.add(title, position);
	}
	
	/**
	 * Adds a marker to map with auto-generated marker id.
	 * @param title A title of the marker
	 * @param snippet A snippet of the marker
	 * @param position Marker's position
	 * @return The {@link SupportMarker} object that was added
	 */
	public SupportMarker addMarker(String title, String snippet, LatLng position){
		return mMarkerManager.add(title, snippet, position);
	}
	
	/**
	 * Adds a marker to map with given marker id.
	 * @param id Marker's id
	 * @param options A marker options object that defines how to render the marker.
	 * @return The {@link SupportMarker} object that was added
	 */
	public SupportMarker addMarker(long id, MarkerOptions options){
		return mMarkerManager.add(id, options);
	}
	
	/**
	 * Removes all markers, overlays, and polylines from the map.
	 */
	public void clear(){
		mGoogleMap.clear();
		mMarkerManager.clear(false);
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

	public void setInitialCameraPosition(LatLng position){
		mIsInitialPositionRequested = true;
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
	}
	
	public void setInitialCameraPosition(LatLng position, float zoomLevel){
		mIsInitialPositionRequested = true;
		mIsInitialZoomLevelRequested = true;
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel));
	}

	public void setGoogleMap(GoogleMap map) throws NoPlayServicesFoundException {
		if(map==null){
			throw new NoPlayServicesFoundException();
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
	
	public PolylineManager getPolylineManager(){
		return mPolylineManager;
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
     * Adds a polyline to this map.
     * @param points Array of positions composing polyline
     * @return The id of the polyline that was added to map.
     */
    public long addPolyline(List<LatLng> points){
        return mPolylineManager.add(points);
    }

    /**
     * Adds a polyline to this map.
     * @param color The color of the polyline
     * @param points Array of positions composing polyline
     * @return The id of the polyline that was added to map.
     */
    public long addPolyline(int color, LatLng... points){
        return mPolylineManager.add(color, points);
    }

    /**
     * Adds a polyline to this map.
     * @param color The color of the polyline
     * @param points Array of positions composing polyline
     * @return The id of the polyline that was added to map.
     */
    public long addPolyline(int color, List<LatLng> points){
        return mPolylineManager.add(color, points);
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
     * Appends a point to an existing polyline.
     * @param id Polyline's id
     * @param point a Point to append with
     */
    public void appendPolyline(long id, LatLng point, boolean shouldAnimateToLastPoint){
        mPolylineManager.append(id, point);
        if(shouldAnimateToLastPoint){
            animateTo(point);
        }
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
	
	public void setOnMarkerClickListener(SupportGoogleMap.SupportOnMarkerClickListener listener){
		mMarkerManager.setOnMarkerClickListener(listener);
	}
	
	public void setOnMarkerDragListener(SupportGoogleMap.SupportOnMarkerDragListener listener){
		mMarkerManager.setOnMarkerDragListener(listener);
	}

	public void updateMarker(SupportMarker marker, MarkerOptions options){
		mMarkerManager.update(marker, options);
	}
	
	public void updateMarker(SupportMarker marker, LatLng position){
		mMarkerManager.update(marker, position);
	}
	
	public void updateMarker(SupportMarker marker, String title){
		mMarkerManager.update(marker, title);
	}
	
	public void updateMarker(SupportMarker marker, String title, String snippet){
		mMarkerManager.update(marker, title, snippet);
	}
	
	public void removeMarker(SupportMarker marker){
		mMarkerManager.remove(marker);
	}
	
	public void removeMarker(Marker marker){
		mMarkerManager.remove(marker);
	}

    /**
     * Animates the movement of the camera from the current position to the position defined
     * in the update. During the animation, a call to getCameraPosition() returns an intermediate
     * location of the camera. <p>See CameraUpdateFactory for a set of updates.
     * @param position
     */
    public void animateTo(LatLng position){
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(position));
    }

    /**
     * Animates the movement of the camera from the current position to the position defined
     * in the update. During the animation, a call to getCameraPosition() returns an intermediate
     * location of the camera. <p>See CameraUpdateFactory for a set of updates.
     * @param marker
     */
    public void animateTo(SupportMarker marker){
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getMarker().getPosition()));
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
		public void onInfoWindowClick(long id, Marker marker);
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
