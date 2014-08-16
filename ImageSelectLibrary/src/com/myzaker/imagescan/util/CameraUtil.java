package com.myzaker.imagescan.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.myzaker.imagescan.R;
import com.myzaker.imagescan.ShowImageActivity;
import com.myzaker.imagescan.bean.ImageBean;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 
 * Camera util
 * 
 * @author James
 */
public class CameraUtil {

	String lastTakePicLocation;

	public CameraUtil() {
		super();
	}

	public void cameraInit(Activity activity) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File f = null;

		try {

			Locale mLocale = activity.getResources().getConfiguration().locale;

			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", mLocale)
					.format(new Date());

			File albumF = StorageUtils.getCacheDirectory(activity);

			f = File.createTempFile(timeStamp, ".jpg", albumF);

			lastTakePicLocation = f.getAbsolutePath();

			takePictureIntent
					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

		} catch (IOException e) {
			e.printStackTrace();
			lastTakePicLocation = null;
		}

		try {
			activity.startActivityForResult(takePictureIntent,
					ShowImageActivity.REQUEST_CODE_CAMERA);
		} catch (Exception e) {
			Toast.makeText(activity, R.string.cannot_show_camera,
					Toast.LENGTH_SHORT).show();
		}
	}

	public ImageBean getlastTakedPic(Context context) {

		ImageBean mImageBean = null;

		String[] projection = new String[] {
				MediaStore.Images.ImageColumns._ID,
				MediaStore.Images.ImageColumns.DATA,
				MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
				MediaStore.Images.ImageColumns.DATE_TAKEN,
				MediaStore.Images.ImageColumns.MIME_TYPE };
		final Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

		// Put it in the image view
		if (cursor.moveToFirst()) {
			int imageId = cursor.getInt(0);
			String imageLocation = cursor.getString(1);
			File imageFile = new File(imageLocation);
			if (imageFile.exists()) {
				ImageBean mImageItem = new ImageBean();
				mImageItem.setImgId(imageId);
				mImageItem.setImagePath(imageLocation);
			}
		}

		if (mImageBean == null) {
			if (!TextUtils.isEmpty(lastTakePicLocation)) {
				File imageFile = new File(lastTakePicLocation);
				if (imageFile.exists()) {
					mImageBean = new ImageBean();
					mImageBean.setImagePath(lastTakePicLocation);
				}
			}
		}

		return mImageBean;
	}
}
