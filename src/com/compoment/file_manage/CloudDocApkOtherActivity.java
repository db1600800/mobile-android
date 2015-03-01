package com.compoment.file_manage;

import java.util.ArrayList;
import java.util.List;

import com.android_demonstrate_abstractcode.R;
import com.compoment.loading_progressdialog.LoadingProgressBar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;

/**
 * @ClassName: ActivityWidgetDoc
 * @Description: ��widget�����ĵ��б����
 * @author lipei
 * @date 2012-8-23 ����03:05:23
 */
public class CloudDocApkOtherActivity extends Activity {
	private LayoutParams params;
	private ImageView image;
	private int progressLength;
	private int eachAddLength;

	private String sdcardPath = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();

	private static List<ApkFileInfo> listContent = null;

	private ListView listView; // �ļ��б�
	private CloudDocApkOtherListAdapter adapter;// �ļ��б�������
	private final String documentPostfix = ".rtf.mht.chm.doc.xls.ppt.txt.pdf.docx.xlsx.pptx.wps";
	private final String applicationPostfix = ".apk";
	private final String mediaPostfix = ".jpeg.aac.mid.swf.flv.bmp.gif.jpg.psd.png.mp3.mp4.wav.rm.ogg.avi.3gp.wmv.mpeg.dat.wma.rmvb.imag.asf.mov.amr.psd.m4u.m4v.mpe.mpg.mpg4.m3u.m4a.m4b.mp2.mpga";
	private final String otherPostfix = ".mht.xlsx.pptx.psd.ogg.dat.imag.asf.mov.swf.flv.chm.aac.pdg.mid.jpeg.rtf.amr.psd.m4u.m4v.mpe.mpg.mpg4.m3u.m4a.m4b.mp2.mpga";

	public static List<ApkFileInfo> listFile = null;
	public static List<ApkFileInfo> listFolder = null;
	public static boolean scandSwitcher = true;// �㷵��ʱ
												// �������������е��߳�break����
	Button buttonSortList;
	CloudDocApkOtherSortAdapter adapterSortFile = null;
	String type = null;
	boolean firstTime = true;

	LoadingProgressBar loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cloud_apk_main);

		listContent = new ArrayList();
		listFile = new ArrayList<ApkFileInfo>();
		listFolder = new ArrayList<ApkFileInfo>();

//		Bundle bundle = new Bundle();
//		bundle = this.getIntent().getExtras();
//		type = bundle.getString("type");
		//TextView title = (TextView) this.findViewById(R.id.title_layout);
		
		type="apk";
