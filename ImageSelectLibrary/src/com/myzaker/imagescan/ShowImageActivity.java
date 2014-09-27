package com.myzaker.imagescan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.myzaker.imagescan.adapter.GroupListAdapter;
import com.myzaker.imagescan.adapter.ImageGridAdpater;
import com.myzaker.imagescan.bean.ImageBean;
import com.myzaker.imagescan.bean.ImageFolderBean;
import com.myzaker.imagescan.bean.TempDataController;
import com.myzaker.imagescan.task.LocalImageLoadTask;
import com.myzaker.imagescan.task.LocalImageLoadTask.OnLocalImageLoadTaskListener;
import com.myzaker.imagescan.util.CameraUtil;
import com.myzaker.imagescan.util.SkinUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 
 * select pic MainActivity
 * 
 */
public class ShowImageActivity extends Activity {

	public final static String tag = "ShowImageActivity";

	public static SkinUtil mSkinUtil;

	public static final String KEY_MAX_IMAGE_SELECT = "KEY_MAX_IMAGE_SELECT";
	public static final String KEY_RETURN_SELECT = "KEY_RETURN_SELECT";
	public static final String KEY_RESTORE_FLAG = "KEY_RESTORE_FLAG";

	public final static int REQUEST_CODE_PREVIEW = 1122;
	public final static int RESULT_CODE_PREVIEW_BACK = 1123;
	public final static int RESULT_CODE_PREVIEW_FINISH = 1124;

	public final static int RESULT_CODE_OUT = 1000;

	public final static int RESULT_CODE_CANCEL = 1001;

	public final static int REQUEST_CODE_CAMERA = 1133;

	ImageGridAdpater mImageGridAdpater;

	private GridView mGridView;

	private ImageButton btn_camera;

	private Button btn_gallery, btn_finish;

	private TextView show_count_images;

	private ProgressDialog mProgressDialog;

	private String defaultFolderName;

	private View mFolderListLayout;

	private ListView mFolderList;

	private GroupListAdapter mGroupListAdapter;

	public static int imagesMaxSize = 6;

	public static int folder_index = 0;

	CameraUtil mCameraUtil = new CameraUtil();

	boolean isRestore = false;

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

