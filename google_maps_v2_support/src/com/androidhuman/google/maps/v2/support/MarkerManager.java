package com.androidhuman.google.maps.v2.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerManager implements OnInfoWindowClickListener{
	
	private GoogleMap mGoogleMap;
	private HashMap<Long, Marker> mMarkerMap;
	
	private SupportOnInfoWindowClickListener mInfoWindowClickListener;
	
	public MarkerManager(){
		mMarkerMap = new LinkedHashMap<Long, Marker>();
	}
	
	public MarkerManager(GoogleMap map){
		this();
		setGoogleMap(map);
	}
	
	public void setGoogleMap(GoogleMap map){
		this.mGoogleMap = map;
		mGoogleMap.setOnInfoWindowClickListener(this);
	}
	
	public void setOnInfoWindowClockListener(SupportOnInfoWindowClickListener listener){
		this.mInfoWindowClickListener = listener;
	}
	
	/**
	 * Adds a marker to map with auto-generated marker id.
	 * @param options A marker options object that defines how to render the marker.
	 * @return The id of the marker that has added
	 */
	public long add(MarkerOptions options){
		long id = mMarkerMap.size();
		add(id, options);
		return id;
	}
	
	public void add(long id, MarkerOptions options){
		Marker marker = mGoogleMap.addMarker(options);
		mMarkerMap.put(id, marker);
	}
	
	public void update(long id, MarkerOptions options){
		Marker marker = getMarker(id);
		marker.remove();
		mMarkerMap.remove(id);
		
		Marker newMarker = mGoogleMap.addMarker(options);
		mMarkerMap.put(id, newMarker);
	}
	
	public void update(long id, LatLng position){
		Marker oldMarker = getMarker(id);
		Marker marker = mGoogleMap.addMarker(
				new MarkerOptions()
					.title(oldMarker.getTitle())
					.snippet(oldMarker.getSnippet())
					.position(position)
					.draggable(oldMarker.isDraggable()));
		oldMarker.remove();
		mMarkerMap.remove(id);
		mMarkerMap.put(id, marker);
	}
	
	public void update(long id, String title){
		Marker oldMarker = getMarker(id);
		Marker marker = mGoogleMap.addMarker(
				new MarkerOptions()
					.title(title)
					.snippet(oldMarker.getSnippet())
					.position(oldMarker.getPosition())
					.draggable(oldMarker.isDraggable()));
		oldMarker.remove();
		mMarkerMap.remove(id);
		mMarkerMap.put(id, marker);
	}
	
	public void remove(long id){
		Marker marker = getMarker(id);
		marker.remove();
		mMarkerMap.remove(id);
	}
	
	public void remove(Marker marker){
		Collection<Marker> values = mMarkerMap.values();
		for(Marker m : values){
			if(marker.equals(m)){
				m.remove();
				mMarkerMap.remove(m);
				break;
			}
		}
	}
	
	public Marker getMarker(long id){
		Marker marker = mMarkerMap.get(id);
		if(marker!=null){
			return marker;
		}else{
			throw new IllegalArgumentException("Marker with given id+"+id+" does not exists.");
		}
	}
	
	private int extractIdAsInt(Marker marker){
		String markerAsString = marker.getId();
		return Integer.parseInt(markerAsString.substring(1, markerAsString.length()));
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		if(mInfoWindowClickListener!=null){
			int id = extractIdAsInt(marker);
			mInfoWindowClickListener.onInfoWindowClicked(id, marker);
		}
	}

}
