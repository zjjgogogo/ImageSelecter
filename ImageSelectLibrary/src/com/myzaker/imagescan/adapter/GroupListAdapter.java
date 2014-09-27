package com.myzaker.imagescan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myzaker.imagescan.R;
import com.myzaker.imagescan.ShowImageActivity;
import com.myzaker.imagescan.Imageload.BitmapCache;
import com.myzaker.imagescan.Imageload.LocalImageView;
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
	protected Context context;
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
		this.context = context;
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
			viewHolder.mImageView = (LocalImageView) convertView
					.findViewById(R.id.group_image);
			viewHolder.mTextViewTitle = (TextView) convertView
					.findViewById(R.id.group_title);
			viewHolder.mTextViewCounts = (TextView) convertView
					.findViewById(R.id.group_count);
			viewHolder.divider = convertView.findViewById(R.id.divider);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView
					.setImageResource(ShowImageActivity.mSkinUtil.gridItemDefaultRes);
		}

		viewHolder.divider
				.setBackgroundColor(ShowImageActivity.mSkinUtil.folderDividerColor);
		viewHolder.mTextViewCounts
				.setTextColor(ShowImageActivity.mSkinUtil.folderSubTextColor);

		if (select_index == position) {
			viewHolder.mTextViewTitle
					.setTextColor(ShowImageActivity.mSkinUtil.folderSelectTextColor);
		} else {
			viewHolder.mTextViewTitle
					.setTextColor(ShowImageActivity.mSkinUtil.folderTextColor);
		}

		viewHolder.mTextViewTitle.setText(mImageBean.getFolderName());
		viewHolder.mTextViewCounts.setText(context.getString(
				R.string.pic_folder_num, mImageBean.getImageCounts()));
		viewHolder.mImageView.setTag(path);

		viewHolder.mImageView
				.setDefaultImageResId(ShowImageActivity.mSkinUtil.gridItemDefaultRes);
		viewHolder.mImageView.setImageUrl("file://" + path,
				BitmapCache.getImageLoader());

		return convertView;
	}

	public static class ViewHolder {
		public LocalImageView mImageView;
		public TextView mTextViewTitle;
		public TextView mTextViewCounts;
		public View divider;

	}

}
