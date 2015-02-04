package com.compoment.listview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android_demonstrate_abstractcode.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//http://blog.csdn.net/johnny901114/article/details/7841685
//http://syab11.iteye.com/blog/987708



/** main.xml
 * <?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:background="@drawable/default_bg" >

    <ExpandableListView
        android:id="@+id/list"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent"
        android:layout_alignParentLeft="true"
        android:cacheColorHint="#00000000"
        android:divider="@android:color/white"
        android:dividerHeight="1dp" />

    <LinearLayout
        android:id="@+id/topGroup"
        android:layout_width="fill_parent"
        android:layout_height="50dip"
        android:visibility="gone"
        android:layout_alignParentTop="true">
        
         <ImageView
            android:id="@+id/ImageView01"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center_vertical"
            android:paddingTop="10dip"
            android:src="@drawable/user_group" >
        </ImageView>

        <RelativeLayout
            android:id="@+id/layout_013"
            android:layout_alignRight="@id/ImageView01"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" >

            <TextView
                android:id="@+id/content_001"
                android:layout_width="wrap_content"
                android:layout_height="fill_parent"
                android:layout_centerVertical="true"
                android:singleLine="true"
                android:paddingLeft="10px"
                android:textColor="#FFFFFF"
                android:textSize="26px" >
            </TextView>

            <ImageView
                android:id="@+id/narrowImage"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentRight="true"
                 />
        </RelativeLayout>
    </LinearLayout>
</RelativeLayout>
 * 
 * groupitem.xml
 * <?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="50dip"
    android:orientation="horizontal"
    android:layout_gravity="center_horizontal" >

        <ImageView
            android:id="@+id/ImageView01"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center_vertical"
            android:paddingTop="10dip"
            android:src="@drawable/user_group" >
        </ImageView>

        <RelativeLayout
            android:id="@+id/layout_013"
            android:layout_alignRight="@id/ImageView01"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" >

            <TextView
                android:id="@+id/content_001"
                android:layout_width="wrap_content"
                android:layout_height="fill_parent"
                android:layout_centerVertical="true"
                android:singleLine="true"
                android:paddingLeft="10px"
                android:textColor="#FFFFFF"
                android:textSize="26px" >
            </TextView>

            <ImageView
                android:id="@+id/narrowImage"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentRight="true" >
            </ImageView>
        </RelativeLayout>
    <!-- </LinearLayout> -->

</LinearLayout>
 * 
 * childitem.xml
 * <?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
  android:layout_width="fill_parent"
  android:layout_height="150dp"
  android:id="@+id/childlayout"
  android:orientation="horizontal">
     <ImageView android:id="@+id/child_image" 
                android:layout_width="wrap_content" 
                android:layout_height="wrap_content"
                android:background="@drawable/child_image"
                android:paddingTop="10dip"
                android:layout_marginLeft="40dip"></ImageView>
 <LinearLayout
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  android:orientation="vertical">
     <TextView android:text="" 
               android:id="@+id/child_text" 
               android:layout_width="wrap_content"
               android:layout_height="wrap_content"
               android:gravity="center_vertical"
               android:textSize="16dip"
               android:layout_gravity="center_vertical"></TextView>
     <TextView android:text="" 
               android:id="@+id/child_text2" 
               android:layout_width="wrap_content"
               android:layout_height="wrap_content"
               android:textSize="12dip"
               android:gravity="center_vertical"
               android:layout_gravity="center_vertical"></TextView>
 </LinearLayout>
</LinearLayout>
 * */