		outState.putBoolean(KEY_RESTORE_FLAG, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		if (savedInstanceState != null) {
			isRestore = savedInstanceState.getBoolean(KEY_RESTORE_FLAG);
		}

		mSkinUtil = new SkinUtil(getIntent());

		defaultFolderName = getString(R.string.all_images_str);

		setContentView(R.layout.activity_show_image);
		mGridView = (GridView) findViewById(R.id.grid);
		btn_gallery = (Button) findViewById(R.id.header_gallery);
		btn_finish = (Button) findViewById(R.id.header_finish);
		btn_camera = (ImageButton) findViewById(R.id.footer_camera);
		show_count_images = (TextView) findViewById(R.id.show_count_images);

		switchViewSkin();

		btn_gallery.setText(defaultFolderName);

		btn_finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mFolderList != null
						&& mFolderList.getVisibility() == View.VISIBLE) {
					closeFolderList();
					return;
				}
				back();

			}
		});

		btn_camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mFolderList != null
						&& mFolderList.getVisibility() == View.VISIBLE) {
					closeFolderList();
				}

				if (TempDataController.getSelectImageDatas().size() < imagesMaxSize) {
					if (mCameraUtil != null) {
						mCameraUtil.cameraInit(ShowImageActivity.this);
					}
				} else {
					Toast.makeText(
							ShowImageActivity.this,
							getString(R.string.pic_select_num_error,
									imagesMaxSize), Toast.LENGTH_SHORT).show();
				}
			}
		});

		switchViewSkin();

		if (!isRestore) {

			imagesMaxSize = getIntent().getIntExtra(KEY_MAX_IMAGE_SELECT,
					imagesMaxSize);

			TempDataController.clean();

			folder_index = 0;

			getImages();

		} else {
			setDataToContainer();

			updateSelectNumber();

			initFolderList();
		}

	}

	protected void switchViewSkin() {
		findViewById(R.id.header)
				.setBackgroundColor(mSkinUtil.headerBarBgColor);
		((TextView) findViewById(R.id.header_title))
				.setTextColor(mSkinUtil.headerBarTextColor);
		mGridView.setBackgroundColor(mSkinUtil.contentBgColor);
		btn_gallery.setTextColor(getResources().getColorStateList(
				mSkinUtil.headerBarFinishRes));
		btn_finish.setTextColor(getResources().getColorStateList(
				mSkinUtil.headerBarFinishRes));
		findViewById(R.id.bottom_divider).setBackgroundColor(
				mSkinUtil.headerBarBgColor);
		findViewById(R.id.footerbar).setBackgroundColor(
				mSkinUtil.footerBarBgColor);
		btn_camera.setImageResource(mSkinUtil.cameraIconRes);
		show_count_images.setTextColor(mSkinUtil.selectPicBottomTextColor);
	}

	// 对点击返回按钮的事件响应
	@Override
	public void onBackPressed() {

		if (mFolderList != null && mFolderList.getVisibility() == View.VISIBLE) {
			closeFolderList();
		} else {
			setResult(RESULT_CODE_CANCEL);
			finish();
			overridePendingTransition(R.anim.in, R.anim.out);
		}
	}

	protected void back() {
		setResult(RESULT_CODE_OUT);
		finish();
		overridePendingTransition(R.anim.in, R.anim.out);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	protected void initFolderList() {
		mFolderListLayout = findViewById(R.id.layout_group_list);

		OnClickListener mCloseClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				closeFolderList();
			}
		};

		mFolderListLayout.setOnClickListener(mCloseClick);

		findViewById(R.id.header).setOnClickListener(mCloseClick);
		findViewById(R.id.footerbar).setOnClickListener(mCloseClick);

		ArrayList<ImageFolderBean> mFolders = new ArrayList<ImageFolderBean>(
				TempDataController.getFolderDatas());

		ArrayList<ImageBean> mAllImageBeans = TempDataController
				.getAllImageDatas();

		if (mAllImageBeans.size() < 1) {
			return;
		}

		ImageFolderBean mImageFolderBean = new ImageFolderBean();
		mImageFolderBean.setFolderName(defaultFolderName);
		mImageFolderBean.setImageCounts(mAllImageBeans.size());
		mImageFolderBean.setTopImagePath(mAllImageBeans.get(0).getImagePath());

		mFolders.add(0, mImageFolderBean);

		ListView mGroupListView = (ListView) findViewById(R.id.group_list);

		mFolderList = mGroupListView;

		mGroupListView
				.setBackgroundColor(ShowImageActivity.mSkinUtil.contentBgColor);

		mGroupListAdapter = new GroupListAdapter(this, mFolders);

		mGroupListView.setAdapter(mGroupListAdapter);

		updateFolderList();

		mGroupListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				folder_index = position;

				List<ImageBean> imageDataList = getFolderList();

				mImageGridAdpater.setImageDatas(imageDataList);

				mImageGridAdpater.notifyDataSetChanged();

				updateFolderBtn();

				updateFolderList();

				closeFolderList();

			}
		});
	}

	protected void updateFolderList() {
		if (mGroupListAdapter != null) {
			mGroupListAdapter.setSelect_index(folder_index);
			mFolderList.setSelection(folder_index);
			mGroupListAdapter.notifyDataSetChanged();
		}
	}

	protected void showFolderList() {

		if (mFolderList != null && mFolderList.getVisibility() == View.VISIBLE) {
			closeFolderList();
			return;
		}

		mFolderListLayout.setVisibility(View.VISIBLE);
		mFolderList.setVisibility(View.VISIBLE);
		ViewHelper.setTranslationY(mFolderList, -mFolderList.getHeight());
		ViewHelper.setAlpha(mFolderListLayout, 0);
		ViewPropertyAnimator.animate(mFolderList).translationY(0)
				.setListener(null);
		ViewPropertyAnimator.animate(mFolderListLayout).alpha(1)
				.setListener(null);
	}

	protected void closeFolderList() {

		ViewPropertyAnimator.animate(mFolderList)
				.translationY(-mFolderList.getHeight())
				.setListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animator arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animator arg0) {
						// TODO Auto-generated method stub
						mFolderList.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onAnimationCancel(Animator arg0) {
						// TODO Auto-generated method stub

					}
				});
		ViewPropertyAnimator.animate(mFolderListLayout).alpha(0)
				.setListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animator arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animator arg0) {
						// TODO Auto-generated method stub
						mFolderListLayout.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onAnimationCancel(Animator arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	protected List<ImageBean> getFolderList() {
		switch (folder_index) {
		case 0:
			return TempDataController.getAllImageDatas();
		default:
			ImageFolderBean mImageFolderBean = TempDataController
					.getFolderDatas().get(folder_index - 1);
			HashMap<String, List<ImageBean>> mAllDatas = TempDataController
					.getTotalDatas();
			return mAllDatas.get(mImageFolderBean.getFolderName());
		}
	}

	private void updateFolderBtn() {

		if (folder_index == 0) {
			btn_gallery.setText(defaultFolderName);
		} else {
			ImageFolderBean mImageFolderBean = TempDataController
					.getFolderDatas().get(folder_index - 1);
			btn_gallery.setText(mImageFolderBean.getFolderName());
		}
	}

	private void updateSelectNumber() {

		int imgSize = TempDataController.getSelectImageDatas().size();

		String imageSizeStr = String.valueOf(imgSize);

		SpannableStringBuilder spn = new SpannableStringBuilder(imageSizeStr
				+ "/" + imagesMaxSize);
		spn.setSpan(
				new ForegroundColorSpan(mSkinUtil.selectPicBottomTextColor2),
				0, imageSizeStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		show_count_images.setText(spn);
	}

	// get images in extern storge
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, R.string.no_extern_storge, Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}

		LocalImageLoadTask mImageLoadTask = new LocalImageLoadTask(
				getApplicationContext());
		mImageLoadTask
				.setOnLocalImageLoadTaskListener(mLocalImageLoadTaskListener);
		mImageLoadTask.execute();

	}

	OnClickListener mPreViewOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Integer mInteger = (Integer) v.getTag();
			if (mInteger != null) {
				int position = mInteger;

				Intent intent = new Intent(ShowImageActivity.this,
						PreviewActivity.class);

				Bundle bundle = new Bundle();
				bundle.putInt(PreviewActivity.KEY_PIC_INDEX, position);
				bundle.putInt(PreviewActivity.KEY_FOLD_INDEX, folder_index);
				intent.putExtras(bundle);
				startActivityForResult(intent, REQUEST_CODE_PREVIEW);

			}

		}
	};

	OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			List<ImageBean> mImageBeans = getFolderList();

			ImageBean mImageBean = mImageBeans.get(position);

			if (mImageBean.isSelect()) {

				mImageBean.setSelect(false);
				TempDataController.removeSelectImage(mImageBean);
			} else {
				if (TempDataController.getSelectImageDatas().size() < imagesMaxSize) {
					mImageBean.setSelect(true);
					TempDataController.addSelectImage(mImageBean);
				} else {
					Toast.makeText(
							ShowImageActivity.this,
							getString(R.string.pic_select_num_error,
									imagesMaxSize), Toast.LENGTH_SHORT).show();
				}
			}
			updateSelectNumber();
			mImageGridAdpater.notifyDataSetChanged();

		}
	};

	OnLocalImageLoadTaskListener mLocalImageLoadTaskListener = new OnLocalImageLoadTaskListener() {

		@Override
		public void onStart() {

			// show progress
			mProgressDialog = ProgressDialog.show(ShowImageActivity.this, null,
					getString(R.string.loading));
		}

		@Override
		public void onEnd() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}

			setDataToContainer();

			updateSelectNumber();

			initFolderList();
		}

		@Override
		public void onCancel() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		}
	};

	protected void setDataToContainer() {
		ArrayList<ImageBean> mAllArrayList = TempDataController
				.getAllImageDatas();

		if (mAllArrayList.isEmpty()) {
			Toast.makeText(ShowImageActivity.this, R.string.no_any_pic,
					Toast.LENGTH_SHORT).show();
			return;
		}

		mImageGridAdpater = new ImageGridAdpater(ShowImageActivity.this,
				mAllArrayList);
		mImageGridAdpater.setOnPreviewClicklistener(mPreViewOnClickListener);
		mGridView.setAdapter(mImageGridAdpater);
		mGridView.setOnItemClickListener(mItemClickListener);

		btn_gallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showFolderList();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_CAMERA) {
			if (resultCode == Activity.RESULT_OK) {

				ImageBean mImageBean = null;
				if (mCameraUtil != null) {
					mImageBean = mCameraUtil.getlastTakedPic(this);
				}
				if (mImageBean != null) {
					mImageBean.setSelect(true);
					TempDataController.addSelectImage(mImageBean);
					back();
				} else {
					onBackPressed();
				}
			}
		}

		if (requestCode == REQUEST_CODE_PREVIEW) {

			if (mImageGridAdpater != null) {
				mImageGridAdpater.notifyDataSetChanged();
			}
			if (resultCode == RESULT_CODE_PREVIEW_BACK) {
				final int currentItemIndex = data.getIntExtra(
						KEY_RETURN_SELECT, 0);
				mGridView.post(new Runnable() {

					@Override
					public void run() {
						mGridView.setSelection(currentItemIndex);
					}
				});
				updateSelectNumber();
			} else if (resultCode == RESULT_CODE_PREVIEW_FINISH) {
				back();
			}
		}

		// Log.i("1111", "我被调用了" + requestCode + ", " + requestCode);
		// SharedPreferences sp = getSharedPreferences("ppp", 0);
		// Log.i("2222222", "文件路径：" + sp.getString("m", ""));
		// String path = sp.getString("m", "");
		// // 拍照的路径加到里面
		// imageCheckedPaths.add(path);
		// // 一起直接返回就行了，然后结束这个activity
		// SharedPreferencesUtil.saveArray(imageCheckedPaths,
		// ShowImageActivity.this);
		// ShowImageActivity.this.finish();
		// } else {
		// // 相机点击了返回按钮 这里删初缓存的临时文件
		// SharedPreferences sp = getSharedPreferences("ppp", 0);
		// Log.i("444444", "删除文件路径："
		// + sp.getString("m", "" + "-----删除缓存临时文件----------"));
		// String path = sp.getString("m", "");
		// CameraUtil.deleteTempFile(path);
		// }
		//
		// }
		// // 预览界面数据返回
		// if (resultCode == YULAN_RESULT_CODE && requestCode == 104) {
		// SharedPreferences sp = getSharedPreferences("yulanreturn", 0);
		// // // 返回过来的是所有已选图片的路径
		// String path = sp.getString("yulanpath", "");
		// boolean flag = sp.getBoolean("isSelect", false);
		// int imgsize = sp.getInt("imgSize", 0);
		//
		// // System.out.println("是否勾选了:" + flag);
		// for (int i = 0; i < list.size(); i++) {
		// if (list.get(i).getImagePath().equals(path)) {
		// list.get(i).setSelect(flag);
		// }
		// }
		// imageCheckedPaths = adapter.getSelectItemsPaths();
		// if (flag) {
		// for (int m = 0; m < imageCheckedPaths.size(); m++) {
		// // 如果是以前已经勾选过的 就不管
		// if (imageCheckedPaths.get(m).equals(path)) {
		// imageCheckedPaths.remove(m);
		// }
		// }
		// imageCheckedPaths.add(path);
		// adapter.setSize(imgsize);
		// } else {
		// for (int j = 0; j < imageCheckedPaths.size(); j++) {
		// if (imageCheckedPaths.get(j).equals(path)) {
		// imageCheckedPaths.remove(j);
		// }
		// }
		// adapter.setSize(imgsize);
		// }
		// adapter.notifyDataSetChanged();
		// sp.edit().clear();
		// }

	}
}
