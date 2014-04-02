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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import android.util.Log;

import com.androidhuman.google.maps.v2.support.SupportGoogleMap.SupportOnInfoWindowClickListener;
import com.androidhuman.google.maps.v2.support.SupportGoogleMap.SupportOnMarkerClickListener;
import com.androidhuman.google.maps.v2.support.SupportGoogleMap.SupportOnMarkerDragListener;
import com.androidhuman.google.maps.v2.support.model.SupportMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerManager 
	implements OnInfoWindowClickListener, OnMarkerClickListener, OnMarkerDragListener{
	
	private static final String TAG = "gmap_v2_support_marker";
	
	private GoogleMap mGoogleMap;
	private HashMap<Long, Marker> mMarkerMap;
	private HashMap<Marker, Long> mMarkerIdMap;
	
	private SupportOnInfoWindowClickListener mInfoWindowClickListener;
	private SupportOnMarkerClickListener mMarkerClickListener;
	private SupportOnMarkerDragListener mMarkerDragListener;
	
	public MarkerManager(){
		mMarkerMap = new LinkedHashMap<Long, Marker>();
		mMarkerIdMap = new LinkedHashMap<Marker, Long>();
	}
	
	public MarkerManager(GoogleMap map){
		this();
		setGoogleMap(map);
	}
	
	/**
	 * Adds a marker to map with auto-generated marker id.
	 * @param options A marker options object that defines how to render the marker.
	 * @return The {@link SupportMarker} object that was added
	 */
	public SupportMarker add(MarkerOptions options){
		long id = mMarkerMap.size();
		Marker marker = addMarkerWithRef(id, options);
		return new SupportMarker(id, marker);
	}
	
	/**
	 * Adds a marker to map with auto-generated marker id.
	 * @param title A title of the marker
	 * @param position Marker's position
	 * @return The {@link SupportMarker} object that was added
	 */
	public SupportMarker add(String title, LatLng position){
		long id = mMarkerMap.size();
		MarkerOptions options = new MarkerOptions();
		options.position(position);
		options.title(title);
		Marker marker = addMarkerWithRef(id, options);
		return new SupportMarker(id, marker);
	}
	
	/**
	 * Adds a marker to map with auto-generated marker id.
	 * @param title A title of the marker
	 * @param snippet A snippet of the marker
	 * @param position Marker's position
	 * @return The {@link SupportMarker} object that was added
	 */
	public SupportMarker add(String title, String snippet, LatLng position){
		long id = mMarkerMap.size();
		MarkerOptions options = new MarkerOptions();
		options.position(position);
		options.title(title);
		options.snippet(snippet);
		Marker marker = addMarkerWithRef(id, options);
		return new SupportMarker(id, marker);
	}
	
	/**
	 * Adds a marker to map with given marker id.
	 * @param id Marker's id
	 * @param options A marker options object that defines how to render the marker.
	 * @return The {@link SupportMarker} object that was added
	 */
	public SupportMarker add(long id, MarkerOptions options){
		Marker marker = mGoogleMap.addMarker(options);
		mMarkerMap.put(id, marker);
		return new SupportMarker(id,marker);
	}
	
	
	private Marker addMarkerWithRef(long id, MarkerOptions options){
		Marker marker = mGoogleMap.addMarker(options);
		mMarkerMap.put(id, marker);
		return marker;
	}
	
	public void clear(boolean clearObjectsInMap){
		if(clearObjectsInMap){
			mGoogleMap.clear();
		}
		mMarkerMap.clear();
		mMarkerIdMap.clear();
	}
	
	private long findIdByMarker(Marker marker){
		if(mMarkerIdMap.containsKey(marker)){
			return mMarkerIdMap.get(marker);
		}
		
		Set<Entry<Long, Marker>> entries = mMarkerMap.entrySet();
		for(Entry<Long, Marker> entry : entries){
			Marker m = entry.getValue();
			if(m.equals(marker)){
				mMarkerIdMap.put(marker, entry.getKey());
				return entry.getKey();
			}
		}
		throw new IllegalArgumentException("No id exists that mathces given marker.");
	}
	
	public Marker getMarker(long id){
		Marker marker = mMarkerMap.get(id);
		if(marker!=null){
			return marker;
		}else{
			throw new IllegalArgumentException("Marker with given id="+id+" does not exists.");
		}
	}
	
	public void update(SupportMarker marker, MarkerOptions options){
		if(marker==null){
			Log.e(TAG, "Marker is null. Did you instantiated the Marker properly?");
			return;
		}
		Marker oldMarker = marker.getMarker();
		oldMarker.remove();
		mMarkerMap.remove(marker.getId());
		
		Marker newMarker = mGoogleMap.addMarker(options);
		if(oldMarker.isInfoWindowShown()){
			newMarker.showInfoWindow();
		}
		mMarkerMap.put(marker.getId(), newMarker);
		updateMarkerIdRefIfNeeded(marker.getId(), oldMarker, newMarker);
		marker.setMarker(newMarker);
	}
	
	public void update(SupportMarker marker, LatLng position){
		if(marker==null){
			Log.e(TAG, "Marker is null. Did you instantiated the Marker properly?");
			return;
		}
		Marker oldMarker = marker.getMarker();
		Marker newMarker = mGoogleMap.addMarker(
				new MarkerOptions()
					.title(oldMarker.getTitle())
					.snippet(oldMarker.getSnippet())
					.position(position)
					.draggable(oldMarker.isDraggable()));
		if(oldMarker.isInfoWindowShown()){
			newMarker.showInfoWindow();
		}
		updateMarkerIdRefIfNeeded(marker.getId(), oldMarker, newMarker);
		oldMarker.remove();
		mMarkerMap.remove(marker.getId());
		mMarkerMap.put(marker.getId(), newMarker);
		marker.setMarker(newMarker);
	}
	
	public void update(SupportMarker marker, String title){
		if(marker==null){
			Log.e(TAG, "Marker is null. Did you instantiated the Marker properly?");
			return;
		}
		Marker oldMarker = marker.getMarker();
		Marker newMarker = mGoogleMap.addMarker(
				new MarkerOptions()
					.title(title)
					.snippet(oldMarker.getSnippet())
					.position(oldMarker.getPosition())
					.draggable(oldMarker.isDraggable()));
		if(oldMarker.isInfoWindowShown()){
			newMarker.showInfoWindow();
		}
		updateMarkerIdRefIfNeeded(marker.getId(), oldMarker, newMarker);
		oldMarker.remove();
		mMarkerMap.remove(marker.getId());
		mMarkerMap.put(marker.getId(), newMarker);
		marker.setMarker(newMarker);
	}
	
	public void update(SupportMarker marker, String title, String snippet){
		if(marker==null){
			Log.e(TAG, "Marker is null. Did you instantiated the Marker properly?");
			return;
		}
		Marker oldMarker = marker.getMarker();
		Marker newMarker = mGoogleMap.addMarker(
				new MarkerOptions()
					.title(title)
					.snippet(snippet)
					.position(oldMarker.getPosition())
					.draggable(oldMarker.isDraggable()));
		if(oldMarker.isInfoWindowShown()){
			newMarker.showInfoWindow();
		}
		updateMarkerIdRefIfNeeded(marker.getId(), oldMarker, newMarker);
		oldMarker.remove();
		mMarkerMap.remove(marker.getId());
		mMarkerMap.put(marker.getId(), newMarker);
		marker.setMarker(newMarker);
	}
	
	public void remove(SupportMarker aMarker){
		if(aMarker==null){
			Log.e(TAG, "Marker is null. Did you instantiated the Marker properly?");
			return;
		}
		Marker marker = aMarker.getMarker();
		mMarkerMap.remove(aMarker.getId());
		mMarkerIdMap.remove(marker);
		marker.remove();
	}
	
	public void remove(Marker marker){
		if(marker==null){
			Log.e(TAG, "Marker is null. Did you instantiated the Marker properly?");
			return;
		}
		Collection<Marker> values = mMarkerMap.values();
		for(Marker m : values){
			if(marker.equals(m)){
				mMarkerMap.remove(m);
				mMarkerIdMap.remove(m);
				m.remove();
				break;
			}
		}
	}
	
	public void setGoogleMap(GoogleMap map){
		this.mGoogleMap = map;
		mGoogleMap.setOnInfoWindowClickListener(this);
		mGoogleMap.setOnMarkerClickListener(this);
		mGoogleMap.setOnMarkerDragListener(this);
	}
	
	public void setOnInfoWindowClickListener(SupportOnInfoWindowClickListener listener){
		this.mInfoWindowClickListener = listener;
	}
	
	public void setOnMarkerClickListener(SupportOnMarkerClickListener listener){
		mMarkerClickListener = listener;
	}
	
	public void setOnMarkerDragListener(SupportOnMarkerDragListener listener){
		mMarkerDragListener = listener;
	}

	private void updateMarkerIdRefIfNeeded(long id, Marker oldMarker, Marker newMarker){
		if(mMarkerIdMap.remove(oldMarker)!=null){
			mMarkerIdMap.put(newMarker, id);
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		if(mInfoWindowClickListener!=null){
			long id = findIdByMarker(marker);
			mInfoWindowClickListener.onInfoWindowClick(id, marker);
		}
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		if(mMarkerDragListener!=null){
			long id = findIdByMarker(marker);
			mMarkerDragListener.onMarkerDrag(id, marker);
		}
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		if(mMarkerDragListener!=null){
			long id = findIdByMarker(marker);
			mMarkerDragListener.onMarkerDragEnd(id, marker);
		}
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		if(mMarkerDragListener!=null){
			long id = findIdByMarker(marker);
			mMarkerDragListener.onMarkerDragStart(id, marker);
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if(marker.isInfoWindowShown()){
			marker.hideInfoWindow();
		}else{
			marker.showInfoWindow();
		}
		if(mMarkerClickListener!=null){
			long id = findIdByMarker(marker);
			return mMarkerClickListener.onMarkerClick(id, marker);
		}else{
			return true;
		}
	}

}