//public class LikeExpandableListActivity extends Activity implements
//		OnScrollListener {
//	// 存放父列表数据
//	List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
//	// 放子列表列表数据
//	List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
//	ExpandableAdapter expandAdapter;
//	ExpandableListView expandableList;
//	private int indicatorGroupHeight;
//	 /** 
//     *当前打开的父节点 
//     */  
//	private int the_group_expand_position = -1;
//    /** 
//     * 打开的父节点数 
//     */  
//	private int count_expand = 0;
//	
//	/** 
//     * 打开的父节点   <groupPosition, groupPosition>
//     */  
//	private Map<Integer, Integer> groupPositions = new HashMap<Integer, Integer>();
//	 /** 
//     * 滑动子列表时在上方显示父节点的view 
//     */  
//	private LinearLayout view_flotage = null;
//	/**
//	 * 父节点内容
//	 * */
//	private TextView group_content = null;
//	private ImageView narrowImage;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.main);
//		initData();
//
//
//		initView();
//	}
//
//	private void initData() {
//		for (int i = 1; i <= 20; i++) {
//			Map<String, String> curGroupMap = new HashMap<String, String>();
//			groupData.add(curGroupMap);
//			curGroupMap.put("group_text", "Group " + i);
//			List<Map<String, String>> children = new ArrayList<Map<String, String>>();
//			for (int j = 1; j < 15; j++) {
//				Map<String, String> curChildMap = new HashMap<String, String>();
//				children.add(curChildMap);
//				curChildMap.put("child_text1", "Child " + j);
//				curChildMap.put("child_text2", "Child " + j);
//			}
//			childData.add(children);
//		}
//	}
//
//	public void initView() {
//		
//		expandAdapter = new ExpandableAdapter(LikeExpandableListActivity.this);
//		expandableList = (ExpandableListView) findViewById(R.id.list);
//		View v = new View(this);
//		expandableList.addHeaderView(v);
//		expandableList.setAdapter(expandAdapter);
//		expandableList.setGroupIndicator(null);
//		
//		/**
//		 * 监听父节点打开的事件
//		 */
//		expandableList.setOnGroupExpandListener(new OnGroupExpandListener() {
//			@Override
//			public void onGroupExpand(int groupPosition) {
//				the_group_expand_position = groupPosition;
//				groupPositions.put(groupPosition, groupPosition);
//				count_expand = groupPositions.size();
//			}
//		});
//		/**
//		 * 监听父节点关闭的事件
//		 */
//		expandableList
//				.setOnGroupCollapseListener(new OnGroupCollapseListener() {
//					@Override
//					public void onGroupCollapse(int groupPosition) {
//						groupPositions.remove(groupPosition);
//						expandableList.setSelectedGroup(groupPosition);
//						count_expand = groupPositions.size();
//					}
//				});
//		
//		 /** 
//         * 滑动子列表时在上方显示父节点的view 
//         */  
//		view_flotage = (LinearLayout) findViewById(R.id.topGroup);
//		view_flotage.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				view_flotage.setVisibility(View.GONE);
//				expandableList.collapseGroup(the_group_expand_position);
//				expandableList.setSelectedGroup(the_group_expand_position);
//			}
//		});
//		
//		
//		group_content = (TextView) findViewById(R.id.content_001);
//		narrowImage = (ImageView) findViewById(R.id.narrowImage);
//		narrowImage.setBackgroundResource(R.drawable.btn_browser2);
//		//设置滚动事件
//		expandableList.setOnScrollListener(this);
//	}
//
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
//		//防止三星,魅族等手机第一个条目可以一直往下拉,父条目和悬浮同时出现的问题
//		if(firstVisibleItem==0){
//			view_flotage.setVisibility(View.GONE);
//		}
//		// 控制滑动时TextView的显示与隐藏
//		int npos = view.pointToPosition(0, 0);
//		if (npos != AdapterView.INVALID_POSITION) {
//			long pos = expandableList.getExpandableListPosition(npos);
//			int childPos = ExpandableListView.getPackedPositionChild(pos);
//			final int groupPos = ExpandableListView.getPackedPositionGroup(pos);
//			if (childPos == AdapterView.INVALID_POSITION) {
//				View groupView = expandableList.getChildAt(npos
//						- expandableList.getFirstVisiblePosition());
//				indicatorGroupHeight = groupView.getHeight();
//			}
//			
//			if (indicatorGroupHeight == 0) {
//				return;
//			}
//			
//			if (count_expand > 0) {
//				the_group_expand_position = groupPos;
//				group_content.setText(groupData.get(the_group_expand_position)
//						.get("group_text"));
//				if (the_group_expand_position != groupPos||!expandableList.isGroupExpanded(groupPos)) {
//					view_flotage.setVisibility(View.GONE);
//				} else {
//					view_flotage.setVisibility(View.VISIBLE);
//				}
//			}
//			if (count_expand == 0) {
//				view_flotage.setVisibility(View.GONE);
//			}
//		}
//
//		if (the_group_expand_position == -1) {
//			return;
//		}
//		/**
//		 * calculate point (0,indicatorGroupHeight)
//		 */
//		int showHeight = getHeight();
//		MarginLayoutParams layoutParams = (MarginLayoutParams) view_flotage
//				.getLayoutParams();
//		// 得到悬浮的条滑出屏幕多少
//		layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
//		view_flotage.setLayoutParams(layoutParams);
//	}
//
//	class ExpandableAdapter extends BaseExpandableListAdapter {
//		LikeExpandableListActivity exlistview;
//		@SuppressWarnings("unused")
//		private int mHideGroupPos = -1;
//
//		public ExpandableAdapter(LikeExpandableListActivity elv) {
//			super();
//			exlistview = elv;
//		}
//
//		// **************************************
//		public Object getChild(int groupPosition, int childPosition) {
//			return childData.get(groupPosition).get(childPosition)
//					.get("child_text1").toString();
//		}
//
//		public long getChildId(int groupPosition, int childPosition) {
//			return childPosition;
//		}
//
//		public int getChildrenCount(int groupPosition) {
//			return childData.get(groupPosition).size();
//		}
//
//		public View getChildView(int groupPosition, int childPosition,
//				boolean isLastChild, View convertView, ViewGroup parent) {
//			View view = convertView;
//			if (view == null) {
//				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				view = inflater.inflate(R.layout.childitem, null);
//			}
//			final TextView title = (TextView) view
//					.findViewById(R.id.child_text);
//			title.setText(childData.get(groupPosition).get(childPosition)
//					.get("child_text1").toString());
//			final TextView title2 = (TextView) view
//					.findViewById(R.id.child_text2);
//			title2.setText(childData.get(groupPosition).get(childPosition)
//					.get("child_text2").toString());
//			return view;
//		}
//
//		public View getGroupView(int groupPosition, boolean isExpanded,
//				View convertView, ViewGroup parent) {
//			View view = convertView;
//			if (view == null) {
//				LayoutInflater inflaterGroup = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				view = inflaterGroup.inflate(R.layout.groupitem, null);
//			}
//			TextView title = (TextView) view.findViewById(R.id.content_001);
//			title.setText(getGroup(groupPosition).toString());
//			ImageView image = (ImageView) view.findViewById(R.id.narrowImage);
//
//			System.out.println("isExpanded----->" + isExpanded);
//			if (isExpanded) {
//				image.setBackgroundResource(R.drawable.btn_browser2);
//			} else {
//				image.setBackgroundResource(R.drawable.btn_browser);
//			}
//			return view;
//		}
//
//		public long getGroupId(int groupPosition) {
//			return groupPosition;
//		}
//
//		public Object getGroup(int groupPosition) {
//			return groupData.get(groupPosition).get("group_text").toString();
//		}
//
//		public int getGroupCount() {
//			return groupData.size();
//
//		}
//
//		public boolean hasStableIds() {
//			return true;
//		}
//
//		public boolean isChildSelectable(int groupPosition, int childPosition) {
//			return false;
//		}
//
//		public void hideGroup(int groupPos) {
//			mHideGroupPos = groupPos;
//		}
//	}
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		return super.dispatchTouchEvent(ev);
//	}
//
//	private int getHeight() {
//		int showHeight = indicatorGroupHeight;
//		int nEndPos = expandableList.pointToPosition(0, indicatorGroupHeight);
//		if (nEndPos != AdapterView.INVALID_POSITION) {
//			long pos = expandableList.getExpandableListPosition(nEndPos);
//			int groupPos = ExpandableListView.getPackedPositionGroup(pos);
//			if (groupPos != the_group_expand_position) {
//				View viewNext = expandableList.getChildAt(nEndPos
//						- expandableList.getFirstVisiblePosition());
//				showHeight = viewNext.getTop();
//			}
//		}
//		return showHeight;
//	}
//
//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//	}
//}
