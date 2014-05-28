package app.sample.streetlocation;

import com.example.streetlocation.R;



import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.sample.streetlocation.constant.DatabaseManager;
import app.sample.streetlocation.constant.POI;
import app.sample.streetlocation.entity.Declare;
import app.sample.streetlocation.entity.MyIcon;
import app.sample.streetlocation.entity.MyIcon2;
import app.sample.streetlocation.entity.TestPoint;

public class NavigateMap extends ActivityGroup {
	
	
	static LinearLayout container = null;
	public static TextView changeStart;
	public static EditText startValue;
	public static Button myLocationBtn,rimBtn;
	public static Button confirm;
	public static ImageView editStartValue;
	
	protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	setContentView(R.layout.navigate_map);
	
	
	
	changeStart = (TextView)findViewById(R.id.changeStart);
    startValue = (EditText) findViewById(R.id.startValue);
    myLocationBtn = (Button)findViewById(R.id.mylocation);
    editStartValue = (ImageView)findViewById(R.id.editStartValue);
    rimBtn = (Button)findViewById(R.id.rim);
    confirm = (Button)findViewById(R.id.confirm);
    
    
    confirm.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			
			  /**数据库管理类*/
	        DatabaseManager databaseManager = null;



	/*创建数据库和POI表格*/
	               databaseManager = new DatabaseManager(NavigateMap.this);
	               databaseManager.createPoiTable();


	        //差一条数据进去
	               if(!Declare.start_name.equals("")){
	              TestPoint tp= new TestPoint(Declare.start_lat,Declare.start_lon);
	              POI poi= new POI(Declare.start_name ,tp);
	               //pois.add(poi );     
	               databaseManager.insertHistoricalPoi(poi);
	               }

	               if(!Declare.end_name.equals("")){
	            	   TestPoint tp= new TestPoint(Declare.end_lat,Declare.end_lon);
	 	              POI poi= new POI(Declare.end_name ,tp);
	 	               //pois.add(poi );     
	 	               databaseManager.insertHistoricalPoi(poi);
	               
	               }
	               
			
			Intent intent = new Intent(NavigateMap.this,
					RouteSearcher.class);
			startActivity(intent);
			finish();
		}
			
	});


	
	
    
    container = (LinearLayout) findViewById(R.id.containerBody);
    container.addView(getLocalActivityManager().startActivity(
				"mapActivity",
				new Intent(NavigateMap.this, PositionActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
				.getDecorView());
		MyIcon mi = new MyIcon(this);
		getWindow().addContentView(mi,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT)); 
		MyIcon2 m2 = new MyIcon2(this);
	//	getWindow().addContentView(m2,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT)); 
		
	}
	
	 
		
}
