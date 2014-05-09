package app.sample.streetlocation;

import com.tencent.tencentmap.streetviewsdk.StreetViewShow;
import com.tencent.tencentmap.streetviewsdk.map.basemap.GeoPoint;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import app.sample.streetlocation.StreetView.LocListener;
import com.example.streetlocation.R;

public class SecondFragment extends Fragment{    
    
	
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