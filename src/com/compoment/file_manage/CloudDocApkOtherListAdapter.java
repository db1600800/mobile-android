package com.compoment.file_manage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android_demonstrate_abstractcode.R;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;



public class CloudDocApkOtherListAdapter extends BaseAdapter {
	public  List<ApkFileInfo> listContent;
	private LayoutInflater layoutInflater;
	public List<ApkFileInfo> listChecked;
	private Context context;
	private Handler handler;
	private final int CHANGE_ALL_SELECT_BUTTON = 10;
	private final int RENAME_FOLDER_REFRESH = 11;
	public ApkFileInfo fileInfo;
	String  itemType="";
    
	
	public CloudDocApkOtherListAdapter(Context context, List<ApkFileInfo> list, Handler handler) {
		this.listContent = list;
		this.layoutInflater = LayoutInflater.from(context);
		this.context = context;
		listChecked = new ArrayList<ApkFileInfo>();
		this.handler = handler;
	}

	public void updateListView(List<ApkFileInfo> list) {
//		this.listContent = list;
//		this.notifyDataSetChanged();
//		isNotifyDataSetChanged=true;
		
		Log.i("bbq",""+list.size());
		//((ActivityWidgetApk)context).refreshAdapter(list);
		
	}

	public int getCount() {
		return listContent.size();
	}

