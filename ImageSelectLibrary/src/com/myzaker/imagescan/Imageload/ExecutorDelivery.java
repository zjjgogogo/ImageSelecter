package com.myzaker.imagescan.Imageload;

import java.util.concurrent.Executor;

import android.os.Handler;

/**
 * Delivers responses and errors.
 */
public class ExecutorDelivery {
	/** Used for posting responses, typically to the main thread. */
	private final Executor mResponsePoster;

	/**
	 * Creates a new response delivery interface.
	 * 
	 * @param handler
	 *            {@link Handler} to post responses on
	 */
	public ExecutorDelivery(final Handler handler) {
		// Make an Executor that just wraps the handler.
		mResponsePoster = new Executor() {
			@Override
			public void execute(Runnable command) {
				handler.post(command);
			}
		};
	}

	public void postResponse(LocalImageRequest request,
			LocalImageResponse response) {
		mResponsePoster
				.execute(new ResponseDeliveryRunnable(request, response));
	}

	public void postError(LocalImageRequest request, ImageError error) {
		LocalImageResponse response = new LocalImageResponse();
		response.setSuccess(false);
		response.error = error;
		mResponsePoster
				.execute(new ResponseDeliveryRunnable(request, response));
	}

	/**
	 * A Runnable used for delivering network responses to a listener on the
	 * main thread.
	 */
	private class ResponseDeliveryRunnable implements Runnable {
		private final LocalImageRequest mRequest;
		private final LocalImageResponse mResponse;

		public ResponseDeliveryRunnable(LocalImageRequest request,
				LocalImageResponse response) {
			mRequest = request;
			mResponse = response;
		}

		@Override
		public void run() {
			// If this request has canceled, finish it and don't deliver.
			if (mRequest.isCanceled()) {
				mRequest.finish();
				return;
			}

			// Deliver a normal response or error, depending.
			if (mResponse.isSuccess()) {
				mRequest.deliverResponse(mResponse.result);
			} else {
				mRequest.deliverError(mResponse.error);
			}

		}
	}

}
