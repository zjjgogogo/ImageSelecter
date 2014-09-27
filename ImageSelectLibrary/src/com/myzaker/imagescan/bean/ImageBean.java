package com.myzaker.imagescan.bean;

import android.text.TextUtils;

/**
 * 
 * the pic info for each pic, which is in each folder
 * 
 * @author James
 * 
 * 
 */
public class ImageBean {

	private int imgId;

	private String imagePath;

	private String thumbnailPath;

	private boolean isSelect = false;

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getImagePath() { 
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getThumbnailPath() {
		if(!TextUtils.isEmpty(thumbnailPath))
		{
			return thumbnailPath;
		}
		return getImagePath();
	}

	public void setThumbnailPath(String thumbailPath) {
		this.thumbnailPath = thumbailPath;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

}
