package com.myzaker.imagescan.Imageload;

import com.myzaker.imagescan.Imageload.LocalImageLoader.LocalImageContainer;

public interface ImageListener {

	public void onErrorResponse(ImageError error);

	public void onResponse(LocalImageContainer response, boolean isImmediate);
}
