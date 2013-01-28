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
	
	public void showInfoWindow(){
		if(marker!=null){
			marker.showInfoWindow();
		}
	}
	
	public void hideInfoWindow(){
		if(marker!=null){
			marker.hideInfoWindow();
		}
	}

}
