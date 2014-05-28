package app.sample.streetlocation.constant;

/**
 * ���ݿⳣ���� �洢���ݿ�����Ļ�������
 * 
 */
public interface DatabaseConstants {
	/** ���ݿ��� */
	public static final String DATABASE_NAME = "client.db";
	/** ��Ȥ����� */
	public static final String TABLE_POI_NAME = "poi";
	/** ��Ȥ����ֶ� */
	public static final String TABLE_POI_ID = "id";
	public static final String TABLE_POI_Description = "description";
	public static final String TABLE_POI_LAT = "lat";
	public static final String TABLE_POI_LON = "lon";
	public static final String TABLE_POI_TIME=	"time";
	public static final String TABLE_POI_TYPE = "type";
	/** �ͻ�����Ȥ������������ղص��Լ���ʷ�� */	
	public static final int TYPE_HISTORICAL_POINT = 1;
	public static final int TYPE_COLLECTED_POINT = 2;

	/** ��������SQL��� */
	public static final String CREATE_TABLE_POI = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_POI_NAME + "("+TABLE_POI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TABLE_POI_Description
			+ " TEXT," + TABLE_POI_LAT + " TEXT," + TABLE_POI_LON
			+ " TEXT," + TABLE_POI_TIME + " TEXT," + TABLE_POI_TYPE
			+ " INTEGER)";
}

