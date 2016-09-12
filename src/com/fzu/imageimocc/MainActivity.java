package com.fzu.imageimocc;

import com.fzu.imageimocc.processing.ColorMatrix;
import com.fzu.imageimocc.processing.PixelsEffect;
import com.fzu.imageimocc.processing.PrimaryColor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button btn_PrimaryColor;
	private Button btn_ColorMatrix;
	private Button btn_PixelsEffect;
	private Button btn_MeiTu;
	private Button btn_HowOld;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_PrimaryColor = (Button) findViewById(R.id.btn_PrimaryColor);
		btn_PrimaryColor.setOnClickListener(this);
		btn_ColorMatrix = (Button) findViewById(R.id.btn_ColorMatrix);
		btn_ColorMatrix.setOnClickListener(this);
		btn_PixelsEffect = (Button) findViewById(R.id.btn_PixelsEffect);
		btn_PixelsEffect.setOnClickListener(this);
		btn_MeiTu = (Button) findViewById(R.id.btn_MeiTu);
		btn_MeiTu.setOnClickListener(this);
		btn_HowOld = (Button) findViewById(R.id.btn_Howold);
		btn_HowOld.setOnClickListener(this);
		
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_PrimaryColor:
			startActivity(new Intent(this, PrimaryColor.class));
			break;
		case R.id.btn_ColorMatrix:
			startActivity(new Intent(this, ColorMatrix.class));
			break;
		case R.id.btn_PixelsEffect:
			 startActivity(new Intent(this, PixelsEffect.class));
			break;
		case R.id.btn_MeiTu:
			 startActivity(new Intent(this, MeiTu.class));
			break;
		case R.id.btn_Howold:
			 startActivity(new Intent(this, HowoldActivity.class));
			break;
		}
		
	}



}
