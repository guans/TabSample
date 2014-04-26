package app.tabsample;

import com.tencent.tencentmap.streetviewsdk.StreetViewShow;
import com.tencent.tencentmap.streetviewsdk.map.basemap.GeoPoint;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import app.tabsample.StreetView.LocListener;

public class SecondFragment extends Fragment{    
    
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
    
    GeoPoint center=new GeoPoint((int)(30.519922 * 1E6), (int)(114.397054 * 1E6));
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
    }  
      
    public View onCreateView(LayoutInflater inflater, ViewGroup container,   
            Bundle savedInstanceState) {   
        return inflater.inflate(R.layout.second, container, false);   
    }   
}  