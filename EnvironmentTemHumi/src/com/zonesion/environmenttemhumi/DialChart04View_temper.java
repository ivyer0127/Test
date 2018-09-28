package com.zonesion.environmenttemhumi;

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.chart.DialChart;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotAttrInfo;
import org.xclcharts.renderer.plot.Pointer;
import org.xclcharts.view.GraphicalView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;

public class DialChart04View_temper extends GraphicalView {
	
	private String TAG = "DialChart04View";	
	private DialChart chart = new DialChart();
	private float mPercentage = 0.1f;
	
	public DialChart04View_temper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	public DialChart04View_temper(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initView();
	 }
	 
	 public DialChart04View_temper(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initView();
	 }
	 
	 private void initView()
	 {
		chartRender();
	 }
	 
	 @Override  
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
	        super.onSizeChanged(w, h, oldw, oldh);  
	        chart.setChartRange(w ,h ); 
	    }  		
			
	 public void chartRender()
		{
			try {								
							
				//设置标题背景			
				chart.setApplyBackgroundColor(true);
				chart.setBackgroundColor( Color.rgb(255, 255, 255) );
				//绘制边框
				chart.showRoundBorder();
						
				//设置当前百分比
				chart.getPointer().setPercentage(mPercentage);
				
				//设置指针长度
				chart.getPointer().setLength(0.7f,0.1f);
				
				//增加轴
				addAxis();						
				/////////////////////////////////////////////////////////////
				//增加指针
				addPointer();
				//设置附加信息
				addAttrInfo();
				/////////////////////////////////////////////////////////////								
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
			
		}
		
		public void addAxis()
		{
		
			List<String> rlabels  = new ArrayList<String>();
			int j=0;
			for(int i=-20;i<=80;)
			{
				if(0 == i || j == 1)
				{
					rlabels.add(Integer.toString(i));
					j = 0;
				}else{
					rlabels.add("");
					j++;
				}
										
				i+=5;
			}
			chart.addOuterTicksAxis(0.75f, rlabels);
		
			//环形颜色轴
			List<Float> ringPercentage = new ArrayList<Float>();				
			ringPercentage.add( 0.33f);
			ringPercentage.add( 0.33f);
			ringPercentage.add( 1 - 2 * 0.33f);
			
			List<Integer> rcolor  = new ArrayList<Integer>();				
			rcolor.add(Color.rgb(133, 206, 130));
			rcolor.add(Color.rgb(252, 210, 9));		
			rcolor.add(Color.rgb(229, 63, 56));	
			chart.addStrokeRingAxis(0.75f,0.65f, ringPercentage, rcolor);
																	
			chart.getPlotAxis().get(1).getFillAxisPaint().setColor(Color.rgb(255, 255, 255) );
			
			chart.getPlotAxis().get(0).setDetailModeSteps(4);
			chart.getPlotAxis().get(0).getTickLabelPaint().setColor(Color.GRAY);
			chart.getPlotAxis().get(0).getTickMarksPaint().setColor(Color.GRAY);
			chart.getPlotAxis().get(0).hideAxisLine();			
			chart.getPlotAxis().get(0).showAxisLabels();			
			
			chart.getPointer().setPointerStyle(XEnum.PointerStyle.TRIANGLE);
			chart.getPointer().setBaseRadius(10);
			chart.getPointer().getPointerPaint().setStrokeWidth(7);			
			chart.getPointer().getBaseCirclePaint().setColor(Color.YELLOW );
			
		}
		
		
	private void addAttrInfo() {
		/////////////////////////////////////////////////////////////
		PlotAttrInfo plotAttrInfo = chart.getPlotAttrInfo();
		// 设置附加信息

		Paint paintBT = new Paint();
		paintBT.setColor(Color.BLUE);
		paintBT.setTextAlign(Align.CENTER);
		paintBT.setTextSize(30);
		paintBT.setFakeBoldText(true);
		paintBT.setAntiAlias(true);
		plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM,"当前温度："+
		Float.toString(MathHelper.getInstance().round(mPercentage * 100, 2)) + "℃", 1f, paintBT);
	}
		
		public void addPointer()
		{				
		
		}
		public void setCurrentStatus(float percentage)
		{
			//清理
			chart.clearAll();
					
			mPercentage =  percentage;
			//设置当前百分比
			chart.getPointer().setPercentage(mPercentage + 0.2f);
			addAxis();						
			addPointer();
			addAttrInfo();
		}


		@Override
		public void render(Canvas canvas) {
			// TODO Auto-generated method stub
			 try{
		            chart.render(canvas);
		        } catch (Exception e){
		        	Log.e(TAG, e.toString());
		        }
		}

}
