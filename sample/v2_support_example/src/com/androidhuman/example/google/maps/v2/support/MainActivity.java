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
package com.androidhuman.example.google.maps.v2.support;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity{

	ListView menuList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		menuList = (ListView)findViewById(R.id.list);
		String[] items = new String[]{"Marker", "Polyline"};
		ArrayAdapter<String> adapter = 
				new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		
		menuList.setAdapter(adapter);
		menuList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				switch(position){
				case 0:
					startActivity(new Intent(MainActivity.this, MarkerActivity.class));
					break;
					
				case 1:
					startActivity(new Intent(MainActivity.this, PolylineActivity.class));
					break;
				}
			}
			
		});
	}



}
