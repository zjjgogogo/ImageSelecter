package com.test.imagedemo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.myzaker.imagescan.ShowImageActivity;
import com.myzaker.imagescan.bean.ImageBean;
import com.myzaker.imagescan.bean.TempDataController;
import com.myzaker.imagescan.util.SkinUtil;

public class TestActivity extends Activity {

	final int REQUEST_CODE = 0x1212;

	ArrayList<ImageBean> mImageBeans = new ArrayList<ImageBean>();

	final int size = 6;

	GridView mGridView;

	SelectImageGridAdpater mImageGridAdpater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);

		Button open_photo = (Button) findViewById(R.id.open_photo);
		open_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mImageBeans.size() < size) {
					Intent intent = new Intent(TestActivity.this,
							ShowImageActivity.class);
					intent.putExtra(ShowImageActivity.KEY_MAX_IMAGE_SELECT,
							size - mImageBeans.size());
					startActivityForResult(intent, REQUEST_CODE);
				} else {
					Toast.makeText(TestActivity.this, "Too more!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button open_photo_night = (Button) findViewById(R.id.open_photo_night);
		open_photo_night.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mImageBeans.size() < size) {
					Intent intent = new Intent(TestActivity.this,
							ShowImageActivity.class);
					intent.putExtra(SkinUtil.KEY_HEADERBAR_BG_COLOR, 0xff191d20);
					intent.putExtra(SkinUtil.KEY_HEADERBAR_FINISH_RES,
							R.drawable.botton_finish_color_new);
					intent.putExtra(SkinUtil.KEY_HEADERBAR_TEXT_COLOR,
							0xff626262);
					intent.putExtra(SkinUtil.KEY_CONTENT_BG_COLOR, 0xff0f1111);
					intent.putExtra(SkinUtil.KEY_FOLDER_DIVIDER_COLOR,
							0xff1e1e1e);
					intent.putExtra(SkinUtil.KEY_SELECTPIC_BOTTOM_TEXT2_COLOR,
							0xff0078a8);
					intent.putExtra(SkinUtil.KEY_SELECTPIC_BOTTOM_TEXT_COLOR,
							0xff848484);
					intent.putExtra(SkinUtil.KEY_FOLDER_TEXT_COLOR, 0xff626262);
					intent.putExtra(SkinUtil.KEY_FOLDER_SUBTEXT_COLOR,
							0xff343434);
					intent.putExtra(SkinUtil.KEY_FOLDER_SELECTTEXT_COLOR,
							0xff0078a8);
					intent.putExtra(SkinUtil.KEY_FOOTERBAR_BG_COLOR, 0xff191d20);
					intent.putExtra(SkinUtil.KEY_FOOTERBAR_DIVIDER_COLOR,
							0xff292d2d);
					intent.putExtra(SkinUtil.KEY_PREVIEW_MASK_COLOR,
							0xc80f1111);
					intent.putExtra(ShowImageActivity.KEY_MAX_IMAGE_SELECT,
							size - mImageBeans.size());
					startActivityForResult(intent, REQUEST_CODE);
				} else {
					Toast.makeText(TestActivity.this, "Too more!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button open_remove = (Button) findViewById(R.id.remove_all);
		open_remove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				cleanData();
			}
		});

		mGridView = (GridView) findViewById(R.id.grid);
	}

	protected void cleanData() {

		if (mImageGridAdpater != null) {
			mImageBeans.clear();
			mImageGridAdpater.notifyDataSetChanged();
		}

	}

	protected void updateData() {
		mImageBeans.addAll(TempDataController.getSelectImageDatas());
		if (mImageGridAdpater == null) {
			mImageGridAdpater = new SelectImageGridAdpater(this, mImageBeans);
			mGridView.setAdapter(mImageGridAdpater);
		} else {
			mImageGridAdpater.notifyDataSetChanged();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE
				&& resultCode == ShowImageActivity.RESULT_CODE_OUT) {
			updateData();
		}

	}

}
