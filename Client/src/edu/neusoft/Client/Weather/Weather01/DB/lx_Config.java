package edu.neusoft.Client.Weather.Weather01.DB;

public class lx_Config {
	public static String CityName;
	public static String RefreshSpeed;
	public static String ProvideSmsService;
	public static String SaveSmsInfo;
	public static String KeyWord;

	public static void LoadDefaultConfig(){
		CityName = "¼ÃÄÏ";
		RefreshSpeed = "60";
		ProvideSmsService = "true";
		SaveSmsInfo = "true";
		KeyWord = "ÎÞµÐ";
	}

}
