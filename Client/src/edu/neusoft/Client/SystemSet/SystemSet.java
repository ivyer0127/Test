package edu.neusoft.Client.SystemSet;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import edu.neusoft.Client.R;
import edu.neusoft.Client.Database.Config;
import edu.neusoft.Client.Database.DBAdapter;

public class SystemSet extends Activity{
	
    private EditText serverTel;
    private EditText serverIp;
    private EditText serverCom;
    private RadioButton smsMethodView;
    private RadioButton blueToothMethodView;
    private EditText cityNameView;
    private EditText refreshSpeedView;
    private CheckBox weatherServiceView;
    private CheckBox locationServiceView;
    private EditText startTimeView;
    private Button applyBtn;
    private Button resetBtn;
    public static DBAdapter dbAdapter;
    
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_set);
       
        serverTel = (EditText)findViewById(R.id.server_tel);
        serverIp = (EditText)findViewById(R.id.edit_ip);
        serverCom = (EditText)findViewById(R.id.edit_com);
        smsMethodView=(RadioButton)findViewById(R.id.radiobutton_sms);
        blueToothMethodView=(RadioButton)findViewById(R.id.radiobutton_blue);
        cityNameView=(EditText)findViewById(R.id.edit_city);
        refreshSpeedView=(EditText)findViewById(R.id.frequence);
        weatherServiceView=(CheckBox)findViewById(R.id.checkbox_plan);
        locationServiceView=(CheckBox)findViewById(R.id.checkbox01);
        startTimeView=(EditText)findViewById(R.id.planstart_time);
        applyBtn =(Button)findViewById(R.id.apply);
        resetBtn = (Button)findViewById(R.id.reset);
        
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        dbAdapter.LoadConfig();
        
        applyBtn.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
        		SetNewData();
        		UpdateUi();
        	}
        });
        
        resetBtn.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v){
        		dbAdapter.LoadConfig();
        		UpdateUi();
        	}
        });
        
        UpdateUi();
	}
	
	private void SetNewData(){
		Config.ServerTel = serverTel.getText().toString();
		if(smsMethodView.isChecked()==true)
			Config.ControlMethod = "sms";
		else {
			Config.ControlMethod = "bluetooth";
		}
		if (blueToothMethodView.isChecked()==true)
			Config.ControlMethod = "bluetooth";
		else {
			Config.ControlMethod = "sms";
		}
		Config.CityName = cityNameView.getText().toString();
		Config.RefreshSpeed = refreshSpeedView.getText().toString();
		
		if (weatherServiceView.isChecked() == true)
			Config.WeatherService = "true";
		else {
			Config.WeatherService = "false";
		}
		if (locationServiceView.isChecked()==true)
			Config.LocationService = "true";
		else {
			Config.LocationService = "false";
		}
		Config.StartTime = startTimeView.getText().toString();
		Config.ServerIP = serverIp.getText().toString();
		Config.ServerCom = serverCom.getText().toString();
		
		dbAdapter.SaveConfig();
	}
	
	private void UpdateUi(){
		serverTel.setText(Config.ServerTel);
		
		if(Config.ControlMethod.equals("sms")==true){
			//smsMethodView.setChecked(false);
			//blueToothMethodView.setChecked(true);
		}
		locationServiceView.setChecked(Config.LocationService.equals("true")?true:false);
		cityNameView.setText(Config.CityName);
		refreshSpeedView.setText(Config.RefreshSpeed);
		weatherServiceView.setChecked(Config.WeatherService.equals("true")?true:false);
		startTimeView.setText(Config.StartTime);
		serverIp.setText(Config.ServerIP);
		serverCom.setText(Config.ServerCom);
	}
}
