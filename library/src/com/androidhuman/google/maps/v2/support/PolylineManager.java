package com.androidhuman.google.maps.v2.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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
	
	public long add(LatLng... points){
		return add(new PolylineOptions().add(points));
	}
	
	public long add(List<LatLng> points){
		return add(new PolylineOptions().add((LatLng[])points.toArray()));
	}
	
	public long add(int color, LatLng... points){
		return add(new PolylineOptions().color(color).add(points));
	}
	
	public long add(int color, List<LatLng> points){
		return add(new PolylineOptions().color(color).add((LatLng[])points.toArray()));
	}
	
	public void add(long id, PolylineOptions options){
		Polyline marker = mGoogleMap.addPolyline(options);
		mPolylineMap.put(id, marker);
	}
	
	public void append(long id, LatLng point){
		Polyline oldLine = mPolylineMap.get(id);
		int color = oldLine.getColor();
		List<LatLng> points = oldLine.getPoints();
		float width = oldLine.getWidth();
		float zIndex = oldLine.getZIndex();
		
		// Copy old points
		List<LatLng> newPoints = new ArrayList<LatLng>();
		newPoints.addAll(points);
		// Add new point
		newPoints.add(point);
		
		// Remove old polyline
		oldLine.remove();
		mPolylineMap.remove(id);
		
		// Add new polyline with new point
		Polyline newLine = mGoogleMap.addPolyline(
				new PolylineOptions().addAll(newPoints)
					.color(color).width(width).zIndex(zIndex));
		mPolylineMap.put(id, newLine);
	}
	
	public void clear(boolean clearObjectsInMap){
		if(clearObjectsInMap){
			mGoogleMap.clear();
		}
		mPolylineMap.clear();
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
