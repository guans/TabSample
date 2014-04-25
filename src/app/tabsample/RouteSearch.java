package app.tabsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RouteSearch extends Activity  {
	

	
	private RouteTest myroute;			
	private String dest="";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routesearch);        
        
    }
    public void search(View s)
    {
    	//Toast.makeText(this, "I lied, I love KUNG FUuuuuuuuuUUuuu...!!", Toast.LENGTH_LONG).show();
    	if("".equals(((EditText)findViewById(R.id.destin)).getText().toString()))
    	{
    		Toast.makeText(RouteSearch.this, "请输入目的地", Toast.LENGTH_SHORT).show();
    		
    		return;
    	}
    	if("".equals(((EditText)findViewById(R.id.start)).getText().toString()))
    	{
    		Toast.makeText(RouteSearch.this, "起点", Toast.LENGTH_SHORT).show();
    		
    		return;
    	}
    	
    	/*
    	//将目的地发送至地图活动
        EditText editText1 =(EditText)findViewById(R.id.destination);  
        dest=editText1.getText().toString();  
        
        Bundle bundle =new Bundle();
        bundle.putSerializable("dest", dest);
        Intent intent=new Intent(RouteSearch.this,HomeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        */
        Intent intent=new Intent(RouteSearch.this,HomeActivity.class);
        startActivity(intent);
        
    }
    public String getDest(){
    	return dest;
    }
}