	public ApkFileInfo getItem(int position) {
		return listContent.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ApkFileInfo temp = listContent.get(position);
	
		if (temp.isFolder == true) {// use this layout,if it's a folder

			if(convertView==null )
			{
				convertView = layoutInflater.inflate(R.layout.cloud_apk_list_item_folder, null);
				convertView.setTag(temp.isFolder);
			}else if(convertView!=null && (Boolean)convertView.getTag()==false)
			{
				convertView = layoutInflater.inflate(R.layout.cloud_apk_list_item_folder, null);
				convertView.setTag(temp.isFolder);
			}
			
		
			
			//folder name
			TextView tv = (TextView) convertView
					.findViewById(R.id.tv_foldername);
			
			TextView tvFileNum = (TextView)convertView.findViewById(R.id.tv_fileNum);
			
			Button btn = (Button) convertView
					.findViewById(R.id.btn_folderOperation);
			
			CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb_folder);
			
			
			
			
			final String fileName = temp.folderName.toString();
			tv.setText(fileName);
			
			//file num
		
			tvFileNum.setText("("+ temp.fileCount +")");
			
			//button folder operation
		
			btn.setOnTouchListener(new OnTouchListener() {
		
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction()==MotionEvent.ACTION_DOWN)
					{
					
					fileInfo = CloudDocApkOtherListAdapter.this.listContent.get(position);
					showOperateFolderDialog();
					}
					return false;
				}
			});
			
			//cb
			setCheckBoxOnCheckedChangeListener(cb, position);
			if (listChecked.contains(listContent.get(position))) {
				cb.setChecked(true);
			} else {
				cb.setChecked(false);
			}

		} else {// use this layout, if it 's a file
			
			
			
			
			if(convertView==null )
			{
				convertView = layoutInflater.inflate(R.layout.cloud_apk_list_item, null);
				convertView.setTag(temp.isFolder);
			}else if(convertView!=null && (Boolean)convertView.getTag()==true)
			{
				convertView = layoutInflater.inflate(R.layout.cloud_apk_list_item, null);
				convertView.setTag(temp.isFolder);
			}
			
			// ����ɫ������ʾ

			// �ļ���
			TextView fileText = (TextView) convertView
					.findViewById(R.id.text_filename);
			final String fileName = temp.fileName.toString();
			fileText.setText(fileName);

			// ����޸�ʱ��
			TextView lastModifyTime = (TextView) convertView
					.findViewById(R.id.text_time);
			lastModifyTime.setText(temp.lastModifiedTime.toString());
			
			//file.length
			TextView tvFileLength = (TextView)convertView.findViewById(R.id.tv_filelength);
			long exactLength = new File(temp.filePath).length();
			if(exactLength < 1024*1024){
				tvFileLength.setText(exactLength/1024 + "KB");
			}else{
				Float length1 = exactLength/(1024*1024f);
				Float length2 = (int)(length1*10)/10.0f;
				tvFileLength.setText(length2+"MB");
			}
			
			
			
			//image
			ImageView imageItem = (ImageView) convertView
					.findViewById(R.id.imageitem);
			setImageSrc(imageItem, listContent.get(position));

			// checkbox
			CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkbox);
			setCheckBoxOnCheckedChangeListener(cb, position);
			if (listChecked.contains(listContent.get(position))) {
				cb.setChecked(true);
			} else {
				cb.setChecked(false);
			}

			// �򿪰�ť
			Button buttonOpen = (Button) convertView
					.findViewById(R.id.button_item_open);
			buttonOpen.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (position > listContent.size() - 1)return;
					
					UtilFileClassify.openFile(context, listContent.get(position));
				}
			});

			// ɾ��ť
			Button buttonDelete = (Button) convertView
					.findViewById(R.id.button_item_delete);
			buttonDelete.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (position > listContent.size() - 1)return;
					
					deleFile(position);
				}
			});
		}

		if (position % 2 == 0) {
			//convertView.setBackgroundResource(R.drawable.item0selector);
		} else {
			//convertView.setBackgroundResource(R.drawable.item1selector);
		}

		return convertView;
	}

	private void deleFile(final int position) {
		final String filePath = listContent.get(position).filePath;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("确定删除吗?")
				.setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (position > listContent.size() - 1) {
							return;
						}
						File file = new File(filePath);
						file.delete();
						
						ApkFileInfo info=listContent.get(position);
						
//						if (((ActivityFileManager)context).currentPageId == R.id.button_other) {
//							
//							((ActivityFileManager)context).listContentForSdcard.remove(info);
//						}
						
						CloudDocApkOtherActivity.listFile.remove(info);
						
						
						listChecked.remove(info);
						//listContent.remove(info);
						
					
						Message msg = new Message();
						msg.what = 8;
						Bundle data = new Bundle();
						data.putInt("position", position);
						msg.setData(data);
						handler.sendMessage(msg);
						Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).create().show();
	}

	private void setImageSrc(ImageView imageItem, ApkFileInfo fileInfo) {
		
		String postfix = UtilFileClassify.getFilePostfix(fileInfo.fileName);
		
		if(postfix == null){
			//imageItem.setImageResource(R.drawable.default_other);
			return;
		}
		
		String type = UtilFileClassify.getFileTypeForItemImage(postfix);
		
		if (type.equals("video")) {
			if(postfix.equals(".flv")){       // flv,swf
				imageItem.setImageResource(R.drawable.cloud_flv);
			} else if(postfix.equals(".swf")){
				imageItem.setImageResource(R.drawable.cloud_swf);
			} else {
				imageItem.setImageResource(R.drawable.cloud_video);           //video
			}
			
			
		} else if (type.equals("audio")) {                             //audio
			imageItem.setImageResource(R.drawable.cloud_audio);
		} else if (type.equals("image")) {                             
			if(postfix.equals(".bmp")){                    
				imageItem.setImageResource(R.drawable.cloud_bmp);           //bmp
			} else if(postfix.equals(".gif")){
				imageItem.setImageResource(R.drawable.cloud_gif);           //gif
			} else if(postfix.equals(".jpg")){
				imageItem.setImageResource(R.drawable.cloud_jpg);           //jpg
			} else if(postfix.equals(".png")){
				imageItem.setImageResource(R.drawable.cloud_png);           //png
			} else if(postfix.equals(".psd")){
				imageItem.setImageResource(R.drawable.cloud_psd);           //psd
			} else {
				imageItem.setImageResource(R.drawable.cloud_image);         //img
			}
			
			
		} else if (type.equals("apk")) {
			imageItem.setImageDrawable(fileInfo.icon);
			
			
			
		} else if (type.equals("application")) { 
			// rar office pdf zip
			if(postfix.equals(".doc") || postfix.equals(".docx") 
					|| postfix.equals(".rtf") || postfix.equals(".wps")){        //word
				imageItem.setImageResource(R.drawable.cloud_doc);
			} else if(postfix.equals(".xls") || postfix.equals(".xlsx")){ //excel
				imageItem.setImageResource(R.drawable.cloud_xls);
			} else if(postfix.equals(".ppt") || postfix.equals(".pptx")){ //ppt
				imageItem.setImageResource(R.drawable.cloud_ppt);
			} else if(postfix.equals(".zip")){                            //zip
				imageItem.setImageResource(R.drawable.cloud_zip);
			} else if(postfix.equals(".rar")){                            //rar
				imageItem.setImageResource(R.drawable.cloud_rar);
			}
			else if(postfix.equals(".pdf")){                             //pdf
				imageItem.setImageResource(R.drawable.cloud_pdf);
			} else {
				imageItem.setImageResource(R.drawable.cloud_default_other); 
			}
			
			
		} else if (type.equals("text")) {
			if(postfix.equals(".txt")){                                   //txt
				imageItem.setImageResource(R.drawable.cloud_txt);
			} else {
				imageItem.setImageResource(R.drawable.cloud_document_default);
			}
		} else if (type.equals("other")) {
			imageItem.setImageResource(R.drawable.cloud_default_other);
		}

	}

	/**
	 * checkbox״̬�ı�ʱ ����Ӧ��position�ŵ�List��
	 * 
	 * @param cb
	 * @param position
	 */
	private void setCheckBoxOnCheckedChangeListener(final CheckBox cb,
			final int position) {

		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (position > listContent.size() - 1)
					return;
				handler.sendEmptyMessage(CHANGE_ALL_SELECT_BUTTON);// ��Listview����ѡ��ʱ��ı�ȫѡ��ť
				if (isChecked) {
					if (!listChecked.contains(listContent.get(position))) {
						listChecked.add(listContent.get(position));
					}
				} else {
					if (listChecked.contains(listContent.get(position))) {
						listChecked.remove(listContent.get(position));
					}
				}
			}
		});
	}

	private final int OPEN_FOLDER = 6;

	protected void showOperateFolderDialog() {
	
//		ListView lv = new ListView(context);
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//		params.setMargins(30, 10, 30, 10);
//		lv.setLayoutParams(params);
//		lv.setCacheColorHint(0);
//		String[] item = { "��", "������", "ɾ��" };
//		lv.setAdapter(new AdapterFileOperate(context,item));
//		
//		final Dialog dialog = new AlertDialog.Builder(context)
//				.setTitle("��ѡ�����")
//				.setView(lv)
//				.create();
//		
//		lv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				switch(position){
//				case 0://open floder
//					handler.sendEmptyMessage(OPEN_FOLDER);
//					break;
//				case 1://rename folder
//					showRenameFolderDialog();
//					break;
//				case 2://delete folder
//					showDeleteFolderDialog();
//					break;
//				}
//				dialog.dismiss();
//			}
//		});
//		dialog.show();
	}

	EditText ed;

	protected void showRenameFolderDialog() {
		ed = new EditText(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(30, 10, 30, 10);
		// params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		ed.setLayoutParams(params);
		ed.setText(fileInfo.folderName);

		Dialog dialog = new AlertDialog.Builder(context).setTitle("重命名")
				.setView(ed)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String newFolderName = ed.getText().toString();

						if (null != newFolderName && !newFolderName.equals("")) {
							if (isNewFolderNameExisted(newFolderName) == true) {
								showToast("重命名失败，请用一个不同的名字！");
							} else if (!newFolderName
									.equals(fileInfo.folderName)) {
								UtilFolderOperate.renameFolder(fileInfo.folderPath,
										ed.getText().toString(), context);
								handler.sendEmptyMessage(RENAME_FOLDER_REFRESH);
								listChecked.remove(fileInfo);
							}

						} else {
							showToast("文件夹名字不允许为空！");
						}
					}
				}).create();

		dialog.show();

	}

	protected boolean isNewFolderNameExisted(String newName) {
		List<ApkFileInfo> listFolder = UtilFolderOperate.getMyFolderList();
		for (int i = 0; i < listFolder.size(); i++) {
			if (listFolder.get(i).folderName.equals(newName)) {
				return true;
			}
		}
		return false;
	}

	protected void showDeleteFolderDialog() {
//		Dialog dialog = new AlertDialog.Builder(context).setTitle("ȷ��ɾ��")
//				.setMessage("��ȷ�ϣ�ɾ��" + fileInfo.folderName + "�ļ����Լ������ļ���")
//				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//
//					}
//				})
//				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						
//						UtilFolderOperate.deleteFolder(context, fileInfo.folderPath);
//						listContent.remove(fileInfo);
//						if (((ActivityFileManager)context).currentPageId == R.id.button_other) {
//							((ActivityFileManager)context).listContentForSdcard.remove(fileInfo);
//						}
//				
//						AdapterFileList.this.updateListView(listContent);
//						handler.sendEmptyMessage(3);
//					}
//				}).create();
//		dialog.show();
	}

	protected void showToast(String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();

	}

}
