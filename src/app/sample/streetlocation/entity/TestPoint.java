package app.sample.streetlocation.entity;

import java.io.Serializable;

public class TestPoint implements Serializable{
	
	public TestPoint()
	{
		altitude=0;
		latitude=0;
	}
	
	public TestPoint(long alt,long lon)
	{
		altitude=alt;
		latitude=lon;
	}
	
	public TestPoint(double alt,double lon)
	{
		altitude=(long)(alt* 1e6);
		latitude=(long)(lon* 1e6);
	}
	
	public void SetAlt(long alt)
	{
		altitude=alt;
	}
	
	public void SetLon(long lon)
	{
		latitude=lon;
	}
	/**
     * 纬度的10E6
     */
    public long altitude;

    /**
     * 经度的10E6
     */
    public long latitude;
    
    /**
     */
    @Override
	public String toString() {
		  return ("经度"+ altitude+",经度"+ latitude);
		  
		 }
    
}
