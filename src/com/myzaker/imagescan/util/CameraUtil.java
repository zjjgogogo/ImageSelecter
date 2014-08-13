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
import android.widget.Toast;

import com.myzaker.imagescan.R;
import com.myzaker.imagescan.ShowImageActivity;

/**
 * 
 * Camera util
 * 
 * @author James
 */
public class CameraUtil {

	private Activity activity;
	private String mCurrentPhotoPath;

	public CameraUtil(Activity activity) {
		super();
		this.activity = activity;
	}

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

	private String getAlbumName() {
		return activity.getString(R.string.app_name);
	}

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

		try {
			activity.startActivityForResult(takePictureIntent,
					ShowImageActivity.REQUEST_CODE_CAMERA);
		} catch (Exception e) {
			Toast.makeText(activity, R.string.cannot_show_camera,
					Toast.LENGTH_SHORT).show();
		}
	}

	public String getmCurrentPhotoPath() {
		return mCurrentPhotoPath;
	}

	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
}
