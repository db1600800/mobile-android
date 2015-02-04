package com.compoment.video_record_play;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * 创建camera
 * 
 *
 */
public class CameraCreater {
	private static final String TAG = CameraCreater.class.getCanonicalName();
	private static Camera camera;
	private static boolean useFrontFacingCamera;
	
	// Default values
	private static int fps = 15;
	private static int width = 352;
	private static int height = 288;
	private static SurfaceHolder holder = null;
	private static PreviewCallback callback = null;
	
	private static final int MIN_SDKVERSION_addCallbackBuffer = 7;
	private static final int MIN_SDKVERSION_setPreviewCallbackWithBuffer = 7;
	private static final int MIN_SDKVERSION_setDisplayOrientation = 8;
	//private static final int MIN_SDKVERSION_getSupportedPreviewSizes = 5;
	
	private static Method addCallbackBufferMethod = null;
	private static Method setDisplayOrientationMethod = null;
	private static Method setPreviewCallbackWithBufferMethod = null;

	static{
		CameraCreater.useFrontFacingCamera = true;
	}
	
	static{
		if(getSDKVersion() >= CameraCreater.MIN_SDKVERSION_addCallbackBuffer){
			// According to http://developer.android.com/reference/android/hardware/Camera.html both addCallbackBuffer and setPreviewCallbackWithBuffer
			// are only available starting API level 8. But it's not true as these functions exist in API level 7 but are hidden.
			try {
				CameraCreater.addCallbackBufferMethod = Camera.class.getMethod("addCallbackBuffer", byte[].class);
			} catch (Exception e) {
				Log.e(CameraCreater.TAG, e.toString());
			} 
		}
		
		if(getSDKVersion() >= CameraCreater.MIN_SDKVERSION_setPreviewCallbackWithBuffer){
			try {
				CameraCreater.setPreviewCallbackWithBufferMethod = Camera.class.getMethod(
					"setPreviewCallbackWithBuffer", PreviewCallback.class);
			}  catch (Exception e) {
				Log.e(CameraCreater.TAG, e.toString());
			} 
		}
				
		if(getSDKVersion() >= CameraCreater.MIN_SDKVERSION_setDisplayOrientation){
			try {
				CameraCreater.setDisplayOrientationMethod = Camera.class.getMethod("setDisplayOrientation", int.class);
			} catch (Exception e) {
				Log.e(CameraCreater.TAG, e.toString());
			} 
		}
	}
	
	public static int getSDKVersion()
	{
	return Integer.parseInt(Build.VERSION.SDK);
	}
	
	public static Camera getCamera(){
		return CameraCreater.camera;
	}
	
	public static Camera openCamera(int fps, int width, int height, SurfaceHolder holder, PreviewCallback callback){
		if(CameraCreater.camera == null){
			try{
				if(CameraCreater.useFrontFacingCamera){
					CameraCreater.camera = CameraCreater.openFrontFacingCamera();
				}
				else{
					Log.e(TAG, "********** can not open front facing camera, open the default camera");
					CameraCreater.setUseFrontFacingCamera(false);
					CameraCreater.camera = Camera.open();
				}
				//MyCameraProducer.camera = Camera.open();
				Log.e(TAG, "********** MyCameraProducer.camera="+CameraCreater.camera);
				CameraCreater.fps = fps;
				CameraCreater.width = width;
				CameraCreater.height = height;
				CameraCreater.holder = holder;
				CameraCreater.callback = callback;
				
				Log.e(TAG, "********** open camera ***** fps="+fps+", width="+width+", height="+height);
				
				Camera.Parameters parameters = CameraCreater.camera.getParameters();
				
				/*
				 * http://developer.android.com/reference/android/graphics/ImageFormat.html#NV21
				 * YCrCb format used for images, which uses the NV21 encoding format. 
				 * This is the default format for camera preview images, when not otherwise set with setPreviewFormat(int). 
				 */
				parameters.setPreviewFormat(PixelFormat.YCbCr_420_SP);
				parameters.setPreviewFrameRate(CameraCreater.fps);
			
				try{
					parameters.setPictureSize(CameraCreater.width , CameraCreater.height);
					//parameters.setPictureSize(640 , 480);
					CameraCreater.camera.setParameters(parameters);
				}
				catch(Exception e){
					// FFMpeg converter will resize the video stream
					Log.d(CameraCreater.TAG, e.toString());
				}
				
				CameraCreater.camera.setPreviewDisplay(CameraCreater.holder);
				CameraCreater.initializeCallbacks(CameraCreater.callback);
			}
			catch(Exception e){
				CameraCreater.releaseCamera();
				Log.e(CameraCreater.TAG, e.toString());
			}
		}
		return CameraCreater.camera;
	}
	
