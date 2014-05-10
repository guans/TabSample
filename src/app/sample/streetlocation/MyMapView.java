package app.sample.streetlocation;

import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.MapView;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import app.sample.streetlocation.entity.MyIcon;


public class MyMapView extends MapView{

	public MyMapView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		GeoPoint g = this.getProjection().fromPixels(MyIcon.w, MyIcon.h);
	//	Log.i("============", "wwwwwwwwww======" + MyIcon.w);
	//	Log.i("============", "hhhhhhhhhh======" + MyIcon.h);
		PositionActivity a=new PositionActivity();
		a.getPosition(g);
		
		
		return super.onTouchEvent(arg0);
	}
	
	
	

}
