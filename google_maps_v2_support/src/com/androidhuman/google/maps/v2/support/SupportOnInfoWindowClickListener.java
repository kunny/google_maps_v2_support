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
