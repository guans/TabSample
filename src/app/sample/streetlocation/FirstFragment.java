package app.sample.streetlocation;

import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;


import android.support.v4.app.Fragment;
import android.os.Bundle;    
import android.view.ContextMenu;    
import android.view.LayoutInflater;    
import android.view.Menu;    
import android.view.MenuItem;    
import android.view.View;    
import android.view.ViewGroup;    
import android.view.ContextMenu.ContextMenuInfo;  
import com.example.streetlocation.R;

public class FirstFragment extends Fragment{    
    
	MapView mMapView;
	MapController mMapController;
	
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);    
        
    }    
      
    //绘制视图  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,     
                             Bundle savedInstanceState) {    
        View root = inflater.inflate(R.layout.first, container, false);    
        mMapView = (MapView)root.findViewById(R.id.mymap);
		mMapView.setBuiltInZoomControls(false); //设置启用内置的缩放控件
		mMapController = mMapView.getController();
		GeoPoint ge=new GeoPoint((int)(30.519922 * 1E6), (int)(114.397054 * 1E6));
		mMapView.getController().setCenter(ge) ;
		mMapView.getController().setZoom(16);
        return root;     
    }     
        
        
}    