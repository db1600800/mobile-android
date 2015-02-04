package com.compoment.video_record_play;

import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


public class VideoCameraPreviewSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	public static final int FLAG_CAMERA_FRONT = 0;
	public static final int FLAG_CAMERA_BACK = 1;

	private SurfaceHolder holder;
	private Camera camera;
	private boolean isPreviewing;
	private VideoPreviewRecordPlayActivity context;
	private int frontOrBack;//前置或后置摄像头

	public VideoCameraPreviewSurfaceView(VideoPreviewRecordPlayActivity context,
			int frontOrBack) {
		super(context);
		this.context = context;
		this.holder = super.getHolder();
		this.holder.addCallback(this);
		this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		this.frontOrBack = frontOrBack;
		setZOrderOnTop(false);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		startPreview();
	}

	
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (isPreviewing) {
			camera.stopPreview();
			isPreviewing = false;
		}
		try {
			Camera.Parameters p = camera.getParameters();
			List<Size> list = p.getSupportedPreviewSizes();

			
			boolean isSupportWidthAndHeight = false;
			for (Size size : list) {
				// landscape
				if (size.height == height && size.width == width) {
					isSupportWidthAndHeight = true;
					break;
				}

				// portrial
				// if (size.height == width && size.width == height) {
				// isSupportWidthAndHeight = true;
				// break;
				// }

				// Log.e("MessageVideoPreviewSurfaceView", "  width = " + size.width +
				// " height = " + size.height);
			}
			if (!isSupportWidthAndHeight) {
				width = 640;
				height = 480;
				// width = 480;
				// height = 640;
			}

			// context.setPreviewSize(width, height); // landscape
			//通知activity
			context.setPreviewSize(width, height); // protrail��

			p.setPreviewSize(width, height); // landscape��
			// p.setPreviewSize(height, width); // protrail��
			requestLayout();
			// p.setPreviewFormat(PixelFormat.YCbCr_420_SP);
			camera.setParameters(p);

			camera.setPreviewDisplay(holder);
			camera.startPreview();
			isPreviewing = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		stopPreview();
	}

	/** Camera*/
	private void startPreview() {
		
		while (camera != null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			
			if (frontOrBack == FLAG_CAMERA_FRONT
					&& CameraUtil.getNumberOfCameras() > 1) { // ����ǰ������ͷ
				camera = CameraCreater.openFrontFacingCamera();
				setCameraDisplayOrientation(context,
						CameraUtil.getNumberOfCameras() - 1, camera);
				Log.e("camera", "open front camera");
			} else { //
				camera = Camera.open();
				// camera.setDisplayOrientation(90);
				Log.e("camera", "open back camera");
			}
			
		} catch (Exception e) {
			Toast.makeText(context, "�޷���������ͷ", Toast.LENGTH_SHORT).show();
			((Activity) context).finish();
			e.printStackTrace();
		}
	}

	private static void setCameraDisplayOrientation(Activity activity,
			int cameraId, Camera camera) {

		int result = CameraUtil.getRotation(activity, cameraId);
		
		//这个地方需留意下  2.2才可用
		if(getSDKVersion()>7)
		{
		  camera.setDisplayOrientation(result); // landscape
		}
		// camera.setDisplayOrientation((result + 180) % 360); // portrait�����
	}

	/** Camera����Ԥ�� */
	private void stopPreview() {
		if (camera != null) {
			camera.stopPreview();
			isPreviewing = false;
			Log.e("camera", "release()");
			camera.release();
			Log.e("camera", "release()  finished");
			camera = null;
		}
	}

	public SurfaceHolder getHolder() {
		return holder;
	}

	public void setHolder(SurfaceHolder holder) {
		this.holder = holder;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public static int getSDKVersion()
	{
	return Integer.parseInt(Build.VERSION.SDK);
	}
}
