package com.myzaker.imagescan.Imageload;

public class ImageError extends Exception {
 
	private static final long serialVersionUID = 891098803784807769L;

	public ImageError() {
	}

	public ImageError(String exceptionMessage) {
		super(exceptionMessage);
	}

	public ImageError(String exceptionMessage, Throwable reason) {
		super(exceptionMessage, reason);
	}

	public ImageError(Throwable cause) {
		super(cause);
	}
}
