package com.androidhuman.google.maps.v2.support.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class SupportMarker {
	private static final long INVALID_ID = -1;
	
	private Marker marker;
	private long id = INVALID_ID;
	
	public SupportMarker(long id, Marker marker){
		this.id = id;
		this.marker = marker;
	}
	
	public Marker getMarker(){
		return marker;
	}
	
	public void setMarker(Marker marker){
		this.marker = marker;
	}
	public String getTitle(){
		return marker!=null ? marker.getTitle() : null;
	}
	
	public String getSnippet(){
		return marker!=null ? marker.getSnippet() : null;
	}
	
	public LatLng getPosition(){
		return marker!=null ? marker.getPosition() : null;
	}
	
	public long getId(){
		return id;
	}

}
