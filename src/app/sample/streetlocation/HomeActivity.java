package app.sample.streetlocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.streetlocation.R;
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
import com.tencent.tencentmap.mapsdk.map.RouteOverlay;
import com.tencent.tencentmap.mapsdk.route.QPlaceInfo;
import com.tencent.tencentmap.mapsdk.route.QRouteSearchResult;
import com.tencent.tencentmap.mapsdk.route.RouteSearch;
import com.tencent.tencentmap.mapsdk.search.GeocoderSearch;
import com.tencent.tencentmap.mapsdk.search.PoiItem;
import com.tencent.tencentmap.mapsdk.search.PoiResults;
import com.tencent.tencentmap.mapsdk.search.PoiSearch;
import com.tencent.tencentmap.mapsdk.search.ReGeocoderResult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
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
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import app.sample.streetlocation.MapOverlay.OnTapListener;
import app.sample.streetlocation.RouteSearcher.MyBaseAdapter;
import app.sample.streetlocation.constant.Settings;
import app.sample.streetlocation.entity.Declare;
import app.sample.streetlocation.entity.TestPoint;

public class HomeActivity extends MapActivity {

	QRouteSearchResult busrouteresult = null;
	RouteOverlay busRouteOverlay = null;

	// ��ǰ״̬����
	public static int state = Settings.NORMAL; // ��ǰ״̬ΪĬ��
	// ��עȫ����

	GeoPoint stpoint;

	// �㲥���͵�ǰλ��
	private String ACTION = "LOCATION_CHANGE_ACTION";

	MapOverlay mapOverlay = null;
	int iTipTranslateX = 0;
	int iTipTranslateY = 0;
	View viewTip = null; // ��ע��ͼ

	MapView mMapView;
	MapController mMapController;

	Button btnTraffic = null;
	Button btnAnimationTo = null;
	Button btnZoomSatellite = null;

	LocListener mListener; // ���ܻص���Ϣ
	PowerManager.WakeLock mWakeLock; // ������
	double x = 0; // Ĭ�Ͼ�γ��
	double y = 0;
	int mReqType, mReqGeoType, mReqLevel; // ��λ����
	RadioGroup mEditReqGeoType;
	RadioGroup mEditReqLevel;

	boolean longClick = false;// ������־λ

	private List<GeoPoint> listPts;

