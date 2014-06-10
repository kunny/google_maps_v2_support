package com.androidhuman.google.maps.v2.support;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class V2SupportUtils {
	public static Intent getPlayServicesIntent(){
		return new Intent(Intent.ACTION_VIEW)
			.setData(Uri.parse("market://details?id=com.google.android.gms"));
	}
	
	public static void moveToPlayServiceDownloadPage(Context context){
		context.startActivity(getPlayServicesIntent());
	}
}
