package app.tabsample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tencent.tencentmap.lbssdk.TencentMapLBSApi;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiListener;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiResult;
import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.Overlay;
import com.tencent.tencentmap.mapsdk.map.OverlayItem;
import com.tencent.tencentmap.mapsdk.map.PoiOverlay;
import com.tencent.tencentmap.mapsdk.map.Projection;
import com.tencent.tencentmap.mapsdk.search.PoiItem;
import com.tencent.tencentmap.mapsdk.search.PoiResults;
import com.tencent.tencentmap.mapsdk.search.PoiSearch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Adil Soomro
 * 
 */
public class HomeActivity extends MapActivity   {

	
	MapView mMapView;
	MapController mMapController;

	Button btnTraffic = null;
	Button btnAnimationTo = null;
	Button btnZoomSatellite = null;

	LocListener mListener; // 接受回调信息
	PowerManager.WakeLock mWakeLock; // 监视器
	double x = 30.519922;
	double y = 114.397054;
	int mReqType, mReqGeoType, mReqLevel;
	RadioGroup mEditReqGeoType;
	RadioGroup mEditReqLevel;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		

		// 显示交通
		mMapView = (MapView) findViewById(R.id.maptest);
		btnTraffic = (Button) this.findViewById(R.id.jiaotong);
		btnTraffic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean boTraffic = mMapView.isTraffic();
				if (boTraffic == false) {
					int iCurrentLevel = mMapView.getZoomLevel();
					if (iCurrentLevel < 10) // 实时交通在10级以上才显示
					{
						mMapView.getController().setZoom(10);
					}
					mMapView.setTraffic(true);
				} else {
					mMapView.setTraffic(false);
				}
			}
		});
		
		

		// 显示当前位置
		
		
		mReqGeoType = TencentMapLBSApi.GEO_TYPE_WGS84;
		mReqLevel = TencentMapLBSApi.LEVEL_ADMIN_AREA;

		mListener = new LocListener(mReqGeoType, mReqLevel, 1);

		// 注意, manifest 文件中已配置 key

		// 添加定位监听器
		int req = TencentMapLBSApi.getInstance().requestLocationUpdate(
				HomeActivity.this.getApplicationContext(), mListener);
		Log.e("REQLOC", "res: " + req);
		TencentMapLBSApi.getInstance().setGPSUpdateInterval(1000);

		if (req == -2) {
			// mTextRes.setText("Key不正确. 请在manifest文件中设置正确的Key");
		}
		
		
		
		
		Button btn = (Button) findViewById(R.id.follow);
		btn.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			public void onClick(View arg0) {
				
				class LocListener extends TencentMapLBSApiListener {
					
					public LocListener(int reqGeoType, int reqLevel,
							int reqDelay) {
						super(reqGeoType, reqLevel, reqDelay);
					}

					@Override
					public void onLocationUpdate(TencentMapLBSApiResult locRes) {
						x=locRes.Altitude;
						y=locRes.Latitude;
					}

				}
				
				
				GeoPoint ge = new GeoPoint((int) (x * 1E6), (int) (y * 1E6));
				Runnable runAnimate = new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(HomeActivity.this, "animation finish",
								Toast.LENGTH_LONG).show();
					}
				};
				mMapView.getController().animateTo(ge, runAnimate);
				
			}
		});

		
		
		
		

		// 移除监视器
		//TencentMapLBSApi.getInstance().removeLocationUpdate();

		// 打开卫星地图
		btnZoomSatellite = (Button) this.findViewById(R.id.weixing);
		btnZoomSatellite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean boSatellite = mMapView.isSatellite();
				if (boSatellite == true) {
					mMapView.setSatellite(false);
					btnZoomSatellite.setText("打开卫星影像");
				} else {
					mMapView.setSatellite(true);
					btnZoomSatellite.setText("关闭卫星影像");
				}
			}
		});
