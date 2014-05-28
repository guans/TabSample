package app.sample.streetlocation;

import java.util.ArrayList;
import java.util.List;

import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.Overlay;
import com.tencent.tencentmap.mapsdk.map.PoiOverlay;
import com.tencent.tencentmap.mapsdk.map.Projection;
import com.tencent.tencentmap.mapsdk.map.RouteOverlay;
import com.tencent.tencentmap.mapsdk.route.QPlaceInfo;
import com.tencent.tencentmap.mapsdk.route.QRouteSearchResult;
import com.tencent.tencentmap.mapsdk.route.RouteSearch;
import com.tencent.tencentmap.mapsdk.search.PoiItem;
import com.tencent.tencentmap.mapsdk.search.PoiResults;
import com.tencent.tencentmap.mapsdk.search.PoiSearch;
import com.tencent.tencentmap.streetviewsdk.StreetViewListener;
import com.tencent.tencentmap.streetviewsdk.StreetViewShow;

import com.tencent.tencentmap.streetviewsdk.map.basemap.GeoPoint;
import com.tencent.tencentmap.streetviewsdk.overlay.ItemizedOverlay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import app.sample.streetlocation.StreetView.LocListener;
import app.sample.streetlocation.entity.CustomPoiData;
import app.sample.streetlocation.entity.Declare;
import app.sample.streetlocation.entity.TestPoint;
import com.example.streetlocation.R;

