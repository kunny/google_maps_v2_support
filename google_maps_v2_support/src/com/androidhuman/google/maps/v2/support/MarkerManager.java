package com.androidhuman.google.maps.v2.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerManager implements OnInfoWindowClickListener{
	
	private GoogleMap mGoogleMap;
	private HashMap<Long, Marker> mMarkerMap;
	private HashMap<Marker, Long> mMarkerIdMap;
	
	private SupportOnInfoWindowClickListener mInfoWindowClickListener;
	
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
	 * @return The id of the marker that has added
	 */
	public long add(MarkerOptions options){
		long id = mMarkerMap.size();
		add(id, options);
		return id;
	}
	
	/**
	 * Adds a marker to map with given marker id.
	 * @param id Marker's id
	 * @param options A marker options object that defines how to render the marker.
	 */
	public void add(long id, MarkerOptions options){
		Marker marker = mGoogleMap.addMarker(options);
		mMarkerMap.put(id, marker);
	}
	
	public void clear(boolean clearObjectsInMap){
		if(clearObjectsInMap){
			mGoogleMap.clear();
		}
		mMarkerMap.clear();
		mMarkerIdMap.clear();
	}
	
	public int extractIdAsInt(Marker marker){
		String markerAsString = marker.getId();
		return Integer.parseInt(markerAsString.substring(1, markerAsString.length()));
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
	
	public void update(long id, MarkerOptions options){
		Marker oldMarker = getMarker(id);
		oldMarker.remove();
		mMarkerMap.remove(id);
		
		Marker newMarker = mGoogleMap.addMarker(options);
		mMarkerMap.put(id, newMarker);
		updateMarkerIdRefIfNeeded(id, oldMarker, newMarker);
	}
	
	public void update(long id, LatLng position){
		Marker oldMarker = getMarker(id);
		Marker marker = mGoogleMap.addMarker(
				new MarkerOptions()
					.title(oldMarker.getTitle())
					.snippet(oldMarker.getSnippet())
					.position(position)
					.draggable(oldMarker.isDraggable()));
		updateMarkerIdRefIfNeeded(id, oldMarker, marker);
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
		updateMarkerIdRefIfNeeded(id, oldMarker, marker);
		oldMarker.remove();
		mMarkerMap.remove(id);
		mMarkerMap.put(id, marker);
	}
	
	public void remove(long id){
		Marker marker = getMarker(id);
		mMarkerMap.remove(id);
		mMarkerIdMap.remove(marker);
		marker.remove();
	}
	
	public void remove(Marker marker){
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
	}
	
	public void setOnInfoWindowClickListener(SupportOnInfoWindowClickListener listener){
		this.mInfoWindowClickListener = listener;
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
			mInfoWindowClickListener.onInfoWindowClicked(id, marker);
		}
	}

}
