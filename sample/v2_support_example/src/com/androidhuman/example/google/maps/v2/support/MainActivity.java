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
