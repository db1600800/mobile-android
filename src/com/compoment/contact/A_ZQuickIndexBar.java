package com.compoment.contact;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android_demonstrate_abstractcode.R;





import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class A_ZQuickIndexBar extends TableLayout {
	Map az = new HashMap();
	OnQuickIndexSelectedListener quickIndexListener;
	int lastY = 0;

	
	public boolean isTouch = false;

	public A_ZQuickIndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		//this.setBackgroundResource(R.drawable.contract_num_bg);
		this.setBackgroundResource(Color.TRANSPARENT);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (az != null && az.size() == 0 /* && ((QuickIndexItem)az.get(0)).x==0 */) {// ȡ��26����ĸ�ؼ�������λ��
			int tablerowCount = this.getChildCount();
			for (int i = 0; i < tablerowCount; i++) {
				TableRow row = (TableRow) this.getChildAt(i);
				TextView view = (TextView) row.getChildAt(0);
				QuickIndexItem point = new QuickIndexItem();
				Rect mRectSrc = new Rect();
				row.getGlobalVisibleRect(mRectSrc);
				point.x = mRectSrc.left;
				point.ytop = mRectSrc.top;
				point.ybottom = mRectSrc.bottom;
				point.txt = view.getText().toString();
				point.view = view;

				az.put(i, point);
			}
		}

		int eventY = (int) event.getY();// ���λ��Y

		int messageViewHeight = 0;// �ź���ʾ��+����߶�
		Rect mRect = new Rect();
		this.getGlobalVisibleRect(mRect);
		messageViewHeight = mRect.top;

		int focusItem = 0;
		if (event.getAction() == MotionEvent.ACTION_MOVE ||event.getAction() == MotionEvent.ACTION_DOWN) {
	
			{

				lastY = eventY;

				for (int i = 0; i < az.size(); i++) {

					int x = ((QuickIndexItem) az.get(i)).x;
					int y = ((QuickIndexItem) az.get(i)).ytop;
					int ybottom = ((QuickIndexItem) az.get(i)).ybottom;

					if (i == 0) {
						// messageViewHeight = y;
					}

					if (eventY + messageViewHeight >= ((QuickIndexItem) az
							.get(i)).ytop
							&& eventY + messageViewHeight < ((QuickIndexItem) az
									.get(i)).ybottom) {
						// ���λ���ڴ˿ؼ������
						String c = ((QuickIndexItem) az.get(i)).txt;
						// ((QuickIndexItem)
						// az.get(i)).view.setBackgroundResource(R.drawable.contact_num_bg);

						quickIndexListener
								.onQuickIndexSelected(((QuickIndexItem) az
										.get(i)).txt);
						

					} else {
						
					}
				}

			}

			this.setBackgroundColor(Color.GRAY);
			this.getBackground().setAlpha(50);
			
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			
			
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			
			this.setBackgroundResource(Color.TRANSPARENT);
		}

		
		
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	// list ���� ����������
	public void setFocusItem(String s) {
		int tablerowCount = this.getChildCount();

		for (int i = 0; i < tablerowCount; i++) {
			TableRow row = (TableRow) this.getChildAt(i);
			TextView view = (TextView) row.getChildAt(0);
			QuickIndexItem point = new QuickIndexItem();
			Rect mRectSrc = new Rect();
			row.getGlobalVisibleRect(mRectSrc);
			point.x = mRectSrc.left;
			point.ytop = mRectSrc.top;
			point.ybottom = mRectSrc.bottom;
			point.txt = view.getText().toString();
			point.view = view;
			az.put(i, point);
		}

		String azString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


		for (int i = 0; i < az.size(); i++) {
			if (((QuickIndexItem) az.get(i)).txt.equals(s)) {
				String temp = ((QuickIndexItem) az.get(i)).view.getText()
						.toString();
				((QuickIndexItem) az.get(i)).view
						.setBackgroundResource(R.drawable.contact_quick_index_focus_block_bg);
			}else if(azString.indexOf(s) == -1 && i==0) 
			{
				((QuickIndexItem) az.get(i)).view
				.setBackgroundResource(R.drawable.contact_quick_index_focus_block_bg);
			}
			else {
				((QuickIndexItem) az.get(i)).view
						.setBackgroundColor(Color.TRANSPARENT);
				((QuickIndexItem) az.get(i)).view.setGravity(Gravity.CENTER);
			}
		}
	}

	public interface OnQuickIndexSelectedListener {
		public abstract void onQuickIndexSelected(String paramChar);
	}

	public void setOnQuickIndexSelectedListener(
			OnQuickIndexSelectedListener paramOnSelectedListener) {
		this.quickIndexListener = paramOnSelectedListener;
	}

	public class QuickIndexItem {
		public int x;
		public int ytop;
		public int ybottom;
		public String txt;
		public TextView view;
	}
}
