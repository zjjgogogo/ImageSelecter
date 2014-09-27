package com.myzaker.imagescan.Imageload;

import java.util.HashMap;
import java.util.LinkedList;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.myzaker.imagescan.Imageload.LocalImageResponse.ErrorListener;
import com.myzaker.imagescan.Imageload.LocalImageResponse.Listener;

public class LocalImageLoader {

	public LocalImageLoader(LocalImageLoadQueue queue, ImageCache imageCache) {
		mRequestQueue = queue;
		mCache = imageCache;
	}

	/** Runnable for in-flight response delivery. */
	private Runnable mRunnable;
	private final LocalImageLoadQueue mRequestQueue;
	private final ImageCache mCache;
	private int mBatchResponseDelayMs = 100;
	private final HashMap<String, BatchedImageRequest> mInFlightRequests = new HashMap<String, BatchedImageRequest>();
	private final Handler mHandler = new Handler(Looper.getMainLooper());

	/** HashMap of the currently pending responses (waiting to be delivered). */
	private final HashMap<String, BatchedImageRequest> mBatchedResponses = new HashMap<String, BatchedImageRequest>();

	public LocalImageContainer get(String requestUrl,
			ImageListener mImageListener, int maxWidth, int maxHeight) {

		final String cacheKey = LocalBitmapUtil.getCacheKey(requestUrl,
				maxWidth, maxHeight);

		// Try to look up the request in the cache of remote images.
		Bitmap cachedBitmap = mCache.getBitmap(cacheKey);
		if (cachedBitmap != null) {
			// Return the cached bitmap.
			LocalImageContainer container = new LocalImageContainer(
					cachedBitmap, requestUrl, null, null);
			mImageListener.onResponse(container, true);
			return container;
		}

		// The bitmap did not exist in the cache, fetch it!
		LocalImageContainer imageContainer = new LocalImageContainer(null,
				requestUrl, cacheKey, mImageListener);

		// Update the caller to let them know that they should use the default
		// bitmap.
		mImageListener.onResponse(imageContainer, true);

		// Check to see if a request is already in-flight.
		BatchedImageRequest request = mInFlightRequests.get(cacheKey);
		if (request != null) {
			// If it is, add this request to the list of listeners.
			request.addContainer(imageContainer);
			return imageContainer;
		}

		// The request is not already in flight. Send the new request to the
		// network and
		// track it.
		LocalImageRequest newRequest = new LocalImageRequest(requestUrl,
				new Listener() {

					@Override
					public void onResponse(Bitmap response) {
						onGetImageSuccess(cacheKey, response);
					}
				}, maxWidth, maxHeight, new ErrorListener() {
					@Override
					public void onErrorResponse(ImageError error) {
						onGetImageError(cacheKey, error);
					}
				});

		mRequestQueue.add(newRequest);
		mInFlightRequests.put(cacheKey, new BatchedImageRequest(newRequest,
				imageContainer));
		return imageContainer;
	}

	/**
	 * Handler for when an image was successfully loaded.
	 * 
	 * @param cacheKey
	 *            The cache key that is associated with the image request.
	 * @param response
	 *            The bitmap that was returned from the network.
	 */
	private void onGetImageSuccess(String cacheKey, Bitmap response) {
		// cache the image that was fetched.
		mCache.putBitmap(cacheKey, response);

		// remove the request from the list of in-flight requests.
		BatchedImageRequest request = mInFlightRequests.remove(cacheKey);

		if (request != null) {
			// Update the response bitmap.
			request.mResponseBitmap = response;

			// Send the batched response
			batchResponse(cacheKey, request);
		}
	}

	/**
	 * Handler for when an image failed to load.
	 * 
	 * @param cacheKey
	 *            The cache key that is associated with the image request.
	 */
	private void onGetImageError(String cacheKey, ImageError error) {
		// Notify the requesters that something failed via a null result.
		// Remove this request from the list of in-flight requests.
		BatchedImageRequest request = mInFlightRequests.remove(cacheKey);

		// Set the error for this request
		request.setError(error);

		if (request != null) {
			// Send the batched response
			batchResponse(cacheKey, request);
		}
	}

