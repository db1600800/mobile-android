package com.compoment.cutpic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.android_demonstrate_abstractcode.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;

/*CutpicPopupwindowNameLayoutPopupWindow menuPopupWindow = new CutpicPopupwindowNameLayoutPopupWindow(context, new OnClickListener() {
 @Override
 public void onClick(View arg0) {
 menuPopupWindow.dismiss();
 }});
 menuPopupWindow.show();*/

public class CutpicPopupwindowNameLayoutPopupWindow {
	private PopupWindow menuPopUp;
	/** 取消 */
	public Button btnCancel;
	/** 确定 */
	public Button btnConfirm;
	/** 控件属性 */
	public TextView menuPopTitle;
	/** 英文名: */
	public TextView enNameTxt;
	/** 中文名: */
	public TextView cnNameTxt;
	/** 控件类型: */
	public TextView controlTypeTxt;
	/***/
	public EditText enNameEdit;
	/***/
	public EditText cnNameEdit;
	/***/
	public Spinner controlTypeSpinner;
	public Spinner controlBelongSpinner;
	public String spinnervalue;
	public String belongSpinnerValue;
	public ImgBean belongWhichControl;
	private LayoutInflater inflater;
	
	List controlTypeSpinner_data_android = Arrays.asList(new String[] { "TextView", "Button","EditText","LinearLayout","RelativeLayout" ,"ImageView"});
	List controlTypeSpinner_data_ios = Arrays.asList(new String[] { "UITextView", "UIButton","UILabel","UIView" ,"UIImageView","UITableViewCell","UITableView"});
	List controlTypeSpinner_data=null;
	
	/** false在中部显示 true在底部显示 */
	private boolean isBottomOrCenter = false;
    CutMainActivity activity;
	public CutpicPopupwindowNameLayoutPopupWindow(Context context,
			OnClickListener onClicklistener) {
		activity=(CutMainActivity)context;
		inflater = LayoutInflater.from(context);
		controlTypeSpinner_data=controlTypeSpinner_data_ios;
		View view = inflater.inflate(R.layout.cutpic_popupwindow_name_layout,
				null);
		// 取消
		btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		btnCancel.setTag("cancel");
		btnCancel.setOnClickListener(onClicklistener);
		// 确定
		btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
		btnConfirm.setTag("ok");
		btnConfirm.setOnClickListener(onClicklistener);
		// 控件属性
		menuPopTitle = (TextView) view.findViewById(R.id.menu_pop_title);
		// 英文名:
		enNameTxt = (TextView) view.findViewById(R.id.en_name_txt);
		// 中文名:
		cnNameTxt = (TextView) view.findViewById(R.id.cn_name_txt);
		// 控件类型:
		controlTypeTxt = (TextView) view.findViewById(R.id.control_type_txt);
		//
		enNameEdit = (EditText) view.findViewById(R.id.en_name_edit);
		//
		cnNameEdit = (EditText) view.findViewById(R.id.cn_name_edit);
		//
		controlTypeSpinner = (Spinner) view
				.findViewById(R.id.control_type_spinner);
		//List controlTypeSpinner_data = Arrays.asList(new String[] { "TextView", "Button","EditText","LinearLayout","RelativeLayout" ,"ImageView"});
		ArrayAdapter controlTypeSpinner_adapter = new ArrayAdapter<String>(
				context, android.R.layout.simple_spinner_item,
				controlTypeSpinner_data);
		controlTypeSpinner.setAdapter(controlTypeSpinner_adapter);
		controlTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				spinnervalue = (String) arg0.getItemAtPosition(arg2);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		controlBelongSpinner = (Spinner) view
				.findViewById(R.id.control_belong_spinner);
		
		List<String> controlBelongSpinner_data=new ArrayList();
	
		for(ImgBean bean:activity.controls)
			
		{
			controlBelongSpinner_data.add(bean.controlType+"  "+bean.enName );
		}
		controlBelongSpinner_data.add("");
		ArrayAdapter controlBelongSpinner_adapter = new ArrayAdapter<String>(
				context, android.R.layout.simple_spinner_item,
				controlBelongSpinner_data);
		controlBelongSpinner.setAdapter(controlBelongSpinner_adapter);
		controlBelongSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				belongSpinnerValue = (String) arg0.getItemAtPosition(arg2);
				if(belongSpinnerValue.equals(""))
				{
					belongWhichControl=null;
				}else
				{
				belongWhichControl=(ImgBean)activity.controls.get(arg2);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		initPopWindow(view);
	}

	// not need change
	private void initPopWindow(View view) {
		menuPopUp = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, true);//
		menuPopUp.setFocusable(true);
		menuPopUp.setOutsideTouchable(true);
		//
		menuPopUp.setBackgroundDrawable(new BitmapDrawable());// 注掉 点
																// PopupWindow之外的区域后，
																// PopupWindow不会消失。
		//
		menuPopUp.setBackgroundDrawable(new ColorDrawable(
				android.R.color.transparent));// #c0000000 ColorDrawable
													// dw = new
													// ColorDrawable(-00000);全透明
													// // 注掉 点
													// PopupWindow之外的区域后，
													// PopupWindow不会消失。
		view.setFocusableInTouchMode(true);
		view.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU && menuPopUp.isShowing()) {
					menuPopUp.dismiss();
					return true;
				}
				return false;
			}
		});
	}

	private class TosatTouchInterceptor implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			menuPopUp.dismiss();
			return false;
		}
	}

	public void show(View root) {
		if (!menuPopUp.isShowing() && !isBottomOrCenter) {
			menuPopUp.showAtLocation(root, Gravity.CENTER, 0, 0);
		} else if (!menuPopUp.isShowing() && isBottomOrCenter) {
			menuPopUp.showAtLocation(root, Gravity.CENTER | Gravity.BOTTOM, 0,
					0);
		}
	}

	public void hide() {
		if (menuPopUp.isShowing()) {
			menuPopUp.dismiss();
		}
	}

	/**
	 *
	 */
	public void dismiss() {
		if (menuPopUp != null) {
			menuPopUp.dismiss();
		}
	}

	public boolean isShow() {
		return menuPopUp.isShowing();
	}
}
