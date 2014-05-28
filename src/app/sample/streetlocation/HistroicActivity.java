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
	
	/** 搜索到的结果数量 */
	public int searchResultCount = 0;
	/** 整个界面是一个ListView控件 */
	public ListView listView;
	/** listView对应的内容 */
//	private List<Map<String, Object>> content;
	/** 返回按钮，返回上一个界面 */
	public Button clearButton;
	/**ButtonBar 包括设置起点 终点等选项*/
	public LinearLayout buttonBar;
	/**当前被选中的Item*/
	public View currentItem = null;
	/**当前被选中的item位置*/
	public int position = -1;
	/**数据库管理类*/
	public DatabaseManager databaseManager = null;
	public ArrayList<POI> pois = null;
	/* 在Activity首次创建时调用 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 自定义标题栏 */
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.search_result_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.search_result_historical_titlebar);
		/*创建数据库和POI表格*/
		databaseManager = new DatabaseManager(this);
		databaseManager.createPoiTable();
		/* 初始化UI对象 */
		buttonBar = (LinearLayout)findViewById(R.id.search_result_button_bar);
		((Button)findViewById(R.id.search_result_start_point_button)).setOnClickListener(this);
		((Button)findViewById(R.id.search_result_end_point_button)).setOnClickListener(this);
		//禁止收藏按钮
		((Button)findViewById(R.id.search_result_show_on_button)).setOnClickListener(this);

		clearButton = (Button) findViewById(R.id.clearbutton);
			clearButton.setOnClickListener(this);
		TextView searchResultContentView = (TextView) findViewById(R.id.result_size);
		listView = (ListView) findViewById(R.id.search_result_listview);
		
		
		
		
		/*查询所有的收藏点*/
		pois = databaseManager.searchHistoricalPois();
		searchResultCount = pois.size();
		//searchResultContentView.setText(searchResultCount+"个历史点");
		/*将这些点进行展示*/
		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < searchResultCount; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("poi", pois.get(i).description);
			map.put("coor", pois.get(i).coordinate.toString());
			contents.add(map);
		}
		
		
		
		
		/* 将映射添加到Adapter */
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents,
				R.layout.search_result_listi_tem,
				new String[] { "poi", "coor" }, new int[] {
						R.id.search_result_list_poi,
						R.id.search_result_list_coor });
		listView.setAdapter(adapter);
		// 设置监听
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View item,
					int position, long arg3) {
				/*选中某项Item*/
				if(currentItem != null)
					currentItem.setBackgroundColor(Color.TRANSPARENT);
				HistroicActivity.this.position = position;
				currentItem = item;
				currentItem.setBackgroundColor(Color.BLUE);
				buttonBar.setVisibility(View.VISIBLE);
			}
		});
		
		
		
		
		
	}
	
	
	/* 在Activity创建后或重启后调用 */
	public void onStart() {
		super.onStart();
	}

	/* 在Activity暂停时调用 */
	protected void onPause() {
		super.onPause();
	}

	/* onStart后会调用 */
	protected void onResume() {
		super.onResume();
	}

	/* 在Activity 销毁时调用 */
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
		//清空收藏点
		if (v.getId() == clearButton.getId()) {
            databaseManager.clearHistoricalPois();
			Intent intent = new Intent();
			intent.setClass(HistroicActivity.this, HistroicActivity.class);
			startActivity(intent);
			HistroicActivity.this.finish();
		}
		//设置起点
		else if(v.getId() == R.id.search_result_start_point_button){
			Declare.start_name=pois.get(position).getDescription();
			Declare.start_lat = pois.get(position).getCoordinate().altitude;
			Declare.start_lon = pois.get(position).getCoordinate().latitude;
			Intent intent = new Intent(HistroicActivity.this,
					RouteSearcher.class);
			startActivity(intent);
			finish();
		}
		//设置终点
		else if(v.getId() == R.id.search_result_end_point_button){
			Declare.end_name=pois.get(position).getDescription();
			Declare.end_lat = pois.get(position).getCoordinate().altitude;
			Declare.end_lon = pois.get(position).getCoordinate().latitude;
			Intent intent = new Intent(HistroicActivity.this,
					RouteSearcher.class);
			startActivity(intent);
			finish();
		}
		//在地图上显示点
		else if(v.getId() == R.id.search_result_show_on_button){/*
			 标记相应的位置 
			Controller.getInstance().addMarkerItem(
					new OverlayItem(pois.get(position).getDescription(), pois.get(position).getCoordinate(), null));

			// 跳转到MapActivity界面 
			Intent intent = new Intent(HistoricRecordActivity.this,
					MapActivity.class);
			startActivity(intent);

			HistoricRecordActivity.this.finish();
			// 跟踪位置 
			Controller.getInstance().followPosition(
					pois.get(position).getCoordinate());*/
			
			// 跳转到MapActivity界面 
			Intent intent = new Intent();
			intent.setClass(HistroicActivity.this,
					HomeActivity.class);
			intent.putExtra("nlat",(int) pois.get(position).getCoordinate().altitude);
			intent.putExtra("nlon", (int)pois.get(position).getCoordinate().latitude);
			intent.putExtra("flag", "搜索");
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
