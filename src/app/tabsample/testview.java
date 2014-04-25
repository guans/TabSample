package app.tabsample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import app.tabsample.HomeActivity.SimulateLocationOverlay;
import app.tabsample.StreetView.LocListener;

import com.tencent.tencentmap.lbssdk.TencentMapLBSApi;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiListener;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiResult;
import com.tencent.tencentmap.mapsdk.map.PoiOverlay;
import com.tencent.tencentmap.mapsdk.search.PoiItem;
import com.tencent.tencentmap.mapsdk.search.PoiResults;
import com.tencent.tencentmap.mapsdk.search.PoiSearch;
import com.tencent.tencentmap.streetviewsdk.StreetThumbListener;
import com.tencent.tencentmap.streetviewsdk.StreetViewListener;
import com.tencent.tencentmap.streetviewsdk.StreetViewShow;
import com.tencent.tencentmap.streetviewsdk.map.basemap.GeoPoint;
import com.tencent.tencentmap.streetviewsdk.overlay.ItemizedOverlay;

public class testview extends Activity implements StreetViewListener{
	/**
     * View Container
     */
    private ViewGroup mContainer;


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
    

    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        mContainer = (LinearLayout)findViewById(R.id.layout1);
       

        Intent intent = getIntent();

		Bundle bundle = intent.getExtras();
		
		TestPoint test=(TestPoint)bundle.getSerializable("stview");
		
		center = new GeoPoint((int)(test.altitude ), (int)(test.latitude ));
	
		/*
     // 接受显示全景的经纬度信息
        new Thread(new Runnable(){
            @Override
            public void run() {
            	Intent intent = getIntent();

        		Bundle bundle = intent.getExtras();
        		if (bundle != null) {
        			
        			TestPoint test=getIntent().getParcelableExtra("test");
        			
        			center = new GeoPoint((int)(test.altitude * 1E6), (int)(test.latitude * 1E6));
        				
        			}
            }
        }).start();
        */
                        
        // 使用经纬度获取街景
        // GeoPoint center = new GeoPoint((int)(31.216073 * 1E6),
        // (int)(121.595304 * 1E6));
        // StreetViewShow.getInstance().showStreetView(this, center, 300, this,
        // -170, 0);
        
        // 使用svid获取街景
       // StreetViewShow.getInstance().showStreetView(this, "10011026130910162137500", this, -170, 0);
        
       //  center = new GeoPoint((int)(30.519922 * 1E6), (int)(114.397054 * 1E6));
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