package com.myzaker.imagescan.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.myzaker.imagescan.R;
import com.myzaker.imagescan.ShowImageActivity;

/**
 * @author oywf
 * 
 */
public class CameraUtil {
	private Activity activity;
	// 当前照片的路径
	private String mCurrentPhotoPath;

	public CameraUtil(Activity activity) {
		super();
		this.activity = activity;
	}

	/**
	 * 获取SD卡路径，创建文件夹
	 * 
	 * @return file
	 */
	private File getAlbumDir() {
		File storageDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			String path = Environment.getExternalStorageDirectory() + "/"
					+ getAlbumName();
			storageDir = new File(path);

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraTemp", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v("storage", "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	/**
	 * 存放附件路径
	 * 
	 * @return
	 */
	private String getAlbumName() {
		return activity.getString(R.string.app_name);
	}

	/**
	 * 拍照
	 * 
	 * @param activity
	 */
	public void cameraInit(Activity activity) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File f = null;

		try {

			Locale mLocale = activity.getResources().getConfiguration().locale;

			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", mLocale)
					.format(new Date());

			File albumF = getAlbumDir();

			f = File.createTempFile(timeStamp, ".jpg", albumF);

			mCurrentPhotoPath = f.getAbsolutePath();

			takePictureIntent
					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

		} catch (IOException e) {
			e.printStackTrace();
			f = null;
			mCurrentPhotoPath = null;
		}

		activity.startActivityForResult(takePictureIntent,
				ShowImageActivity.REQUEST_CODE_CAMERA);
	}

	public String getmCurrentPhotoPath() {
		return mCurrentPhotoPath;
	}

	/**
	 * 根据路径删除图片
	 * 
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
}
