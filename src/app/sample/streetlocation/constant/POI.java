package app.sample.streetlocation.constant;

import app.sample.streetlocation.entity.TestPoint;



public class POI {
	/**POI����*/
	public  String description = null;
	/**����*/
	public  TestPoint coordinate = null;
	/**
	 * ���캯��
	 * @param description item����
	 * @param coordinate item����
	 * @param marker itemͼ��
	 */
	public POI(String description, TestPoint coordinate){
		this.description = description;
		this.coordinate = coordinate;
	}
	/**
	 * ���item����
	 * @return ����description
	 */
	public String getDescription() {
		return description;
	} 
	/**
	 * ���item����
	 * @return ����coordinate
	 */
	public TestPoint getCoordinate(){
		return coordinate;
	}
	/**
	 * ����item����
	 * 
	 */
	public void setDescription(String description) {
		 this.description = description;
	} 
	/**
	 * ����item����
	 * 
	 */
	public void setCoordinate(TestPoint coordinate){
		 this.coordinate = coordinate;
	}
	/**����equals����*/
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
		  return ("����"+Long.toString(coordinate.altitude)+",����"+Long.toString(coordinate.latitude));
		  
		 }
}