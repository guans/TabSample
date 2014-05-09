package app.sample.streetlocation.entity;

import java.io.Serializable;

public class TestPoint implements Serializable{
	
	public TestPoint()
	{
		altitude=0;
		latitude=0;
	}
	
	public TestPoint(int alt,int lon)
	{
		altitude=alt;
		latitude=lon;
	}
	
	public TestPoint(double alt,double lon)
	{
		altitude=(int)(alt* 1e6);
		latitude=(int)(lon* 1e6);
	}
	
	public void SetAlt(int alt)
	{
		altitude=alt;
	}
	
	public void SetLon(int lon)
	{
		latitude=lon;
	}
	/**
     * 纬度的10E6
     */
    public int altitude;

    /**
     * 经度的10E6
     */
    public int latitude;
    
    /**
     */

    
}