	public static void releaseCamera(){
		if(CameraCreater.camera != null){
			CameraCreater.camera.stopPreview();
			CameraCreater.deInitializeCallbacks();
			CameraCreater.camera.release();
			CameraCreater.camera = null;
		}
	}
	
	public static void setDisplayOrientation(int degrees){
		if(CameraCreater.camera != null && CameraCreater.setDisplayOrientationMethod != null){
			try {
				CameraCreater.setDisplayOrientationMethod.invoke(CameraCreater.camera, degrees);
			} catch (Exception e) {
				Log.e(CameraCreater.TAG, e.toString());
			}
		}
	}
	
	public static void setDisplayOrientation(Camera camera, int degrees){
		if(camera != null && CameraCreater.setDisplayOrientationMethod != null){
			try {
				CameraCreater.setDisplayOrientationMethod.invoke(camera, degrees);
			} catch (Exception e) {
				Log.e(CameraCreater.TAG, e.toString());
			}
		}
	}
	
	public static void addCallbackBuffer(Camera camera, byte[] buffer) {
		try {
			CameraCreater.addCallbackBufferMethod.invoke(camera, buffer);
		} catch (Exception e) {
			Log.e(CameraCreater.TAG, e.toString());
		}
	}
	
	public static void addCallbackBuffer(byte[] buffer) {
		try {
			CameraCreater.addCallbackBufferMethod.invoke(CameraCreater.camera, buffer);
		} catch (Exception e) {
			Log.e(CameraCreater.TAG, e.toString());
		}
	}

	public static boolean isAddCallbackBufferSupported(){
		return CameraCreater.addCallbackBufferMethod != null;
	}
	
	/*public static boolean isFrontFacingCameraEnabled(){
		return MyCameraProducer.useFrontFacingCamera;
	}*/
	
	/*public static void useRearCamera(){
		MyCameraProducer.useFrontFacingCamera = false;
	}
	
	public static void useFrontFacingCamera(){
		MyCameraProducer.useFrontFacingCamera = true;
	}*/
	
	public static Camera toggleCamera(){
		if(CameraCreater.camera != null){
			CameraCreater.useFrontFacingCamera = !CameraCreater.useFrontFacingCamera;
			CameraCreater.releaseCamera();
			CameraCreater.openCamera(CameraCreater.fps, 
					CameraCreater.width, 
					CameraCreater.height,
					CameraCreater.holder, 
					CameraCreater.callback);
		}
		return CameraCreater.camera;
	}
	
	private static void initializeCallbacks(PreviewCallback callback){
		if(CameraCreater.camera != null){
			/*if(MyCameraProducer.setPreviewCallbackWithBufferMethod != null){
				Log.d(TAG, "-------------------- initializeCallbacks set default, callback="+callback);
				try {
					MyCameraProducer.setPreviewCallbackWithBufferMethod.invoke(MyCameraProducer.camera, callback);
				} catch (Exception e) {
					Log.e(MyCameraProducer.TAG, e.toString());
					e.printStackTrace();
				}
			}
			else{
				Log.d(TAG, "-------------------- initializeCallbacks set default, callback="+callback);
				MyCameraProducer.camera.setPreviewCallback(callback);
			}*/
			CameraCreater.camera.setPreviewCallback(callback);
			
		}
	}
	
	private static void deInitializeCallbacks(){
		if(CameraCreater.camera != null){
			if(CameraCreater.setPreviewCallbackWithBufferMethod != null){
				try {
					CameraCreater.setPreviewCallbackWithBufferMethod.invoke(CameraCreater.camera, new Object[]{ null });
				} catch (Exception e) {
					Log.e(CameraCreater.TAG, e.toString());
				}
			}
			else{
				CameraCreater.camera.setPreviewCallback(null);
			}
		}
	}
	
	public static boolean hasFrontCamera() {
		if(FrontFacingCameraMapper.hasPreferredCamera()) {
			return true;
		}
		if(FrontFacingCameraSwitcher.getSwitcher() != null) {
			return true;
		}
		if(FrontFacingCameraOpener.hasFrontCamera()){
			return true;
		}
		return false;
	}
	
