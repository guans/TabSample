package app.sample.streetlocation;

import com.example.streetlocation.R;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


/**
 * @author Adil Soomro
 *
 */
@SuppressWarnings("deprecation")
public class AwesomeActivity extends TabActivity {
	TabHost tabHost;
	/** Called when the activity is first created. */
	Intent intent; //跳转用的
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tabHost = getTabHost();
		setTabs();
	}
	private void setTabs()
	{
		addTab("地图", R.drawable.tab_home, HomeActivity.class);
		addTab("全景", R.drawable.tab_search, TestFragmentActivity.class);
		addTab("拍拍", R.drawable.tab_search, SearchActivity.class);
		addTab("导航", R.drawable.tab_home, RouteSearcher.class);
		addTab("搜索", R.drawable.tab_search, SearchActivity.class);
	}
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);		
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
	public void openCameraActivity(View b)
	{
		Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		menu.add(0,1,1,R.string.search);
		menu.add(0,2,2,R.string.exit);
		menu.add(0,3,3,R.string.navigate);
		menu.add(0,4,4,R.string.about);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId())
		{
		case 1:
			Intent intent=new Intent(AwesomeActivity.this,SearchActivity.class);
			startActivity(intent);
			break;
		case 2:
			finish(); break;
		case 3:
			 intent=new Intent(AwesomeActivity.this,RouteSearcher.class);
			startActivity(intent);
			break;
		case 4:
			 intent=new Intent(AwesomeActivity.this,TestFragmentActivity.class);
			startActivity(intent);break;
		
		
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
}