package edu.neusoft.Client.Database;

import java.security.PublicKey;

import edu.neusoft.Client.common.SystemConst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.Toast;

public class DBAdapter {
	private SQLiteDatabase db;
	private final Context context;
	private DBOpenHelper dbOpenHelper;
	
	public DBAdapter (Context _context){
		context = _context;
	}
	
	public void open()throws SQLiteException{
		dbOpenHelper = new DBOpenHelper(context,SystemConst.DB_NAME,null,SystemConst.DB_VERSION);
		try{
			db = dbOpenHelper.getWritableDatabase();
		}catch(SQLiteException ex){
			db = dbOpenHelper.getReadableDatabase();
		}
	}
	
	//从数据库查找系统设置表，加载数据到系统常量
	public void LoadConfig(){
		Cursor result =  db.query(SystemConst.DB_TABLE_CONFIG, new String[] { SystemConst.KEY_ID, SystemConst.KEY_SERVER_TEL, SystemConst.KEY_CONTROL_METHOD,
				  SystemConst.KEY_CITY_NAME, SystemConst.KEY_REFRESH_SPEED,
				  SystemConst.KEY_WEATHER_SERVICE, SystemConst.KEY_LOCATION_SERVICE, SystemConst.KEY_START_TIME,SystemConst.KEY_SERVER_IP,SystemConst.KEY_SERVER_COM}, 
				  SystemConst.KEY_ID + "=1", null, null, null, null);
		  if (result.getCount() == 0 || !result.moveToFirst()){
				return;
			}
		  Config.ServerTel = result.getString(result.getColumnIndex(SystemConst.KEY_SERVER_TEL));
		  Config.ControlMethod = result.getString(result.getColumnIndex(SystemConst.KEY_CONTROL_METHOD));
		  Config.CityName = result.getString(result.getColumnIndex(SystemConst.KEY_CITY_NAME));
		  Config.RefreshSpeed =  result.getString(result.getColumnIndex(SystemConst.KEY_REFRESH_SPEED));
		  Config.WeatherService = result.getString(result.getColumnIndex(SystemConst.KEY_WEATHER_SERVICE));
		  Config.LocationService =  result.getString(result.getColumnIndex(SystemConst.KEY_LOCATION_SERVICE));
		  Config.StartTime = result.getString(result.getColumnIndex(SystemConst.KEY_START_TIME));
		  Config.ServerIP =result.getString(result.getColumnIndex(SystemConst.KEY_SERVER_IP));
		  Config.ServerCom =result.getString(result.getColumnIndex(SystemConst.KEY_SERVER_COM));
		  Toast.makeText(context, "系统设置读取成功", Toast.LENGTH_SHORT).show();
		
	}
	
	public void SaveConfig(){
		ContentValues updateValues =  new ContentValues();
		updateValues.put(SystemConst.KEY_CITY_NAME, Config.CityName);
		updateValues.put(SystemConst.KEY_CONTROL_METHOD, Config.ControlMethod);
		updateValues.put(SystemConst.KEY_LOCATION_SERVICE, Config.LocationService);
		updateValues.put(SystemConst.KEY_REFRESH_SPEED, Config.RefreshSpeed);
		updateValues.put(SystemConst.KEY_SERVER_COM, Config.ServerCom);
		updateValues.put(SystemConst.KEY_SERVER_IP, Config.ServerIP);
		updateValues.put(SystemConst.KEY_SERVER_TEL, Config.ServerTel);
		updateValues.put(SystemConst.KEY_START_TIME, Config.StartTime);
		updateValues.put(SystemConst.KEY_WEATHER_SERVICE, Config.WeatherService);
		db.update(SystemConst.DB_TABLE_CONFIG, updateValues,
				SystemConst.KEY_ID+"=1", null);
		Toast.makeText(context, "系统设置保存成功", Toast.LENGTH_SHORT).show();
	}
	
	public void close(){
		if (db!=null){
			db.close();
			db = null;
		}
	}

	

}
