package com.compoment.file_manage;



import com.android_demonstrate_abstractcode.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;



public class CloudDocApkOtherSortAdapter extends BaseAdapter{
	Context context;
	String[] sortByWhat = {"按时间","按大小","按名称"};
	public int sortPosition  = 0;
	public boolean[] isAscend = {false,false,false};
	Handler handler;
	String type;
	PopupWindow dialog;
	public CloudDocApkOtherSortAdapter(Context context){
		this.context = context;
	}
	
	
	public void setValueToDefault(){
		sortPosition = 0;
		isAscend[0] = false;
		isAscend[1] = false;
		isAscend[2] = false;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sortByWhat.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		
		if(null == convertView){
			convertView = LayoutInflater.from(context).inflate(R.layout.cloud_doc_apk_other_sort_list_item, null);
		}
		
		//textview sort by what
		TextView tv = (TextView)convertView.findViewById(R.id.tv_sort_item);
		tv.setText(sortByWhat[position]);
		
		//radiobutton
		RadioButton rb = (RadioButton)convertView.findViewById(R.id.rb_sort_item);
		if(position == sortPosition){
			rb.setChecked(true);
		}else{
			rb.setChecked(false);
		}
		
		//button for showing ascend or descend
		ImageView img = (ImageView)convertView.findViewById(R.id.img_sort_item);
		if(isAscend[position] == false){
			//img.setImageResource(R.drawable.descend);
		} else {
			//img.setImageResource(R.drawable.ascend);
		}
		
		
		rb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sortPosition = position;
				
				if(("sort").equals(type))
				{
				handler.sendEmptyMessage(position);
			
				dialog.dismiss();
				}else
				{
				 CloudDocApkOtherSortAdapter.this.notifyDataSetChanged();
				}
			}
		});
		
		
		return convertView;
	}

	
	public void setHandler(Handler handler,String type,PopupWindow dialog)
	{
		this.type=type;
		this.handler=handler;
		this.dialog=dialog;
	}
	
}
