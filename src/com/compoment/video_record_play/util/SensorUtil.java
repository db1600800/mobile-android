package com.compoment.video_record_play.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorUtil {
	// ��ֱ�����ֻ���Ͳ����ΪORIENTATION_0��˳ʱ����ת90��ΪORIENTATION_90
	public static final int ORIENTATION_0 = 0;
	public static final int ORIENTATION_90 = 1;
	public static final int ORIENTATION_180 = 2;
	public static final int ORIENTATION_270 = 3;

	private static int x = 0;
	private static int y = 0;
	private static int z = 0;

	public static int getCurrentDeviceOrientation(Context context) {
		x = 0;
		y = 0;
		z = 0;

		SensorEventListener listener = new SensorEventListener() {
			public void onAccuracyChanged(Sensor s, int accuracy) {
			}

			public void onSensorChanged(SensorEvent se) {
				x = (int) se.values[SensorManager.DATA_X];
				y = (int) se.values[SensorManager.DATA_Y];
				z = (int) se.values[SensorManager.DATA_Z];
			}
		};

		// ��ȡ���ٶȴ�����
		SensorManager sensorMgr = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// ע����ٶȴ�����������������Ǽ���������
		sensorMgr.registerListener(listener, sensor,
				SensorManager.SENSOR_DELAY_GAME);

		int count = 0;
		while (x == 0 && y == 0 && z == 0 && count < 4) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
			count++;
		}

		sensorMgr.unregisterListener(listener);

		return getOrientation(x, y);
	}

	private static int getOrientation(int x, int y) {
		final int BORDER = 8; // �ٽ�ֵ

		if (Math.abs(x) <= BORDER && y > 0) {
			return ORIENTATION_0;
		} else if (x > 0 && Math.abs(y) <= BORDER) {
			return ORIENTATION_90;
		} else if (Math.abs(x) <= BORDER && y < 0) {
			return ORIENTATION_180;
		} else if (x < 0 && Math.abs(y) <= BORDER) {
			return ORIENTATION_270;
		} else {
			return ORIENTATION_0;
		}
	}
}