	/**
	 * Starts the runnable for batched delivery of responses if it is not
	 * already started.
	 * 
	 * @param cacheKey
	 *            The cacheKey of the response being delivered.
	 * @param request
	 *            The BatchedImageRequest to be delivered.
	 * @param error
	 *            The volley error associated with the request (if applicable).
	 */
	private void batchResponse(String cacheKey, BatchedImageRequest request) {
		mBatchedResponses.put(cacheKey, request);
		// If we don't already have a batch delivery runnable in flight, make a
		// new one.
		// Note that this will be used to deliver responses to all callers in
		// mBatchedResponses.
		if (mRunnable == null) {
			mRunnable = new Runnable() {
				@Override
				public void run() {
					for (BatchedImageRequest bir : mBatchedResponses.values()) {
						for (LocalImageContainer container : bir.mContainers) {
							// If one of the callers in the batched request
							// canceled the request
							// after the response was received but before it was
							// delivered,
							// skip them.
							if (container.mListener == null) {
								continue;
							}
							if (bir.getError() == null) {
								container.setBitmap(bir.mResponseBitmap);
								container.mListener
										.onResponse(container, false);
							} else {
								container.mListener.onErrorResponse(bir
										.getError());
							}
						}
					}
					mBatchedResponses.clear();
					mRunnable = null;
				}

			};
			// Post the runnable.
			mHandler.postDelayed(mRunnable, mBatchResponseDelayMs);
		}
	}

	public class LocalImageContainer {

		private String mRequestUrl;

		private Bitmap mBitmap;

		public final ImageListener mListener;

		public final String mCacheKey;

		public LocalImageContainer(Bitmap bitmap, String requestUrl,
				String cacheKey, ImageListener listener) {
			mBitmap = bitmap;
			mRequestUrl = requestUrl;
			mCacheKey = cacheKey;
			mListener = listener;
		}

		public String getRequestUrl() {
			return mRequestUrl;
		}

		public Bitmap getBitmap() {
			return mBitmap;
		}

		public void setBitmap(Bitmap bitmap) {
			this.mBitmap = bitmap;
		}

		/**
		 * Releases interest in the in-flight request (and cancels it if no one
		 * else is listening).
		 */
		public void cancelRequest() {
			if (mListener == null) {
				return;
			}

			BatchedImageRequest request = mInFlightRequests.get(mCacheKey);
			if (request != null) {
				boolean canceled = request
						.removeContainerAndCancelIfNecessary(this);
				if (canceled) {
					mInFlightRequests.remove(mCacheKey);
				}
			} else {
				// check to see if it is already batched for delivery.
				request = mBatchedResponses.get(mCacheKey);
				if (request != null) {
					request.removeContainerAndCancelIfNecessary(this);
					if (request.mContainers.size() == 0) {
						mBatchedResponses.remove(mCacheKey);
					}
				}
			}
		}

	}

	/**
	 * Wrapper class used to map a Request to the set of active ImageContainer
	 * objects that are interested in its results.
	 */
	private class BatchedImageRequest {
		/** The request being tracked */
		private final LocalImageRequest mRequest;

		/** The result of the request being tracked by this item */
		private Bitmap mResponseBitmap;

		/** Error if one occurred for this response */
		private ImageError mError;

		/**
		 * List of all of the active ImageContainers that are interested in the
		 * request
		 */
		private final LinkedList<LocalImageContainer> mContainers = new LinkedList<LocalImageContainer>();

		/**
		 * Constructs a new BatchedImageRequest object
		 * 
		 * @param request
		 *            The request being tracked
		 * @param container
		 *            The ImageContainer of the person who initiated the
		 *            request.
		 */
		public BatchedImageRequest(LocalImageRequest request,
				LocalImageContainer container) {
			mRequest = request;
			mContainers.add(container);
		}

		/**
		 * Set the error for this response
		 */
		public void setError(ImageError error) {
			mError = error;
		}

		/**
		 * Get the error for this response
		 */
		public ImageError getError() {
			return mError;
		}

		/**
		 * Adds another ImageContainer to the list of those interested in the
		 * results of the request.
		 */
		public void addContainer(LocalImageContainer container) {
			mContainers.add(container);
		}

		/**
		 * Detatches the bitmap container from the request and cancels the
		 * request if no one is left listening.
		 * 
		 * @param container
		 *            The container to remove from the list
		 * @return True if the request was canceled, false otherwise.
		 */
		public boolean removeContainerAndCancelIfNecessary(
				LocalImageContainer container) {
			mContainers.remove(container);
			if (mContainers.size() == 0) {
				mRequest.cancel();
				return true;
			}
			return false;
		}
	}
}
