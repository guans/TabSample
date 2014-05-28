package app.sample.streetlocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.streetlocation.R;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import app.sample.streetlocation.GetNavStart.ListInfoListener;
import app.sample.streetlocation.GetNavStart.MyBaseAdapter;
import app.sample.streetlocation.constant.DatabaseConstants;
import app.sample.streetlocation.constant.DatabaseManager;
import app.sample.streetlocation.constant.POI;
import app.sample.streetlocation.entity.Declare;
import app.sample.streetlocation.entity.TestPoint;



public class HistroicActivity extends Activity implements OnClickListener, DatabaseConstants {
	
	/** �������Ľ������ */
	public int searchResultCount = 0;
	/** ����������һ��ListView�ؼ� */
	public ListView listView;
	/** listView��Ӧ������ */
//	private List<Map<String, Object>> content;
	/** ���ذ�ť��������һ������ */
	public Button clearButton;
	/**ButtonBar ����������� �յ��ѡ��*/
	public LinearLayout buttonBar;
	/**��ǰ��ѡ�е�Item*/
	public View currentItem = null;
	/**��ǰ��ѡ�е�itemλ��*/
	public int position = -1;
	/**���ݿ������*/
	public DatabaseManager databaseManager = null;
	public ArrayList<POI> pois = null;
	/* ��Activity�״δ���ʱ���� */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* �Զ�������� */
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.search_result_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.search_result_historical_titlebar);
		/*�������ݿ��POI���*/
		databaseManager = new DatabaseManager(this);
		databaseManager.createPoiTable();
		/* ��ʼ��UI���� */
		buttonBar = (LinearLayout)findViewById(R.id.search_result_button_bar);
		((Button)findViewById(R.id.search_result_start_point_button)).setOnClickListener(this);
		((Button)findViewById(R.id.search_result_end_point_button)).setOnClickListener(this);
		//��ֹ�ղذ�ť
		((Button)findViewById(R.id.search_result_show_on_button)).setOnClickListener(this);

		clearButton = (Button) findViewById(R.id.clearbutton);
			clearButton.setOnClickListener(this);
		TextView searchResultContentView = (TextView) findViewById(R.id.result_size);
		listView = (ListView) findViewById(R.id.search_result_listview);
		
		
		
		
		/*��ѯ���е��ղص�*/
		pois = databaseManager.searchHistoricalPois();
		searchResultCount = pois.size();
		//searchResultContentView.setText(searchResultCount+"����ʷ��");
		/*����Щ�����չʾ*/
		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < searchResultCount; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("poi", pois.get(i).description);
			map.put("coor", pois.get(i).coordinate.toString());
			contents.add(map);
		}
		
		
		
		
		/* ��ӳ����ӵ�Adapter */
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents,
				R.layout.search_result_listi_tem,
				new String[] { "poi", "coor" }, new int[] {
						R.id.search_result_list_poi,
						R.id.search_result_list_coor });
		listView.setAdapter(adapter);
		// ���ü���
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View item,
					int position, long arg3) {
				/*ѡ��ĳ��Item*/
				if(currentItem != null)
					currentItem.setBackgroundColor(Color.TRANSPARENT);
				HistroicActivity.this.position = position;
				currentItem = item;
				currentItem.setBackgroundColor(Color.BLUE);
				buttonBar.setVisibility(View.VISIBLE);
			}
		});
		
		
		
		
		
	}
	
	
	/* ��Activity���������������� */
	public void onStart() {
		super.onStart();
	}

	/* ��Activity��ͣʱ���� */
	protected void onPause() {
		super.onPause();
	}

	/* onStart������ */
	protected void onResume() {
		super.onResume();
	}

	/* ��Activity ����ʱ���� */
	protected void onDestroy() {
		super.onDestroy();
		if(databaseManager != null)
		databaseManager.close();
	}
	@Override
	public void onBackPressed(){
		Intent intent = new Intent();
		intent.setClass(HistroicActivity.this, GetNavStart.class);
		startActivity(intent);
		HistroicActivity.this.finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//����ղص�
		if (v.getId() == clearButton.getId()) {
            databaseManager.clearHistoricalPois();
			Intent intent = new Intent();
			intent.setClass(HistroicActivity.this, HistroicActivity.class);
			startActivity(intent);
			HistroicActivity.this.finish();
		}
		//�������
		else if(v.getId() == R.id.search_result_start_point_button){
			Declare.start_name=pois.get(position).getDescription();
			Declare.start_lat = pois.get(position).getCoordinate().altitude;
			Declare.start_lon = pois.get(position).getCoordinate().latitude;
			Intent intent = new Intent(HistroicActivity.this,
					RouteSearcher.class);
			startActivity(intent);
			finish();
		}
		//�����յ�
		else if(v.getId() == R.id.search_result_end_point_button){
			Declare.end_name=pois.get(position).getDescription();
			Declare.end_lat = pois.get(position).getCoordinate().altitude;
			Declare.end_lon = pois.get(position).getCoordinate().latitude;
			Intent intent = new Intent(HistroicActivity.this,
					RouteSearcher.class);
			startActivity(intent);
			finish();
		}
		//�ڵ�ͼ����ʾ��
		else if(v.getId() == R.id.search_result_show_on_button){/*
			 �����Ӧ��λ�� 
			Controller.getInstance().addMarkerItem(
					new OverlayItem(pois.get(position).getDescription(), pois.get(position).getCoordinate(), null));

			// ��ת��MapActivity���� 
			Intent intent = new Intent(HistoricRecordActivity.this,
					MapActivity.class);
			startActivity(intent);

			HistoricRecordActivity.this.finish();
			// ����λ�� 
			Controller.getInstance().followPosition(
					pois.get(position).getCoordinate());*/
			
			// ��ת��MapActivity���� 
			Intent intent = new Intent();
			intent.setClass(HistroicActivity.this,
					HomeActivity.class);
			intent.putExtra("nlat",(int) pois.get(position).getCoordinate().altitude);
			intent.putExtra("nlon", (int)pois.get(position).getCoordinate().latitude);
			intent.putExtra("flag", "����");
			startActivity(intent);
			finish();
		}
	}
	
	
	
	
	
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x0001) {
				
			} else if (msg.what == 0x0002) {
				
				
				
			} else if (msg.what == 0x0003) {    
				
				
			}else if(msg.what == 0x0004){
			
			}
		};
	};
	

}
