package com.myzaker.imagescan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myzaker.imagescan.util.BitmapUtil;

public class PreviewActivity extends Activity {

	// 预览的图片
	private ImageView image_show_item;
	// 选择的图片
	private ImageView image_show_choose;
	// 显示张数
	private TextView show_count;

	private Button btn_finish;

	// 点击之后传递过来的值
	private String imagePath;
	// 传递过来已经选择了多少张图片了
	private int imgSize;
	private boolean isSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		imagePath = bundle.getString("imagePath");
		imgSize = bundle.getInt("imgSize");
		isSelected = bundle.getBoolean("isSelected");

		setContentView(R.layout.activity_preview_image);

		image_show_item = (ImageView) findViewById(R.id.image_show_item);
		image_show_choose = (ImageView) findViewById(R.id.image_show_choose);
		show_count = (TextView) findViewById(R.id.show_count);
		btn_finish = (Button) findViewById(R.id.header_finish);

		initViewSkin();

		Bitmap mBitmap = BitmapUtil.getBitmap(this, imagePath);
		image_show_item.setImageBitmap(mBitmap);

		updateShowCount();

		image_show_choose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (imgSize <= ShowImageActivity.imagesMaxSize) {
					if (isSelected) {
						isSelected = false;
						imgSize--;
						updateShowCount();

					} else {
						if (imgSize == ShowImageActivity.imagesMaxSize) {
							showToastTip(getResources().getString(
									R.string.pic_select_num_error,
									ShowImageActivity.imagesMaxSize));

							return;
						}
						isSelected = true;
						imgSize++;
						updateShowCount();

					}
				} else {
					showToastTip(getResources().getString(
							R.string.pic_select_num_error,
							ShowImageActivity.imagesMaxSize));
				}
			}
		});
	}

	protected void initViewSkin() {

		findViewById(R.id.header).setBackgroundColor(
				ShowImageActivity.mSkinUtil.headerBarBgColor);

		findViewById(R.id.content).setBackgroundColor(
				ShowImageActivity.mSkinUtil.contentBgColor);

		((TextView) findViewById(R.id.header_title))
				.setTextColor(ShowImageActivity.mSkinUtil.headerBarTextColor);

		findViewById(R.id.mask).setBackgroundResource(
				ShowImageActivity.mSkinUtil.previewMaskRes);

		show_count
				.setTextColor(ShowImageActivity.mSkinUtil.previewBottomTextColor);

		btn_finish.setTextColor(getResources().getColorStateList(
				ShowImageActivity.mSkinUtil.headerBarFinishRes));
	}

	protected void showToastTip(String tip) {
		Toast.makeText(PreviewActivity.this, tip, Toast.LENGTH_SHORT).show();
	}

	protected void updateShowCount() {

		show_count.setText(getResources().getString(R.string.pic_selected_num,
				imgSize, (ShowImageActivity.imagesMaxSize - imgSize)));
		if (isSelected) {
			image_show_choose
					.setImageResource(ShowImageActivity.mSkinUtil.previewSelectRes);
		} else {
			image_show_choose
					.setImageResource(ShowImageActivity.mSkinUtil.previewUnselectRes);
		}
	}

	@Override
	public void onBackPressed() {
		// 将值存入
		SharedPreferences ps = getSharedPreferences("yulanreturn",
				Context.MODE_PRIVATE);
		Editor editor = ps.edit();
		// 当前选取数据的列表信息
		editor.putInt("imgSize", imgSize);
		editor.putString("yulanpath", imagePath);
		editor.putBoolean("isSelect", isSelected);
		editor.commit();
		setResult(103);
		super.onBackPressed();
	}

}
