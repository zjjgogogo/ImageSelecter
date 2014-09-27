package com.myzaker.imagescan.localimageload;

import android.graphics.Bitmap;
import android.renderscript.RenderScript.Priority;

import com.myzaker.imagescan.localimageload.LocalImageResponse.ErrorListener;
import com.myzaker.imagescan.localimageload.LocalImageResponse.Listener;

public class LocalImageRequest implements Comparable<LocalImageRequest> {

	private boolean isCanceled = false;

	private LocalImageLoadQueue mRequestQueue;

	private Integer mSequence;

	private String url;

	private int maxWidth;

	private int maxHeight;

	private Listener mListener;

	private ErrorListener mErrorListener;

	public LocalImageRequest(String url, Listener listener, int maxWidth,
			int maxHeight, ErrorListener mErrorListener) {

		this.url = url;
		this.mListener = listener;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.mErrorListener = mErrorListener;
	}

	public boolean isCanceled() {
		return isCanceled;
	}

	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	public void deliverResponse(Bitmap bitmap) {
		if (mListener != null) {
			mListener.onResponse(bitmap);
		}
	}

	public void deliverError(ImageError error) {
		if (mErrorListener != null) {
			mErrorListener.onErrorResponse(error);
		}
	}

	public void finish() {
		if (mRequestQueue != null) {
			mRequestQueue.finish(this);
		}
	}

	public LocalImageLoadQueue getmRequestQueue() {
		return mRequestQueue;
	}

	public void setmRequestQueue(LocalImageLoadQueue mRequestQueue) {
		this.mRequestQueue = mRequestQueue;
	}

	public Integer getmSequence() {
		return mSequence;
	}

	public void setmSequence(Integer mSequence) {
		this.mSequence = mSequence;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public void cancel() {
		isCanceled = true;
	}

	public void setRequestQueue(LocalImageLoadQueue mRequestQueue) {
		this.mRequestQueue = mRequestQueue;
	}

	public final void setSequence(int sequence) {
		mSequence = sequence;
	}

	@Override
	public int compareTo(LocalImageRequest another) {

		return this.mSequence - another.mSequence;
	}
}
