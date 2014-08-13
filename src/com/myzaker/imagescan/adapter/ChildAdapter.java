package com.myzaker.imagescan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myzaker.imagescan.PreviewActivity;
import com.myzaker.imagescan.R;
import com.myzaker.imagescan.ShowImageActivity;
import com.myzaker.imagescan.bean.ImageBean;

/**
 * @author oywf
 * 
 */
public class ChildAdapter extends BaseAdapter {
	public final String TAG = getClass().getSimpleName();
	// 用来存储本页图片的选择过的图片的路径
	private ArrayList<String> mSelectPathList = new ArrayList<String>();
	private ArrayList<ImageBean> list;
	protected LayoutInflater mInflater;
	private TextView show_count_images;
	// adapter中用
	private int imgSize = 0;
	// 用于设置最大选择数
	private int imagesMaxSize;
	private Handler mHanlder;
	private Activity activity;
	private SpannableStringBuilder spn;

	/**
	 * @param context
	 * @param list
	 * @param imagesMaxSize
	 * @param mHanlder
	 * @param show_count_images
	 * @param activity
	 */
	public ChildAdapter(Context context, ArrayList<ImageBean> list,
			int imagesMaxSize, Handler mHanlder, TextView show_count_images,
			Activity activity) {
		this.list = list;
		this.imagesMaxSize = imagesMaxSize;
		this.mHanlder = mHanlder;
		this.show_count_images = show_count_images;
		mInflater = LayoutInflater.from(context);
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final String path = list.get(position).getImagePath();
		final ImageBean currentImageBean = list.get(position);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.grid_child_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.child_image);
			viewHolder.child_yulan = (ImageView) convertView
					.findViewById(R.id.preview);
			viewHolder.tv_pingzhang_image = (View) convertView
					.findViewById(R.id.select_view);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView
					.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		viewHolder.mImageView.setTag(path);

		if (currentImageBean.isSelect()) {
			viewHolder.tv_pingzhang_image.setVisibility(View.VISIBLE);
			spn = new SpannableStringBuilder(imgSize + "/" + imagesMaxSize);
			spn.setSpan(new ForegroundColorSpan(Color.parseColor("#00B4FD")),
					0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			show_count_images.setText(spn);
		} else {
			viewHolder.tv_pingzhang_image.setVisibility(View.GONE);
			spn = new SpannableStringBuilder(imgSize + "/" + imagesMaxSize);
			spn.setSpan(new ForegroundColorSpan(Color.parseColor("#00B4FD")),
					0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			show_count_images.setText(spn);
		}

		// 预览按钮点击
		viewHolder.child_yulan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, PreviewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("imagePath", path);
				bundle.putBoolean("isSelected", currentImageBean.isSelect());
				bundle.putInt("imgSize", imgSize);
				bundle.putInt("imgMaxSize", imagesMaxSize);
				intent.putExtras(bundle);
				activity.startActivityForResult(intent, 104);
			}
		});

		viewHolder.mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (imgSize < imagesMaxSize) {
					currentImageBean.setSelect(!currentImageBean.isSelect());
					if (currentImageBean.isSelect()) {
						viewHolder.tv_pingzhang_image
								.setVisibility(View.VISIBLE);
						mSelectPathList.add(path);
						imgSize++;
						spn = new SpannableStringBuilder(imgSize + "/"
								+ imagesMaxSize);
						spn.setSpan(
								new ForegroundColorSpan(
										ShowImageActivity.mSkinUtil.selectPicBottomTextColor2),
								0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						show_count_images.setText(spn);
					} else {
						viewHolder.tv_pingzhang_image.setVisibility(View.GONE);
						removePathByPath(path);
						imgSize--;
						spn = new SpannableStringBuilder(imgSize + "/"
								+ imagesMaxSize);
						spn.setSpan(
								new ForegroundColorSpan(
										ShowImageActivity.mSkinUtil.selectPicBottomTextColor2),
								0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						show_count_images.setText(spn);
						if (imgSize == 0) {
						}
					}
				} else if (imgSize >= imagesMaxSize) {
					if (currentImageBean.isSelect() == true) {
						currentImageBean.setSelect(!currentImageBean.isSelect());
						viewHolder.tv_pingzhang_image.setVisibility(View.GONE);
						removePathByPath(path);
						imgSize--;
						spn = new SpannableStringBuilder(imgSize + "/"
								+ imagesMaxSize);
						spn.setSpan(
								new ForegroundColorSpan(
										ShowImageActivity.mSkinUtil.selectPicBottomTextColor2),
								0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						show_count_images.setText(spn);
						if (imgSize == 0) {
							spn = new SpannableStringBuilder(imgSize + "/"
									+ imagesMaxSize);
							spn.setSpan(
									new ForegroundColorSpan(
											ShowImageActivity.mSkinUtil.selectPicBottomTextColor2),
									0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							show_count_images.setText(spn);
						}
					} else {
						Message message = Message.obtain(mHanlder, 404);
						message.sendToTarget();
					}

				}
			}

		});

		// setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));用于设置变暗
		// ------------------------------------------以下是图片加载优化--------------------------
		// System.out.println("当前图片路径：" + path);
//		UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapgetHashValue(
//				currentImageBean.getImgId(), "file://" + path),
//				viewHolder.mImageView, R.drawable.default_board_img);

		// UniversalImageLoadTool.disPlay1(ThumbnailsUtil.MapgetHashValue(
		// currentImageBean.getImgId(), "file://" + path),
		// new RotateImageViewAware(viewHolder.mImageView, path),
		// R.drawable.default_board_img);

		// -------------------------------------------------------------------------------------

		return convertView;
	}

	/**
	 * 获取所有选中的文件的路径
	 * 
	 * @param list
	 * @return
	 */
	public ArrayList<String> getSelectItemsPaths() {

		return mSelectPathList;
	}

	/**
	 * 通过图片路径移除在list中所存在的图片
	 */
	public void removePathByPath(String path) {
		if (mSelectPathList.size() > 0) {
			for (int i = 0; i < mSelectPathList.size(); i++) {
				if (mSelectPathList.get(i).equals(path)) {
					mSelectPathList.remove(i);
				}
			}
		}
	}

	/**
	 * 接受传递过来的 已经选中的图片的路径列表 更改他们bean的状态值
	 * 
	 * @param selectList
	 */
	public void setSelectPaths(List<String> selectList) {
		if (selectList.size() > 0 && list.size() > 0) {
			for (int i = 0; i < selectList.size(); i++) {
				// 对childImageBeans列表中含有路径的值进行对象状态重置
				for (int k = 0; k < list.size(); k++) {
					if (list.get(k).getImagePath().equals(selectList.get(i))) {
						list.get(k).setSelect(true);
					}
				}
			}
		}
	}

	/**
	 * 将之前全部选择的与现在的进行数据合并
	 */
	public void setSelectPathsALL(List<String> selectList) {
		for (int i = 0; i < selectList.size(); i++) {
			mSelectPathList.add(selectList.get(i));
		}
	}

	public void setSize(int selectedSize) {
		imgSize = selectedSize;
	}

	public static class ViewHolder {
		public ImageView mImageView;
		// 预览按钮
		public ImageView child_yulan;
		// 遮挡屏障
		public View tv_pingzhang_image;
	}

}
