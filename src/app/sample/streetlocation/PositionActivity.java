package app.sample.streetlocation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import com.example.streetlocation.R;
import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.OverlayItem;
import com.tencent.tencentmap.mapsdk.search.GeocoderSearch;
import com.tencent.tencentmap.mapsdk.search.ReGeocoderResult;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import app.sample.streetlocation.GetNavStart.ListInfoListener;
import app.sample.streetlocation.GetNavStart.MyBaseAdapter;
import app.sample.streetlocation.entity.Declare;

public class PositionActivity extends MapActivity {
	// static BMapManager mBampMan = null;
	static MyMapView mMapView;
	// static MKSearch mkSerach;
	// static LocationListener mLocationListener = null;
	// MyLocationOverlay mLocationOverlay = null; // 定位图层
	public static GeoPoint gp;
	public   GeocoderSearch geocodersearcher=new GeocoderSearch(PositionActivity.this);
	public static GeoPoint gresult;
	private String textset;
	// private static LocationClient mLocClient;

	// public static List<FuJin> fujinList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		mMapView = (MyMapView) findViewById(R.id.bmapsView);

		// mLocationOverlay = new MyLocationOverlay(this, mMapView);

		// mMapView.getOverlays().add(mLocationOverlay);

		mMapView.setBuiltInZoomControls(true);
		GeoPoint ggg = new GeoPoint((int) (30.519922 * 1e6),
				(int) (114.397054 * 1E6));// 上海体育馆
		mMapView.getController().setCenter(ggg);

		
		/*//此处在textview里显示当前所指的坐标
		new Thread(new Runnable() {
			@Override
			public void run() {
				//NavigateMap.startValue.setText(info.strAddr);
			//	NavigateMap.changeStart.setText("选择起点");

			}

		}).start();*/

	}
	
	
	
	public  void getPosition(GeoPoint g) {
//		GeoPoint g2 = new GeoPoint((int)(31.244558 * 1e6), (int)(121.506831 *
//		1e6));
			System.out.print(g.getLatitudeE6()+"  "+g.getLongitudeE6());
			
			gresult=g;
			if(Declare.type==false)
			{
				Declare.start_lat=g.getLatitudeE6();
				Declare.start_lon=g.getLongitudeE6();
			}
			else
			{
				Declare.end_lat=g.getLatitudeE6();
				Declare.end_lon=g.getLongitudeE6();
			}
				
			handler.sendEmptyMessage(0x0001);

	}
	
	
	
	  void dosearch()
	{
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				//NavigateMap.startValue.setText(info.strAddr);
			//	NavigateMap.changeStart.setText("选择起点");
				try {
					GeoPoint geoRegeocoder=gresult;
					ReGeocoderResult regeocoderResult=geocodersearcher.searchFromLocation(geoRegeocoder);
					textset=regeocoderResult.poilist.get(0).name;
					handler.sendEmptyMessage(0x0002);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		}).start();
		
	}
	
	 
	 private  Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x0001) {
				dosearch();
				
			} else if (msg.what == 0x0002) {
				
				
				if(Declare.type==false)
				{
					Declare.start_name=textset;
				
				}
				else
				{
					Declare.end_name=textset;
				
				}
				
				NavigateMap.startValue.setText(textset);
				NavigateMap.changeStart.setText("选择起点");
				
			} else if (msg.what == 0x0003) {    
				
				
			}else if(msg.what == 0x0004){
			
			}
		};
	};
	
	
	
	
	
}
