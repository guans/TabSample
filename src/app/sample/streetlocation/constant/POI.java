package app.sample.streetlocation.constant;

import app.sample.streetlocation.entity.TestPoint;



public class POI {
	/**POI描述*/
	public  String description = null;
	/**坐标*/
	public  TestPoint coordinate = null;
	/**
	 * 构造函数
	 * @param description item描述
	 * @param coordinate item坐标
	 * @param marker item图标
	 */
	public POI(String description, TestPoint coordinate){
		this.description = description;
		this.coordinate = coordinate;
	}
	/**
	 * 获得item描述
	 * @return 返回description
	 */
	public String getDescription() {
		return description;
	} 
	/**
	 * 获得item坐标
	 * @return 返回coordinate
	 */
	public TestPoint getCoordinate(){
		return coordinate;
	}
	/**
	 * 设置item描述
	 * 
	 */
	public void setDescription(String description) {
		 this.description = description;
	} 
	/**
	 * 设置item坐标
	 * 
	 */
	public void setCoordinate(TestPoint coordinate){
		 this.coordinate = coordinate;
	}
	/**重载equals函数*/
	@Override
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(! (o instanceof POI))
			return false;
		if(((POI)o).description.equals(description))
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		  return ("经度"+Long.toString(coordinate.altitude)+",经度"+Long.toString(coordinate.latitude));
		  
		 }
}
