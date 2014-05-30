package app.sample.streetlocation;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.sample.streetlocation.SearchActivity.ListInfoListener;
import app.sample.streetlocation.SearchActivity.MyBaseAdapter;
import app.sample.streetlocation.entity.Declare;

import com.example.streetlocation.R;
import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.route.QDriveRouteInfo;
import com.tencent.tencentmap.mapsdk.route.QDriveRouteSegment;
import com.tencent.tencentmap.mapsdk.route.QPlaceInfo;
import com.tencent.tencentmap.mapsdk.route.QRouteSearchResult;
import com.tencent.tencentmap.mapsdk.search.PoiItem;
import com.tencent.tencentmap.mapsdk.route.RouteSearch;

public class RouteSearcher extends Activity {

	private List<QDriveRouteSegment> suggestionList; // �ݳ�·���б�
	private ListView listInfo; // ����·�߽ڵ��б�
	private QRouteSearchResult searchresult;
	private RouteSearch routs;
	private QDriveRouteInfo driveroute;

	private EditText text_start;
	private EditText text_end;
	private int lat, lon;
	private int start_lat, start_lon;
	private String start_name, end_name;
	private int end_lat, end_lon;
	private Button nav_button;
	private Button nav_map_button;
	private Button street_nav_button;
	/**����������������յ㽻��*/
	private ImageButton exchange;
	// private RouteTest myroute;
	// private String dest="";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routesearch);
		
		//��û��ѡ����ʼ��㣬����û�λ�ó�ʼ��Ϊ���
		
		if(Declare.start_name.equals("") && Declare.UserLat!=0)
		{
			Declare.start_lat=(long) (Declare.UserLat * 1E6);
			Declare.start_lon=(long) (Declare.UserLon * 1E6);
			Declare.start_name="�ҵ�λ��";
		}

        
		text_start = (EditText) this.findViewById(R.id.start);
		text_end = (EditText) this.findViewById(R.id.destin);
		nav_button = (Button) this.findViewById(R.id.nav_button);
		nav_map_button = (Button) this.findViewById(R.id.nav_map_button);
		street_nav_button = (Button) this.findViewById(R.id.street_nav_button);
		
		text_start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub				
					Bundle bundle = new Bundle();
					bundle.putSerializable("flag", "0"); // ��־λ
					Intent intent = new Intent(RouteSearcher.this,
							GetNavStart.class);
					intent.putExtras(bundle);
					startActivity(intent);
				
			}
		});
		
		text_end.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub				
					Bundle bundle = new Bundle();
					bundle.putSerializable("flag", "1"); // ��־λ
					Intent intent = new Intent(RouteSearcher.this,
							GetNavStart.class);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();				
			}
		});

		nav_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!Declare.start_name.equals("") &&!Declare.end_name.equals(""))
				{
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							QPlaceInfo st = new QPlaceInfo();
							QPlaceInfo ed = new QPlaceInfo();
							QRouteSearchResult driveSearchResult = null;
							RouteSearch routeSearch = new RouteSearch(
									RouteSearcher.this);

							st.point = new GeoPoint((int) (Declare.start_lat),
									(int) (Declare.start_lon));
							ed.point = new GeoPoint((int) (Declare.end_lat),
									(int) (Declare.end_lon));
							driveSearchResult = routeSearch.searchDriveRoute(
									"�人", st, "�人", ed);

							driveroute = driveSearchResult.driveRouteInfo;
							suggestionList = driveroute.routeSegmentList;
							if (suggestionList != null) {
								handler.sendEmptyMessage(0x0001);

							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
			}
		});

		nav_map_button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(!Declare.start_name.equals("") &&!Declare.end_name.equals("")) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("flag", "����"); // ��־λ
					Intent intent = new Intent(RouteSearcher.this,
							HomeActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});

		street_nav_button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(!Declare.start_name.equals("") &&!Declare.end_name.equals("")) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("flag", "ȫ������"); // ��־λ
					Intent intent = new Intent(RouteSearcher.this,
							TestFragmentActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});

		
		
		exchange=(ImageButton)this.findViewById(R.id.exchange);
		exchange.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				String temp=text_start.getText().toString();
				text_start.setText(text_end.getText().toString());
				text_end.setText(temp);
			}
		});
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {

				if (Declare.start_name != "")
					handler.sendEmptyMessage(0x0002);
				if (Declare.end_name != "")
					handler.sendEmptyMessage(0x0003);
				// ��ô�getnavstart��õ���Ϣ
				Intent intent = getIntent();

				Bundle bundle = intent.getExtras();
				if (bundle != null && bundle.getString("flag").equals("10")) { // ���
					Declare.start_lat = (Integer) bundle
							.getSerializable("start_lat");
					Declare.start_lon = (Integer) bundle
							.getSerializable("start_lon");
					Declare.start_name = (String) bundle
							.getSerializable("start_name");
					// text_start.setText(Declare.start_name);
					handler.sendEmptyMessage(0x0002);
				} else if (bundle != null
						&& bundle.getString("flag").equals("11")) { // �յ�
					Declare.end_lat = (Integer) bundle
							.getSerializable("start_lat");
					Declare.end_lon = (Integer) bundle
							.getSerializable("start_lon");
					Declare.end_name = (String) bundle
							.getSerializable("start_name");
					// text_end.setText(Declare.end_name);
					handler.sendEmptyMessage(0x0003);
				}

			}
		}).start();

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x0001) {
				listInfo = (ListView) RouteSearcher.this
						.findViewById(R.id.route_info);
				listInfo.setAdapter(new MyBaseAdapter(RouteSearcher.this,
						suggestionList));
			} else if (msg.what == 0x0002) {
				text_start.setText(Declare.start_name);
			} else if (msg.what == 0x0003) {
				text_end.setText(Declare.end_name);

			} else if (msg.what == 0x0004) {

			}
		};
	};

	class MyBaseAdapter extends BaseAdapter {
		private Context context; // ���մ���Ļ���
		private List<?> list; // ���մ����List
		// @SuppressLint("ParserError")
		private LayoutInflater mInflater; // ���벼����Դ�Ĺ�����

		// @SuppressLint("ParserError")
		public MyBaseAdapter(Context context, List<?> list) { // ���캯��
			this.context = context;
			mInflater = LayoutInflater.from(context);
			this.list = list;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView contentName = new TextView(context); // ����һ��TextView
			TextView contentContent = new TextView(context); // ����һ��TextView
			LinearLayout linearLayout = (LinearLayout) mInflater.inflate(
					R.layout.content, null); // ��ȡ������Դ����
			contentName.setTextSize(20);
			linearLayout.addView(contentName);
			linearLayout.addView(contentContent);
			contentName
					.setText(((List<QDriveRouteSegment>) list).get(position).description); // ��ʾ�����
			return linearLayout;
		}
	}

}
