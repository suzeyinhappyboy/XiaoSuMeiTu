package com.fzu.imageimocc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.facepp.error.FaceppParseException;
import com.fzu.imageimocc.howold.FaceDetect;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HowoldActivity extends Activity implements OnClickListener {

	private static final int PICK_CODE = 0x110;
	private ImageView mPhoto;
	private Button mGetImage;
	private Button mDetect;
	private TextView mTip;
	private View mWatting;
	private String mCurrentPhotoStr;
	private Bitmap mPhotoImg;
	private Paint mPaint;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_howold);
		
		initViews();
		initEvents();
		mPaint = new Paint();
	}
	private void initEvents() {
		mGetImage.setOnClickListener(this);
		mDetect.setOnClickListener(this);
	}
	private void initViews() {
		mPhoto = (ImageView) findViewById(R.id.photo);
		mGetImage = (Button) findViewById(R.id.GetImage);
		mDetect = (Button) findViewById(R.id.Detect);
		mTip = (TextView) findViewById(R.id.tip);
		mWatting = findViewById(R.id.waitting);
		mDetect.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_CODE) {
			if (data != null) {
				Uri uri = data.getData();
				Cursor cursor = getContentResolver().query(uri, null, null, null, null);
				cursor.moveToFirst();
				int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA	);
				mCurrentPhotoStr = cursor.getString(idx);
				cursor.close();				
				System.out.println("mCurrentPhotoStr="+mCurrentPhotoStr);
				
				resizePhoto();
				mPhoto.setImageBitmap(mPhotoImg);
				mDetect.setVisibility(View.VISIBLE);
				mTip.setText("click detect -->");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void resizePhoto() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoStr,options);
		double radio = Math.max((double)options.outWidth / 1024f, (double)options.outHeight / 1024f);
		//double radio = Math.max(options.outWidth * 1.0d / 1024f, options.outHeight * 1.0d / 1024f);
		options.inSampleSize = (int) Math.ceil(radio);
		System.out.println("options.inSampleSize="+options.inSampleSize);
		options.inJustDecodeBounds = false;
		mPhotoImg = BitmapFactory.decodeFile(mCurrentPhotoStr, options);
	}
	
	private static final int MSG_SUCCESS = 0x111;
	private static final int MSG_ERROR = 0x112;
	
	private Handler mHander = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SUCCESS:
				mWatting.setVisibility(View.GONE);
				JSONObject rs= (JSONObject) msg.obj;
				prepareRsBitmap(rs);
				mPhoto.setImageBitmap(mPhotoImg);
				break;
			case MSG_ERROR:
				mWatting.setVisibility(View.GONE);
				String errorMsg = (String) msg.obj;
				if (TextUtils.isEmpty(errorMsg)) {
					mTip.setText("ERROR");
				} else {
					mTip.setText(errorMsg);
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		};
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.GetImage:
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, PICK_CODE);
			break;
		case R.id.Detect:
			mWatting.setVisibility(View.VISIBLE);

			FaceDetect.detect(mPhotoImg, new FaceDetect.Callback() {
				
				@Override
				public void success(JSONObject result) {
					Message msg = Message.obtain();
					msg.what = MSG_SUCCESS;
					msg.obj = result;
					mHander.sendMessage(msg);
				}
				
				@Override
				public void error(FaceppParseException exception) {
					Message msg = Message.obtain();
					msg.what = MSG_ERROR;
					msg.obj = exception.getErrorMessage();
					mHander.sendMessage(msg);
					
				}
			});
			break;
		default:
			break;
		}
		
	}
	protected void prepareRsBitmap(JSONObject rs) {
		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth((float) 5.0);
		
		Bitmap bitmap = Bitmap.createBitmap(mPhotoImg.getWidth(),mPhotoImg.getHeight(),mPhotoImg.getConfig());
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(mPhotoImg,0,0,null);
		
		try {
			JSONArray faces = rs.getJSONArray("face");
			int facecount = faces.length();
			mTip.setText("find "+facecount+" face");
			for (int i = 0; i < facecount; i++) {
				JSONObject face = faces.getJSONObject(i);
				JSONObject posObj=face.getJSONObject("position");
				float x = (float) posObj.getJSONObject("center").getDouble("x");
				float y = (float) posObj.getJSONObject("center").getDouble("y");
				float w = (float) posObj.getDouble("width");
				float h = (float) posObj.getDouble("height");
				
				x = x/100 * bitmap.getWidth();
				y = y/100 * bitmap.getHeight();
				w = w/100 * bitmap.getWidth();
				h = h/100 * bitmap.getHeight();
				
				
				canvas.drawLine(x - w/2, y-h/2, x - w/2, y+h/2,mPaint );
				canvas.drawLine(x - w/2, y-h/2, x + w/2, y-h/2,mPaint );
				canvas.drawLine(x - w/2, y+h/2, x + w/2, y+h/2,mPaint );
				canvas.drawLine(x + w/2, y-h/2, x + w/2, y+h/2,mPaint );
				
				int age = face.getJSONObject("attribute").getJSONObject("age").getInt("value");
				String gender = face.getJSONObject("attribute").getJSONObject("gender").getString("value");
				Bitmap ageBitmap = buildAgeBitmap(age,"Male".equals(gender));
				
				int agewith = ageBitmap.getWidth();
				int ageheight = ageBitmap.getHeight();
				
				if (bitmap.getWidth()<mPhoto.getWidth() && bitmap.getHeight()<mPhoto.getHeight()) {
					double radio = Math.max(bitmap.getWidth()*1.0f/mPhoto.getWidth(), bitmap.getHeight()*1.0F/mPhoto.getHeight());
					ageBitmap = Bitmap.createScaledBitmap(ageBitmap, (int)(agewith*radio), (int)(ageheight*radio), false);
					
				} 
				canvas.drawBitmap(ageBitmap, x - ageBitmap.getWidth()/2,y - h/2 - ageBitmap.getHeight(), null);
				mPhotoImg = bitmap;
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private Bitmap buildAgeBitmap(int age, boolean isMale) {
		TextView tv = (TextView) findViewById(R.id.age_gender);
		tv.setText(age + "");
		if (isMale) {
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male), null, null, null);
		} else {
			tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female), null, null, null);
		}
		tv.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(tv.getDrawingCache());
		tv.destroyDrawingCache();
		return bitmap;
	}


}
