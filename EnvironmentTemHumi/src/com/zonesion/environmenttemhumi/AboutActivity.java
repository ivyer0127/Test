package com.zonesion.environmenttemhumi;

import com.zonesion.environmenttemhumi.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class AboutActivity extends Activity {
	ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);
		imageView = (ImageView) findViewById(R.id.imageView);
	}

}
