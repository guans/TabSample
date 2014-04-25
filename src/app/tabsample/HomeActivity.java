package app.tabsample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import com.tencent.tencentmap.lbssdk.TencentMapLBSApi;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiListener;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiResult;
import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.ItemizedOverlay;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.Overlay;
import com.tencent.tencentmap.mapsdk.map.OverlayItem;
import com.tencent.tencentmap.mapsdk.map.PoiOverlay;
import com.tencent.tencentmap.mapsdk.map.Projection;
import com.tencent.tencentmap.mapsdk.search.GeocoderSearch;
import com.tencent.tencentmap.mapsdk.search.PoiItem;
import com.tencent.tencentmap.mapsdk.search.PoiResults;
import com.tencent.tencentmap.mapsdk.search.PoiSearch;
import com.tencent.tencentmap.mapsdk.search.ReGeocoderResult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import app.tabsample.MapOverlay.OnTapListener;

/**
 * @author Adil Soomro
 * 
 */

public class HomeActivity extends MapActivity   {

	
	
	//当前状态常数
	public static  int state = Settings.NORMAL;  //当前状态为默认
	//标注全景点
	
	GeoPoint stpoint;
	
	
	MapOverlay mapOverlay=null;
	int iTipTranslateX=0;
	int iTipTranslateY=0;
	View viewTip=null;          //标注视图
	
	MapView mMapView;
	MapController mMapController;

	Button btnTraffic = null;
	Button btnAnimationTo = null;
	Button btnZoomSatellite = null;

	LocListener mListener; // 接受回调信息
	PowerManager.WakeLock mWakeLock; // 监视器
	double x = 30.519922;            //默认经纬度
	double y = 114.397054;
	int mReqType, mReqGeoType, mReqLevel;   //定位参数
	RadioGroup mEditReqGeoType;
	RadioGroup mEditReqLevel;
	
	boolean longClick=false;//长按标志位

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		
        //获得地图对象和控制
		mMapView = (MapView) findViewById(R.id.maptest);
		mMapView.setBuiltInZoomControls(true); //设置启用内置的缩放控件
		mMapController = mMapView.getController();
		
		
		
		
		//响应长按 显示点击后显示经纬度
		
		mMapView.setOnLongClickListener(new View.OnLongClickListener()  {
           @Override
           public boolean onLongClick(View v) {
               longClick = true;
               return false;
           }
       });
		mMapView.setOnTouchListener(new OnTouchListener() {
            
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               //接收一次长按事件
               if(event.getAction() == MotionEvent.ACTION_UP  && longClick){
                   longClick = false;
                   int iClickX = (int)event.getX();
                   int iClickY = (int)event.getY();
                   GeoPoint geoPt = mMapView.getProjection().fromPixels(iClickX, iClickY);
                   String s =
                       String.format("lon=%f,lat=%f", geoPt.getLongitudeE6() * 1E-6, geoPt.getLatitudeE6() * 1E-6);
                   Log.d("", s);
               }
               return false;
           }
       });
		
		
		//标注类
		//动态载入的界面
		LayoutInflater layoutInfla = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		viewTip=layoutInfla.inflate(R.layout.layouttipview, null);
	
		Drawable marker = getResources().getDrawable(R.drawable.markpoint);  //得到需要标在地图上的资源
		
		this.iTipTranslateY=marker.getIntrinsicHeight();
		
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
				.getIntrinsicHeight());   //为maker定义位置和边界
		
		mapOverlay=new MapOverlay(marker, this);
		
		mapOverlay.setOnTapListener(onTapListener);
		mMapView.addOverlay(mapOverlay); //添加标注，可以通过mMapView.getOverlays().remove删除标注，删除后可以通过mapview.refreshMap()刷新地图   
		                                        //添加ItemizedOverlay实例到mMapView
		//mMapView.invalidate();  //刷新地图             
		
	
		
		// 显示交通
		
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

		//显示标注点的全景图
		Button stview = (Button) findViewById(R.id.streetview);
		stview.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			public void onClick(View arg0) {

				if(mapOverlay.getFocus()!=null)
				{
				 //最后点击的标签显示全景
				 TestPoint test=new TestPoint();
				 test.altitude=(int) (mapOverlay.getFocus().getPoint().getLatitudeE6());    
				 test.latitude=(int) (mapOverlay.getFocus().getPoint().getLongitudeE6());
				 
				 Bundle bundle =new Bundle();
			     bundle.putSerializable("stview", test);
			     Intent intent=new Intent(HomeActivity.this,testview.class);
			     intent.putExtras(bundle);
			     startActivity(intent);
				}
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

	
	
	OnTapListener onTapListener=new OnTapListener(){

		@Override
		public void onTap(OverlayItem itemTap) {
			// TODO Auto-generated method stub
			if(viewTip==null||itemTap==null)
			{
				return;
			}
			MapView.LayoutParams layParaOntap=new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,MapView.LayoutParams.WRAP_CONTENT,itemTap.getPoint(),iTipTranslateX,-iTipTranslateY,MapView.LayoutParams.BOTTOM_CENTER);
			if(mMapView.indexOfChild(viewTip)==-1)
			{
				mMapView.addView(viewTip,layParaOntap);
			}
			else
			{
				mMapView.updateViewLayout(viewTip,layParaOntap);
			}
		}

		@Override
		public void onEmptyTap(GeoPoint pt) {
			// TODO Auto-generated method stub
			if(mMapView.indexOfChild(viewTip)>=0)
			{
				mMapView.removeView(viewTip);
			}
		}};
		
		
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