	// ���߳��и���UI�õ�handler
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x0001) {

				if (busRouteOverlay == null) {
					busRouteOverlay = new RouteOverlay();
					mMapView.addOverlay(busRouteOverlay);
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

				GeoPoint geoPtLeftUp = null;
				GeoPoint geoPtRightDown = null; // ��ȡ·��������Ͻǵ㣬�����½ǵ�

				GeoPoint geoPt = null;
				for (int i = 0; i < iPtSize; i++) {
					geoPt = listPts.get(i);
					if (geoPt == null) {
						continue;
					}

					if (geoPtLeftUp == null) {
						geoPtLeftUp = new GeoPoint(geoPt.getLatitudeE6(),
								geoPt.getLongitudeE6());
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
						geoPtRightDown = new GeoPoint(geoPt.getLatitudeE6(),
								geoPt.getLongitudeE6());
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
				mMapView.getController()
						.zoomToSpan(geoPtLeftUp, geoPtRightDown);

				mMapView.getController().setCenter(geoPtLeftUp);
				mMapView.getController().setZoom(9);
				//mMapView.setBuiltInZoomControls(false);

			} else if (msg.what == 0x0002) {

			} else if (msg.what == 0x0003) {

			} else if (msg.what == 0x0004) {

			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);

		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"LBSAPI");
		
		// ��õ�ͼ����Ϳ���
		mMapView = (MapView) findViewById(R.id.maptest);
		mMapView.setBuiltInZoomControls(true); // �����������õ����ſؼ�
		mMapController = mMapView.getController();

		// ��ʾ��ǰλ�ã������ü�����
		mReqGeoType = TencentMapLBSApi.GEO_TYPE_WGS84;
		mReqLevel = TencentMapLBSApi.LEVEL_ADMIN_AREA;

		mListener = new LocListener(mReqGeoType, mReqLevel, 1);

		// ע��, manifest �ļ��������� key

		// ��Ӷ�λ������
		int req = TencentMapLBSApi.getInstance().requestLocationUpdate(
				HomeActivity.this.getApplicationContext(), mListener);
		Log.e("REQLOC", "res: " + req);
		// ���ü���Ƶ�� ��ʱ1��
		TencentMapLBSApi.getInstance().setGPSUpdateInterval(1000);

		if (req == -2) {
			// mTextRes.setText("Key����ȷ. ����manifest�ļ���������ȷ��Key");
		}

		// ��ע��
		// ��̬����Ľ���
		LayoutInflater layoutInfla = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		viewTip = layoutInfla.inflate(R.layout.layouttipview, null);

		Drawable marker = getResources().getDrawable(R.drawable.markpoint); // �õ���Ҫ���ڵ�ͼ�ϵ���Դ

		this.iTipTranslateY = marker.getIntrinsicHeight();

		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight()); // Ϊmaker����λ�úͱ߽�

		mapOverlay = new MapOverlay(marker, this);

		mapOverlay.setOnTapListener(onTapListener);
		mMapView.addOverlay(mapOverlay); // ��ӱ�ע������ͨ��mMapView.getOverlays().removeɾ����ע��ɾ�������ͨ��mapview.refreshMap()ˢ�µ�ͼ
											// ���ItemizedOverlayʵ����mMapView
		// mMapView.invalidate(); //ˢ�µ�ͼ

		// ��ʾ��ͨ
		btnTraffic = (Button) this.findViewById(R.id.jiaotong);
		btnTraffic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean boTraffic = mMapView.isTraffic();
				if (boTraffic == false) {
					int iCurrentLevel = mMapView.getZoomLevel();
					if (iCurrentLevel < 10) // ʵʱ��ͨ��10�����ϲ���ʾ
					{
						mMapView.getController().setZoom(10);
					}
					mMapView.setTraffic(true);
				} else {
					mMapView.setTraffic(false);
				}
			}
		});
		// ���ٵ�ǰλ��
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
						x = locRes.Altitude;
						y = locRes.Latitude;
						// ȫ�ֱ�������
						Declare.UserLat = x;
						Declare.UserLon = y;

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
				// ��ʾ��ǰ������
				new Thread(new Runnable() {
					@Override
					public void run() {

						Bitmap bmpMarker = null;
						Resources res = HomeActivity.this.getResources();
						bmpMarker = BitmapFactory.decodeResource(res,
								R.drawable.mark_location);

						SimulateLocationOverlay simuOvelay = new SimulateLocationOverlay(
								bmpMarker);
						mMapView.addOverlay(simuOvelay);

						GeoPoint geoSimulateLocation = new GeoPoint(
								(int) (x * 1e6), (int) (y * 1e6));
						simuOvelay.setGeoCoords(geoSimulateLocation);
						simuOvelay.setAccuracy(5000);

					}
				}).start();
			}
		});

		// ��ʾ��ע���ȫ��ͼ
		Button stview = (Button) findViewById(R.id.streetview);
		stview.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			public void onClick(View arg0) {

				if (mapOverlay.getFocus() != null) {
					// ������ı�ǩ��ʾȫ��
					TestPoint test = new TestPoint();
					test.altitude = (int) (mapOverlay.getFocus().getPoint()
							.getLatitudeE6());
					test.latitude = (int) (mapOverlay.getFocus().getPoint()
							.getLongitudeE6());

					Bundle bundle = new Bundle();
					bundle.putSerializable("stview", test);
					Intent intent = new Intent(HomeActivity.this,
							testview.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});

		// �Ƴ�������
		// TencentMapLBSApi.getInstance().removeLocationUpdate();
		// �����ǵ�ͼ
		btnZoomSatellite = (Button) this.findViewById(R.id.weixing);
		btnZoomSatellite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean boSatellite = mMapView.isSatellite();
				if (boSatellite == true) {
					mMapView.setSatellite(false);
					btnZoomSatellite.setText("������Ӱ��");
				} else {
					mMapView.setSatellite(true);
					btnZoomSatellite.setText("�ر�����Ӱ��");
				}
			}
		});

		// ������������ ����API��ʾ��������POI
		new Thread(new Runnable() {
			@Override
			public void run() {
				Intent intent = getIntent();

				Bundle bundle = intent.getExtras();
				if (bundle != null
						&& (bundle.getSerializable("flag").equals("����"))) // ��������·��
				{
					RouteSearch routeSearch = new RouteSearch(HomeActivity.this);
					QPlaceInfo placeStart = new QPlaceInfo();
					placeStart.point = new GeoPoint((int) (Declare.start_lat),
							(int) (Declare.start_lon));
					QPlaceInfo placeEnd = new QPlaceInfo();
					placeEnd.point = new GeoPoint((int) (Declare.end_lat),
							(int) (Declare.end_lon));

					QRouteSearchResult busRouteResult = null;
					try {
						busRouteResult = routeSearch.searchBusRoute("�人",
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
				if (bundle != null
						&& bundle.getSerializable("flag").equals("����")) {

					String dest = "181";
					int lat = bundle.getInt("nlat");
					int lon = bundle.getInt("nlon");

					if (lat != 0 && lon != 0) {

						Bitmap bmpMarker = null;
						Resources res = HomeActivity.this.getResources();
						bmpMarker = BitmapFactory.decodeResource(res,
								R.drawable.mark_location);

						SimulateLocationOverlay simuOvelay = new SimulateLocationOverlay(
								bmpMarker);
						mMapView.addOverlay(simuOvelay);

						GeoPoint geoSimulateLocation = new GeoPoint(
								(int) (lat), (int) (lon));
						simuOvelay.setGeoCoords(geoSimulateLocation);
						simuOvelay.setAccuracy(5000);

						mMapView.getController().animateTo(geoSimulateLocation);
						mMapController.setZoom(17);
						mMapView.refreshMap();
					}
				}
							
				
			}
		}).start();
	
		mMapView.setBuiltInZoomControls(true); // �����������õ����ſؼ�
		mMapController = mMapView.getController(); // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		mMapController.setZoom(9);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mWakeLock!=null)
		{
			try{
		mWakeLock.release();
			}catch(Exception e)
			{
				
			}
		}
		// ɾ����λ������
		TencentMapLBSApi.getInstance().removeLocationUpdate();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mWakeLock!=null)
		{
			try{
		mWakeLock.release();
			}catch(Exception e)
			{
				
			}
		}
		// ɾ����λ������
		TencentMapLBSApi.getInstance().removeLocationUpdate();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mWakeLock.acquire();
	}

	OnTapListener onTapListener = new OnTapListener() {

		@Override
		public void onTap(OverlayItem itemTap) {
			// TODO Auto-generated method stub
			if (viewTip == null || itemTap == null) {
				return;
			}
			MapView.LayoutParams layParaOntap = new MapView.LayoutParams(
					MapView.LayoutParams.WRAP_CONTENT,
					MapView.LayoutParams.WRAP_CONTENT, itemTap.getPoint(),
					iTipTranslateX, -iTipTranslateY,
					MapView.LayoutParams.BOTTOM_CENTER);
			if (mMapView.indexOfChild(viewTip) == -1) {
				mMapView.addView(viewTip, layParaOntap);
			} else {
				mMapView.updateViewLayout(viewTip, layParaOntap);
			}
		}

		@Override
		public void onEmptyTap(GeoPoint pt) {
			// TODO Auto-generated method stub
			if (mMapView.indexOfChild(viewTip) >= 0) {
				mMapView.removeView(viewTip);
			}
		}
	};

	class LocListener extends TencentMapLBSApiListener {

		public LocListener(int reqGeoType, int reqLevel, int reqDelay) {
			super(reqGeoType, reqLevel, reqDelay);
		}

		@Override
		public void onLocationUpdate(TencentMapLBSApiResult locRes) {

			x = locRes.Latitude;
			y = locRes.Longitude;
			// ȫ�ֱ�������
			Declare.UserLat = x;
			Declare.UserLon = y;

		}

	}
	
}


