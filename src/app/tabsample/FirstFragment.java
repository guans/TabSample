package app.tabsample;

import android.support.v4.app.Fragment;
import android.os.Bundle;    
import android.view.ContextMenu;    
import android.view.LayoutInflater;    
import android.view.Menu;    
import android.view.MenuItem;    
import android.view.View;    
import android.view.ViewGroup;    
import android.view.ContextMenu.ContextMenuInfo;  

public class FirstFragment extends Fragment{    
    
    
    @Override    
    public void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);    
    }    
      
      
    //绘制视图  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,     
                             Bundle savedInstanceState) {    
        View root = inflater.inflate(R.layout.first, container, false);    
        registerForContextMenu(root.findViewById(R.id.editText1));    
        return root;     
    }     
        
        
    @Override    
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {    
        super.onCreateContextMenu(menu, v, menuInfo);    
        menu.add(Menu.NONE, 0, Menu.NONE, "菜单1");    
        menu.add(Menu.NONE, 1, Menu.NONE, "菜单2");    
    }    
    
    @Override    
    public boolean onContextItemSelected(MenuItem item) {    
        return super.onContextItemSelected(item);    
    }    
        
}    