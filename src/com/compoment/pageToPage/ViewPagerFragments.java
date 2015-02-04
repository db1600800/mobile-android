package com.compoment.pageToPage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.android_demonstrate_abstractcode.R;

import android.widget.AdapterView.OnItemClickListener;


/**
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="vertical" >
    
    <android.support.v4.view.ViewPager
        android:id="@+id/viewpage"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:background="#000000"
        android:flipInterval="30" />

</LinearLayout>
*/


public class ViewPagerFragments extends FragmentActivity implements
		OnScrollListener {
	Context context;
	public View containView;
	ViewPager viewPager;

	public ViewPagerFragments() {

	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 获取数据
	
		super.onCreate(savedInstanceState);
		context = this;
		this.setContentView(R.layout.viewpager);
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		initViewPage();

	}

	/**
	 * 初始化 view page的相关数据
	 */
	public void initViewPage() {

		final List<Fragment> fragments = new ArrayList();

		// fragments.add(fragment);
		// notifyDataSetChanged();
		fragments.add(new Fragment());//例如
	

		viewPager.setAdapter(new FragmentPagerAdapter(
				getSupportFragmentManager()) {

			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				return fragments.get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return fragments.size();
			}
		});
		viewPager.setCurrentItem(0);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub

			}

		});

	}

	private View inflateView(int resource) {
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return vi.inflate(resource, null);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
	}

}
