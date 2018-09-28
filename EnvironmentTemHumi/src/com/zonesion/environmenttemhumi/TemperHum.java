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
	private WSNRTConnect wRTConnect;                            //����WSNRTConnectʵ��
	private String mMac = "00:12:4B:00:05:52:77:EB";            //��ʪ�ȴ������ڵ�IEEE��ַ
	private String ID = "12345678";                           //�û��˺�
	private String KEY = "12345678";    //�û���Կ
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temperhum);
		chart_hum = (DialChart04View_hum)findViewById(R.id.circle_view2);
		chart_temper = (DialChart04View_temper)findViewById(R.id.circle_view);
		connect_statement  = (TextView) findViewById(R.id.connect);
		jingbaoImageView = (ImageView)findViewById(R.id.imageView1);
		jingbaoImageView1 = (ImageView)findViewById(R.id.bjimage2);
		wRTConnect = new WSNRTConnect(ID, KEY); 				//����wRTConnect���ӣ�����ʼ��
		wRTConnect.setServerAddr("zhiyun360.com:28081"); 		//�������Ʒ����ַ
		//���ü���
		wRTConnect.setRTConnectListener(new WSNRTConnectListener() { 
			@Override
			//������յ����������ݰ�
			public void onMessageArrive(String mac, byte[] dat) {
				onSensorData(mac, dat);
			}

			@Override
			//���Ʒ���Ͽ����ӵĴ���
			public void onConnectLost(Throwable arg0) {
				Toast.makeText(TemperHum.this, "�Ͽ�����", Toast.LENGTH_SHORT).show();
				connect_statement.setText("���ݷ�������ʧ�ܣ�");
			}

			@Override
			//���Ʒ������ӽ����ɹ�����
			public void onConnect() {
				// TODO Auto-generated method stub
				Toast.makeText(TemperHum.this, "�������سɹ�", Toast.LENGTH_SHORT).show();
				connect_statement.setText("���ݷ������ӳɹ���");
				if (mMac.length() > 0) {
					//��ѯ��ǰ��������ֵ
					wRTConnect.sendMessage(mMac, "{A0=?,A1=?}".getBytes()); 
					Toast.makeText(TemperHum.this, "{A0=?,A1=?}", Toast.LENGTH_SHORT).show();
				}
			}
		});

		wRTConnect.connect(); 									//�����Ʒ���������
	
	}
	@Override
	public void onDestroy() {
		wRTConnect.disconnect(); 								//�Ͽ���������
		super.onDestroy();
	}
	//�����յ������ݣ���ȡ��ǰ��ʪ��ֵ
	public void onSensorData(String mac, byte[] dat) {
		System.out.println("mac:" + mac + ",dat:" + new String(dat));
		String data = new String(dat);
		//data = data.substring(0);
		if (data.charAt(0) != '{') { 							//�ж����ַ��Ƿ�Ϊ��{��
			return;
		}
		if (data.charAt(data.length() - 1) != '}') 				//�ж�β�ַ��Ƿ�Ϊ��}��
			return;
		data = data.substring(1, data.length() - 1); 			//��ȡ{}�ڵ��ַ���

		//���ݸ�ʽ A0=25,A1=34.0
		String[] tags = data.split(","); 						//�ԡ�,�����ָ��ַ���
		for (String tag : tags) { 								//ѭ����ȡÿ���ֵ�ԣ�����A0=25
			String[] cv = tag.split("="); 						//�ԡ�=�����ָ��ַ���
			if (cv.length != 2)
				continue;
			if (mMac.equalsIgnoreCase(mac)) { 					//�ж�Mac��ַ
				if (cv[0].equals("A0")) { 						//�жϲ���A0
					float v = Float.parseFloat(cv[1]);
					chart_temper.setCurrentStatus(v/100f);
					chart_temper.invalidate();
					if(bjflag == 1){
						if(v < fsmin || v > fsmax){            	//���ñ����޶�����
							jingbaoImageView.setImageResource(R.drawable.alarm_activ);
							Toast.makeText(TemperHum.this, "�¶ȳ���", Toast.LENGTH_SHORT).show();
						}else{
							jingbaoImageView.setImageResource(R.drawable.alarm_on);
							Toast.makeText(TemperHum.this, "�¶Ⱥ���", Toast.LENGTH_SHORT).show();
						}
					}else{
						jingbaoImageView.setImageResource(R.drawable.alarm_off);
					}
				}
				if (cv[0].equals("A1")) { 						//�жϲ���A0
					float v = Float.parseFloat(cv[1]);
					chart_hum.setCurrentStatus(v/100f);
					chart_hum.invalidate();
					if(bjflag1 == 1){
						if(v < fxmin || v > fxmax){
							jingbaoImageView1.setImageResource(R.drawable.alarm_activ);
							Toast.makeText(TemperHum.this, "ʪ�ȳ���", Toast.LENGTH_SHORT).show();
						}else{
							jingbaoImageView1.setImageResource(R.drawable.alarm_on);
							Toast.makeText(TemperHum.this, "ʪ�Ⱥ���", Toast.LENGTH_SHORT).show();
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
	//���ܣ���ѯ��ǰ��ʪ�ȴ�������ֵ
	public void dataCollection(View v) {
		wRTConnect.sendMessage(mMac, "{A0=?,A1=?}".getBytes());
		Toast.makeText(TemperHum.this, "{A0=?,A1=?}", Toast.LENGTH_SHORT).show();
	}

	//���ܣ����û�ȡ������
		public void button1(View v){
			bjcmd = (Button)findViewById(R.id.button1);
			if(bjflag == 0){
				minfs = (EditText)findViewById(R.id.editText1);
				maxfs = (EditText)findViewById(R.id.editText2);
				String min = minfs.getText().toString();
				String max = maxfs.getText().toString();
				if(min.equals("") || max.equals("") )
				{
					Toast.makeText(TemperHum.this, "������������", Toast.LENGTH_SHORT).show();
				}else{
					fsmin = Integer.parseInt(min);
					fsmax = Integer.parseInt(max);
					if(fsmin > fsmax){
						Toast.makeText(TemperHum.this, "������������", Toast.LENGTH_SHORT).show();
					}else{
						bjflag = 1 ;
						bjcmd.setText("�رձ���");
					}
				}
			}else{
				bjflag = 0;
				bjcmd.setText("��������");
			}
			wRTConnect.sendMessage(mMac, "{A0=?,A1=?}".getBytes());
		}
		//���ܣ����û�ȡ������
		public void button2(View v) {
			bjcmd1 = (Button)findViewById(R.id.button2);
			if(bjflag1 == 0){
				minfx = (EditText)findViewById(R.id.editText3);
				maxfx = (EditText)findViewById(R.id.editText4);
				String min = minfx.getText().toString();
				String max = maxfx.getText().toString();
				if(min.equals("") || max.equals("") ){
					Toast.makeText(TemperHum.this, "������������", Toast.LENGTH_SHORT).show();
				}else{
					fxmin = Integer.parseInt(min);
					fxmax = Integer.parseInt(max);
					if(fxmin > fxmax){
						Toast.makeText(TemperHum.this, "������������", Toast.LENGTH_SHORT).show();
					}else{
						bjflag1 = 1 ;
						bjcmd1.setText("�رձ���");
					}
				}
			}else{
				bjflag1 = 0;
				bjcmd1.setText("��������");
			}
			wRTConnect.sendMessage(mMac, "{A0=?,A1=?}".getBytes());
		}

	public void aboutUs(View v) {
		Intent intent = new Intent();
		intent.setClass(this, AboutActivity.class);
		this.startActivity(intent);
		//ʵ��activity�л�ʱ�Ķ���Ч��
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); 
	}

	public ImageView getJingbaoImageView() {
		return jingbaoImageView;
	}

	public void setJingbaoImageView(ImageView jingbaoImageView) {
		this.jingbaoImageView = jingbaoImageView;
	}
}  
