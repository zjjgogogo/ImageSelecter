package com.myzaker.imagescan.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myzaker.imagescan.R;
import com.myzaker.imagescan.ShowImageActivity;
import com.myzaker.imagescan.Imageload.UniversalImageLoadTool;
import com.myzaker.imagescan.bean.ImageFolderBean;

/**
 * 
 * folders adapter
 * 
 * @author James。
 */
public class GroupListAdapter extends BaseAdapter {
	final String TAG = "GroupListAdapter";
	protected List<ImageFolderBean> list;
	protected LayoutInflater mInflater;

	private int select_index = 0;

	@Override
	public int getCount() {
		return list.size();
	}

	public void setSelect_index(int select_index) {
		this.select_index = select_index;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public GroupListAdapter(Context context, List<ImageFolderBean> list) {
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		ImageFolderBean mImageBean = list.get(position);
		String path = mImageBean.getTopImagePath();
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.group_choice_item, parent,
					false);
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.group_image);
			viewHolder.mTextViewTitle = (TextView) convertView
					.findViewById(R.id.group_title);
			viewHolder.mTextViewCounts = (TextView) convertView
					.findViewById(R.id.group_count);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView
					.setImageResource(R.drawable.friends_sends_pictures_no);
		}

		if (select_index == position) {
			viewHolder.mTextViewTitle
					.setTextColor(ShowImageActivity.mSkinUtil.headerBarBgColor);
		} else {
			viewHolder.mTextViewTitle.setTextColor(Color.BLACK);
		}

		viewHolder.mTextViewTitle.setText(mImageBean.getFolderName());
		viewHolder.mTextViewCounts.setText(Integer.toString(mImageBean
				.getImageCounts()) + "张");
		viewHolder.mImageView.setTag(path);

		UniversalImageLoadTool.disPlay("file://" + path, viewHolder.mImageView,
				R.drawable.friends_sends_pictures_no);

		return convertView;
	}

	public static class ViewHolder {
		public ImageView mImageView;
		public TextView mTextViewTitle;
		public TextView mTextViewCounts;

	}

}
