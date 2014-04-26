package app.tabsample;

import java.util.ArrayList;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import app.tabsample.HomeActivity.LocListener;

import com.tencent.tencentmap.streetviewsdk.StreetThumbListener;
import com.tencent.tencentmap.streetviewsdk.StreetViewListener;
import com.tencent.tencentmap.streetviewsdk.StreetViewShow;
import com.tencent.tencentmap.streetviewsdk.map.basemap.GeoPoint;
import com.tencent.tencentmap.streetviewsdk.overlay.ItemizedOverlay;

import com.tencent.tencentmap.lbssdk.TencentMapLBSApi;  
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiListener;  
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiResult;
public class StreetView extends FragmentActivity implements StreetViewListener{
	/**
     * View Container
     */
    private ViewGroup mContainer;

    /**
     * 缩略图View
     */
    private ImageView mThumbImage;

    private Handler mHandler;
    
    /**
     * 街景View
     */
    private View mStreetView;

    
    
    //
    int mReqType, mReqGeoType, mReqLevel;
    LocListener mListener; // 接受回调信息
    
    GeoPoint center=null;
    //new GeoPoint((int)(30.519922 * 1E6), (int)(114.397054 * 1E6));
    
    
    public class LocListener extends TencentMapLBSApiListener {

  		public LocListener(int reqGeoType, int reqLevel, int reqDelay) {
  			super(reqGeoType, reqLevel, reqDelay);
  		}

  		@Override
  		public void onLocationUpdate(TencentMapLBSApiResult locRes) {
  			// String res = locResToString(locRes);

  			// String date = (new Date()).toLocaleString();
  			// mTextRes.setText(date + "\n" + res);
  			
  			
  			  center = new GeoPoint((int)(locRes.Latitude * 1E6), (int)(locRes.Longitude * 1E6));

  			//  StreetViewShow.getInstance().showStreetView(StreetView.this, center, 100, StreetView.this, -170, 0);
  		}

  	}
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.streetview_activity);
        mContainer = (LinearLayout)findViewById(R.id.layout);
        mThumbImage = (ImageView)findViewById(R.id.image);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mThumbImage.setImageBitmap((Bitmap)msg.obj);
            }
        };
                        
        // 使用经纬度获取街景
        // GeoPoint center = new GeoPoint((int)(31.216073 * 1E6),
        // (int)(121.595304 * 1E6));
        // StreetViewShow.getInstance().showStreetView(this, center, 300, this,
        // -170, 0);
        
        // 使用svid获取街景
       // StreetViewShow.getInstance().showStreetView(this, "10011026130910162137500", this, -170, 0);
        
         center = new GeoPoint((int)(30.519922 * 1E6), (int)(114.397054 * 1E6));
        StreetViewShow.getInstance().showStreetView(this, center, 100, this, -170, 0);
       
    
    }

    protected View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.streetview_activity, container, false);
    	mContainer = (LinearLayout)findViewById(R.id.layout);
        mThumbImage = (ImageView)findViewById(R.id.image);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mThumbImage.setImageBitmap((Bitmap)msg.obj);
            }
        };
        center = new GeoPoint((int)(30.519922 * 1E6), (int)(114.397054 * 1E6));
        StreetViewShow.getInstance().showStreetView(this, center, 100, this, -170, 0);
                        
       
		return v;
       
    
    }
    
    //获取当前GPS位置 并定位
    public void dofollow(View s)
    {
    	mReqGeoType = TencentMapLBSApi.GEO_TYPE_WGS84;
		mReqLevel = TencentMapLBSApi.LEVEL_ADMIN_AREA;

		mListener = new LocListener(mReqGeoType, mReqLevel, 1);

		// 注意, manifest 文件中已配置 key

		// 添加定位监听器
		int req = TencentMapLBSApi.getInstance().requestLocationUpdate(
				StreetView.this.getApplicationContext(), mListener);
		Log.e("REQLOC", "res: " + req);
		TencentMapLBSApi.getInstance().setGPSUpdateInterval(1000);

		
		if (req == -2) {
			// mTextRes.setText("Key不正确. 请在manifest文件中设置正确的Key");
		}
		
		  StreetViewShow.getInstance().showStreetView(this, center, 100, this, -170, 0);
    }
    
  

    	
    	
    @Override
    protected void onDestroy() {
    	StreetViewShow.getInstance().destory();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StreetViewShow.getInstance().requestStreetThumb("10011024120916113135600",//"10011505120412110900000",
                new StreetThumbListener() {

                    @Override
                    public void onGetThumbFail() {
                       
                    }

                    @Override
                    public void onGetThumb(Bitmap bitmap) {
                        Message msg = new Message();
                        msg.obj = bitmap;
                        mHandler.sendMessage(msg);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onViewReturn(final View v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	
            	mStreetView = v;
                mContainer.addView(mStreetView);
                Log.d("street", StreetViewShow.getInstance().getStreetStatus().toString());
            
            	
            }
        });
    }

    @Override
    public void onNetError() {
        // 网络错误处理
    }

    @Override
    public void onDataError() {
        // 解析数据错误处理
    }

    CustomerOverlay overlay;
    
    @Override
    public ItemizedOverlay getOverlay() {
        if (overlay == null) {
            ArrayList<CustomPoiData> pois = new ArrayList<CustomPoiData>();
            pois.add(new CustomPoiData(39984066, 116307968, getBm(R.drawable.poi_center),
                    getBm(R.drawable.poi_center_pressed), 0));
            pois.add(new CustomPoiData(39984166, 11630800, getBm(R.drawable.pin_green),
                    getBm(R.drawable.pin_green_pressed), 40));
            pois.add(new CustomPoiData(39984000, 116307968, getBm(R.drawable.pin_yellow),
                    getBm(R.drawable.pin_yellow_pressed), 80));
            pois.add(new CustomPoiData(39984066, 116308088, getBm(R.drawable.pin_red),
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

	@Override
	public void onLoaded() {
		runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	mStreetView.setVisibility(View.VISIBLE);
            }
        });
	}

    @Override
    public void onAuthFail() {
        // 验证失败
    }

}