	public static Camera openFrontFacingCamera() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Camera camera = null;
		CameraCreater.setUseFrontFacingCamera(true);
		//1. From mapper
		if((camera = FrontFacingCameraMapper.getPreferredCamera()) != null){
			Log.d(TAG, "********** 1. get camera from mapper");
			return camera;
		}
		
		//2. Use switcher
		if(FrontFacingCameraSwitcher.getSwitcher() != null){
			Log.d(TAG, "********** 2. get camera from Use switcher");
			camera = Camera.open();
			FrontFacingCameraSwitcher.getSwitcher().invoke(camera, (int)1);
			return camera;
		}
		
		//3. use CameraOpener
		if((camera = FrontFacingCameraOpener.getCamera()) != null){
			Log.d(TAG, "********** 3. use api 2.3 to get front camera");
			return camera;
		}

		CameraCreater.setUseFrontFacingCamera(false);
		//4. Use parameters
		Log.d(TAG, "********** 4. get camera from Use parameters");
		camera = Camera.open();
		Camera.Parameters parameters = camera.getParameters();
		parameters.set("camera-id", 2);
		camera.setParameters(parameters);
		return camera;
	}
	
	/***
	 * FrontFacingCameraSwitcher
	 * @author Mamadou Diop
	 *
	 */
	static class FrontFacingCameraSwitcher
	{
		private static Method DualCameraSwitchMethod;
		
		static{
			try{
				FrontFacingCameraSwitcher.DualCameraSwitchMethod = Class.forName("android.hardware.Camera").getMethod("DualCameraSwitch",int.class);
			}
			catch(Exception e){
				Log.d(CameraCreater.TAG, e.toString());
			}
		}
		
		static Method getSwitcher(){
			return FrontFacingCameraSwitcher.DualCameraSwitchMethod;
		}
	}
	
	/**
	 * FrontFacingCameraMapper
	 * @author Mamadou Diop
	 *
	 */
	static class FrontFacingCameraMapper
	{
		private static int preferredIndex = -1;
		
		static FrontFacingCameraMapper Map[] = {
			new FrontFacingCameraMapper("android.hardware.HtcFrontFacingCamera", "getCamera"),
			// Sprint: HTC EVO 4G and Samsung Epic 4G
			// DO not forget to change the manifest if you are using OS 1.6 and later
			new FrontFacingCameraMapper("com.sprint.hardware.twinCamDevice.FrontFacingCamera", "getFrontFacingCamera"),
			// Huawei U8230
            new FrontFacingCameraMapper("android.hardware.CameraSlave", "open"),
			// Default: Used for test reflection
			// new FrontFacingCameraMapper("android.hardware.Camera", "open"),
		};
		
		static{
			int index = 0;
			for(FrontFacingCameraMapper ffc: FrontFacingCameraMapper.Map){
				try{
					Class.forName(ffc.className).getDeclaredMethod(ffc.methodName);
					FrontFacingCameraMapper.preferredIndex = index;
					break;
				}
				catch(Exception e){
					Log.d(CameraCreater.TAG, e.toString());
				}
				
				++index;
			}
		}
		
		private final String className;
		private final String methodName;
		
		FrontFacingCameraMapper(String className, String methodName){
			this.className = className;
			this.methodName = methodName;
		}
		
		static Camera getPreferredCamera(){
			if(FrontFacingCameraMapper.preferredIndex == -1){
				return null;
			}
			
			try{
				Method method = Class.forName(FrontFacingCameraMapper.Map[FrontFacingCameraMapper.preferredIndex].className)
				.getDeclaredMethod(FrontFacingCameraMapper.Map[FrontFacingCameraMapper.preferredIndex].methodName);
				return (Camera)method.invoke(null);
			}
			catch(Exception e){
				Log.e(CameraCreater.TAG, e.toString());
			}
			return null;
		}
		
		static boolean hasPreferredCamera(){
			if(FrontFacingCameraMapper.preferredIndex == -1){
				return false;
			}
			
			try{
				Method method = Class.forName(FrontFacingCameraMapper.Map[FrontFacingCameraMapper.preferredIndex].className)
				.getDeclaredMethod(FrontFacingCameraMapper.Map[FrontFacingCameraMapper.preferredIndex].methodName);
				return true;
			}
			catch(Exception e){
				Log.e(CameraCreater.TAG, e.toString());
			}
			return false;
		}
	}
	
	static class FrontFacingCameraOpener {
		
		static Camera getCamera(){
			int cameraCount = 0;
		    Camera camera = null;
		    try {
		    	Class cameraInfoClz = Class.forName("android.hardware.Camera$CameraInfo");
		    	Log.d(TAG, "*** clz="+cameraInfoClz);
		    	Object cameraInfo = cameraInfoClz.newInstance();
		    	
		    	Method getNumberOfCamerasMethod = Camera.class.getMethod("getNumberOfCameras");
		    	Log.d(TAG, "*** getNumberOfCamerasMethod="+getNumberOfCamerasMethod);
		    	if(getNumberOfCamerasMethod != null) {
		    		cameraCount = (Integer)getNumberOfCamerasMethod.invoke(Camera.class);
		    		Log.d(TAG, "*** cameraCount="+cameraCount);
		    	}
		    	Field cameraFacingFrontField = cameraInfoClz.getField("CAMERA_FACING_FRONT");
		    	int CAMERA_FACING_FRONT = (Integer)cameraFacingFrontField.get(null);
		    	Log.d(TAG, "*** CAMERA_FACING_FRONT="+CAMERA_FACING_FRONT);
		    	for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
		    		Method getCameraInfoMethod = Camera.class.getMethod("getCameraInfo",new Class[] {int.class, cameraInfoClz});
			    	Log.d(TAG, "*** getCameraInfoMethod="+getCameraInfoMethod);
			    	if(getCameraInfoMethod != null) {
			    		getCameraInfoMethod.invoke(Camera.class, camIdx, cameraInfo);
			    		Field field = cameraInfoClz.getField("facing"); 
			    		int facingValue = (Integer)field.get(cameraInfo);
			    		Log.d(TAG, "*** facingValue="+facingValue);
			    		if(facingValue == CAMERA_FACING_FRONT) {
			    			Method openMethod = Camera.class.getMethod("open", new Class[]{int.class});
					    	Log.d(TAG, "*** openMethod="+openMethod);
					    	if(openMethod != null) {
					    		camera = (Camera)openMethod.invoke(Camera.class, camIdx);
					    	}
			    		}
			    	}
		    	}
		    } catch(Exception e) {
		    	e.printStackTrace();
		    }
		    return camera;
		    /*Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		    Log.d(TAG, "*** cameraInfo="+cameraInfo);
		    cameraCount = Camera.getNumberOfCameras(); // get cameras number
		          
		    for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
		        Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo
		        if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_FRONT) { // �������ͷ�ķ�λ��Ŀǰ�ж���ֵ�����ֱ�ΪCAMERA_FACING_FRONTǰ�ú�CAMERA_FACING_BACK����
		            try {            
		            	camera = Camera.open( camIdx );
		            } catch (RuntimeException e) {
		                e.printStackTrace();
		            }
		        }
		    }
		    return camera;*/
		}
		
		static boolean hasFrontCamera(){
			int cameraCount = 0;
		    try {
		    	Class cameraInfoClz = Class.forName("android.hardware.Camera$CameraInfo");
		    	Object cameraInfo = cameraInfoClz.newInstance();
		    	
		    	Method getNumberOfCamerasMethod = Camera.class.getMethod("getNumberOfCameras");
		    	if(getNumberOfCamerasMethod != null) {
		    		cameraCount = (Integer)getNumberOfCamerasMethod.invoke(Camera.class);
		    	}
		    	Field cameraFacingFrontField = cameraInfoClz.getField("CAMERA_FACING_FRONT");
		    	int CAMERA_FACING_FRONT = (Integer)cameraFacingFrontField.get(null);
		    	for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
		    		Method getCameraInfoMethod = Camera.class.getMethod("getCameraInfo",new Class[] {int.class, cameraInfoClz});
			    	if(getCameraInfoMethod != null) {
			    		getCameraInfoMethod.invoke(Camera.class, camIdx, cameraInfo);
			    		Field field = cameraInfoClz.getField("facing"); 
			    		int facingValue = (Integer)field.get(cameraInfo);
			    		if(facingValue == CAMERA_FACING_FRONT) {
			    			Method openMethod = Camera.class.getMethod("open", new Class[]{int.class});
					    	if(openMethod != null) {
					    		return true;
					    	}
					    	
			    		}
			    	}
		    	}
		    } catch(Exception e) {
		    	e.printStackTrace();
		    }
		    return false;
		}
	}

	public static boolean isUseFrontFacingCamera() {
		return useFrontFacingCamera;
	}

	public static void setUseFrontFacingCamera(boolean useFrontFacingCamera) {
		CameraCreater.useFrontFacingCamera = useFrontFacingCamera;
	}
}
