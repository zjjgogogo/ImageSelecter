package com.myzaker.imagescan.bean;


/**
 * the image folder's info
 * 
 * @author James
 * 
 */
public class ImageFolderBean {

	/**
	 * the first pic path in this folder
	 */
	private String topImagePath;

	/**
	 * the name of the folder
	 */
	private String folderName;
	/**
	 * the num of the pics in this folder
	 */
	private int imageCounts;

	public String getTopImagePath() {
		return topImagePath;
	}

	public void setTopImagePath(String topImagePath) {
		this.topImagePath = topImagePath;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public int getImageCounts() {
		return imageCounts;
	}

	public void setImageCounts(int imageCounts) {
		this.imageCounts = imageCounts;
	}

}
