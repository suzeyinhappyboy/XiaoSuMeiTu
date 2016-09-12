package com.fzu.imageimocc.processing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
/**
 * 
 * @author Administrator
 *
 */
public class ImageProcess {

	private Bitmap mBitmap;
	private float[] mColorMatrix = {
			(float) 0.393,(float) 0.768,(float) 0.189,0,0,   
            (float) 0.349,(float) 0.686,(float) 0.168,0,0,   
            (float) 0.272,(float) 0.534,(float) 0.131,0,0,   
            0,0,0,1,0};  
	public ImageProcess(Bitmap bitmap) {
		this.mBitmap = bitmap;
	}
		
	
	/**
	 * 怀旧效果
	 * @return
	 */
	public Bitmap Method_Reminiscence() {
		Bitmap bmp = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        android.graphics.ColorMatrix colorMatrix = new android.graphics.ColorMatrix();
        colorMatrix.set(mColorMatrix);

        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(mBitmap, 0, 0, paint);
		return bmp;
		
	}
	
	/** 
     * 光晕效果 
     * @param bmp 
     * @param x 光晕中心点在bmp中的x坐标 
     * @param y 光晕中心点在bmp中的y坐标 
     * @param r 光晕的半径 
     * @return 
     */  
    public Bitmap Method_Fuzzy(int x, int y, float r)  
    {  
        long start = System.currentTimeMillis();  
        // 高斯矩阵  
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };  
          
        int width = mBitmap.getWidth();  
        int height = mBitmap.getHeight();  
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);  
          
        int pixR = 0;  
        int pixG = 0;  
        int pixB = 0;  
          
        int pixColor = 0;  
          
        int newR = 0;  
        int newG = 0;  
        int newB = 0;  
          
        int delta = 18; // 值越小图片会越亮，越大则越暗  
          
        int idx = 0;  
        int[] pixels = new int[width * height];  
        mBitmap.getPixels(pixels, 0, width, 0, 0, width, height);  
        for (int i = 1, length = height - 1; i < length; i++)  
        {  
            for (int k = 1, len = width - 1; k < len; k++)  
            {  
                idx = 0;  
                int distance = (int) (Math.pow(k - x, 2) + Math.pow(i - y, 2));  
                // 不是中心区域的点做模糊处理  
                if (distance > r * r)  
                {  
                    for (int m = -1; m <= 1; m++)  
                    {  
                        for (int n = -1; n <= 1; n++)  
                        {  
                            pixColor = pixels[(i + m) * width + k + n];  
                            pixR = Color.red(pixColor);  
                            pixG = Color.green(pixColor);  
                            pixB = Color.blue(pixColor);  
                              
                            newR = newR + (int) (pixR * gauss[idx]);  
                            newG = newG + (int) (pixG * gauss[idx]);  
                            newB = newB + (int) (pixB * gauss[idx]);  
                            idx++;  
                        }  
                    }  
                      
                    newR /= delta;  
                    newG /= delta;  
                    newB /= delta;  
                      
                    newR = Math.min(255, Math.max(0, newR));  
                    newG = Math.min(255, Math.max(0, newG));  
                    newB = Math.min(255, Math.max(0, newB));  
                      
                    pixels[i * width + k] = Color.argb(255, newR, newG, newB);  
                      
                    newR = 0;  
                    newG = 0;  
                    newB = 0;  
                }  
            }  
        }  
          
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);  
        long end = System.currentTimeMillis();  
        System.out.println("used time="+(end - start));  
        return bitmap;  
    } 
    
    /** 
     * 图片锐化（拉普拉斯变换） 
     * @param bmp 
     * @return 
     */  
    public Bitmap Method_Sharpen()  
    {  
        long start = System.currentTimeMillis();  
        // 拉普拉斯矩阵  
        int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };  
          
        int width = mBitmap.getWidth();  
        int height = mBitmap.getHeight();  
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);  
          
        int pixR = 0;  
        int pixG = 0;  
        int pixB = 0;  
          
        int pixColor = 0;  
          
        int newR = 0;  
        int newG = 0;  
        int newB = 0;  
          
        int idx = 0;  
        float alpha = 0.3F;  
        int[] pixels = new int[width * height];  
        mBitmap.getPixels(pixels, 0, width, 0, 0, width, height);  
        for (int i = 1, length = height - 1; i < length; i++)  
        {  
            for (int k = 1, len = width - 1; k < len; k++)  
            {  
                idx = 0;  
                for (int m = -1; m <= 1; m++)  
                {  
                    for (int n = -1; n <= 1; n++)  
                    {  
                        pixColor = pixels[(i + n) * width + k + m];  
                        pixR = Color.red(pixColor);  
                        pixG = Color.green(pixColor);  
                        pixB = Color.blue(pixColor);  
                          
                        newR = newR + (int) (pixR * laplacian[idx] * alpha);  
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);  
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);  
                        idx++;  
                    }  
                }  
                  
                newR = Math.min(255, Math.max(0, newR));  
                newG = Math.min(255, Math.max(0, newG));  
                newB = Math.min(255, Math.max(0, newB));  
                  
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);  
                newR = 0;  
                newG = 0;  
                newB = 0;  
            }  
        }  
          
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);  
        long end = System.currentTimeMillis();  
        System.out.println("used time="+(end - start));  
        return bitmap;  
    }  
}
