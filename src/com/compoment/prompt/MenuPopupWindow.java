package com.compoment.prompt;


import java.util.ArrayList;
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

/*MenuPopupWindow menuPopupWindow = new MenuPopupWindow(this, "", new OnClickListener() {
	@Override
	public void onClick(View arg0) {
	}
});*/


public class MenuPopupWindow {
	private PopupWindow menuPopUp;
	private TextView menu_pop_title;
	public ListView listView;
	private LayoutInflater inflater;
	public String whichSelect;
	private boolean isViewMenu = false;
	private MenuPopupWindow exitPw;


	/**
	 * title
	 * edit
	 * 2btn
	 */
	public MenuPopupWindow(Context context,String title,String info,OnClickListener onClicklistener) {
		isViewMenu = false;
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.prompt_menupopupwindow_title_edit_2btn, null);

		menu_pop_title = (TextView) view.findViewById(R.id.menu_pop_title);
		menu_pop_title.setText(title);
		EditText edit = (EditText) view.findViewById(R.id.tv_edit_name);
		edit.setText(info);

		Button cancel = (Button) view.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(onClicklistener);

		Button confirm = (Button) view.findViewById(R.id.btn_confirm);
		confirm.setOnClickListener(onClicklistener);

		initPopWindow(view);
	}



	/**
	 * title
	 * msg
	 * 2btn
	 */
	public MenuPopupWindow(Context context,String title,String deletedInfo,OnClickListener onClicklistener,String type) {
		isViewMenu = false;
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.prompt_menupopupwindow_title_msg_2btn, null);

		menu_pop_title = (TextView) view.findViewById(R.id.menu_pop_title);
		menu_pop_title.setText(title);

		TextView delInfo = (TextView) view.findViewById(R.id.tv_deleted_info);
		delInfo.setText(deletedInfo);


		Button cancel = (Button) view.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(onClicklistener);

		Button confirm = (Button) view.findViewById(R.id.btn_confirm);
		confirm.setOnClickListener(onClicklistener);

		initPopWindow(view);
	}

	/**
	 * only msg
	 */
	public MenuPopupWindow(Context context,String title,OnClickListener onClicklistener) {
		isViewMenu = false;
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.prompt_menupopupwindow_only_msg, null);

		menu_pop_title = (TextView) view.findViewById(R.id.tv_deleted_info);
		menu_pop_title.setText(title);


		initPopWindow(view);

		//轻触退出
		TosatTouchInterceptor tosatTouchInterceptor = new TosatTouchInterceptor();
		menuPopUp.setTouchInterceptor(tosatTouchInterceptor);
	}

/**
 * list
 */
	public MenuPopupWindow(Context context, List items,OnItemClickListener onItemClickListener) {

		isViewMenu = false;
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(
				R.layout.prompt_menupopupwindow_listview, null);

		listView = (ListView) view.findViewById(R.id.lv_menus);
		final ListItemsAdapter adapter = new ListItemsAdapter(
				items, this, context);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onItemClickListener);
		initPopWindow(view);
	}


	/**
	 * list
	 */
	class ListItemsAdapter extends BaseAdapter {

		List items;
		public String select;
		MenuPopupWindow menuPopup;
		Context context;


		public ListItemsAdapter(List items, MenuPopupWindow menuPopup,
				Context context) {
			this.items = items;
			this.menuPopup = menuPopup;
			this.context = context;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView,
				final ViewGroup parent) {
			final ViewGroup vg = (ViewGroup) inflater.inflate(
					R.layout.prompt_menupopupwindow_listview_item, null);

			final ImageView pathImgView = (ImageView) vg
					.findViewById(R.id.path_img);

			TextView pathNameTxtView = (TextView) vg
					.findViewById(R.id.path_name);

			TextView pathSpaceTxtView = (TextView) vg
					.findViewById(R.id.path_space);

			return vg;
		}
	}



/**
 * GridView
 */
	public MenuPopupWindow(Context context, List items,OnItemClickListener onItemClickListener,String gridviewflag) {

		isViewMenu = false;
		inflater = LayoutInflater.from(context);

		View view = inflater.inflate(R.layout.prompt_menupopupwindow_gridview, null);

		GridView menuGrid = (GridView) view.findViewById(R.id.gridview);
		menuGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		menuGrid.setAdapter(new GridViewAdapter(context));
		menuGrid.setOnItemClickListener(onItemClickListener);
		initPopWindow(view);
	}


    private class GridViewAdapter extends BaseAdapter{

	    	int[] menuImageArray = {
	    			R.drawable.prompt_menupopupwindow_grideview_open, R.drawable.prompt_menupopupwindow_grideview_cancel, R.drawable.prompt_menupopupwindow_grideview_update,
	    			R.drawable.prompt_menupopupwindow_grideview_exit };

	    	String[] menuNameArray = { "打开", "删除", "更新", "退出" };
	    	Context context;
	    	public GridViewAdapter(Context context){
	    		this.context = context;
	    	}
	    	@Override
	    	public int getCount() {

	    		return menuImageArray.length;
	    	}

	    	@Override
	    	public Object getItem(int position) {

	    		return null;
	    	}

	    	@Override
	    	public long getItemId(int position) {

	    		return 0;
	    	}



	    	@Override
	    	public View getView(int position, View convertView, ViewGroup parent) {
	    		this.notifyDataSetChanged();// i dont know why.but if i dont do this,the position is always 0.

	    		convertView = LayoutInflater.from(context).inflate(R.layout.prompt_menupopupwindow_gridview_item, null);

	    		ImageView img = (ImageView)convertView.findViewById(R.id.img_gridview_item);
	    		TextView tv = (TextView)convertView.findViewById(R.id.tv_gridview_item);


	    		img.setImageResource(menuImageArray[position]);
	    		tv.setText(menuNameArray[position]);
	    		convertView.setTag(menuNameArray[position]);

	    		convertView.setClickable(false);


	    		return convertView;
	    	}

	    }




	//not need change

	private void initPopWindow(View view) {
		menuPopUp = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);//

		menuPopUp.setFocusable(true);

		menuPopUp.setOutsideTouchable(false);
		//
		menuPopUp.setBackgroundDrawable(new BitmapDrawable());
		//
		menuPopUp.setBackgroundDrawable(new ColorDrawable(R.color.prompt_menupopupwindow_bg));

		view.setFocusableInTouchMode(true);

		view.setOnKeyListener(new View.OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {

					if(keyCode == KeyEvent.KEYCODE_MENU && menuPopUp.isShowing()){
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

		if (!menuPopUp.isShowing() && !isViewMenu) {
			menuPopUp.showAtLocation(root, Gravity.CENTER, 0, 0);
		}else if(!menuPopUp.isShowing() && isViewMenu){
			menuPopUp.showAtLocation(root, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
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
	public void dismiss(){
		if(menuPopUp != null){
			menuPopUp.dismiss();
		}
	}

	public boolean isShow(){
		return menuPopUp.isShowing();
	}





}
