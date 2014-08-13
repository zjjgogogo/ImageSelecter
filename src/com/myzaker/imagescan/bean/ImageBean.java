package com.myzaker.imagescan.bean;

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

	private String thumbailPath;

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

	public String getThumbailPath() {
		return thumbailPath;
	}

	public void setThumbailPath(String thumbailPath) {
		this.thumbailPath = thumbailPath;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

}
