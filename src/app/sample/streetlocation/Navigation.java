package app.sample.streetlocation;

import java.util.ArrayList;

import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.streetviewsdk.StreetViewListener;
import com.tencent.tencentmap.streetviewsdk.StreetViewShow;
import com.tencent.tencentmap.streetviewsdk.map.basemap.GeoPoint;
import com.tencent.tencentmap.streetviewsdk.overlay.ItemizedOverlay;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import app.sample.streetlocation.StreetView.LocListener;
import app.sample.streetlocation.entity.CustomPoiData;
import com.example.streetlocation.R;

public class Navigation extends Activity implements StreetViewListener{
	
	/**
     * View Container
     */
    private ViewGroup mContainer;


    private Handler mHandler;
    
    /**
     * 街景View
     */
    private View mStreetView;
    
    LocListener mListener; // 接受回调信息
    
    GeoPoint center=new GeoPoint((int)(30.519922 * 1E6), (int)(114.397054 * 1E6));
    
	MapView mMapView;
	MapController mMapController;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.navigation);    
	        //获得地图对象和控制
			mMapView = (MapView) findViewById(R.id.maptest);
			
	        
			mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
			mMapController = mMapView.getController(); // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
			mMapController.setZoom(9);
			
			
			//  mContainer = (LinearLayout)findViewById(R.id.street1);
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
