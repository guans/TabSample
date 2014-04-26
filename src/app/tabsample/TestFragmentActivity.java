package app.tabsample;

import java.util.ArrayList;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import app.tabsample.StreetView.LocListener;


public class TestFragmentActivity extends FragmentActivity implements StreetViewListener  {    
	/**
     * View Container
     */
    private ViewGroup mContainer;
    private Handler mHandler;
    
    /**
     * �־�View
     */
    private View mStreetView;
 //
    int mReqType, mReqGeoType, mReqLevel;
    LocListener mListener; // ���ܻص���Ϣ
    
    GeoPoint center=new GeoPoint((int)(30.519922 * 1E6), (int)(114.397054 * 1E6));

    /** Called when the activity is first created. */    
    public void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);    
        setContentView(R.layout.navigation);    
        
        //1111
        mContainer = (LinearLayout)findViewById(R.id.layout2);
        StreetViewShow.getInstance().showStreetView(this, center, 100, this, -170, 0);
            
//        FirstFragment firstFragment=new FirstFragment();    
//        //��Activity��ͨ�������FragmentͨѶ    
//        getFragmentManager().beginTransaction().add(android.R.id.content, firstFragment).commit();    
            
        FragmentManager fm = getSupportFragmentManager();    
     //  addShowHideListener(R.id.btn_1, fm.findFragmentById(R.id.firstFragment));    
       //addShowHideListener(R.id.btn_2, fm.findFragmentById(R.id.secondFragment));   
     
        
        getFragmentManager().beginTransaction().add(android.R.id.firstFragment, firstFragment).commit();    

        
           
    }    
        
    void addShowHideListener(int buttonId, final Fragment fragment) {    
        final Button button = (Button)findViewById(buttonId);    
        button.setOnClickListener(new OnClickListener() {    
            public void onClick(View v) {    
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();    
                //ΪFragment���õ��뵭��Ч��    
                ft.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);    
                            
                if (fragment.isHidden()) {    
                    ft.show(fragment);    	
                    button.setText("����");    
                } else {    
                    ft.hide(fragment);    
                    button.setText("��ʾ");    
                }    
                ft.commit();    
            }    
        });    
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

    public void onNetError() {
        // ���������
    }

    public void onDataError() {
        // �������ݴ�����
    }

    CustomerOverlay overlay;
    
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

	public void onLoaded() {
		runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	mStreetView.setVisibility(View.VISIBLE);
            }
        });
	}

    public void onAuthFail() {
        // ��֤ʧ��
    }
}   