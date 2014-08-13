package com.myzaker.imagescan.Imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class UniversalImageLoadTool {

	private static ImageLoader imageLoader = ImageLoader.getInstance();

	private static final long discCacheLimitTime = 3600 * 24 * 15L;

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	public static boolean checkImageLoader() {
		return imageLoader.isInited();
	}

	public static void disPlay(String uri, ImageView imageAware, int default_pic) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(default_pic).showImageForEmptyUri(default_pic)
				.showImageOnFail(default_pic).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		imageLoader.displayImage(uri, imageAware, options);
	}

	public static void checkImageLoaderConfiguration(Context context) {
		if (!UniversalImageLoadTool.checkImageLoader()) {
			// This configuration tuning is custom. You can tune every option,
			// you may tune some of them,
			// or you can create default configuration by
			// ImageLoaderConfiguration.createDefault(this);
			// method.
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context)
					.threadPriority(Thread.NORM_PRIORITY)
					.denyCacheImageMultipleSizesInMemory()
					.memoryCacheExtraOptions(218, 440)
					// max width, max
					// height
					.discCacheExtraOptions(218, 440, CompressFormat.JPEG, 100,
							null)
					// Can slow ImageLoader, use it carefully
					.memoryCacheSize(6 * 1024 * 1024)
					.discCacheFileNameGenerator(new Md5FileNameGenerator())
					.discCache(
							new LimitedAgeDiscCache(StorageUtils
									.getCacheDirectory(context),
									new Md5FileNameGenerator(),
									discCacheLimitTime))
					.tasksProcessingOrder(QueueProcessingType.LIFO).build();
			// Initialize ImageLoader with configuration.
			ImageLoader.getInstance().init(config);
		}
	}

	public static void clear() {
		imageLoader.clearMemoryCache();
		imageLoader.clearDiscCache();
	}

	public static void resume() {
		imageLoader.resume();
	}

	/**
	 * 暂停加载
	 */
	public static void pause() {
		imageLoader.pause();
	}

	/**
	 * 停止加载
	 */
	public static void stop() {
		imageLoader.stop();
	}

	/**
	 * 销毁加载
	 */
	public static void destroy() {
		imageLoader.destroy();
	}
}