/*
		Intent intent = getIntent();

		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			String dest = (String) bundle.getSerializable("dest");
			if (!dest.equals("") && dest != null) {

				PoiSearch poiSearch = new PoiSearch(HomeActivity.this);
				poiSearch.setPageCapacity(15);
				poiSearch.setPageNumber(0);

				try {

					GeoPoint geoCenter = new GeoPoint((int) (30.519922 * 1e6),
							(int) (114.397054 * 1e6));
					PoiResults poiResult = poiSearch.searchPoiInCircle(dest,
							geoCenter, 1000);

					List<PoiItem> listPois = poiResult.getCurrentPagePoiItems();
					if (listPois == null) {
						return;
					}
					PoiOverlay myPoiOverlay = new PoiOverlay(null);
					mMapView.addOverlay(myPoiOverlay);
					myPoiOverlay.setPoiItems(listPois);
					myPoiOverlay.showInfoWindow(0);

				} catch (Exception e) {
					Log.e("Error", "错误: " + e);
				}

			}
		}
		
		
		*/
		// 接受搜索参数 调用API显示搜索到的POI
        new Thread(new Runnable(){
            @Override
            public void run() {
            	Intent intent = getIntent();

        		Bundle bundle = intent.getExtras();
        		if (bundle != null) {
        			String dest = (String) bundle.getSerializable("dest");
        			if (!dest.equals("") && dest != null) {

        				PoiSearch poiSearch = new PoiSearch(HomeActivity.this);
        				poiSearch.setPageCapacity(15);
        				poiSearch.setPageNumber(0);

        				try {

        					GeoPoint geoCenter = new GeoPoint((int) (30.519922 * 1e6),
        							(int) (114.397054 * 1e6));
        					PoiResults poiResult = poiSearch.searchPoiInCircle(dest,
        							geoCenter, 1000);

        					List<PoiItem> listPois = poiResult.getCurrentPagePoiItems();
        					if (listPois == null) {
        						return;
        					}
        					PoiOverlay myPoiOverlay = new PoiOverlay(null);
        					mMapView.addOverlay(myPoiOverlay);
        					myPoiOverlay.setPoiItems(listPois);
        					myPoiOverlay.showInfoWindow(9);

        				} catch (Exception e) {
        					Log.e("Error", "错误: " + e);
        				}

        			}
        		}
            }
        }).start();
        
        
     // 显示当前点坐标
        new Thread(new Runnable(){
            @Override
            public void run() {
            	
            	Bitmap bmpMarker=null;
        		Resources res=HomeActivity.this.getResources();
        		bmpMarker=BitmapFactory.decodeResource(res, R.drawable.mark_location);
        		
            	SimulateLocationOverlay simuOvelay=new SimulateLocationOverlay(bmpMarker);
        		mMapView.addOverlay(simuOvelay);
        		
        		GeoPoint geoSimulateLocation=new GeoPoint((int)(x*1e6), (int)(y*1e6));
        		simuOvelay.setGeoCoords(geoSimulateLocation);
        		simuOvelay.setAccuracy(5000);
   
        				}
        }).start();
        

		
		mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
		mMapController = mMapView.getController(); // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		mMapController.setZoom(9);

	}

	class LocListener extends TencentMapLBSApiListener {

		public LocListener(int reqGeoType, int reqLevel, int reqDelay) {
			super(reqGeoType, reqLevel, reqDelay);
		}

		@Override
		public void onLocationUpdate(TencentMapLBSApiResult locRes) {
			// String res = locResToString(locRes);

			// String date = (new Date()).toLocaleString();
			// mTextRes.setText(date + "\n" + res);
			x = locRes.Latitude;
			y = locRes.Longitude;
		}

	}
	
	//当前坐标点类
	class SimulateLocationOverlay extends Overlay {
		
		GeoPoint geoPoint;
		Bitmap bmpMarker;
		float fAccuracy=0f;
		

		public SimulateLocationOverlay(Bitmap mMarker) {
		    bmpMarker = mMarker;
		}
		
		public void setGeoCoords(GeoPoint geoSimulateLoc)
		{
			if(geoPoint==null)
			{
				geoPoint=new GeoPoint(geoSimulateLoc.getLatitudeE6(),geoSimulateLoc.getLongitudeE6());
			}
			else
			{
				geoPoint.setLatitudeE6(geoSimulateLoc.getLatitudeE6());
				geoPoint.setLongitudeE6(geoSimulateLoc.getLongitudeE6());
			}
		}
		
		public void setAccuracy(float fAccur)
		{
			fAccuracy=fAccur;
		}

		@Override
		public void draw(Canvas canvas, MapView mapView) {
			if(geoPoint==null)
			{
				return;
			}
			Projection mapProjection = mapView.getProjection();
			Paint paint = new Paint();
			Point ptMap = mapProjection.toPixels(geoPoint, null);
			paint.setColor(Color.BLUE);
			paint.setAlpha(8);
			paint.setAntiAlias(true);

			float fRadius=mapProjection.metersToEquatorPixels(fAccuracy);
			canvas.drawCircle(ptMap.x, ptMap.y, fRadius, paint);
			paint.setStyle(Style.STROKE);
			paint.setAlpha(200);
			canvas.drawCircle(ptMap.x, ptMap.y, fRadius, paint);

			if(bmpMarker!=null)
			{
				paint.setAlpha(255);
				canvas.drawBitmap(bmpMarker, ptMap.x - bmpMarker.getWidth() / 2, ptMap.y
						- bmpMarker.getHeight() / 2, paint);
			}
			
			super.draw(canvas, mapView);
		}
	}

	

}