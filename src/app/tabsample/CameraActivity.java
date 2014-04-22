package app.tabsample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Adil Soomro
 *
 */
public class CameraActivity extends Activity {
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);      
    }
    
    public void takePhotoCamera(View v)
    {
    	//Toast.makeText(this, "ohhhhh, That is swearly cooool..\nHow did you do that?? O_o ", Toast.LENGTH_LONG).show();
    	
    	 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
    	 startActivityForResult(intent, 1);
    }
    public void pickFromGallery(View v) {
		Toast.makeText(this, "That was really awesom, isn't that?", Toast.LENGTH_LONG).show();
	}
    
    
    
    
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        // TODO Auto-generated method stub  
        super.onActivityResult(requestCode, resultCode, data);  
        if (resultCode == Activity.RESULT_OK) {  
            String sdStatus = Environment.getExternalStorageState();  
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
                Log.i("TestFile",  
                        "SD card is not avaiable/writeable right now.");  
                return;  
            }  
            String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";     
            Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
            Bundle bundle = data.getExtras();  
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
          
            FileOutputStream b = null;  
           //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？  
            File file = new File("/sdcard/myImage/");  
            file.mkdirs();// 创建文件夹  
            String fileName = "/sdcard/myImage/"+name;  
  
            try {  
                b = new FileOutputStream(fileName);  
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    b.flush();  
                    b.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            ((ImageView) findViewById(R.id.pics)).setImageBitmap(bitmap);// 将图片显示在ImageView里  
        }  
    }  
    
    
}