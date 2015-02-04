package com.compoment.video_record_play;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;

public class CameraUtil {

	public static final int ORIENTATION_HYSTERESIS = 5;

	public static final String TAG = "*** CameraUtil ***";
	private static int numberOfCameras = 1;
	private static boolean[] cameraFront = new boolean[] { false, true };
	private static int[] cameraOrientations = new int[] { 90, 270 };

	static {
		Camera camera = null;
		try {
			Class cameraInfoClz = Class.forName("android.hardware.Camera$CameraInfo");
			Log.d(TAG, "*** clz=" + cameraInfoClz);
			Object cameraInfo = cameraInfoClz.newInstance();

			Method getNumberOfCamerasMethod = Camera.class
					.getMethod("getNumberOfCameras");
			Log.d(TAG, "*** getNumberOfCamerasMethod=" + getNumberOfCamerasMethod);
			if (getNumberOfCamerasMethod != null) {
				numberOfCameras = (Integer) getNumberOfCamerasMethod
						.invoke(Camera.class);
				Log.d(TAG, "*** cameraCount=" + numberOfCameras);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int cameraOrientation = 0;
		try {
			Class cameraInfoClz = Class.forName("android.hardware.Camera$CameraInfo");
			Log.d(TAG, "*** clz=" + cameraInfoClz);
			Object cameraInfo = cameraInfoClz.newInstance();
			Log.d(TAG, "*** cameraInfo=" + cameraInfo);
			Field cameraFacingFrontField = cameraInfoClz
					.getField("CAMERA_FACING_FRONT");
			int CAMERA_FACING_FRONT = (Integer) cameraFacingFrontField.get(null);
			Log.d(TAG, "*** CAMERA_FACING_FRONT=" + CAMERA_FACING_FRONT);
			Method getCameraInfoMethod = Camera.class.getMethod("getCameraInfo",
					new Class[] { int.class, cameraInfoClz });
			Log.d(TAG, "*** getCameraInfoMethod=" + getCameraInfoMethod);
			if (getCameraInfoMethod != null) {
				for (int i = 0; i < numberOfCameras; i++) {
					int cameraId = i;
					getCameraInfoMethod.invoke(Camera.class, i, cameraInfo);
					Field field = cameraInfoClz.getField("facing");
					int facingValue = (Integer) field.get(cameraInfo);
					Log.d(TAG, "*** facingValue=" + facingValue);
					if (facingValue == CAMERA_FACING_FRONT) {
						cameraFront[i] = true;
					} else {
						cameraFront[i] = false;
					}
					Field orientationField = cameraInfoClz.getField("orientation");
					cameraOrientation = (Integer) orientationField.get(cameraInfo);
					cameraOrientations[i] = cameraOrientation;
					Log.d(TAG, "*** cameraOrientation=" + cameraOrientation);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getNumberOfCameras() {
		return numberOfCameras;
	}

	public static int roundOrientation(int orientation, int orientationHistory) {
		boolean changeOrientation = false;
		if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
			changeOrientation = true;
		} else {
			int dist = Math.abs(orientation - orientationHistory);
			dist = Math.min(dist, 360 - dist);
			changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
		}
		if (changeOrientation) {
			return ((orientation + 45) / 90 * 90) % 360;
		}
		return orientationHistory;
	}

	public static int getRotation(Activity activity, int cameraId) {
		int degrees = CameraUtil.getDisplayRotation(activity);
		Log.e("CameraUtil.getDisplayRotation", "degrees = " + degrees);
		int result = CameraUtil.getDisplayOrientation(degrees, cameraId);
		return result;
	}

	public static int getDisplayRotation(Activity activity) {
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getOrientation();//getRotation()
		switch (rotation) {
		case Surface.ROTATION_0:
			return 0;
		case Surface.ROTATION_90:
			return 90;
		case Surface.ROTATION_180:
			return 180;
		case Surface.ROTATION_270:
			return 270;
		}
		return 0;
	}

	public static int getDisplayOrientation(int degrees, int cameraId) {
		// See android.hardware.Camera.setDisplayOrientation for
		// documentation.
		Camera camera = null;
		boolean isFrontCamera = false;
		int cameraOrientation = 90;
		if (cameraId < cameraFront.length) {
			isFrontCamera = cameraFront[cameraId];
			cameraOrientation = cameraOrientations[cameraId];
		}

		Log.e("CameraUtil.getDisplayOrientation", "cameraOrientation = "
				+ cameraOrientation + ", isFrontCamera = " + isFrontCamera);

		int result;
		if (isFrontCamera) {
			// result = (cameraOrientation + degrees) % 360;
			// result = (360 - result) % 360; // compensate the mirror
			result = (cameraOrientation + degrees) % 360;
		} else { // back-facing
			result = (cameraOrientation - degrees + 360) % 360;
		}

		Log.e("CameraUtil.getDisplayOrientation", "result = " + result);

		return result;
	}
	
	public static int getDisplayOrientation_origin(int degrees, int cameraId) {
		// See android.hardware.Camera.setDisplayOrientation for
		// documentation.
		boolean isFrontCamera = false;
		int cameraOrientation = 90;
		if (cameraId < cameraFront.length) {
			isFrontCamera = cameraFront[cameraId];
			cameraOrientation = cameraOrientations[cameraId];
		}
		int result;
		if (isFrontCamera) {
			result = (cameraOrientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
		} else { // back-facing
			result = (cameraOrientation - degrees + 360) % 360;
		}
		return result;
	}
}
