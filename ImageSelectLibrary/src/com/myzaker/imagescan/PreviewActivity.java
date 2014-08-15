package com.myzaker.imagescan;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myzaker.imagescan.adapter.ImagePageAdapter;
import com.myzaker.imagescan.bean.ImageBean;
import com.myzaker.imagescan.bean.ImageFolderBean;
import com.myzaker.imagescan.bean.TempDataController;

public class PreviewActivity extends Activity {

	final String TAG = "PreviewActivity";

	public static final String KEY_PIC_INDEX = "KEY_PIC_INDEX";
	public static final String KEY_FOLD_INDEX = "KEY_FOLD_INDEX";
	public static final String KEY_IMAGE_PATH = "KEY_IMAGE_PATH";

	private TextView show_count;

	private Button btn_finish;

	private ViewPager mViewPager;

	private int currentIndex = -1;

	boolean isFromPreview = false;

	ImagePageAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();

		currentIndex = bundle.getInt(KEY_PIC_INDEX, currentIndex);

		int folderCurrentIndex = bundle.getInt(KEY_FOLD_INDEX, -1);

		setContentView(R.layout.activity_preview_image);

		show_count = (TextView) findViewById(R.id.show_count);
		btn_finish = (Button) findViewById(R.id.header_finish);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);

		List<ImageBean> images = null;
		if (folderCurrentIndex > 0) {
			ImageFolderBean mImageFolderBean = TempDataController
					.getFolderDatas().get(folderCurrentIndex - 1);
			images = TempDataController.getTotalDatas().get(
					mImageFolderBean.getFolderName());
			((TextView) findViewById(R.id.header_title))
					.setText(mImageFolderBean.getFolderName());
		} else if (folderCurrentIndex == 0) {
			((TextView) findViewById(R.id.header_title))
					.setText(R.string.all_images_str);
			images = TempDataController.getAllImageDatas();
		} else {
			isFromPreview = true;
			images = new ArrayList<ImageBean>(
					TempDataController.getPreviewImageDatas());
			TempDataController.getPreviewImageDatas().clear();
			((TextView) findViewById(R.id.header_title))
					.setText(R.string.preview_image_str);
			btn_finish.setText(R.string.preview_delete_btn);

		}

		mAdapter = new ImagePageAdapter(this);
		mAdapter.onSelectBtnClickListener(mSelectBtnClickListener);
		mAdapter.setPreView(folderCurrentIndex == -1);
		mAdapter.setPageDatas(images);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(mOnPageChangeListener);
		mViewPager.setCurrentItem(currentIndex);

		initViewSkin();
		if (folderCurrentIndex != -1) {
			updateShowCount();
			btn_finish.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setResult(ShowImageActivity.RESULT_CODE_PREVIEW_FINISH);
					finish();
				}
			});
		} else {
			btn_finish.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					List<ImageBean> images = mAdapter.getDatas();

					int position = mViewPager.getCurrentItem();
					images.remove(position);
					mAdapter = new ImagePageAdapter(PreviewActivity.this);
					mAdapter.setPageDatas(images);
					mAdapter.setPreView(true);
					mViewPager.setAdapter(mAdapter);
					mViewPager.setCurrentItem(position);
					if (images.isEmpty()) {
						onBackPressed();
					}

				}
			});
		}

	}

	OnClickListener mSelectBtnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			ImageBean mImageBean = mAdapter.getItem(currentIndex);

			if (mImageBean == null) {
				return;
			}

			ImageView mIcon = null;

			if (v instanceof ImageView) {

				mIcon = (ImageView) v;

			}

			if (mImageBean.isSelect()) {

				if (mIcon != null) {
					mIcon.setImageResource(ShowImageActivity.mSkinUtil.previewUnselectRes);
				}
				mImageBean.setSelect(false);

				TempDataController.removeSelectImage(mImageBean);

			} else {

				if (TempDataController.getSelectImageDatas().size() < ShowImageActivity.imagesMaxSize) {

					mImageBean.setSelect(true);

					TempDataController.addSelectImage(mImageBean);

					if (mIcon != null) {
						mIcon.setImageResource(ShowImageActivity.mSkinUtil.previewSelectRes);
					}

				} else {

					showToastTip(getString(R.string.pic_select_num_error,
							ShowImageActivity.imagesMaxSize));

				}
			}

			updateShowCount();

		}
	};

	OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			currentIndex = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	@Override
	public void onBackPressed() {

		if (!isFromPreview) {
			Intent mIntent = new Intent();
			mIntent.putExtra(ShowImageActivity.KEY_RETURN_SELECT, currentIndex);
			setResult(ShowImageActivity.RESULT_CODE_PREVIEW_BACK, mIntent);
			finish();
		} else {
			if (mAdapter != null) {
				TempDataController.getPreviewImageDatas().addAll(
						mAdapter.getDatas());
			}
			finish();
		}
	}

	protected void initViewSkin() {

		findViewById(R.id.header).setBackgroundColor(
				ShowImageActivity.mSkinUtil.headerBarBgColor);

		findViewById(R.id.content).setBackgroundColor(
				ShowImageActivity.mSkinUtil.contentBgColor);

		((TextView) findViewById(R.id.header_title))
				.setTextColor(ShowImageActivity.mSkinUtil.headerBarTextColor);
		show_count
				.setTextColor(ShowImageActivity.mSkinUtil.previewBottomTextColor);

		btn_finish.setTextColor(getResources().getColorStateList(
				ShowImageActivity.mSkinUtil.headerBarFinishRes));
	}

	protected void showToastTip(String tip) {
		Toast.makeText(PreviewActivity.this, tip, Toast.LENGTH_SHORT).show();
	}

	protected void updateShowCount() {

		ArrayList<ImageBean> mSelectList = TempDataController
				.getSelectImageDatas();

		final int selectNum = mSelectList.size();

		show_count.setText(getResources().getString(R.string.pic_selected_num,
				selectNum, (ShowImageActivity.imagesMaxSize - selectNum)));

	}

}
