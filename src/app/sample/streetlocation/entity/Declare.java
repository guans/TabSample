package app.sample.streetlocation.entity;

import java.util.ArrayList;
import java.util.List;

import com.tencent.tencentmap.mapsdk.map.GeoPoint;

public class Declare {
	  public static double UserLat=0;
	  public static double  UserLon=0;
	  public static long   start_lat=0;
	  public static long   start_lon=0;
	  public static String   start_name="";
	  public static long   end_lat=0;
	  public static long   end_lon=0;
	  public static String   end_name="";
	  public static boolean   type=false;    //false Æðµã true ÖÕµã
	  public static  List<GeoPoint> ptpoint=new ArrayList<GeoPoint>();    
}