//��ǰ�������
	class SimulateLocationOverlay extends Overlay {

		GeoPoint geoPoint;
		Bitmap bmpMarker;
		float fAccuracy = 0f;

		public SimulateLocationOverlay(Bitmap mMarker) {
			bmpMarker = mMarker;
		}

		public void setGeoCoords(GeoPoint geoSimulateLoc) {
			if (geoPoint == null) {
				geoPoint = new GeoPoint(geoSimulateLoc.getLatitudeE6(),
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

			float fRadius = mapProjection.metersToEquatorPixels(fAccuracy);

			// �˴���һ���⣬��Ϊ̫������Ⱦ �����
			// canvas.drawCircle(ptMap.x, ptMap.y, fRadius, paint);
			paint.setStyle(Style.STROKE);
			paint.setAlpha(200);
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

class MapOverlay extends ItemizedOverlay<OverlayItem> {
	private List<OverlayItem> itemList = new ArrayList<OverlayItem>();
	// private Context mContext;
	private OnTapListener tapListener = null;

	private double mLat1 = 39.911766; // point1γ��
	private double mLon1 = 116.305456; // point1����

	private double mLat2 = 39.80233;
	private double mLon2 = 116.397741;

	private double mLat3 = 30.519922;
	private double mLon3 = 114.397054;

	public MapOverlay(Drawable marker, Context context) {
		super(boundCenterBottom(marker));
		// �ø����ľ�γ�ȹ���GeoPoint����λ��΢�� (�� * 1E6)
		GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
		GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
		GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));

		// ����OverlayItem��������������Ϊ��item��λ�ã������ı�������Ƭ��
		itemList.add(new OverlayItem(p1, "1", "��ѡ�е�һ����"));
		OverlayItem itemNntDrag = new OverlayItem(p2, "2", "�õ��޷���ק");
		itemNntDrag.setDragable(false);
		itemList.add(itemNntDrag);
		itemList.add(new OverlayItem(p3, "3", "��ѡ�е�������"));
		populate(); // createItem(int)��������item��һ���������ݣ��ڵ�����������ǰ�����ȵ����������
	}

	@Override
	public void draw(Canvas canvas, MapView mapView) {

		// Projection�ӿ�������Ļ���ص�����ϵͳ�͵�����澭γ�ȵ�����ϵͳ֮��ı任
		Projection projection = mapView.getProjection();
		for (int index = size() - 1; index >= 0; index--) { // ����GeoList
			OverlayItem overLayItem = getItem(index); // �õ�����������item

			String title = overLayItem.getTitle();
			// �Ѿ�γ�ȱ任�������MapView���Ͻǵ���Ļ��������
			Point point = projection.toPixels(overLayItem.getPoint(), null);

			Paint paintCircle = new Paint();
			paintCircle.setColor(Color.RED);
			canvas.drawCircle(point.x, point.y, 5, paintCircle); // ��Բ

			Paint paintText = new Paint();
			paintText.setColor(Color.BLACK);
			paintText.setTextSize(15);
			canvas.drawText(title, point.x, point.y - 25, paintText); // �����ı�

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
		OverlayItem itemSelect = itemList.get(i);
		setFocus(itemSelect);
		if (tapListener != null) {
			tapListener.onTap(itemSelect);
		}
		return true;
	}

	@Override
	public void onEmptyTap(GeoPoint pt) {
		// TODO Auto-generated method stub
		super.onEmptyTap(pt);

		if (tapListener != null) {
			tapListener.onEmptyTap(pt);
		}
	}

	interface OnTapListener {
		void onTap(OverlayItem itemTap);

		void onEmptyTap(GeoPoint pt);
	}

	public void setOnTapListener(OnTapListener listnerTap) {
		tapListener = listnerTap;
	}
}