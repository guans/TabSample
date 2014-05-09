package app.sample.streetlocation.location;

import com.tencent.tencentmap.lbssdk.TencentMapLBSApiListener;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiResult;

class LocListener extends TencentMapLBSApiListener {
	
	double latitude;
	double longtitude;

	public LocListener(int reqGeoType, int reqLevel, int reqDelay) {
		super(reqGeoType, reqLevel, reqDelay);
	}

	@Override
	public void onLocationUpdate(TencentMapLBSApiResult locRes) {
		// String res = locResToString(locRes);

		// String date = (new Date()).toLocaleString();
		// mTextRes.setText(date + "\n" + res);
		latitude = locRes.Latitude;
		latitude = locRes.Longitude;
	}

}