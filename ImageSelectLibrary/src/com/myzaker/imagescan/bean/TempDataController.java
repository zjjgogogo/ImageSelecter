package com.myzaker.imagescan.bean;import java.util.ArrayList;import java.util.HashMap;import java.util.List;public class TempDataController {	static HashMap<String, List<ImageBean>> mTotalDatas = new HashMap<String, List<ImageBean>>();	static ArrayList<ImageFolderBean> mFolderDatas = new ArrayList<ImageFolderBean>();	static ArrayList<ImageBean> mAllImageDatas = new ArrayList<ImageBean>();	static ArrayList<ImageBean> mSelectImageDatas = new ArrayList<ImageBean>();	static ArrayList<ImageBean> mPreviewImageBeans = new ArrayList<ImageBean>();	public static ArrayList<ImageBean> getPreviewImageDatas() {		return mPreviewImageBeans;	}	public static HashMap<String, List<ImageBean>> getTotalDatas() {		return mTotalDatas;	}	public static ArrayList<ImageFolderBean> getFolderDatas() {		return mFolderDatas;	}	public static ArrayList<ImageBean> getAllImageDatas() {		return mAllImageDatas;	}	public static ArrayList<ImageBean> getSelectImageDatas() {		return mSelectImageDatas;	}	public static boolean isFolderInRecord(String folderName) {		if (mTotalDatas.containsKey(folderName)) {			return true;		}		return false;	}	public static void addSelectImage(ImageBean mImageBean) {		mSelectImageDatas.add(mImageBean);	}	public static void removeSelectImage(ImageBean mImageBean) {		mSelectImageDatas.remove(mImageBean);	}	public static void addFolderBean(ImageFolderBean mImageFolderBean) {		mFolderDatas.add(mImageFolderBean);	}	public static void addImageDataBean(String folderName, ImageBean mImageBean) {		List<ImageBean> mImageBeans = mTotalDatas.get(folderName);		if (mImageBeans == null) {			mImageBeans = new ArrayList<ImageBean>();			mTotalDatas.put(folderName, mImageBeans);		}		if (!mImageBeans.contains(mImageBean)) {			mImageBeans.add(mImageBean);			for (ImageFolderBean mFolderBean : mFolderDatas) {				if (folderName.equals(mFolderBean.getFolderName())) {					mFolderBean.setImageCounts(mImageBeans.size());				}			}			mAllImageDatas.add(mImageBean);		}	}	public static void clean() {		mFolderDatas.clear();		mTotalDatas.clear();		mAllImageDatas.clear();		mSelectImageDatas.clear();	}}