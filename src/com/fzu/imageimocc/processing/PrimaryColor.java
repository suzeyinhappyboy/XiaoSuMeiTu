package com.fzu.imageimocc.processing;

import com.fzu.imageimocc.R;
import com.fzu.imageimocc.R.drawable;
import com.fzu.imageimocc.R.id;
import com.fzu.imageimocc.R.layout;
import com.fzu.imageimocc.ThemeGroupLaout.ImageHelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PrimaryColor extends Activity implements OnSeekBarChangeListener{

	private ImageView mImageView;
	private SeekBar mSeekBarHue,mSeekBarStaration,mSeekBarLum;
	private static int MAX_VALUE = 255;
	private static int MID_VALUE = 127;
	private float mHue,mStauration,mLum;//色调，饱和度，亮度
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.primary_color);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test3);
		mImageView = (ImageView) findViewById(R.id.imageView);
		mImageView.setImageBitmap(bitmap);
		//System.out.println("bitmap.getWidth()="+bitmap.getWidth());
		initview();
		
	}
	private void initview() {				
		mSeekBarHue = (SeekBar) findViewById(R.id.seekBarHue);
		mSeekBarStaration = (SeekBar) findViewById(R.id.seekBarSturation);
		mSeekBarLum = (SeekBar) findViewById(R.id.seekBarLum);
		mSeekBarHue.setOnSeekBarChangeListener(this);
		mSeekBarStaration.setOnSeekBarChangeListener(this);
		mSeekBarLum.setOnSeekBarChangeListener(this);
		mSeekBarHue.setMax(MAX_VALUE);
		mSeekBarStaration.setMax(MAX_VALUE);
		mSeekBarLum.setMax(MAX_VALUE);
		mSeekBarHue.setProgress(MID_VALUE);
		mSeekBarStaration.setProgress(MID_VALUE);
		mSeekBarLum.setProgress(MID_VALUE);
				
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.seekBarHue:
			mHue = (progress - MID_VALUE) * 1.0f / MID_VALUE * 180;
			break;
		case R.id.seekBarSturation:
			mStauration = progress * 1.0f / MID_VALUE;
			break;
		case R.id.seekBarLum:
			mLum = progress * 1.0f / MID_VALUE;
			break;
		}
		System.out.println("mHue="+mHue);
		System.out.println("mStauration="+mStauration);
		System.out.println("mLum="+mLum);
		mImageView.setImageBitmap(ImageHelper.handleImageEffect(bitmap, mHue, mStauration, mLum));
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

}
