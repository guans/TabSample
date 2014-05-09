/**
 * 
 */

package app.sample.streetlocation.entity;

import android.graphics.Bitmap;

/**
 * ������poi overlay�������ݷ�װ ��������ṩ���
 * 
 * @author gordongeng
 */
public class CustomPoiData {

    /**
     * γ�ȵ�10E6
     */
    public int latE6;

    /**
     * ���ȵ�10E6
     */
    public int lonE6;
    
    /**
     * poi����ʾ��ͼƬ
     */
    public Bitmap marker;
    
    /**
     * poi�����ʱ��ʾ��ͼƬ
     */
    public Bitmap markerPressed;
    
    /**
     * �߶�ƫ����
     */
    public float heightOffset;
    
    public String uid;

    public CustomPoiData(int x, int y) {
        this(x, y, null, null, 0);
    }
    
    public CustomPoiData(int x, int y, Bitmap marker) {
    	this(x, y, marker, null, 0);
    }
    
	public CustomPoiData(int x, int y, Bitmap marker, Bitmap markerPressed, float offset) {
		this.latE6 = x;
		this.lonE6 = y;
		this.marker = marker;
		this.markerPressed = markerPressed;
		this.heightOffset = offset;
		this.uid = "";
	}
	
	public void updateMarker(Bitmap bitmap, String uid) {
		this.marker = bitmap;
		this.uid = uid;
	}

}
