package com.androidhuman.google.maps.v2.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class PolylineManager {
	private GoogleMap mGoogleMap;
	private HashMap<Long, Polyline> mPolylineMap;
	
	public PolylineManager(){
		mPolylineMap = new LinkedHashMap<Long, Polyline>();
	}
	
	public PolylineManager(GoogleMap map){
		this();
		setGoogleMap(map);
	}
	

	public long add(PolylineOptions options){
		long id = mPolylineMap.size();
		add(id, options);
		return id;
	}
	
	public long add(LatLng... positions){
		return add(new PolylineOptions().add(positions));
	}
	
	public void add(long id, PolylineOptions options){
		Polyline marker = mGoogleMap.addPolyline(options);
		mPolylineMap.put(id, marker);
	}
	
	public void clear(boolean clearObjectsInMap){
		if(clearObjectsInMap){
			mGoogleMap.clear();
		}
		mPolylineMap.clear();
	}
	
	public int extractIdAsInt(Polyline polyline){
		String markerAsString = polyline.getId();
		return Integer.parseInt(markerAsString.substring(1, markerAsString.length()));
	}
	
	public Polyline getPolyline(long id){
		Polyline polyline = mPolylineMap.get(id);
		if(polyline!=null){
			return polyline;
		}else{
			throw new IllegalArgumentException("Polyline with given id="+id+" does not exists.");
		}
	}
	
	public void remove(long id){
		Polyline polyline = getPolyline(id);
		mPolylineMap.remove(id);
		polyline.remove();
	}
	
	public void remove(Polyline polyline){
		Collection<Polyline> values = mPolylineMap.values();
		for(Polyline p : values){
			if(polyline.equals(p)){
				mPolylineMap.remove(p);
				p.remove();
				break;
			}
		}
	}
	
	public void setGoogleMap(GoogleMap map){
		this.mGoogleMap = map;
	}
	
}
