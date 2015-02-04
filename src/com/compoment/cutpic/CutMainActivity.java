package com.compoment.cutpic;


import java.util.ArrayList;
import java.util.List;

import com.android_demonstrate_abstractcode.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class CutMainActivity extends Activity
{
	private CutView m_view;
	private int degree = 0;
	private LinearLayout rotation_bar;
	private LinearLayout clip_bar;
	private LinearLayout free_rotateBar;
	Display dis;
	private LinearLayout bottom_bar;
	private SeekBar mSeekBar;
	public List<ImgBean> controls;
	
	public EditText txt;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		dis = getWindowManager().getDefaultDisplay();
		
		setContentView(R.layout.cutpic_first);
		
		bottom_bar = (LinearLayout)this.findViewById(R.id.btn_bar);
txt=(EditText)this.findViewById(R.id.txt);

		FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);
		m_view = new CutView(this);
		m_view.setLayoutParams(fl);
		((FrameLayout)this.findViewById(R.id.fr)).addView(m_view);
		
		m_view.SetImage(R.drawable.pic_cut);
		
		free_rotateBar = (LinearLayout)this.findViewById(R.id.free_rotate_bar);
		free_rotateBar.setVisibility(View.GONE);
		
	
		
		rotation_bar = (LinearLayout)this.findViewById(R.id.rotation_btn_bar);
		rotation_bar.setVisibility(View.GONE);
		
		clip_bar = (LinearLayout)this.findViewById(R.id.clip_bar);
		
		controls=new ArrayList();
	}
	

	private int temp_degree; 


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		ContentResolver cr = getContentResolver();
		if(resultCode != RESULT_OK) 
			return;
		
		if(requestCode == PIC_PICK)
		{
			Uri uri = data.getData();
			Cursor cursor = cr.query(uri, null, null, null, null);
			cursor.moveToFirst();
			String imgPath = cursor.getString(1);
			if(uri!=null)
			{
				try
				{
					m_view.SetImage(imgPath);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private final int PIC_PICK = 10;
	public void OnGetPic(View v)
	{
		degree = 0;
		if(m_view != null)
		{
			if(isPressed)
			{
				setFreeRotateBarGone();
			}
			Intent it = new Intent(Intent.ACTION_GET_CONTENT);
			it.setType("image/*");
			startActivityForResult(it, PIC_PICK);
		}
	}
	 CutpicPopupwindowNameLayoutPopupWindow menuPopupWindow ;
	
	 
	 //切图 重点哦
	public void OnCut(View v)
	{
		 menuPopupWindow = new CutpicPopupwindowNameLayoutPopupWindow(this, new OnClickListener() {
		 @Override
		 public void onClick(View view) {
			String tag= view.getTag().toString();
		     if(tag.equals("ok"))
		     {
		    	 ImgBean imgBean=null;

		 		if(m_view != null)
		 		{
		 			 
		 			imgBean=m_view.GetCutImage();
		 			SeeResultActivity.m_bmp = imgBean.img;
		 			
		 			 //CutMainActivity.this.startActivity(new Intent(CutMainActivity.this, SeeResultActivity.class));
		 		}
		 		
		 		
		    	 //英文名字
		    	String enName= menuPopupWindow.enNameEdit.getText().toString();
		    	imgBean.enName=enName;
		    	 //中文名字
		    	String cnName= menuPopupWindow.cnNameEdit.getText().toString();
		    	imgBean.cnName=cnName;
		    	//控件类型
		    	String controlType= menuPopupWindow.spinnervalue;
		    	imgBean.controlType=controlType;
		    	imgBean.controlBelong=menuPopupWindow.belongWhichControl;
		    	controls.add(imgBean);
		    	
		     }
		 menuPopupWindow.dismiss();
		 }});
		 menuPopupWindow.show(v);
		
	}
	
	public void OnFinish(View view)
	{
		 Gson gson = new Gson();  
			
		String jsonString= gson.toJson(controls);
		System.out.println(jsonString);
		//IosTableViewControlCreater iosTableView=new IosTableViewControlCreater(controls);
		
		//txt.setText(iosTableView.m);
	}
	
	private final int CLIPVIEW = 1;
	private final int ROTATIONVIEW = 2;
	private int view_flag = CLIPVIEW;
	public void OnClip(View v)
	{
		if(m_view != null)
		{
			view_flag = CLIPVIEW;
			m_view.showRect(true);
			m_view.setFree();
			setFreeRotateBarGone();
			rotation_bar.setVisibility(View.GONE);
			clip_bar.setVisibility(View.VISIBLE);
		}
	}
	
	
	public void OnRotate(View v)
	{
		if(m_view != null)
		{
			view_flag = ROTATIONVIEW;
			clip_bar.setVisibility(View.GONE);
			rotation_bar.setVisibility(View.VISIBLE);
			m_view.showRect(false);
		}
	}
	
	public void OnRotation(View v)
	{
		if(m_view != null)
		{
			setFreeRotateBarGone();
			degree += 90;
			if(degree >= 360) degree = 0;
			m_view.setRotate(degree);
		}
	}
	
	public void OnLeftRotation(View v)
	{
		if(m_view != null)
		{
			setFreeRotateBarGone();
			degree += -90;
			if(degree <= -360) degree = 0;
			m_view.setRotate(degree);
		}
	}
	
	public void OnXInvert(View v)
	{
		if(m_view != null)
		{
			setFreeRotateBarGone();
			m_view.setXInvert();
		}
	}
	
	public void OnYInvert(View v)
	{
		if(m_view != null)
		{
			setFreeRotateBarGone();
			m_view.setYInvert();
		}
	}
	
	public void OnReset(View v)
	{
		degree = 0;
		if(m_view != null)
		{
			setFreeRotateBarGone();
			degree = 0;
			if(view_flag == CLIPVIEW)
			{
				m_view.reset(true);
			}
			else if(view_flag == ROTATIONVIEW)
			{
				m_view.reset(false);
			}
		}
	}
	
//	public void OnScale_1_1(View v)
//	{
//		if(m_view != null)
//		{
//			m_view.setScale(1f);
//			m_popup.dismiss();
//		}
//	}
//	
//	public void OnScale_3_2(View v)
//	{
//		if(m_view != null)
//		{
//			m_view.setScale(1.5f);
//			m_popup.dismiss();
//		}
//	}
//	
//	public void OnScale_16_9(View v)
//	{
//		if(m_view != null)
//		{
//			m_view.setScale(1.78f);
//			m_popup.dismiss();
//		}
//	}
	
//	public void OnScale_free(View v)
//	{
//		if(m_view != null)
//		{
//			m_view.setFree();
//			m_popup.dismiss();
//		}
//	}
	
	private boolean isPressed = false;
	public void OnFreeRotate(View v)
	{
		if(m_view != null)
		{
			isPressed = !isPressed;
			if(isPressed)
			{
				free_rotateBar.setVisibility(View.VISIBLE);
			}
			else
			{
				free_rotateBar.setVisibility(View.GONE);
			}
		}
	}
	private void setFreeRotateBarGone()
	{
		free_rotateBar.setVisibility(View.GONE);
		isPressed = false;
	}
	
	public void OnFreeRotateReset(View v)
	{
		if(m_view != null)
		{
			mSeekBar.setProgress(50);
		}
	}
}
