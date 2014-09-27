package com.myzaker.imagescan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.myzaker.imagescan.R;
import com.myzaker.imagescan.ShowImageActivity;
import com.myzaker.imagescan.bean.ImageBean;
import com.myzaker.imagescan.localimageload.BitmapCache;
import com.myzaker.imagescan.localimageload.LocalImageView;

public class ImagePageAdapter extends PagerAdapter {

	ArrayList<View> recycleViews = new ArrayList<View>();
	Context context;
	List<ImageBean> datas;
	LayoutInflater mInflater;
	OnClickListener mClickListener;

	boolean isPreView = false;

	public ImagePageAdapter(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setPreView(boolean value) {
		isPreView = value;
	}

	public void setPageDatas(List<ImageBean> datas) {
		this.datas = datas;
	}

	public List<ImageBean> getDatas() {
		return datas;
	}

	@Override
	public int getCount() {
		if (datas != null) {
			return datas.size();
		}
		return 0;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		if (object instanceof View) {

			View view = (View) object;
			container.removeView(view);
			recycleViews.add(view);
		}

	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		View view = null;
		PageViewHolder mHolder = null;
		if (recycleViews.size() > 0) {
			view = recycleViews.remove(0);
			mHolder = (PageViewHolder) view.getTag();
		} else {
			view = mInflater.inflate(R.layout.page_item, null);
			mHolder = new PageViewHolder();
			mHolder.mDefaultImageView = (ImageView) view
					.findViewById(R.id.default_image);
			mHolder.image = (LocalImageView) view
					.findViewById(R.id.image_show_item);
			mHolder.selectBtn = (ImageView) view
					.findViewById(R.id.image_show_choose);
			mHolder.mask = (View) view.findViewById(R.id.mask);
			view.setTag(mHolder);
		}
		container.addView(view);
		ImageBean mImageBean = datas.get(position);
		bindData(mHolder, mImageBean);
		return view;
	}

	protected void bindData(PageViewHolder mHolder, ImageBean mImageBean) {

		if (isPreView) {
			mHolder.mask.setVisibility(View.INVISIBLE);
			mHolder.selectBtn.setVisibility(View.INVISIBLE);
		} else {
			if (mImageBean.isSelect()) {
				mHolder.selectBtn
						.setImageResource(ShowImageActivity.mSkinUtil.previewSelectRes);
			} else {
				mHolder.selectBtn
						.setImageResource(ShowImageActivity.mSkinUtil.previewUnselectRes);
			}
			mHolder.mask
					.setBackgroundColor(ShowImageActivity.mSkinUtil.previewMaskColor);
			mHolder.selectBtn.setOnClickListener(mClickListener);
		}
		mHolder.mDefaultImageView
				.setImageResource(ShowImageActivity.mSkinUtil.gridItemDefaultRes);
		mHolder.image.setImageUrl("file://" + mImageBean.getImagePath(),
				BitmapCache.getImageLoader());
	}

	public ImageBean getItem(int position) {
		if (datas != null) {
			return datas.get(position);
		}
		return null;
	}

	public void onSelectBtnClickListener(OnClickListener mClickListener) {
		this.mClickListener = mClickListener;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	class PageViewHolder {
		View mask;
		ImageView mDefaultImageView;
		LocalImageView image;
		ImageView selectBtn;
	}
}
