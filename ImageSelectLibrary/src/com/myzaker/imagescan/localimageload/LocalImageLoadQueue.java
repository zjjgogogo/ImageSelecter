package com.myzaker.imagescan.localimageload;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Looper;

public class LocalImageLoadQueue {

	private AtomicInteger mSequenceGenerator = new AtomicInteger();

	/** Number of network request dispatcher threads to start. */
	private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 2;

	LocalImageLoaderDispatcher[] mImageLoaderDispatchers;

	/** The queue of requests that are actually going out to the network. */
	private final PriorityBlockingQueue<LocalImageRequest> mRequestQueue = new PriorityBlockingQueue<LocalImageRequest>();

	/** Response delivery mechanism. */
	private final ExecutorDelivery mDelivery;

	public LocalImageLoadQueue() {

		mDelivery = new ExecutorDelivery(new Handler(Looper.getMainLooper()));
		mImageLoaderDispatchers = new LocalImageLoaderDispatcher[DEFAULT_NETWORK_THREAD_POOL_SIZE];

	}

	public void finish(LocalImageRequest request) {

	}

	public void start() {
		stop();
		for (int i = 0; i < mImageLoaderDispatchers.length; i++) {
			LocalImageLoaderDispatcher mImageLoaderDispatcher = new LocalImageLoaderDispatcher(
					mRequestQueue, mDelivery);
			mImageLoaderDispatchers[i] = mImageLoaderDispatcher;
			mImageLoaderDispatcher.start();
		}
	}

	public void stop() {
		for (int i = 0; i < mImageLoaderDispatchers.length; i++) {
			if (mImageLoaderDispatchers[i] != null) {
				mImageLoaderDispatchers[i].quit();
			}
		}

	}

	public LocalImageRequest add(LocalImageRequest request) {

		request.setRequestQueue(this);

		request.setSequence(getSequenceNumber());

		mRequestQueue.add(request);

		return request;

	}

	/**
	 * Gets a sequence number.
	 */
	public int getSequenceNumber() {
		return mSequenceGenerator.incrementAndGet();
	}
}