public class TestFragmentActivity extends FragmentActivity implements
		StreetViewListener {

	QRouteSearchResult busrouteresult = null;
	RouteOverlay busRouteOverlay = null;
	private List<com.tencent.tencentmap.mapsdk.map.GeoPoint> listPts;
	// 接受当前坐标
	private BroadcastReceiver receiver;
	private String ACTION = "LOCATION_CHANGE_ACTION";
	/**
	 * View Container
	 */
	private ViewGroup mContainer;
	private Handler mHandler;
	private View mStreetView2;
	// Handler handler=new Handler();
	Runnable r;

	com.tencent.tencentmap.mapsdk.map.GeoPoint isdraw = new com.tencent.tencentmap.mapsdk.map.GeoPoint(
			0, 0);
	/**
	 * 街景View
	 */
	private View mStreetView;
	//
	int mReqType, mReqGeoType, mReqLevel;
	LocListener mListener; // 接受回调信息
	boolean longClick = false;// 长按标志位

	GeoPoint center = new GeoPoint((int) (30.519922 * 1E6),
			(int) (114.397054 * 1E6));

	// 主线程中更新UI用的handler
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x0001) {

				FirstFragment fragment = (FirstFragment) getSupportFragmentManager()
						.findFragmentById(R.id.firstFragment);
				if (busRouteOverlay == null) {
					busRouteOverlay = new RouteOverlay();
					fragment.mMapView.addOverlay(busRouteOverlay);
				}

				busRouteOverlay
						.setBusRouteInfo(busrouteresult.busRoutePlanInfo.routeList
								.get(0));

				busRouteOverlay.showInfoWindow(0);

				listPts = busrouteresult.busRoutePlanInfo.routeList.get(0).routeNodeList;

				if (listPts == null) {
					return;
				}
				int iPtSize = listPts.size();
				if (iPtSize <= 0) {
					return;
				}

				com.tencent.tencentmap.mapsdk.map.GeoPoint geoPtLeftUp = null;
				com.tencent.tencentmap.mapsdk.map.GeoPoint geoPtRightDown = null; // 获取路径点的左上角点，和右下角点

				com.tencent.tencentmap.mapsdk.map.GeoPoint geoPt = null;
				for (int i = 0; i < iPtSize; i++) {
					geoPt = listPts.get(i);
					if (geoPt == null) {
						continue;
					}

					if (geoPtLeftUp == null) {
						geoPtLeftUp = new com.tencent.tencentmap.mapsdk.map.GeoPoint(
								geoPt.getLatitudeE6(), geoPt.getLongitudeE6());
					} else {
						if (geoPtLeftUp.getLatitudeE6() < geoPt.getLatitudeE6()) {
							geoPtLeftUp.setLatitudeE6(geoPt.getLatitudeE6());
						}
						if (geoPtLeftUp.getLongitudeE6() > geoPt
								.getLongitudeE6()) {
							geoPtLeftUp.setLongitudeE6(geoPt.getLongitudeE6());
						}
					}

					if (geoPtRightDown == null) {
						geoPtRightDown = new com.tencent.tencentmap.mapsdk.map.GeoPoint(
								geoPt.getLatitudeE6(), geoPt.getLongitudeE6());
					} else {
						if (geoPtRightDown.getLatitudeE6() > geoPt
								.getLatitudeE6()) {
							geoPtRightDown.setLatitudeE6(geoPt.getLatitudeE6());
						}
						if (geoPtRightDown.getLongitudeE6() < geoPt
								.getLongitudeE6()) {
							geoPtRightDown.setLongitudeE6(geoPt
									.getLongitudeE6());
						}
					}

				}

				if (geoPtLeftUp == null || geoPtRightDown == null) {
					return;
				}
				fragment.mMapView.getController().zoomToSpan(geoPtLeftUp,
						geoPtRightDown);

				fragment.mMapView.getController().setCenter(geoPtLeftUp);
				fragment.mMapView.getController().setZoom(19);
				fragment.mMapView.setBuiltInZoomControls(false);

			} else if (msg.what == 0x0002) {

				FirstFragment first = (FirstFragment) getSupportFragmentManager()
						.findFragmentById(R.id.firstFragment);

				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();

				ft.setCustomAnimations(android.R.animator.fade_in,
						android.R.animator.fade_out);

				if (first.isHidden()) {
					ft.show(first);
					// but1.setText("隐藏");
				} else {
					ft.hide(first);
					// but1.setText("显示");
				}
				ft.commit();

			} else if (msg.what == 0x0003) {

			} else if (msg.what == 0x0004) {

			}
		}
	};

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation);

		double lat = Declare.UserLat;
		double lon = Declare.UserLon;
		if (lat != 0 && lon != 0) {
			center.setLatitudeE6((int) (lat * 1E6));
			center.setLongitudeE6((int) (lon * 1E6));

		}

		// 开始导航吧

		if (!Declare.start_name.equals("")) {
			center.setLatitudeE6((int) (Declare.start_lat));
			center.setLongitudeE6((int) (Declare.start_lon));
		}

		// 接受搜索参数 调用API显示搜索到的POI
		new Thread(new Runnable() {
			@Override
			public void run() {
				Intent intent = getIntent();

				Bundle bundle = intent.getExtras();
				if (bundle != null
						&& (bundle.getSerializable("flag").equals("全景导航"))) // 画出导航路线
				{
					RouteSearch routeSearch = new RouteSearch(
							TestFragmentActivity.this);
					QPlaceInfo placeStart = new QPlaceInfo();
					placeStart.point = new com.tencent.tencentmap.mapsdk.map.GeoPoint(
							(int) (Declare.start_lat),
							(int) (Declare.start_lon));
					QPlaceInfo placeEnd = new QPlaceInfo();
					placeEnd.point = new com.tencent.tencentmap.mapsdk.map.GeoPoint(
							(int) (Declare.end_lat), (int) (Declare.end_lon));

					QRouteSearchResult busRouteResult = null;
					try {
						busRouteResult = routeSearch.searchBusRoute("武汉",
								placeStart, placeEnd);
						busrouteresult = busRouteResult;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (busRouteResult == null) {
						return;
					}

					handler.sendEmptyMessage(0x0001);

				}

			}

		}).start();

		// 定点

		FirstFragment fragment = (FirstFragment) getSupportFragmentManager()
				.findFragmentById(R.id.firstFragment);

		fragment.mMapController
				.animateTo(new com.tencent.tencentmap.mapsdk.map.GeoPoint(
						(int) (lat * 1E6), (int) (lon * 1E6)));

		// 1111
		mContainer = (LinearLayout) findViewById(R.id.layout2);
		StreetViewShow.getInstance().showStreetView(this, center, 100, this,
				-170, 0);

		// 显示第一个fragment
		if (findViewById(R.id.firstFragment) != null) {
			if (savedInstanceState != null) {
				return;
			}

		}

		// 隐藏小地图、显示小地图
		final Button but1 = (Button) this.findViewById(R.id.imageButton);
		but1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				handler.sendEmptyMessage(0x0001);
			}
			// 播放声音事件

		});
	}

	@Override
	protected void onDestroy() {
		StreetViewShow.getInstance().destory();
		handler.removeCallbacks(r);
		super.onDestroy();
	}

	@Override
	protected void onResume() {

		super.onResume();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
	}

	public void onViewReturn(final View v) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				mStreetView = v;
				mContainer.addView(mStreetView);

				Log.d("street", StreetViewShow.getInstance().getStreetStatus()
						.toString());

			}
		});
	}

	public void onNetError() {
		// 网络错误处理
	}

	public void onDataError() {
		// 解析数据错误处理
		Log.d("street", "此处没有全景");
	}

	CustomerOverlay overlay;

	public ItemizedOverlay getOverlay() {
		if (overlay == null) {
			ArrayList<CustomPoiData> pois = new ArrayList<CustomPoiData>();
			pois.add(new CustomPoiData(39984066, 116307968,
					getBm(R.drawable.poi_center),
					getBm(R.drawable.poi_center_pressed), 0));
			pois.add(new CustomPoiData(39984166, 11630800,
					getBm(R.drawable.pin_green),
					getBm(R.drawable.pin_green_pressed), 40));
			pois.add(new CustomPoiData(39984000, 116307968,
					getBm(R.drawable.pin_yellow),
					getBm(R.drawable.pin_yellow_pressed), 80));
			pois.add(new CustomPoiData(39984066, 116308088,
					getBm(R.drawable.pin_red),
					getBm(R.drawable.pin_red_pressed), 120));
			overlay = new CustomerOverlay(pois);
			overlay.populate();
		}
		return overlay;
	}

	private Bitmap getBm(int resId) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inScaled = false;

		return BitmapFactory.decodeResource(getResources(), resId, options);
	}

	public void onLoaded() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				// TODO Auto-generated method stub
				// 要做的事情，这里可以再次调用此Runnable对象，每次移动位置执行自己，从而实现每两秒实现一次的定时器操作
				int latitude = StreetViewShow.getInstance().getStreetStatus().latitudeE6;
				int langtitude = StreetViewShow.getInstance().getStreetStatus().longitudeE6;
				String s = String.format("lon=%f,lat=%f", latitude * 1E-6,
						langtitude * 1E-6);
				Log.d("全景坐标", s);

				FirstFragment fragment = (FirstFragment) getSupportFragmentManager()
						.findFragmentById(R.id.firstFragment);
				fragment.mMapController
						.animateTo(new com.tencent.tencentmap.mapsdk.map.GeoPoint(
								(int) (latitude), (int) (langtitude)));

				if (!isdraw
						.equals(new com.tencent.tencentmap.mapsdk.map.GeoPoint(
								(int) (latitude), (int) (langtitude)))) {
					Bitmap bmpMarker = null;
					Resources res = TestFragmentActivity.this.getResources();
					bmpMarker = BitmapFactory.decodeResource(res,
							R.drawable.mark_location);

					SimulateLocationOverlay simuOvelay = new SimulateLocationOverlay(
							bmpMarker);
					fragment.mMapView.addOverlay(simuOvelay);

					com.tencent.tencentmap.mapsdk.map.GeoPoint geoSimulateLocation = new com.tencent.tencentmap.mapsdk.map.GeoPoint(
							(int) (latitude), (int) (langtitude));
					simuOvelay.setGeoCoords(geoSimulateLocation);
					simuOvelay.setAccuracy(5000);

					isdraw = geoSimulateLocation;
				}

				mStreetView.setVisibility(View.VISIBLE);

			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		//在这里搞一个可以搞定方向的旋转
		/*
		ImageView img;
		img=(ImageView)findViewById(R.id.bank);
		final Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.markpoint);
		img.setImageBitmap(bitmap);

		//创建操作图片是用的matrix对象
		Matrix matrix=new Matrix();
		//缩放图片动作
		matrix.postScale(1, 1);
		//旋转图片动作
		matrix.postRotate(30,50,100);//以坐标50，100 旋转30°
		//创建新图片
		Bitmap resizedBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		//将上面创建的bitmap转换成drawable对象，使其可以使用在ImageView,ImageButton中
		BitmapDrawable bmd=new BitmapDrawable(resizedBitmap);
		img.setAdjustViewBounds(true);
		img.setImageDrawable(bmd);
		
		
		getWindow().addContentView(img,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));*/
		
		Log.d("touch", StreetViewShow.getInstance().getStreetStatus()
				.toString());
		return super.onTouchEvent(event);
	}

	public void onAuthFail() {
		// 验证失败
	}

	// 当前坐标点类
	class SimulateLocationOverlay extends Overlay {

		com.tencent.tencentmap.mapsdk.map.GeoPoint geoPoint;
		Bitmap bmpMarker;
		float fAccuracy = 0f;

		public SimulateLocationOverlay(Bitmap mMarker) {
			bmpMarker = mMarker;
		}

		public void setGeoCoords(
				com.tencent.tencentmap.mapsdk.map.GeoPoint geoSimulateLoc) {
			if (geoPoint == null) {
				geoPoint = new com.tencent.tencentmap.mapsdk.map.GeoPoint(
						geoSimulateLoc.getLatitudeE6(),
						geoSimulateLoc.getLongitudeE6());
			} else {
				geoPoint.setLatitudeE6(geoSimulateLoc.getLatitudeE6());
				geoPoint.setLongitudeE6(geoSimulateLoc.getLongitudeE6());
			}
		}

		public void setAccuracy(float fAccur) {
			fAccuracy = fAccur;
		}

		@Override
		public void draw(Canvas canvas, MapView mapView) {
			if (geoPoint == null) {
				return;
			}
			Projection mapProjection = mapView.getProjection();
			Paint paint = new Paint();
			Point ptMap = mapProjection.toPixels(geoPoint, null);
			paint.setColor(Color.BLUE);
			paint.setAlpha(8);
			paint.setAntiAlias(true);

			// float fRadius=mapProjection.metersToEquatorPixels(fAccuracy);
			// canvas.drawCircle(ptMap.x, ptMap.y, fRadius, paint);
			// paint.setStyle(Style.STROKE);
			// paint.setAlpha(200);
			// canvas.drawCircle(ptMap.x, ptMap.y, fRadius, paint);

			if (bmpMarker != null) {
				paint.setAlpha(255);
				canvas.drawBitmap(bmpMarker,
						ptMap.x - bmpMarker.getWidth() / 2,
						ptMap.y - bmpMarker.getHeight() / 2, paint);
			}

			super.draw(canvas, mapView);
		}

	}

}