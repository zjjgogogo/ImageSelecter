package com.myzaker.imagescan.Imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
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

	public static void displayImage(String uri, ImageView imageView,
			int default_pic) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(default_pic).showImageForEmptyUri(default_pic)
				.showImageOnFail(default_pic).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		imageLoader.displayImage(uri, imageView, options);
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
					.memoryCacheExtraOptions(480, 800)
					// Can slow ImageLoader, use it carefully
					.discCacheFileNameGenerator(new Md5FileNameGenerator())
					.discCache(
							new LimitedAgeDiscCache(StorageUtils
									.getCacheDirectory(context),
									new Md5FileNameGenerator(),
									discCacheLimitTime))
					.tasksProcessingOrder(QueueProcessingType.FIFO).build();
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

	public static void pause() {
		imageLoader.pause();
	}

	public static void stop() {
		imageLoader.stop();
	}

	public static void destroy() {
		imageLoader.destroy();
	}
}
