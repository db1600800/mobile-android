package com.compoment.contact;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import com.android_demonstrate_abstractcode.R;
import com.compoment.loading_popupwindow.LoadingPopupWindow;
import com.compoment.prompt.MenuPopupWindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ContactActivity extends Activity  {
	ContactView contactView;
	MenuPopupWindow promptPopupWindow;
	MenuPopupWindow promptPopupWindow2;

	Context context;
	Button movtoBtn;
	String currentGroupName;
	public static List viceGroupIds;
	TextView txtOverlay;
	public static EditText et_contact;

	public static int ONLY_ONE_SELECT = 10;
	public static int MUTL_SELECT = -99;
	public static int ADD_LISTENER_IN_OLD_GROUP = 20;
	int selectType = -99;

	public static List selectContact;

	// startActivityForResult
	public static int RESULT_CODE = 100;
	String oldGroupIds;

	private LoadingPopupWindow progress;

	String groupname;
	String groupid;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		String title="";
		if(bundle!=null)
		{
		selectType = bundle.getInt("SelectType");
		
		 title = bundle.getString("Title");
		oldGroupIds = bundle.getString("OldGroupIds");
		}
		
		txtOverlay = (TextView) LayoutInflater.from(context).inflate(
				R.layout.contact_quick_index_popup_char_textview, null);
		txtOverlay.setVisibility(View.INVISIBLE);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,

				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,

				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		try {
			windowManager.addView(txtOverlay, lp);
		} catch (Exception e) {

		}
		setContentView(R.layout.contact_activity);

		currentGroupName = "main";
		TextView titleTextView = (TextView) findViewById(R.id.title);
		if (title == null || "".equals(title))
			title = "请选择收听者";

		titleTextView.setText(title);
		FrameLayout listContainer = (FrameLayout) findViewById(R.id.operator_middle);
		contactView = new ContactView(this,
				ContactView.OTHER_MODEL_GROUP_CONTACT, currentGroupName,
				handler, null, txtOverlay, selectType);

		listContainer.addView(contactView);

		// 取消
		Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				ContactActivity.this.finish();
			}
		});

		// 确定
		movtoBtn = (Button) findViewById(R.id.selBtn);
		movtoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (contactView == null)
					return;
				if (contactView.adapter == null)
					return;
				if (contactView.adapter.selection == null)
					return;
				// (name+","+number+";")
				selectContact = contactView.adapter.selection;

				if (selectContact == null || selectContact.size() <= 0)
					return;

				if (selectType == ContactActivity.ONLY_ONE_SELECT) {

					Intent data = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("Select", (String) selectContact.get(0));
					data.putExtras(bundle);
					ContactActivity.this.setResult(
							ContactActivity.this.RESULT_CODE, data);
					if (null != et_contact) {
						//
						String linkman = selectContact.get(0).toString().trim();
						int index = linkman.indexOf(",");
						String number = linkman.substring(index + 1,
								linkman.length() - 1);
						et_contact.setText(number);
						//
						et_contact.setSelection(number.length());
					}
					ContactActivity.this.finish();

				} else if (selectType == ContactActivity.ADD_LISTENER_IN_OLD_GROUP) {
					String nameS = "";
					String numberS = "";

					for (int i = 0; i < selectContact.size(); i++) {
						String temp = (String) selectContact.get(i);
						int nameP = temp.indexOf(",");
						int numberP = temp.indexOf(";");
						String name = temp.substring(0, nameP);
						String number = temp.substring(nameP + 1, numberP);
						if (nameS.equals("")) {
							nameS += name;
							numberS += number;
						} else {
							nameS += "|" + name;
							numberS += "|" + number;
						}
					}

					Intent data = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("names", nameS);
					bundle.putString("numbers", numberS);
					data.putExtras(bundle);

					ContactActivity.this.setResult(
							ContactActivity.this.RESULT_CODE, data);
					ContactActivity.this.finish();

				} else {
					String nameS = "";
					String numberS = "";

					for (int i = 0; i < selectContact.size(); i++) {
						String temp = (String) selectContact.get(i);
						int nameP = temp.indexOf(",");
						int numberP = temp.indexOf(";");
						String name = temp.substring(0, nameP);
						String number = temp.substring(nameP + 1, numberP);
						if (i == 0) {
							nameS = name;
							numberS = number;
						} else {
							nameS += "," + name;
							numberS += "," + number;
						}

					}
					final Intent data = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("names", nameS);
					bundle.putString("numbers", numberS);
					bundle.putSerializable("selectContact",
							(Serializable) selectContact);
					data.putExtras(bundle);

					if (selectContact.size() > 10) {
						promptPopupWindow = new MenuPopupWindow(context,
								"收听者不能超过10人", new View.OnClickListener() {
									@Override
									public void onClick(final View v) {
										promptPopupWindow.hide();

									}
								});
						promptPopupWindow.show(v);
					} else {
						
						if (selectContact.size() == 1) {

							
							return;
						}
						promptPopupWindow = new MenuPopupWindow(context,
								"新建群组", nameS, new View.OnClickListener() {
									@Override
									public void onClick(final View v) {
										promptPopupWindow.hide();

										if (((Button) v).getText().toString()
												.equals("确定")) {

										

										}
									}
								});
						promptPopupWindow.show(v);
					}

				}
			}
		});

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				List selects = (List) msg.obj;
				if (selects.size() > 0) {
					movtoBtn.setText("选择（" + selects.size() + "）");
				} else {
					movtoBtn.setText("选择");
				}
				break;

			}

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {

		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);

		windowManager.removeView(txtOverlay);

		super.onDestroy();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) { // resultCode
		case 10://
			contactView.refreshListView();
			ContactProvider contactProvider = ContactProvider
					.getInstance(this);
			contactProvider.msgModelSaveContact = true;
			break;
		case 12:// ActivityContactAddImport
			contactView.refreshListView();
			ContactProvider contactProvider2 = ContactProvider
					.getInstance(this);
			contactProvider2.msgModelImportContact = true;
			break;

		default:
			break;
		}
	}

	
}
