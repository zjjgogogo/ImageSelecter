package com.myzaker.imagescan.Imageload;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;


public class BitmapUtil {

	/**
	 * reset bitmap if it was rotated
	 * 
	 * @param bitmap
	 * 
	 * @param path
	 * 
	 */
	public static Bitmap resetPicRotate(Bitmap bitmap, String path) {
		int degree = getPicRotate(path);
		if (degree != 0) {
			Matrix m = new Matrix();
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			m.setRotate(degree); // 旋转angle度
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
		}
		return bitmap;
	}

	/**
	 * check the rotation angle for picture
	 * 
	 * @param path
	 * 
	 * @return rotate angle
	 */
	public static int getPicRotate(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}