//		if (type.equals("apk")) {
//			title.setText("安装包");
//		} else if (type.equals("other")) {
//			title.setText("其它");
//		} else if (type.equals("doc")) {
//			title.setText("文档");
//		}
	
		
		listView = (ListView) this.findViewById(R.id.listview);
		buttonSortList = (Button) findViewById(R.id.sort_btn);
		buttonSortList.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//排序
				showSortByWhatDialog(buttonSortList);

			}
		});
		Button buttonback = (Button) findViewById(R.id.back_btn);
		buttonback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CloudDocApkOtherActivity.this.finish();

			}
		});

		adapter = new CloudDocApkOtherListAdapter(this, listContent, handler);
		adapterSortFile = new CloudDocApkOtherSortAdapter(this);
		listView.setAdapter(adapter);

		// listView.setOnItemClickListener(listClick);
		// listView.setOnTouchListener(this);
		// listView.setEmptyView(emptyView);// setEmptyView
		// listView.setOnItemLongClickListener(listLongClick);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (firstTime && loading == null) {
			firstTime = false;
			if (loading == null) {
				loading = new LoadingProgressBar();
			}
			scand();
		}
	}

	Handler progressDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
			//	loading.cancleProgressDialog();
				break;
			default:
				break;
			}
		}
	};

	public void scand() {
		boolean isSDCardExist = android.os.Environment
				.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED);
		if (!isSDCardExist) {
			Toast.makeText(this, "SD卡不存在,程序退出！", Toast.LENGTH_SHORT).show();
			this.finish();
		} else {

			//loading.showProgressDailg("提示", "正在扫描，请稍等......", this);

			listFile.clear();

			ApkFileInfo path = new ApkFileInfo();
			path.folderName = "���������";
			path.folderPath = "/";
			listFolder.add(path);
			ApkFileInfo path2 = new ApkFileInfo();
			path2.folderName = "UC����";
			path2.folderPath = "/UCDownloads";
			listFolder.add(path2);

			for (int i = 0; i < listFolder.size(); i++) {
				// link the sdpath and the folderName
				listFolder.get(i).folderPath = listFolder.get(i).folderPath;
			}

			new Thread() {

				public void run() {
					Looper.prepare();

					// loading the SDCARD file avoid user add the sdcard path to
					UtilFileClassify.scanSingleFolder(sdcardPath, listFile,
							CloudDocApkOtherActivity.this);
					// UtilFileClassify.loadFileOnly(sdcardPath,
					// listFile,ActivityWidgetApk.this);
					UtilFileClassify.sortListByTime(listFile, false); // isAscend
																		// ==
																		// false
					handler.sendEmptyMessage(0);
					progressDialogHandler.sendEmptyMessage(0);
				}
			}.start();

			scandSwitcher = true;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				listContent.clear();

				if (type.equals("apk")) {

					listContent = UtilFileClassify.classifyFile(listFile,
							applicationPostfix);

				} else if (type.equals("other")) {
					listContent = UtilFileClassify.classifyFile(listFile,
							otherPostfix);
				} else if (type.equals("doc")) {
					listContent = UtilFileClassify.classifyFile(listFile,
							documentPostfix);

				}
				adapter = new CloudDocApkOtherListAdapter(
						CloudDocApkOtherActivity.this, listContent, handler);
				listView.setAdapter(adapter);
				break;
			case 1:
				break;
			}
		}
	};

	Handler sortHandler = new Handler() {
		public void handleMessage(Message msg) {
			final int whichItemPressed = msg.what;

			if (whichItemPressed == adapterSortFile.sortPosition) {// if press
				// the same
				// item
				adapterSortFile.isAscend[whichItemPressed] = !adapterSortFile.isAscend[whichItemPressed];
			}
			if (msg.what != 3) {
				new Thread() {
					public void run() {
						{
							if (whichItemPressed == 0) {// sort list by last
														// modified time
								// sortByWhat = "time";
								UtilFileClassify.sortListByTime(listContent,
										adapterSortFile.isAscend[0]);

							} else if (whichItemPressed == 1) {// sort list by
																// file size
								// sortByWhat = "size";
								UtilFileClassify.sortListBySize(listContent,
										adapterSortFile.isAscend[1]);
							} else {// sort list by file name
								// sortByWhat = "name";
								UtilFileClassify.sortListByName(listContent,
										adapterSortFile.isAscend[2]);
							}

							adapterSortFile.sortPosition = whichItemPressed;
							sortHandler.sendEmptyMessage(3);
						}
					}
				}.start();

			}

			else {
				{

					adapter = new CloudDocApkOtherListAdapter(
							CloudDocApkOtherActivity.this, listContent, handler);
					listView.setAdapter(adapter);

					listView.setSelection(0); // move to first

				}
			}

		}
	};

	protected void showSortByWhatDialog(View view) {

		// ListView lv = new ListView(this);
		// RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// params.setMargins(30, 10, 30, 10);
		// lv.setLayoutParams(params);
		// lv.setCacheColorHint(0);

		LayoutInflater inflater = LayoutInflater.from(this);
		View sortlist = inflater.inflate(
				R.layout.cloud_doc_apk_other_sort_list, null);

		ListView lv = (ListView) sortlist.findViewById(R.id.sort_list);

		lv.setAdapter(adapterSortFile);

		final PopupWindow menuPopUp = new PopupWindow(sortlist, 160,
				LayoutParams.WRAP_CONTENT, true);//

		// menuPopUp.setFocusable(true);

		// menuPopUp.setOutsideTouchable(false);
		//
		// menuPopUp.setBackgroundDrawable(new BitmapDrawable());
		//
		menuPopUp.setBackgroundDrawable(new ColorDrawable(
				R.color.prompt_menupopupwindow_bg));

		// lv.setFocusableInTouchMode(true);

		// lv.setOnKeyListener(new View.OnKeyListener() {
		//
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		//
		// if(keyCode == KeyEvent.KEYCODE_MENU && menuPopUp.isShowing()){
		// menuPopUp.dismiss();
		// return true;
		// }
		// return false;
		// }
		// });

		adapterSortFile.setHandler(sortHandler, "sort", menuPopUp);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				sortHandler.sendEmptyMessage(position);
				menuPopUp.dismiss();
			}
		});

		// menuPopUp.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, 0, 50);
		menuPopUp.showAsDropDown(view);

	}

	public void onPause() {
		scandSwitcher = false;
		this.finish();
		super.onPause();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			scandSwitcher = false;
			this.finish();
			return true;
		}

		return true;
	}

}
