package com.compoment.contact;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.android_demonstrate_abstractcode.R;
import com.compoment.contact.A_ZQuickIndexBar.OnQuickIndexSelectedListener;
import com.compoment.loading_popupwindow.LoadingPopupWindow;
import com.compoment.prompt.MenuPopupWindow;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog.Calls;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.QuickContact;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class ContactView extends FrameLayout implements OnScrollListener,
		OnQuickIndexSelectedListener {

	public ContactViewAdapter adapter;
	private WindowManager windowManager;
	public TextView txtOverlay;
	public Handler handler;
	private DisapearThread disapearThread;
	private int scrollState;
	private ListView list;
	A_ZQuickIndexBar a_zQuickIndexBar;
	private final static int SHOW_LOCKER = 0;
	public final static int DELETE_REFRESH = 1;
	private Timer lockTimer;
	Context context;
	String currentChar="";
	EditText editSearch;
	ImageView btnClearSearch;
	ViewGroup contact_addperson;
	View contact_line;
	//public List<PersonInfo> contactList = null;
	Cursor cur;

	MenuPopupWindow deletePromptPopupWindow;
	MenuPopupWindow movePromptPopupWindow;
	String condition;

	List sortKeyList = null;
	
	LoadingPopupWindow progress=null;
    
	int selectType;
	//	public  static String  ONLY_ONE_SELECT="ONLY_ONE_SELECT";
	//public  static String  MUTL_SELECT="MUTL_SELECT";
	

	public static int ONLY_ON_SELECT = 10;
	public static int MAIN_VIEW = 3;
	public static int VOIC1_VIEW = 3;
	public static int VOIC2_VIEW = 3;
	public static int VOIC3_VIEW = 3;
	public static int SET_SONG_TO = 9;
	// ltchen
	private MenuPopupWindow longClickMenu; // ����ͨ����¼�����˵�
	private String longClickId; // ����ͨ����¼�е�ID
	private String longClickName; // ����ͨ����¼�е����
	Handler activityHandler;

	int useType = OTHER_MODEL_GROUP_CONTACT;
	/*
	 * useType 
	 */
	
	public static int OPERATOR_CONTACT=11;
	public static int IMPORT_CONTACT=12;
	public static int CALL_MODEL_GROUP_CONTACT=13;
	public static int OTHER_MODEL_GROUP_CONTACT=15;
	public static int SEE_CONTACT=16;
	public String whichViceGroup;
	View viewWhenCursorEmpty;
	int sdkversion = 0;
	List arg;
	
	AsyncTask searchTask;

	public ContactView(final Context context, final int useType,
			final String whichViceGroup, final Handler activityHandler,
			List choosedContactsList, final TextView txtOverlay,int selectType) {
		super(context);
		this.activityHandler = activityHandler;
		this.context = context;
		this.useType = useType;
		this.whichViceGroup = whichViceGroup;
		this.arg = choosedContactsList;
		this.selectType=selectType;
		sdkversion = Integer.valueOf(android.os.Build.VERSION.SDK);
		View view = LayoutInflater.from(context).inflate(
				R.layout.contact_view, null);

	
		viewWhenCursorEmpty = LayoutInflater.from(context).inflate(
				R.layout.contact_view_empty, null);

		
		// A_Z ��ĸ��ʾ
		this.txtOverlay = txtOverlay;

		// A_Z����������
		a_zQuickIndexBar = (A_ZQuickIndexBar) view
				.findViewById(R.id.a_zQuickindexbar);
		a_zQuickIndexBar.setOnQuickIndexSelectedListener(this);

		// �б�
		list = (ListView) view.findViewById(R.id.listview);
		list.setOnScrollListener(ContactView.this);
		list.setDivider(getResources().getDrawable(R.drawable.contact_view_list_divider_line));
		list.setDividerHeight(1);
		list.setCacheColorHint(0);
		// �б�ͷ ��������
		View listhead = (View) LayoutInflater.from(context).inflate(
				R.layout.contact_view_list_head, null);
		contact_addperson = (ViewGroup) listhead
				.findViewById(R.id.contact_addperson);

		// ��
		contact_line = (View) listhead.findViewById(R.id.contact_line);
		if ( useType == ContactView.OTHER_MODEL_GROUP_CONTACT
				) {
			contact_addperson.setVisibility(View.GONE);
			contact_line.setVisibility(View.GONE);
		}
		btnClearSearch = (ImageView) listhead
				.findViewById(R.id.btn_clean_search);
		btnClearSearch.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				editSearch.setText("");
				btnClearSearch.setVisibility(View.GONE);
			}
		});
		
		// 列表项长按
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterview,
					View view, int i, long l) {
				// TODO Auto-generated method stub
//				Cursor mcursor = (Cursor) list.getItemAtPosition(i);
//				if (mcursor == null || mcursor.getCount() <= 0) {
//					return true;
//				}
//				longClickName = mcursor.getString(mcursor
//						.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
//				if (condition != null && condition.length() > 0) {
//					longClickId = mcursor.getString(mcursor
//							.getColumnIndex(Phone.CONTACT_ID));
//				} else {
//					longClickId = mcursor.getString(mcursor
//							.getColumnIndex(ContactsContract.Data._ID));
//				}
//				longClickMenu = new MenuPopupWindow(context, longClickName,
//						getResources().getStringArray(
//								R.array.list_longclick_contact), menuItemClick);
//				longClickMenu.show(ContactView.this);
				return false;
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i,
					long arg3) {
				// TODO Auto-generated method stub
				Cursor mcursor = (Cursor) list.getItemAtPosition(i);
				longClickName = mcursor.getString(mcursor
						.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				if (condition != null && condition.length() > 0) {
					longClickId = mcursor.getString(mcursor
							.getColumnIndex(Phone.CONTACT_ID));
				} else {
					longClickId = mcursor.getString(mcursor
							.getColumnIndex(ContactsContract.Data._ID));
				}

			}

		});

		editSearch = (EditText) listhead.findViewById(R.id.edit_search);
		editSearch.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			 if (s.length() != 0) {
					// ��ͨ��ϵ���б�
					contact_line.setVisibility(View.GONE);// ��
					a_zQuickIndexBar.setVisibility(View.GONE);
					contact_addperson.setVisibility(View.GONE);// �����ϵ��
					btnClearSearch.setVisibility(View.VISIBLE);

				} else if ((useType == ContactView.OTHER_MODEL_GROUP_CONTACT )
						&& s.length() == 0) {// ��������״̬ ��������
					btnClearSearch.setVisibility(View.GONE);
					contact_line.setVisibility(View.GONE);
					a_zQuickIndexBar.setVisibility(View.VISIBLE);
					contact_addperson .setVisibility(View.GONE);

				} 
				else 
				{
					// ����״̬
					btnClearSearch.setVisibility(View.GONE);
					contact_line.setVisibility(View.VISIBLE);
					a_zQuickIndexBar.setVisibility(View.VISIBLE);
					contact_addperson.setVisibility(View.VISIBLE);
				}

				condition = s.toString();
				if (s.length() == 0 ) {
					new ThreadsTask().execute();
				} 
				
				else
				{
					if(searchTask==null)
					{
					searchTask=new SearchThreadsTask(condition);
					searchTask.execute("");
					}else if(searchTask!=null )
					{
						searchTask.cancel(true);
						searchTask=new SearchThreadsTask(condition);
						searchTask.execute("");
					}
				
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});

		list.addHeaderView(listhead);

		// ��ѯ��ϵ�� ���adapter
	
		new ThreadsTask().execute();

	

		handler = new Handler() {
			public void handleMessage(Message msg) {

				switch (msg.what) {

				case SHOW_LOCKER:

					if (txtOverlay.isShown()) {

						txtOverlay.setVisibility(View.INVISIBLE);

					}

					break;
				case DELETE_REFRESH:
					refreshListView();
					break;
				default:
					super.handleMessage(msg);

				}

			}
		};
		disapearThread = new DisapearThread();

		addView(view);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (visibleItemCount != 0 && totalItemCount > 1) {
			
			String sort_key = null;
            if(currentChar.equals(""))
            {
			
			Cursor rowCursor = (Cursor) view.getAdapter().getItem(
					firstVisibleItem + 1);

			if(rowCursor==null)
			{
				sort_key="#";
			}else
			{

			sort_key = rowCursor
					.getString(rowCursor.getColumnIndex("sort_key"));
			
			}
            }else
            {
            	sort_key=currentChar;
            	currentChar="";
            }
			if (null != sort_key && sort_key.length() > 0) {// ltchen ��ʱ��Ϊ�ձ���
				txtOverlay.setText(String.valueOf(sort_key.charAt(0))
						.toUpperCase());
			
				{
					a_zQuickIndexBar.setFocusItem(txtOverlay.getText()
							.toString());
				}
			}

		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
		if (scrollState == ListView.OnScrollListener.SCROLL_STATE_IDLE) {
			handler.removeCallbacks(disapearThread);
			handler.postDelayed(disapearThread, 1500);
		} else {
			txtOverlay.setVisibility(View.VISIBLE);
			if (lockTimer != null) {
				lockTimer.cancel();
				lockTimer.purge();
				lockTimer = null;

			}
			lockTimer = new Timer();
			lockTimer.schedule(new LockTimerTask(), 1500);
		}
	}

	public void onDestroy() {

		txtOverlay.setVisibility(View.INVISIBLE);
		windowManager.removeView(txtOverlay);
	}

	// ��ʱ�� ���� a_z��ʾ ����ʧ
	private class LockTimerTask extends TimerTask {

		@Override
		public void run() {

			handler.sendMessage(handler.obtainMessage(SHOW_LOCKER));

		}

	}

	private class DisapearThread implements Runnable {
		public void run() {
			if (scrollState == ListView.OnScrollListener.SCROLL_STATE_IDLE) {
				txtOverlay.setVisibility(View.INVISIBLE);
			}
		}
	}

	// a_z�����������Ļص� ���ߵ�ǰ��������ĸ���ĸ
	@Override
	public void onQuickIndexSelected(String s) {
		// TODO Auto-generated method stub
		if (cur == null || cur.getCount() == 0) {
			return;
		}

		if (String.valueOf(s).equals("#")) {
			list.setSelection(0);
			currentChar="#";
			return;
		}

		char c = s.charAt(0);
		char nextchar = ++c;
		char c1 = s.charAt(0);
		char beforchar = --c1;
		String beforString = null;
		String nextString = null;
		if (nextchar != 'Z' + 1) {
			nextString = String.valueOf(nextchar);
		} else {
			nextString = s;
		}

		if (beforchar != 'A' - 1) {
			beforString = String.valueOf(beforchar);
		} else {
			beforString = s;
		}
		if(sortKeyList==null)
			return ;
		int p = sortKeyList.indexOf(s);
		int beforP = sortKeyList.indexOf(beforString);
		int nextP = sortKeyList.indexOf(nextString);

		int localPosition = p;// binSearch(String.valueOf(s));

		if (localPosition != -1) {

			txtOverlay.setText(s);
			txtOverlay.setVisibility(View.VISIBLE);
			scrollState = ListView.OnScrollListener.SCROLL_STATE_IDLE;

			handler.removeCallbacks(disapearThread);

			handler.postDelayed(disapearThread, 1500);

			list.setSelection(localPosition + 1);
			currentChar=s;
		} else {
			txtOverlay.setText(s);
			txtOverlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(disapearThread);

			handler.postDelayed(disapearThread, 1500);

			if (beforP != -1) {
				scrollState = ListView.OnScrollListener.SCROLL_STATE_IDLE;
				list.setSelection(beforP + 1);
				currentChar=beforString;
			} else if (nextP != -1) {
				scrollState = ListView.OnScrollListener.SCROLL_STATE_IDLE;
				list.setSelection(nextP + 1);
				currentChar=nextString;
			}

		}

	}

	/***
	 * ��ϵ�˳���ѡ�����¼�
	 * 
	 * @author ltchen
	 */
	OnItemClickListener menuItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position,
				long arg3) {
			if (null != longClickMenu) {
				longClickMenu.hide();
			}
			switch (position) {
			case 0: // �鿴��ϵ��
//				((com.gmcc.numberportable.ActivityMain) context)
//						.doContactActivity(longClickId, whichViceGroup, true);
				break;
			case 1: // ������ϵ��

//				CommunicationBarListener communicationBarListenerCall = new CommunicationBarListener(
//						context, true, Integer.valueOf(longClickId), "");
//				communicationBarListenerCall.callForContact(ContactView.this);

				break;
			case 2: // ���Ͷ���
//				CommunicationBarListener communicationBarListenerSend = new CommunicationBarListener(
//						context, true, Integer.valueOf(longClickId), "");
//				communicationBarListenerSend.gotoSendMessage(ContactView.this);

				break;
			case 3: // �ƶ���ϵ��
//
//				ArrayList<ViceNumberInfo> vices = (ArrayList<ViceNumberInfo>) NumberNameProvider
//						.Query((Activity) context);
//				// ��ǰgroupȥ��
//				for (int i = 0; i < vices.size(); i++) {
//					if (((ViceNumberInfo) vices.get(i)).CallingID.equals("0")) {
//						((ViceNumberInfo) vices.get(i)).NickName = "����";
//						if ("main".equals(whichViceGroup)) {
//							vices.remove(i);
//						}
//					} else {
//
//						if (((ViceNumberInfo) vices.get(i)).NickName
//								.equals(whichViceGroup)) {
//							vices.remove(i);
//						}
//					}
//				}
//				
//				movePromptPopupWindow = new MenuPopupWindow(context, "��ʾ",
//						"select", vices, new OnClickListener() {
//							@Override
//							public void onClick(View view) {
//								// ���ȷ�ϰ�ť
//								if (((Button) view).getText().toString()
//										.equals("ȷ��")) {
//									// Ŀ�����
//									String toGroupName = movePromptPopupWindow.whichSelect;
//									if (toGroupName == null
//											|| toGroupName.equals("")) {
//										return;
//									}
//
//									if (toGroupName.equals("����")) {
//										toGroupName = "main";
//									}
//
//									int contactId = Integer
//											.valueOf(longClickId);
//
//									ContactGroupProvider.addGroupContact(
//											contactId, whichViceGroup,
//											toGroupName);
//
//									// adapter.unSelectAll();
//									((ActivityMain) context).contactView
//											.refresh();
//									//refreshListView();
//								}
//
//							
//								movePromptPopupWindow.hide();
//							}
//						});
//				movePromptPopupWindow.show(v);
			

				break;
			case 4: // �༭��ϵ��
//				((com.gmcc.numberportable.ActivityMain) context)
//						.doContactActivity(longClickId, whichViceGroup, false);
				break;
			case 5: // ɾ����ϵ��

//				deletePromptPopupWindow = new MenuPopupWindow(context, "��ʾ",
//						"����ɾ��  " + longClickName + "  ��ϵ��",
//						new View.OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//
//								// ���ȷ�ϰ�ť
//								if (((Button) v).getText().toString()
//										.equals("ȷ��")) {
//									ContactGroupProvider.removeContact(Integer
//											.valueOf(longClickId));
//									
//								}
//								if(condition!=null)
//								{
//									editSearch.setText("");
//									btnClearSearch.setVisibility(View.GONE);
//								}else
//								{
//									refreshListView();
//								}								
//								deletePromptPopupWindow.hide();
//							}
//						});
//				deletePromptPopupWindow.show(v);

				break;
			default:
				break;
			}
		}
	};

	public void refreshListView() {
		if(progress==null)
		{
		progress = new LoadingPopupWindow(context);
		progress.show(list);
		}else
		{
			progress.show(list);
		}
	
		
		sortKeyList = null;
	
		btnClearSearch.setVisibility(View.GONE);
		
		ContactProvider util = ContactProvider.getInstance(context);
		util.reFreshTotalCursor();
		
		new ThreadsTask().execute();
	
	}

	private class ThreadsTask extends AsyncTask<Void, Void, Cursor> {

		boolean firstTime = true;
		Cursor cursor = null;

		/** loading **/
	
		public ThreadsTask() {

		}

		@Override
		protected Cursor doInBackground(Void... params) {
			ContactProvider util = ContactProvider.getInstance(context);
		
			/*用于欢迎页面预加载
			if (util.firstTimeContactsTableMatrixCursor) {
				while (util.firstTimeContactsTableMatrixCursor == true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}*/

		 if (useType == OTHER_MODEL_GROUP_CONTACT) {
				Map contain=util.getGroupContact(whichViceGroup,
						OTHER_MODEL_GROUP_CONTACT);
				if(contain!=null)
				{
				cursor = (Cursor)contain.get("cursor");
				sortKeyList=(List) contain.get("headCharList");
				}else
				{
					cursor = null;
					sortKeyList=null;
				}
			
			} else {
				Map contain=util.getGroupContact(whichViceGroup, -999);
				if(contain!=null)
				{
				cursor = (Cursor)contain.get("cursor");
				sortKeyList=(List) contain.get("headCharList");
			
				}else
				{
					cursor = null;
					sortKeyList=null;
				}
				}
		

			return cursor;
		}

		@Override
		protected void onPostExecute(Cursor curs) {
			cur = curs;

			if (adapter == null) {
				adapter = new ContactViewAdapter(context, curs, activityHandler, arg,selectType);
				adapter.setUseType(useType);
				list.setAdapter(adapter);
			}

			if (curs != null && curs.getCount() > 0) {
				ContactView.this.removeView(viewWhenCursorEmpty);
				adapter.setCondition(null);
				adapter.setUseType(useType);
				adapter.setCursor(curs);
				adapter.changeCursor(curs);
			} else if (curs == null) {
				ContactView.this.removeView(viewWhenCursorEmpty);
				addView(viewWhenCursorEmpty);
			}

			firstTime = false;
			if(progress!=null)
			{
				progress.hidden();
				progress=null;
			}
		}
	}

	private class SearchThreadsTask extends AsyncTask<Void, Void, Cursor> {

		String condition;

		public SearchThreadsTask(String condition) {
			this.condition = condition;
		}

		@Override
		protected Cursor doInBackground(Void... params) {
			ContactProvider util = ContactProvider.getInstance(context);
			Cursor cursor = null;
		  if (useType == ContactView.OTHER_MODEL_GROUP_CONTACT) {
				cursor = util.getGroupContactForSearchHaveNumber(
						whichViceGroup, condition, cur);
			}  else if (useType == SEE_CONTACT) {
				cursor = util.getGroupContactForSearchHaveNumber(
						whichViceGroup, condition, cur);
			
			} 
			return cursor;
		}

		@Override
		protected void onPostExecute(Cursor cursor) {
		
			if (cursor == null) {
				return;
			}

			adapter.setCondition(condition.toString());
			adapter.setUseType(useType);
			adapter.setCursor(cursor);
			adapter.changeCursor(cursor);

		}
	}

}
