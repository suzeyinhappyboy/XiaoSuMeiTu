package com.fzu.imageimocc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fzu.imageimocc.MyHorizontalScrollView.CurrentImageChangeListener;
import com.fzu.imageimocc.MyHorizontalScrollView.OnItemClickListener;
import com.fzu.imageimocc.ThemeGroupLaout.ThemeGroupLayout;
import com.fzu.imageimocc.processing.ImageProcess;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MeiTu extends Activity implements OnClickListener {

	private static final int FLAG_CHOOSE = 0x001;
	private TextView tv_method;
	private ThemeGroupLayout mThemes;
	private ThemeGroupLayout mFilters;
	private View mLoadingView;
	private View mThemeLayout;
	private View mFilterLayout;
	public  static  ImageView iv_image;
	private TextView mTitleLeft;
	private TextView mTitleNext;
	private TextView mTitleText;
	private Bitmap mBitmap;
	private Bitmap mTmpBmp;
	ImageProcess mImageProcess;
	private MyHorizontalScrollView mHorizontalScrollView1,mHorizontalScrollView2;
	private HorizontalScrollViewAdapter mAdapter1,mAdapter2;
	
	private List<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(
			R.drawable.dreamworld, R.drawable.fashion, R.drawable.hawaii, R.drawable.love,
			R.drawable.magic, R.drawable.pulse, R.drawable.shining, R.drawable.sunshine));
	private List<Integer> mMethods = new ArrayList<Integer>(Arrays.asList(
			R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
			R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight,
			R.drawable.nine, R.drawable.ten, R.drawable.eleven));
	private List<String> mMethodNames =  new ArrayList<String>(Arrays.asList("涂鸦","加边框","锐化",
			"怀旧","背景虚化","浮雕","底片","裁剪"));
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meitu);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// ��ֹ����
		Intent intent = new Intent();//创建Intent对象
		intent.setAction(Intent.ACTION_PICK);//设置Intent的Action - 调用系统的选择界面Gallery的Action  "Intent.ACTION_PICK"
		intent.setType("image/*");// 选择图片
		startActivityForResult(intent, FLAG_CHOOSE);
		
		prepareViews();
		
		mHorizontalScrollView1 = (MyHorizontalScrollView) findViewById(R.id.theme_layout);
		mAdapter1 = new HorizontalScrollViewAdapter(this, mDatas);
		//添加滚动回调
/*		mHorizontalScrollView1
				.setCurrentImageChangeListener(new CurrentImageChangeListener()
				{
					@Override
					public void onCurrentImgChanged(int position,
							View viewIndicator)
					{
						mImg.setImageResource(mDatas.get(position));
						viewIndicator.setBackgroundColor(Color
								.parseColor("#AA024DA4"));
					}
				});*/
		//添加点击回调
		mHorizontalScrollView1.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onClick(View view, int position)
			{
				mImageProcess = new ImageProcess(mBitmap);
				int width = mBitmap.getWidth();  
		        int height = mBitmap.getHeight();
				view.setBackgroundColor(Color.BLACK);
				tv_method.setText(mMethodNames.get(position));
				switch (position) {
				case 0:
					
					break;
				case 1:
					
					break;
				case 2:
					iv_image.setImageBitmap(mImageProcess.Method_Sharpen());
					break;
				case 3:
					
					iv_image.setImageBitmap(mImageProcess.Method_Reminiscence());
					break;
				case 4:
					iv_image.setImageBitmap(mImageProcess.Method_Fuzzy(width/2, height/2, 200));
					break;
				case 5:
					
					break;
				case 6:
					
					break;
				case 7:
					
					break;
				}
			}
		});
		//设置适配器
		mHorizontalScrollView1.initDatas(mAdapter1);
		
		/*mHorizontalScrollView2 = (MyHorizontalScrollView) findViewById(R.id.filter_layout);
		mAdapter1 = new HorizontalScrollViewAdapter(this, mMethods);
		//添加滚动回调
		mHorizontalScrollView2
				.setCurrentImageChangeListener(new CurrentImageChangeListener()
				{
					@Override
					public void onCurrentImgChanged(int position,
							View viewIndicator)
					{
						mImg.setImageResource(mDatas.get(position));
						viewIndicator.setBackgroundColor(Color
								.parseColor("#AA024DA4"));
					}
				});
		//添加点击回调
		mHorizontalScrollView2.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onClick(View view, int position)
			{
				//mImg.setImageResource(mDatas.get(position));
				view.setBackgroundColor(Color.BLACK);
			}
		});
		//设置适配器
		mHorizontalScrollView2.initDatas(mAdapter2);*/
		
	}	
	
	private void prepareViews() {
		mTitleLeft = (TextView) findViewById(R.id.titleLeft);
		mTitleNext = (TextView) findViewById(R.id.titleRight);
		mTitleText = (TextView) findViewById(R.id.titleText);
		iv_image = (ImageView) findViewById(R.id.iv_image);		
		tv_method = (TextView) findViewById(R.id.tv_method);
		//mThemes = (ThemeGroupLayout) findViewById(R.id.themes);
		//mFilters = (ThemeGroupLayout) findViewById(R.id.filters);
		mLoadingView = findViewById(R.id.loading);
		mThemeLayout = findViewById(R.id.theme_layout);
		mFilterLayout = findViewById(R.id.filter_layout);
		
		mTitleLeft.setOnClickListener(this);
		mTitleNext.setOnClickListener(this);
		mTitleText.setOnClickListener(this);
		findViewById(R.id.tab_theme).setOnClickListener(this);
		findViewById(R.id.tab_filter).setOnClickListener(this);

		//mTitleText.setText(R.string.record_camera_preview_title);
		mTitleNext.setText(R.string.record_camera_preview_next);
		
		/** 设置图片区域 */
/*		View preview_layout = findViewById(R.id.preview_layout);
		LinearLayout.LayoutParams mPreviewParams = (LinearLayout.LayoutParams) preview_layout
				.getLayoutParams();
		mPreviewParams.height = DeviceUtils.getScreenWidth(this);*/
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && null != data) {
			switch (requestCode) {
			case FLAG_CHOOSE: 
				Uri uri = data.getData();
				System.out.println("uri="+uri);
				Cursor cursor = getContentResolver().query(uri,null,null, null, null);
				cursor.moveToFirst();
				String path = cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
				System.out.println("path="+path);
				mBitmap = BitmapFactory.decodeFile(path);
				mTmpBmp = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
				iv_image.setImageBitmap(mTmpBmp);
				break;
			}
		}
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
