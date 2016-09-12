package com.fzu.imageimocc.howold;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpRequest;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.graphics.Bitmap;

public class FaceDetect {

	public interface Callback{
		void success(JSONObject result);
		void error(FaceppParseException exception);
	}
	
	public static void detect(final Bitmap bm,final Callback callback) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					HttpRequests requests = new HttpRequests(Constant.KEY,Constant.SECRET,true,true);
					Bitmap bmsmall = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bmsmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					
					byte[] arrays = stream.toByteArray();
					PostParameters parameters = new PostParameters();
					parameters.setImg(arrays);
					JSONObject jsonObject = requests.detectionDetect(parameters);
					System.out.println("jsonObject.toString()="+jsonObject.toString());
					if (callback!=null) {
						callback.success(jsonObject);
					}
				} catch (FaceppParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (callback!=null) {
						callback.error(e);
					}
				}
			}
		}).start();
	}
}
