package com.myzaker.imagescan.localimageload;

import android.graphics.Bitmap;

public class LocalImageResponse {

	private boolean isSuccess = false;

	public Bitmap result;

	public ImageError error;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public interface Listener {
		public void onResponse(Bitmap response);
	}

	public interface ErrorListener {
		public void onErrorResponse(ImageError error);
	}

}
