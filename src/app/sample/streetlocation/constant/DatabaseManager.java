package app.sample.streetlocation.constant;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import app.sample.streetlocation.entity.TestPoint;



/**
 * 
 * @author Administrator
 * 
 */
public class DatabaseManager implements DatabaseConstants {
	/**
	 * SQLiteDatabase对象
	 */
	private SQLiteDatabase database = null;

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public DatabaseManager(Context context) {
		if (context != null)
			database = context.openOrCreateDatabase(DATABASE_NAME,
					Context.MODE_PRIVATE, null);
	}
	/**
	 * 创建POI表格
	 */
	public void createPoiTable() {
		if (database != null) {
			database.execSQL(CREATE_TABLE_POI);
		}
	}

	/**
	 * 将兴趣点插于数据库POI表格中 如果已经存在该POI数据则不插入
	 * 
	 * @param poi POI对象 需要插入的数据
	 * @param type int类型 插入的poi是历史数据还是收藏点
	 */
	private void insertPoi(POI poi, int type) {
		/* 检查poi以及type的合法性 */
		if ((database != null) && (poi != null)
				&& ((type == TYPE_HISTORICAL_POINT) || (type == TYPE_COLLECTED_POINT))) {
			/* 首先查询类型为type的所有兴趣点 如果数据库中不存在 则插入 */
			ArrayList<POI> array = searchPois(type);
			if ((array != null) && (!array.contains(poi))) {
				ContentValues cv = new ContentValues();
				cv.put(TABLE_POI_Description, poi.getDescription());
				cv.put(TABLE_POI_LAT, String.valueOf(poi.getCoordinate()
						.altitude));
				cv.put(TABLE_POI_LON, String.valueOf(poi.getCoordinate()
						.latitude));
				cv.put(TABLE_POI_TIME, String.valueOf(0.0));
				cv.put(TABLE_POI_TYPE, type);
				database.insert(TABLE_POI_NAME, null, cv);
			}
		}
	}
	/**
	 * 插入收藏点数据
	 * @param poi POI对象
	 */
	public void insertCollectedPoi(POI poi){
		insertPoi(poi, TYPE_COLLECTED_POINT);
	}
	/**
	 * 插入历史点数据
	 * @param poi POI对象
	 */
	public void insertHistoricalPoi(POI poi){
		insertPoi(poi, TYPE_HISTORICAL_POINT);
	}
	/**
	 * 从表格中查询所有的POI数据
	 * 
	 * @param type POI数据类型 历史数据（1）或者收藏点（2）
	 * @return 查询的记录
	 */
	private ArrayList<POI> searchPois(int type) {
		if((type == TYPE_HISTORICAL_POINT) || (type == TYPE_COLLECTED_POINT)){
			ArrayList<POI> array = new ArrayList<POI>();
			Cursor cursor = database
					.rawQuery("SELECT * FROM " + TABLE_POI_NAME + " WHERE "
							+ TABLE_POI_TYPE + "=" + String.valueOf(type), null);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						int column;
						column = cursor.getColumnIndex(TABLE_POI_Description);
						String description = cursor.getString(column);
						column = cursor.getColumnIndex(TABLE_POI_LAT);
						Long x = new Long(cursor.getString(column));
						column = cursor.getColumnIndex(TABLE_POI_LON);
						Long y = new Long(cursor.getString(column));
						array.add(new POI(description, new TestPoint(x, y)));
					} while (cursor.moveToNext());
					cursor.close();
				}
				cursor.close();
			}
			return array;
		}
		else 
			return null;
	}
	/**
	 * 查询所有的收藏点
	 * @return 所有的收藏点记录
	 */
	public ArrayList<POI> searchCollectedPois(){
		return searchPois(TYPE_COLLECTED_POINT);
	}
	/**
	 * 查询所有的历史点
	 * @return 所有的历史点记录
	 */
	public ArrayList<POI> searchHistoricalPois(){
		return searchPois(TYPE_HISTORICAL_POINT);
	}
	/**
	 * 清除type类型的数据 
	 * @param type int 表示是收藏记录还是历史数据点
	 */
	private void clearPois(int type){
		//检查type的合法性
		if((type == TYPE_HISTORICAL_POINT) || (type == TYPE_COLLECTED_POINT)){
			if(database != null)
				database.execSQL("DELETE FROM " +TABLE_POI_NAME + " WHERE type="+ type);
		}
	}
	/**
	 * 清除收藏点
	 */
	public void clearCollectedPois(){
		clearPois(TYPE_COLLECTED_POINT);
	}
	/**
	 * 清除历史数据
	 */
	public void clearHistoricalPois(){
		clearPois(TYPE_HISTORICAL_POINT);
	}
	/**
	 * 关闭数据库
	 */
	public void close(){
		if(this.database != null)
			database.close();
	}
}