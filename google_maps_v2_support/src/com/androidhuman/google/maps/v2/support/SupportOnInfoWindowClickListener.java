package com.androidhuman.google.maps.v2.support;

import com.google.android.gms.maps.model.Marker;

/**
 * Callback interface for click/tap events on a marker's info window.
 * @author Taeho Kim
 *
 */
public interface SupportOnInfoWindowClickListener {
	
	/**
	 * Called when the marker's info window is clicked.
	 * @param id Marker's id
	 * @param marker The marker of the info window that was clicked.
	 */
	public void onInfoWindowClicked(long id, Marker marker);
}
