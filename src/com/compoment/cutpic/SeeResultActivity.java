package com.compoment.cutpic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.android_demonstrate_abstractcode.R;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SeeResultActivity extends Activity
{
	public static  Bitmap m_bmp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.cutpic_second);

		((ImageView)this.findViewById(R.id.img)).setImageBitmap(m_bmp);
	}

	@Override
	protected void onDestroy()
	{
		((ImageView)this.findViewById(R.id.img)).setImageBitmap(null);
		if(m_bmp != null)
		{
			m_bmp.recycle();
			m_bmp = null;
		}
		super.onDestroy();
	}

	public void OnClick(View v)
	{
		if(m_bmp != null)
		{
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			m_bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
			if(SaveFileToSDCard(null, "123.jpg", output.toByteArray()) == null)
			{
				Toast.makeText(getApplicationContext(), "SD������д�룡", Toast.LENGTH_LONG).show();
				return;
			}
			else
			{
				Toast.makeText(getApplicationContext(), "����ɹ���", Toast.LENGTH_LONG).show();
//				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED));
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED), Environment.getExternalStorageDirectory().getAbsolutePath());
			}
		}
	}

	public String SaveFileToSDCard(String path, String name, byte[] bytes)
	{
		// �ж�SD���Ƿ�ɶ�д
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
			if(path != null && !path.equals(""))
			{
				dir += File.separator + path;
			}

			try
			{
				File fileDir = new File(dir);
				if(!fileDir.exists())
				{
					fileDir.mkdirs();
				}

				dir += File.separator + name;
				File saveFile = new File(dir);
				if(!saveFile.exists())
				{
					saveFile.createNewFile(); // ��Ҫ��AndroidManifest.xml���������
				}
				FileOutputStream os = new FileOutputStream(saveFile);
				os.write(bytes);
				os.close();

				return dir;
			}
			catch(FileNotFoundException e)
			{
				System.out.println("SD openFileOutput - FileNotFoundException!!!");
			}
			catch(IOException e)
			{
				System.out.println("SD openFileOutput - IOException!!!");
			}
		}

		return null;
	}
}
