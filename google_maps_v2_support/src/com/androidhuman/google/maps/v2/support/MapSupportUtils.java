package com.androidhuman.google.maps.v2.support;

import android.content.Intent;
import android.net.Uri;

public class MapSupportUtils {
	public static Intent getPlayServicesIntent(){
		return new Intent(Intent.ACTION_VIEW)
			.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms"));
	}
}
