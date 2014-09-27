package com.myzaker.imagescan.localimageload;

import com.myzaker.imagescan.localimageload.LocalImageLoader.LocalImageContainer;

public interface ImageListener {

	public void onErrorResponse(ImageError error);

	public void onResponse(LocalImageContainer response, boolean isImmediate);
}