class MapOverlay extends ItemizedOverlay<OverlayItem> {
	private List<OverlayItem> itemList = new ArrayList<OverlayItem>();
//	private Context mContext;
	private OnTapListener tapListener=null;

	private double mLat1 = 39.911766; // point1纬度
	private double mLon1 = 116.305456; // point1经度

	private double mLat2 = 39.80233;
	private double mLon2 = 116.397741;

	private double mLat3 = 30.519922;
	private double mLon3 = 114.397054;

	public MapOverlay(Drawable marker, Context context) {
		super(boundCenterBottom(marker));
		// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
		GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
		GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
		GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));

		
		// 构造OverlayItem的三个参数依次为：item的位置，标题文本，文字片段
		itemList.add(new OverlayItem(p1, "1", "已选中第一个点"));
		OverlayItem itemNntDrag=new OverlayItem(p2, "2", "该点无法拖拽");
		itemNntDrag.setDragable(false);
		itemList.add(itemNntDrag);
		itemList.add(new OverlayItem(p3, "3", "已选中第三个点"));		
		populate();  //createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
	}

	@Override
	public void draw(Canvas canvas, MapView mapView) {

		// Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
		Projection projection = mapView.getProjection(); 
		for (int index = size() - 1; index >= 0; index--) { // 遍历GeoList
			OverlayItem overLayItem = getItem(index); // 得到给定索引的item

			String title = overLayItem.getTitle();
			// 把经纬度变换到相对于MapView左上角的屏幕像素坐标
			Point point = projection.toPixels(overLayItem.getPoint(), null); 

			Paint paintCircle = new Paint();
			paintCircle.setColor(Color.RED);
			canvas.drawCircle(point.x, point.y, 5, paintCircle); // 画圆

			Paint paintText = new Paint();
			paintText.setColor(Color.BLACK);
			paintText.setTextSize(15);
			canvas.drawText(title, point.x, point.y - 25, paintText); // 绘制文本

		}

		super.draw(canvas, mapView);
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return itemList.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	protected boolean onTap(int i) {
		OverlayItem itemSelect=itemList.get(i);
		setFocus(itemSelect);
		if(tapListener!=null)
		{
			tapListener.onTap(itemSelect);
		}
		return true;
	}
	
	
	
	@Override
	public void onEmptyTap(GeoPoint pt) {
		// TODO Auto-generated method stub
		super.onEmptyTap(pt);
		
		if(tapListener!=null)
		{
			tapListener.onEmptyTap(pt);
		}
	}



	interface OnTapListener
	{
		void onTap(OverlayItem itemTap);
		void onEmptyTap(GeoPoint pt);
	}
	
	public void setOnTapListener(OnTapListener listnerTap)
	{
		tapListener=listnerTap;
	}
}