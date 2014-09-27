package com.myzaker.imagescan.Imageload;

import java.util.concurrent.BlockingQueue;

import android.os.Process;

public class LocalImageLoaderDispatcher extends Thread {

	private volatile boolean mQuit = false;

	private final BlockingQueue<LocalImageRequest> mQueue;

	private final ExecutorDelivery mDelivery;

	public LocalImageLoaderDispatcher(BlockingQueue<LocalImageRequest> queue,
			ExecutorDelivery delivery) {

		mQueue = queue;
		mDelivery = delivery;
	}

	public void quit() {
		mQuit = true;
		interrupt();
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		LocalImageRequest request;
		while (true) {
			try {
				// Take a request from the queue.
				request = mQueue.take();
			} catch (InterruptedException e) {
				// We may have been interrupted because it was time to quit.
				if (mQuit) {
					return;
				}
				continue;
			}

			try {
				// network request.
				if (request.isCanceled()) {
					request.finish();
					continue;
				}

				LocalImageResponse mImageResponse = new LocalImageResponse();

				String url = request.getUrl();

				String requestUrl = url.substring(url.indexOf("file://") + 6);

				try {
					mImageResponse.result = LocalBitmapUtil.getBitmapByUrl(
							request.getMaxWidth(), request.getMaxHeight(),
							requestUrl);
				} catch (OutOfMemoryError oom) {

					mImageResponse.setSuccess(false);
					mImageResponse.error = new ImageError("cannot get the pic!");

				}
				if (mImageResponse.result != null) {
					mImageResponse.setSuccess(true);
				}

				mDelivery.postResponse(request, mImageResponse);
			} catch (Exception e) {
				mDelivery.postError(request, new ImageError(e));
			}
		}
	}
}
