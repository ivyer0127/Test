package edu.neusoft.Client.Database;

import edu.neusoft.Client.common.SystemConst;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBOpenHelper extends SQLiteOpenHelper{

	public DBOpenHelper(Context context,String name,
			CursorFactory factory,int version){
		super(context,name,factory,version);
	}
	//创建"系统设置"表结构
	private static final String DB_CREATE_CONFIG = "create table "
		+ SystemConst.DB_TABLE_CONFIG + " (" + SystemConst.KEY_ID
		+ " integer primary key autoincrement, " + SystemConst.KEY_SERVER_TEL
		+ " text not null, " + SystemConst.KEY_CONTROL_METHOD + " text, "
		+ SystemConst.KEY_CITY_NAME + " text, " + SystemConst.KEY_REFRESH_SPEED + " text,"
		+ SystemConst.KEY_WEATHER_SERVICE + " text, " + SystemConst.KEY_LOCATION_SERVICE  +  " text, "
		+ SystemConst.KEY_START_TIME + " text, "+ SystemConst.KEY_SERVER_IP +" text, "+SystemConst.KEY_SERVER_COM+" text);";
	//创建"天气预案"表
	private final String DB_CREATE_RESERVEPLAN = "create table Reserve_Plan (_id integer primary key autoincrement,"
		+ "weather varchar(100), temperature varchar(100),"
		+ "solution varchar(300));";
	@Override
	public void onCreate(SQLiteDatabase _db) {
		// TODO Auto-generated method stub
		//创建"系统设置"和"天气预案"表
		_db.execSQL(DB_CREATE_CONFIG);
		_db.execSQL(DB_CREATE_RESERVEPLAN);
		//系统设置默认数据加载
		Config.LoadDefaultConfig();
		//系统设置默认数据插入数据库表中
		ContentValues newValues = new ContentValues();
		newValues.put(SystemConst.KEY_SERVER_TEL, Config.ServerTel);
		newValues.put(SystemConst.KEY_CONTROL_METHOD, Config.ControlMethod);
		newValues.put(SystemConst.KEY_CITY_NAME,Config.CityName);
		newValues.put(SystemConst.KEY_REFRESH_SPEED, Config.RefreshSpeed);
		newValues.put(SystemConst.KEY_WEATHER_SERVICE, Config.WeatherService);
		newValues.put(SystemConst.KEY_LOCATION_SERVICE, Config.LocationService);
		newValues.put(SystemConst.KEY_START_TIME, Config.StartTime);
		newValues.put(SystemConst.KEY_SERVER_IP, Config.ServerIP);
		newValues.put(SystemConst.KEY_SERVER_COM, Config.ServerCom);
		_db.insert(DB_CREATE_CONFIG, null, newValues);
	}

	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
		// TODO Auto-generated method stub
		_db.execSQL("DROP TABLE IF EXISTS"+SystemConst.DB_TABLE_CONFIG);
		_db.execSQL("DROP TABLE IF EXISTS Reserve_Plan");
		onCreate(_db);
	}

}
