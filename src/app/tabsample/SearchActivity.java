package app.tabsample;

import com.tencent.tencentmap.mapsdk.map.GeoPoint;
import com.tencent.tencentmap.mapsdk.search.PoiResults;
import com.tencent.tencentmap.mapsdk.search.PoiSearch;
import com.tencent.tencentmap.streetviewsdk.StreetViewListener;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Adil Soomro
 *
 */
public class SearchActivity extends Activity  {
	private String dest="";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_actvity);        
        
    }
    public void doSearch(View s)
    {
    	//Toast.makeText(this, "I lied, I love KUNG FUuuuuuuuuUUuuu...!!", Toast.LENGTH_LONG).show();
    	if("".equals(((EditText)findViewById(R.id.destination)).getText().toString()))
    	{
    		Toast.makeText(SearchActivity.this, "请输入目的地", Toast.LENGTH_SHORT).show();
    		
    		return;
    	}
    	//将目的地发送至地图活动
        EditText editText1 =(EditText)findViewById(R.id.destination);  
        dest=editText1.getText().toString();  
        
        Bundle bundle =new Bundle();
        bundle.putSerializable("dest", dest);
        Intent intent=new Intent(SearchActivity.this,HomeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        
    }
    public String getDest(){
    	return dest;
    }
}
