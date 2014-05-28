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

//�������ܣ�ͬʱʵ�ֶ�λ
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
		boolean GPS_status = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);// ����ֻ��ǲ���������GPS����״̬true��gps������false��GPSδ����
		boolean NETWORK_status = lm
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);// ��һ��Gpsprovider��Google��·��ͼ��
		 status = "";
		if (GPS_status) {
			status += "GPS����";
		} else {
			status += "GPSδ����";
			// return false;
		}
		if (NETWORK_status) {
			status += "NETWORK ����";
		} else {
			status += "NETWORK δ����";
		}
		
		handler.sendEmptyMessage(0x0001);
	}

	private void initGPS() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// �ж�GPSģ���Ƿ��������û������
		if (!locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(GetLocationActivity.this, "GPS ��λû�п���,���!",
					Toast.LENGTH_SHORT).show();
			// ת���ֻ����ý��棬�û�����GPS
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 0); // ������ɺ󷵻ص�ԭ���Ľ���
		} else {
			
			handler.sendEmptyMessage(0x0002);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		/* ��ʱ3�� */
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
	
	
	
	//���̸߳���UI
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x0001) {
				// �����Ի���
				new AlertDialog.Builder(GetLocationActivity.this)
						.setMessage("" + status).setPositiveButton("OK", null).show();
				// ����Toast
				Toast.makeText(GetLocationActivity.this, status, Toast.LENGTH_LONG)
						.show();
			} else if (msg.what == 0x0002) {
				Toast.makeText(GetLocationActivity.this, "GPS �Ѿ�����",
						Toast.LENGTH_LONG).show();
				// �����Ի���
				new AlertDialog.Builder(GetLocationActivity.this).setMessage("GPS �Ѿ�����")
						.setPositiveButton("OK", null).show();
				
				
				
			} else if (msg.what == 0x0003) {    
				
				
			}else if(msg.what == 0x0004){
			
			}
		};
	};
	
	
}