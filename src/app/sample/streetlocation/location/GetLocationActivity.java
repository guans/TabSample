package app.sample.streetlocation.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;
import app.sample.streetlocation.AwesomeActivity;
import app.sample.streetlocation.GetNavStart;


import com.example.streetlocation.R;

import com.tencent.tencentmap.lbssdk.TencentMapLBSApi;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiListener;
import com.tencent.tencentmap.lbssdk.TencentMapLBSApiResult;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;

//闪屏功能，同时实现定位
public class GetLocationActivity extends Activity {

	
	private String status="";
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 0) {

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private long m_dwSplashTime = 3000;
	private boolean m_bPaused = false;
	private boolean m_bSplashActive = true;
	private Handler x = new Handler();

	public void isGpsOpen() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean GPS_status = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);// 获得手机是不是设置了GPS开启状态true：gps开启，false：GPS未开启
		boolean NETWORK_status = lm
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);// 另一种Gpsprovider（Google网路地图）
		 status = "";
		if (GPS_status) {
			status += "GPS开启";
		} else {
			status += "GPS未开启";
			// return false;
		}
		if (NETWORK_status) {
			status += "NETWORK 开启";
		} else {
			status += "NETWORK 未开启";
		}
		
		handler.sendEmptyMessage(0x0001);
	}

	private void initGPS() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// 判断GPS模块是否开启，如果没有则开启
		if (!locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(GetLocationActivity.this, "GPS 定位没有开启,请打开!",
					Toast.LENGTH_SHORT).show();
			// 转到手机设置界面，用户设置GPS
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
		} else {
			
			handler.sendEmptyMessage(0x0002);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		/* 延时3秒 */
		x.postDelayed(new splashhandler(), 3000);

	}

	class splashhandler implements Runnable {

		public void run() {
			startActivity(new Intent(GetLocationActivity.this,
					AwesomeActivity.class));
			initGPS();
			isGpsOpen();
			x.removeCallbacks(this);
			GetLocationActivity.this.finish();

		}

	}
	
	
	
	//主线程更新UI
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x0001) {
				// 弹出对话框
				new AlertDialog.Builder(GetLocationActivity.this)
						.setMessage("" + status).setPositiveButton("OK", null).show();
				// 弹出Toast
				Toast.makeText(GetLocationActivity.this, status, Toast.LENGTH_LONG)
						.show();
			} else if (msg.what == 0x0002) {
				Toast.makeText(GetLocationActivity.this, "GPS 已经开启",
						Toast.LENGTH_LONG).show();
				// 弹出对话框
				new AlertDialog.Builder(GetLocationActivity.this).setMessage("GPS 已经开启")
						.setPositiveButton("OK", null).show();
				
				
				
			} else if (msg.what == 0x0003) {    
				
				
			}else if(msg.what == 0x0004){
			
			}
		};
	};
	
	
}