package com.myzaker.imagescan.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

public class BitmapUtil {

	/**
	 * read a bitmap from path
	 * 
	 * @param context
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmap(Context context, String filePath) {

		DisplayMetrics mDisplayMetrics = context.getResources()
				.getDisplayMetrics();

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; 
		options.inSampleSize = calculateInSampleSize(options,
				mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 
	 * calculate the pic SampleSize
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1; 
		if (height > reqHeight || width > reqWidth) {

			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

}
