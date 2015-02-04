package com.compoment.welcom_introduct;


import java.util.ArrayList;
import java.util.List;

import com.android_demonstrate_abstractcode.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android_demonstrate_abstractcode.R;


public class WelcomIntroduct extends Activity {

	private RelativeLayout layoutStart; 
	private RelativeLayout layoutIntroduce; 	
	private ViewPager viewpageIntroduce; 
	private RelativeLayout layout; 

 	List<View> viewlist = new ArrayList<View>(); 	
 	LayoutInflater lInflater = getLayoutInflater();
 	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcom_introduce_main);
		
		layoutIntroduce = (RelativeLayout) findViewById(R.id.layout_introduce);		
        initViewPage();		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	public void initViewPage()
	{
		View v1 = lInflater.inflate(R.layout.welcom_introduce_1, null);
		View v2 = lInflater.inflate(R.layout.welcom_introduce_2, null);
		View v3 = lInflater.inflate(R.layout.welcom_introduce_3, null);

		viewlist.add(v1);
		viewlist.add(v2);
		viewlist.add(v3);
		
		ViewPagerAdapter adapterViewPager = new ViewPagerAdapter(viewlist);
		
		viewpageIntroduce = (ViewPager) findViewById(R.id.vp_Introduce);
		viewpageIntroduce.setAdapter(adapterViewPager);
		viewpageIntroduce.setOnPageChangeListener(new OnPageChangeListener() {
			int selected = 0;
			
			@Override
			public void onPageScrollStateChanged(int arg0) {		
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (selected == 2 && arg0 == 2 && arg2 == 0) {		
				}
			}

			@Override
			public void onPageSelected(int arg0) {
				selected = arg0;
			}
		}
	    );

		
		viewpageIntroduce.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
			
				if (event.getAction() == MotionEvent.ACTION_MOVE) {

				}
				return false;
			}

		});
		
	}
	








	class ForwardTask extends AsyncTask<String, Integer, Void> {
	
		@Override
		protected Void doInBackground(String... params) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			publishProgress(1);
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			loginHandler.sendEmptyMessage(0);
			super.onProgressUpdate(values);
		}
	}





	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			
				return true;
			}
//		exitDialog = new DialogFactory(this);
//		exitDialog.createTwoButtonDialog("",
//					"", "ȡ��", "ȷ��",
//					exitDialogClick, exitDialogClick);
//		
		return true;
	}

	
	





	private Handler loginHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

//			Intent intent = new Intent();
//			intent.setClass(LoginActivity.this, ActivityMain.class);
//			Bundle data = new Bundle();
//			data.putBoolean("logined", true);
//			switch (msg.what) {
//			case H_USE_NOW:
//				if (ifFinish) {
//					break;
//				}
//				data.putBoolean("isFirstTime", true);
//
//				intent.putExtras(data);
//				startActivity(intent);
//				break;
//			case H_LOGIN:
//				if (ifFinish) {
//					break;
//				}
//				intent.putExtras(data);
//				startActivity(intent);
//				break;
//			default:
//				break;
			}
		
	};
	
	


	
	/**
	 * @ClassName: AdapterViewPager
	 * @Description: ��¼��������������
	 */
	private class ViewPagerAdapter extends PagerAdapter{
		private List<View> list = new ArrayList<View>();
		
		public ViewPagerAdapter(List<View> list){
			this.list = list;
		}
		
		public void update(List<View> list){
			this.list = list;
			notifyDataSetChanged();
		}


		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			
		}

		@Override
		public Object instantiateItem(View view, int position) {
			try{
				((ViewPager)view).addView(list.get(position),0);
			}catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(position);
		}


		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (object);
		}
		
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}
		
		@Override
		public void finishUpdate(View arg0) {

		}

	}

	
}
