package com.zonesion.environmenttemhumi;

import com.zhiyun360.wsn.droid.WSNRTConnect;
import com.zhiyun360.wsn.droid.WSNRTConnectListener;
import com.zonesion.environmenttemhumi.AboutActivity;
import com.zonesion.environmenttemhumi.DialChart04View_hum;
import com.zonesion.environmenttemhumi.DialChart04View_temper;
import com.zonesion.environmenttemhumi.R;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;


public class TemperHum extends Activity{
	DialChart04View_hum chart_hum = null;
	DialChart04View_temper chart_temper = null;
	private TextView connect_statement;
	private EditText minfs;
	private EditText maxfs;
	private EditText minfx;
	private EditText maxfx;
	private ImageView jingbaoImageView;
	private ImageView jingbaoImageView1;
	private Button bjcmd;
	private Button bjcmd1;
	private WSNRTConnect wRTConnect;                            //创建WSNRTConnect实例
	private String mMac = "00:12:4B:00:05:52:77:EB";            //温湿度传感器节点IEEE地址
	private String ID = "12345678";                           //用户账号
	private String KEY = "12345678";    //用户密钥
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temperhum);
		chart_hum = (DialChart04View_hum)findViewById(R.id.circle_view2);
		chart_temper = (DialChart04View_temper)findViewById(R.id.circle_view);
		connect_statement  = (TextView) findViewById(R.id.connect);
		jingbaoImageView = (ImageView)findViewById(R.id.imageView1);
		jingbaoImageView1 = (ImageView)findViewById(R.id.bjimage2);
		wRTConnect = new WSNRTConnect(ID, KEY); 				//创建wRTConnect链接，并初始化
		wRTConnect.setServerAddr("zhiyun360.com:28081"); 		//设置智云服务地址
		//设置监听
		wRTConnect.setRTConnectListener(new WSNRTConnectListener() { 
			@Override
			//处理接收到的无线数据包
			public void onMessageArrive(String mac, byte[] dat) {
				onSensorData(mac, dat);
			}

			@Override
			//智云服务断开链接的处理
			public void onConnectLost(Throwable arg0) {
				Toast.makeText(TemperHum.this, "断开连接", Toast.LENGTH_SHORT).show();
				connect_statement.setText("数据服务连接失败！");
			}

			@Override
			//智云服务链接建立成功处理
			public void onConnect() {
				// TODO Auto-generated method stub
				Toast.makeText(TemperHum.this, "连接网关成功", Toast.LENGTH_SHORT).show();
				connect_statement.setText("数据服务连接成功！");
				if (mMac.length() > 0) {
					//查询当前传感器数值
					wRTConnect.sendMessage(mMac, "{A0=?,A1=?}".getBytes()); 
					Toast.makeText(TemperHum.this, "{A0=?,A1=?}", Toast.LENGTH_SHORT).show();
				}
			}
		});

		wRTConnect.connect(); 									//与智云服务建立链接
	
	}
	@Override
	public void onDestroy() {
		wRTConnect.disconnect(); 								//断开智云链接
		super.onDestroy();
	}
	//解析收到的数据，获取当前温湿度值
	public void onSensorData(String mac, byte[] dat) {
		System.out.println("mac:" + mac + ",dat:" + new String(dat));
		String data = new String(dat);
		//data = data.substring(0);
		if (data.charAt(0) != '{') { 							//判断首字符是否为‘{’
			return;
		}
		if (data.charAt(data.length() - 1) != '}') 				//判断尾字符是否为‘}’
			return;
		data = data.substring(1, data.length() - 1); 			//截取{}内的字符串

		//数据格式 A0=25,A1=34.0
		String[] tags = data.split(","); 						//以‘,’来分割字符串
		for (String tag : tags) { 								//循环提取每项键值对，比如A0=25
			String[] cv = tag.split("="); 						//以‘=’来分割字符串
			if (cv.length != 2)
				continue;
			if (mMac.equalsIgnoreCase(mac)) { 					//判断Mac地址
				if (cv[0].equals("A0")) { 						//判断参数A0
					float v = Float.parseFloat(cv[1]);
					chart_temper.setCurrentStatus(v/100f);
					chart_temper.invalidate();
					if(bjflag == 1){
						if(v < fsmin || v > fsmax){            	//设置报警限定条件
							jingbaoImageView.setImageResource(R.drawable.alarm_activ);
							Toast.makeText(TemperHum.this, "温度超限", Toast.LENGTH_SHORT).show();
						}else{
							jingbaoImageView.setImageResource(R.drawable.alarm_on);
							Toast.makeText(TemperHum.this, "温度合理", Toast.LENGTH_SHORT).show();
						}
					}else{
						jingbaoImageView.setImageResource(R.drawable.alarm_off);
					}
				}
				if (cv[0].equals("A1")) { 						//判断参数A0
					float v = Float.parseFloat(cv[1]);
					chart_hum.setCurrentStatus(v/100f);
					chart_hum.invalidate();
					if(bjflag1 == 1){
						if(v < fxmin || v > fxmax){
							jingbaoImageView1.setImageResource(R.drawable.alarm_activ);
							Toast.makeText(TemperHum.this, "湿度超限", Toast.LENGTH_SHORT).show();
						}else{
							jingbaoImageView1.setImageResource(R.drawable.alarm_on);
							Toast.makeText(TemperHum.this, "湿度合理", Toast.LENGTH_SHORT).show();
						}
					}else{
						jingbaoImageView1.setImageResource(R.drawable.alarm_off);
					}
				}
			}
		}
	}
	
	int fsmin = 0,fsmax = 0,fxmin = 0,fxmax = 0;
	char bjflag = 0,bjflag1 = 0;
	//功能：查询当前温湿度传感器数值
	public void dataCollection(View v) {
		wRTConnect.sendMessage(mMac, "{A0=?,A1=?}".getBytes());
		Toast.makeText(TemperHum.this, "{A0=?,A1=?}", Toast.LENGTH_SHORT).show();
	}

	//功能：设置或取消报警
		public void button1(View v){
			bjcmd = (Button)findViewById(R.id.button1);
			if(bjflag == 0){
				minfs = (EditText)findViewById(R.id.editText1);
				maxfs = (EditText)findViewById(R.id.editText2);
				String min = minfs.getText().toString();
				String max = maxfs.getText().toString();
				if(min.equals("") || max.equals("") )
				{
					Toast.makeText(TemperHum.this, "请输入合理参数", Toast.LENGTH_SHORT).show();
				}else{
					fsmin = Integer.parseInt(min);
					fsmax = Integer.parseInt(max);
					if(fsmin > fsmax){
						Toast.makeText(TemperHum.this, "请输入合理参数", Toast.LENGTH_SHORT).show();
					}else{
						bjflag = 1 ;
						bjcmd.setText("关闭报警");
					}
				}
			}else{
				bjflag = 0;
				bjcmd.setText("开启报警");
			}
			wRTConnect.sendMessage(mMac, "{A0=?,A1=?}".getBytes());
		}
		//功能：设置或取消报警
		public void button2(View v) {
			bjcmd1 = (Button)findViewById(R.id.button2);
			if(bjflag1 == 0){
				minfx = (EditText)findViewById(R.id.editText3);
				maxfx = (EditText)findViewById(R.id.editText4);
				String min = minfx.getText().toString();
				String max = maxfx.getText().toString();
				if(min.equals("") || max.equals("") ){
					Toast.makeText(TemperHum.this, "请输入合理参数", Toast.LENGTH_SHORT).show();
				}else{
					fxmin = Integer.parseInt(min);
					fxmax = Integer.parseInt(max);
					if(fxmin > fxmax){
						Toast.makeText(TemperHum.this, "请输入合理参数", Toast.LENGTH_SHORT).show();
					}else{
						bjflag1 = 1 ;
						bjcmd1.setText("关闭报警");
					}
				}
			}else{
				bjflag1 = 0;
				bjcmd1.setText("开启报警");
			}
			wRTConnect.sendMessage(mMac, "{A0=?,A1=?}".getBytes());
		}

	public void aboutUs(View v) {
		Intent intent = new Intent();
		intent.setClass(this, AboutActivity.class);
		this.startActivity(intent);
		//实现activity切换时的动画效果
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); 
	}

	public ImageView getJingbaoImageView() {
		return jingbaoImageView;
	}

	public void setJingbaoImageView(ImageView jingbaoImageView) {
		this.jingbaoImageView = jingbaoImageView;
	}
}  
