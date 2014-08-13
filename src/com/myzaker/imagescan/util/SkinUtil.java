package com.myzaker.imagescan.util;import com.myzaker.imagescan.R;import android.content.Intent;import android.graphics.Color;public class SkinUtil {	public final static String KEY_HEADERBAR_BG_COLOR = "KEY_HEADERBAR_BG_COLOR";	public final static String KEY_HEADERBAR_TEXT_COLOR = "KEY_HEADERBAR_TEXT_COLOR";	public final static String KEY_HEADERBAR_FINISH_RES = "KEY_HEADERBAR_FINISH_RES";	public final static String KEY_CONTENT_BG_COLOR = "KEY_CONTENT_BG_COLOR";	public final static String KEY_GRID_ITEM_DEFAULT_RES = "KEY_GRID_ITEM_DEFAULT_RES";	public final static String KEY_GRID_ITEM_SELECT_COLOR = "KEY_GRID_ITEM_SELECT_COLOR";	public final static String KEY_PREVIEW_BOTTOM_TEXT_COLOR = "KEY_PREVIEW_BOTTOM_TEXT_COLOR";	public final static String KEY_PREVIEW_SELECT_RES = "KEY_PREVIEW_SELECT_RES";	public final static String KEY_PREVIEW_UNSELECT_RES = "KEY_PREVIEW_UNSELECT_RES";	public final static String KEY_SELECTPIC_BOTTOM_TEXT_COLOR = "KEY_SELECTPIC_BOTTOM_TEXT_COLOR";	public final static String KEY_SELECTPIC_BOTTOM_TEXT2_COLOR = "KEY_SELECTPIC_BOTTOM_TEXT2_COLOR";	public final static String KEY_CAMERA_ICON_RES = "KEY_CAMERA_ICON_RES";	public final static String KEY_PREVIEW_MASK_RES = "KEY_IMAGE_MASK_RES";	public final static String KEY_SELECTPIC_BOTTOM_TEXT_ICON_RES = "KEY_SELECTPIC_BOTTOM_TEXT_ICON_RES";	public int headerBarBgColor;	public int headerBarTextColor;	public int headerBarFinishRes;	public int contentBgColor;	public int gridItemSelectColor;	public int gridItemDefaultRes;	public int previewBottomTextColor;	public int previewSelectRes;	public int previewUnselectRes;	public int previewMaskRes;	public int selectPicBottomTextIconRes;	public int selectPicBottomTextColor;	public int selectPicBottomTextColor2;	public int cameraIconRes;	public SkinUtil() {		headerBarBgColor = 0xff00B4FD;		headerBarTextColor = 0xffffffff;		headerBarFinishRes = R.drawable.botton_text2_color;		contentBgColor = Color.WHITE;		gridItemSelectColor = 0x99046cd6;		gridItemDefaultRes = R.drawable.friends_sends_pictures_no;		previewBottomTextColor = 0xffbfbfbf;		previewSelectRes = R.drawable.yeschoose;		previewUnselectRes = R.drawable.nochoose;		selectPicBottomTextColor = 0xff848484;		selectPicBottomTextColor2 = 0xff00b4fd;		cameraIconRes = R.drawable.selector_share_camera;		selectPicBottomTextIconRes = R.drawable.arrow;		previewMaskRes = R.drawable.image_mask;	}	public SkinUtil(Intent mIntent) {		this();		headerBarBgColor = mIntent.getIntExtra(KEY_HEADERBAR_BG_COLOR,				headerBarBgColor);		headerBarTextColor = mIntent.getIntExtra(KEY_HEADERBAR_TEXT_COLOR,				headerBarTextColor);		headerBarFinishRes = mIntent.getIntExtra(KEY_HEADERBAR_FINISH_RES,				headerBarFinishRes);		contentBgColor = mIntent.getIntExtra(KEY_CONTENT_BG_COLOR,				contentBgColor);		gridItemSelectColor = mIntent.getIntExtra(KEY_GRID_ITEM_DEFAULT_RES,				gridItemSelectColor);		gridItemDefaultRes = mIntent.getIntExtra(KEY_GRID_ITEM_SELECT_COLOR,				gridItemDefaultRes);		previewBottomTextColor = mIntent.getIntExtra(				KEY_PREVIEW_BOTTOM_TEXT_COLOR, previewBottomTextColor);		previewSelectRes = mIntent.getIntExtra(KEY_PREVIEW_SELECT_RES,				previewSelectRes);		previewUnselectRes = mIntent.getIntExtra(KEY_HEADERBAR_BG_COLOR,				previewUnselectRes);		selectPicBottomTextColor = mIntent.getIntExtra(				KEY_SELECTPIC_BOTTOM_TEXT_COLOR, selectPicBottomTextColor);		selectPicBottomTextColor2 = mIntent.getIntExtra(				KEY_SELECTPIC_BOTTOM_TEXT2_COLOR, selectPicBottomTextColor2);		cameraIconRes = mIntent.getIntExtra(KEY_CAMERA_ICON_RES, cameraIconRes);		selectPicBottomTextIconRes = mIntent.getIntExtra(				KEY_SELECTPIC_BOTTOM_TEXT_ICON_RES, selectPicBottomTextIconRes);		previewMaskRes = mIntent.getIntExtra(				KEY_PREVIEW_MASK_RES, previewMaskRes);	}}