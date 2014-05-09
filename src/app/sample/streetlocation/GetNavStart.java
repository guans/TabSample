package app.sample.streetlocation;

import java.util.List;

import com.example.streetlocation.R;
import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.route.QDriveRouteSegment;
import com.tencent.tencentmap.mapsdk.route.QPlaceInfo;
import com.tencent.tencentmap.mapsdk.route.QRouteSearchResult;
import com.tencent.tencentmap.mapsdk.route.RouteSearch;
import com.tencent.tencentmap.mapsdk.search.PoiItem;
import com.tencent.tencentmap.mapsdk.search.PoiResults;
import com.tencent.tencentmap.mapsdk.search.PoiSearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import app.sample.streetlocation.RouteSearcher.MyBaseAdapter;
import app.sample.streetlocation.SearchActivity.ListInfoListener;

public class GetNavStart extends Activity{
	
	
	private ListView listInfo;   //导航路线节点列表
	private List<PoiItem> suggestionList;  //显示联想词
	private EditText my_text_start;
	private Button my_button;
	private Button confirm_button;
	private Button map_button;
	private int start_lat, start_lon;
	private String start_name, end_name;
	private int end_lat, end_lon;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navstart); 
        
        my_text_start=(EditText)this .findViewById(R.id.mystart);
        
        my_button=(Button)this.findViewById(R.id.search_button);
        my_button .setOnClickListener( new View.OnClickListener(){
            
            public void onClick(View v) {
              // TODO Auto-generated method stub
            
            	if( !"".equals( my_text_start.getText().toString().trim() ) )
            	{		
            		new Thread(new Runnable(){
                        @Override
                        public void run() {
                        	
                        
                        	
                    	PoiSearch  poiSearch=new PoiSearch(GetNavStart.this);
						poiSearch.setPageCapacity(15);
						poiSearch.setPageNumber(0);
						
						PoiResults poiResult = new PoiResults();
						try {
							String key=	my_text_start.getText().toString().trim() ;
							if(key!="")
							{
							poiResult = poiSearch.searchPoiInCity(key,"武汉");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						suggestionList=poiResult.getCurrentPagePoiItems();
						handler.sendEmptyMessage(0x0001);
                        	
                        	
                        }
                    }).start();
                        
            		
            		
            		
            	}
            		
         }
     });
        
        
        
        confirm_button=(Button)this.findViewById(R.id.confirm_button);
        confirm_button .setOnClickListener( new View.OnClickListener(){
            
            public void onClick(View v) {
              // TODO Auto-generated method stub
            	
            	//获得从RouteSearch获得的信息
            	Intent intent = getIntent();

        		Bundle bundle = intent.getExtras();
            //	if (bundle != null&&bundle.getString("flag").equals("0") )
            	{
            		Bundle bundle1=new Bundle();
            		if (bundle != null&&bundle.getString("flag").equals("0"))
            				 bundle1.putSerializable( "flag" , "10" );  //标志位
            		else
            			 	bundle1.putSerializable( "flag" , "11" );  //标志位
            		//表示要发送的是起点
            		 //Bundle bundle1 = new Bundle();
                    
                     bundle1.putSerializable( "start_lat" , start_lat );  
                     bundle1.putSerializable( "start_lon" , start_lon );  
                     bundle1.putSerializable( "start_name" , start_name );  
                     Intent intent1= new Intent(GetNavStart.this , RouteSearcher. class);
                     intent1.putExtras(bundle1);
                     startActivity(intent1);
                     finish();
            		
        		}
            	/*else
            	{
            		//表示要发送的是终点
            		 Bundle bundle2 = new Bundle();
                     bundle2.putSerializable( "flag" , "11" );  //标志位
                     bundle2.putSerializable( "end_lat" , end_lat );  
                     bundle2.putSerializable( "end_lon" , end_lon );  
                     bundle2.putSerializable( "end_name" , end_name );  
                     Intent intent2= new Intent(GetNavStart.this , RouteSearcher. class);
                     intent2.putExtras(bundle2);
                     startActivity(intent2);
            	}*/
            	
            }
        });
        
            
             
	}
	
	
	
	
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x0001) {
				listInfo=(ListView)GetNavStart.this.findViewById(R.id.start_info);
    	        listInfo.setAdapter(new MyBaseAdapter(GetNavStart.this,suggestionList));
    	        listInfo.setOnItemClickListener(new ListInfoListener());
			} else if (msg.what == 0x0002) {
				my_text_start.setText(start_name);
				
				
			} else if (msg.what == 0x0003) {    
				
				
			}else if(msg.what == 0x0004){
			
			}
		};
	};
	
	
	
	
	
	class MyBaseAdapter extends BaseAdapter {
		private Context context; // 接收传入的环境
		private List<?> list; // 接收传入的List
		//@SuppressLint("ParserError")
		private LayoutInflater mInflater; // 引入布局资源的管理器

		//@SuppressLint("ParserError")
		public MyBaseAdapter(Context context, List<?> list) { // 构造函数
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
			TextView contentName = new TextView(context); // 构建一个TextView
			TextView contentContent = new TextView(context); // 构建一个TextView
			LinearLayout linearLayout = (LinearLayout) mInflater.inflate(
					R.layout.content, null); // 获取内容资源布局
			contentName.setTextSize(20);
			linearLayout.addView(contentName);
			linearLayout.addView(contentContent);
			contentName
					.setText(((List<PoiItem>) list).get(position).name); // 显示联想词
			return linearLayout;
		}
	}
	
	
	
	
	// 点击LISTIFO内信息 响应
		class ListInfoListener implements OnItemClickListener {
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			//	System.out.println(suggestionList.get(position).key);
				// mkSearch.suggestionSearch(suggestionList.get(position).key);
				//mkSearch.geocode(suggestionList.get(position).key,
				//		suggestionList.get(position).city);
				
				System.out.println(suggestionList.get(position).name);
				start_lat=suggestionList.get(position).point.getLatitudeE6();
				start_lon=suggestionList.get(position).point.getLongitudeE6();
				start_name=suggestionList.get(position).name;
				my_text_start.setText(start_name);
				handler.sendEmptyMessage(0x0002);
			}
		}
		

}
