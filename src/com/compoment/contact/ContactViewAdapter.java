package com.compoment.contact;

//http://blog.csdn.net/you_and_me12/article/details/6424652

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android_demonstrate_abstractcode.R;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class ContactViewAdapter extends CursorAdapter {
	// public List<PersonInfo> dataSource;
	private LayoutInflater inflater;
	Context context;
	private String condition;
	ContactUtil queryContact;
	// ��¼checkbox��״̬
	HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();
	public ArrayList<String> selection = null;
	Cursor mCursor;
	int useType;
	int selectType;
	public boolean allSelect = false;
	Handler handler;
	
	List arg;
	int threadUnChoosedColor = 0;
	int threadChoosedColor = 0;
    int sdkversion=0;
	@SuppressWarnings("unchecked")
	public ContactViewAdapter(Context context, Cursor c, Handler handler, List arg,int selectType) {
		super(context, c);
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.mCursor = c;
		this.handler = handler;
		this.arg = arg;
		this.selectType=selectType;
		if (arg == null) {
			selection = new ArrayList<String>();
		} else {
			selection = (ArrayList<String>) arg;
		}
		queryContact = new ContactUtil(context);
//		threadUnChoosedColor = context.getResources().getColor(
//				R.color.msg_batch_deleted_unchoosed);
//		threadChoosedColor = context.getResources().getColor(
//				R.color.msg_batch_deleted_choosed);
		sdkversion=Integer.valueOf(android.os.Build.VERSION.SDK) ;
	}

	public void setCondition(String condition) {
		if (condition == null) {
			this.condition = null;
		} else {
			this.condition = condition.toLowerCase();
		}
	}

	public void setUseType(int userTypes) {
		// useType==ContactView.OPERATOR_CONTACT;��������
		// useType==ContactView.IMPORT_CONTACT;�ƶ���ϵ�˵��·���
		// useType==ContactView.SEE_CONTACT;��ͨ
		this.useType = userTypes;
	}

	public void setCursor(Cursor cursor) {
		mCursor = cursor;
	}

	public final class ViewHolder {
		public TextView text_first_char_hint;
		public TextView content;
		public ImageView imgView;
		public TextView numberView;
		public CheckBox checkBox;
		public TextView groupNameView;
		int position;
		boolean checkFlag = false;
	}

	@Override
	public void bindView(View view, final Context context, final Cursor cur) {
		// TODO Auto-generated method stub

		ViewHolder holder = (ViewHolder) view.getTag();
		String sort_key = null;
	
			 sort_key = cur.getString(cur.getColumnIndex("sort_key"));
		
	
		String id = "";

		if (useType == ContactView.SEE_CONTACT && condition != null) {
			id = cur.getString(cur
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
		} else if (useType == ContactView.OTHER_MODEL_GROUP_CONTACT) {
			id = cur.getString(cur
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
		} else {
			if(sdkversion<=20)
			{
				id = cur.getString(cur
						.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			}else
			{
			id = cur.getString(cur
					.getColumnIndex(ContactsContract.Contacts._ID));
			}
		}

		String groupName = "";
		if (useType == ContactView.IMPORT_CONTACT) {
			groupName = ContactProvider.getContactInWhichGroup(Long
					.valueOf(id));
			if (groupName.equals("main")) {
				groupName = "����";
			}
		}

		String name = cur.getString(cur
				.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		String photo_id = cur.getString(cur
				.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));

		String number = "";

		if ((useType == ContactView.SEE_CONTACT && condition != null)) {
			List numbers = ContactProvider.getPhoneNumber(id);
			if (numbers.size() > 0) {
				number = (String) numbers.get(0);
			}
		} else if (useType == ContactView.OTHER_MODEL_GROUP_CONTACT) {
			number = cur
					.getString(cur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		}

		if (useType == ContactView.OTHER_MODEL_GROUP_CONTACT) {
			holder.checkBox.setTag(name + "," + number + ";");
		} else {
			holder.checkBox.setTag(id);
		}

		if (condition != null ) {
			// ����״̬

			// ��������
			String newName = "";
			SpannableStringBuilder style_name = new SpannableStringBuilder(name);

			// sort_key�������ĺ�Ӣ�� �û����룺������ĸ
			if (queryContact.isPinYin(condition)
					&& queryContact.containCn(sort_key)) {

				int conditionLength = condition.length();

				// ������Ӣ�Ĳ���
				for (int j = 0; j < conditionLength; j++) {

					String subCondition = condition.substring(j, j + 1)
							.toLowerCase();
					if (name.toLowerCase().contains(subCondition)) {
						style_name.setSpan(new ForegroundColorSpan(Color.RED),
								name.toLowerCase().indexOf(subCondition),
								name.toLowerCase().indexOf(subCondition) + 1,
								Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // ����ָ��λ�����ֵ���ɫ
					}
				}

				// ���������Ĳ���
				String tempwords[] = sort_key.split(" ");

				String tempsortkey = sort_key;

				tempsortkey = tempsortkey.replace("   ", "");

				String words[] = tempsortkey.split(" ");
				for (int i = 0; i < words.length; i++) {

					newName += words[i];

					if (queryContact.containCn(words[i])
							&& conditionLength == 1) {
						// sort_key�����Կ� ce �� SHI �� ka ���� �û�����(����ĸ)��c
						
						if(i-1<0 || (i-1>=0 && newName.indexOf(words[i - 1])==-1))
						{
							
						}
						else
						{
						
						newName = newName.substring(0,
								newName.indexOf(words[i - 1]));
						}
						int start = newName.length();
						int end;

						String one = condition.toLowerCase();
						if (i-1>=0 && words[i-1]!=null && words[i-1].length()>0 && words[i - 1].substring(0, 1).toLowerCase()
								.contains(one.toLowerCase()))  {
							newName += words[i];
							end = newName.length();
							style_name.setSpan(new ForegroundColorSpan(
									Color.RED), start, end,
									Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // ����ָ��λ�����ֵ���ɫ
						} else {
							newName += words[i];
						}

					} else if (queryContact.containCn(words[i])
							&& conditionLength > 1
							&& !queryContact.binSearch(condition.toLowerCase())) {
						// sort_key�����Կ� ce �� SHI �� ka �� ���û�����(�����ĸ,��������ĸ)��csk
						newName = newName.substring(0,
								newName.indexOf(words[i - 1]));
						int start = newName.length();
						int end;

						boolean append = false;
						for (int j = 0; j < conditionLength; j++) {

							String one = condition.toLowerCase().substring(j,
									j + 1);
							if (words[i - 1].substring(0, 1).toLowerCase()
									.contains(one.toLowerCase())) {

								newName += words[i];
								end = newName.length();
								style_name.setSpan(new ForegroundColorSpan(
										Color.RED), start, end,
										Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // ����ָ��λ�����ֵ���ɫ
								append = true;
								break;
							}

						}

						if (!append)
							newName += words[i];

					} else if (queryContact.containCn(words[i])
							&& conditionLength > 1
							&& queryContact.binSearch(condition.toLowerCase())) { // sort_key�����Կ�
						// ce �� SHI
						// �� ka ��
						// ���û����루�����ĸ����ce

						newName = newName.substring(0,
								newName.indexOf(words[i - 1])); // ���� ce �� SHI ��
																// ka ����ת�ɣ� ��
																// SHI �� ka ����
																// ��ȥ��������ǰ�Ķ�Ӧ��ƴ��
						int start = newName.length();
						int end;

						if (words[i - 1].toLowerCase().contains(
								condition.toLowerCase())) {
							newName += words[i];
							end = newName.length();
							style_name.setSpan(new ForegroundColorSpan(
									Color.RED), start, end,
									Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // ����ָ��λ�����ֵ���ɫ
						} else {

							newName += words[i];
						}

					}
				}

				holder.content.setText(style_name);
			} else {

				if (name.toLowerCase().contains(condition.toLowerCase())) {

					style_name.setSpan(new ForegroundColorSpan(Color.RED), name
							.toLowerCase().indexOf(condition.toLowerCase()),
							name.toLowerCase().indexOf(condition.toLowerCase())
									+ condition.toLowerCase().length(),
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // ����ָ��λ�����ֵ���ɫ
				}

				holder.content.setText(style_name);
			}

			// �����ֻ��
			SpannableStringBuilder style_number = new SpannableStringBuilder(
					number);
			int start = number.indexOf(condition);
			int end = start + condition.length();
			if (start >= 0) {
				style_number.setSpan(new ForegroundColorSpan(Color.RED), start,
						end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // ����ָ��λ�����ֵ���ɫ
			}

			holder.numberView.setText(style_number);
		} else {
			// ������״̬
			holder.content.setText(name);
			holder.numberView.setText(number);
		}

		if (holder.imgView.getBackground() == null) {
			 Bitmap bitmap = ContactProvider.loadImageFromUrl(context,
			 photo_id);

//			Bitmap bitmap = MessagesViewPager.getphotosMap().get(
//					Integer.valueOf(id));
//			if (bitmap != null) {
//				holder.imgView.setImageBitmap(bitmap);
//			} else {
//				holder.imgView
//						.setImageResource(R.drawable.call_defperson_normal);
//			}
		}

		String rowId = (String) holder.checkBox.getTag();
		if (selection.indexOf(rowId) != -1) {
			holder.checkBox.setChecked(true);

			if (holder.checkBox.isEnabled()) {
				if (holder.checkBox.isChecked()) {
					view
					.setBackgroundResource(R.drawable.contact_view_list_item_bg_over);
				} else {
					view.setBackgroundColor(threadUnChoosedColor);
				}
			}

		} else {
			holder.checkBox.setChecked(false);
		}


			holder.groupNameView.setText(groupName);
		
	}

	@Override
	public View newView(Context context, Cursor cur, ViewGroup parent) {
		// TODO Auto-generated method stub

		final ViewHolder holder = new ViewHolder();
		View convertViewtemp = null;

		if (useType == ContactView.OPERATOR_CONTACT
				|| useType == ContactView.IMPORT_CONTACT
				|| useType == ContactView.OTHER_MODEL_GROUP_CONTACT
				|| useType == ContactView.CALL_MODEL_GROUP_CONTACT) {
			convertViewtemp = inflater.inflate(
					R.layout.contact_view_list_item_for_select, null);
			//convertViewtemp.setBackgroundColor(threadUnChoosedColor);
		} else {
			convertViewtemp = inflater
					.inflate(R.layout.contact_view_list_item, null);
		}
		final View convertView = convertViewtemp;

		holder.numberView = (TextView) convertView
				.findViewById(R.id.contact_number_view);
		holder.content = (TextView) convertView.findViewById(R.id.content);
		holder.imgView = (ImageView) convertView
				.findViewById(R.id.contact_item_imgView);
		holder.checkBox = (CheckBox) convertView
				.findViewById(R.id.contact_item_selected);
		holder.groupNameView = (TextView) convertView
				.findViewById(R.id.contact_groupname_view);

		if (useType == ContactView.OPERATOR_CONTACT) {
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.imgView.setVisibility(View.GONE);
		} else if (useType == ContactView.IMPORT_CONTACT) {
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.imgView.setVisibility(View.GONE);
			holder.numberView.setVisibility(View.GONE);
			holder.groupNameView.setVisibility(View.VISIBLE);
		} else if (useType == ContactView.OTHER_MODEL_GROUP_CONTACT) {
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.imgView.setVisibility(View.GONE);
			holder.numberView.setVisibility(View.VISIBLE);
			holder.groupNameView.setVisibility(View.GONE);
		} else if (useType == ContactView.CALL_MODEL_GROUP_CONTACT) {
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.imgView.setVisibility(View.GONE);
			holder.numberView.setVisibility(View.VISIBLE);
			holder.groupNameView.setVisibility(View.GONE);
		} else if (useType == ContactView.SEE_CONTACT && condition != null) {
			holder.checkBox.setVisibility(View.GONE);
			holder.checkBox.setEnabled(false);
			holder.imgView.setVisibility(View.VISIBLE);
			holder.numberView.setVisibility(View.VISIBLE);
		} else {
			holder.checkBox.setEnabled(false);
			holder.checkBox.setVisibility(View.GONE);
			holder.imgView.setVisibility(View.VISIBLE);
		}

		holder.checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {// TODO Auto-generated method stub

				if (useType == ContactView.CALL_MODEL_GROUP_CONTACT || selectType==ContactView.ONLY_ON_SELECT) {
					String rowId = (String) holder.checkBox.getTag();

					selection.clear();
                    
					if(selection.indexOf(rowId)==-1)
					selection.add(rowId);
					ContactViewAdapter.this.notifyDataSetChanged();
					sendHandlerMsg();
				} else {

					String rowId = (String) holder.checkBox.getTag();

					int index = selection.indexOf(rowId);

					if (index != -1) {
						selection.remove(rowId);
					} else {
						selection.add(rowId);
					}

					sendHandlerMsg();
				}

				if (holder.checkBox.isEnabled()) {
					if (holder.checkBox.isChecked()) {
						convertView
						.setBackgroundResource(R.drawable.contact_view_list_item_bg_over);
					} else {
						convertView.setBackgroundColor(threadUnChoosedColor);
					}
				}

			}
		});

		convertView.setTag(holder);

		if (useType != ContactView.SEE_CONTACT) {
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (((ViewHolder) convertView.getTag()).checkBox
							.isChecked()) {
						((ViewHolder) convertView.getTag()).checkBox
								.setChecked(!holder.checkBox.isChecked());

						if (((ViewHolder) convertView.getTag()).checkBox
								.isEnabled()) {
							convertView
									.setBackgroundColor(threadUnChoosedColor);
						}

					} else {
						((ViewHolder) convertView.getTag()).checkBox
								.setChecked(!holder.checkBox.isChecked());
						if (((ViewHolder) convertView.getTag()).checkBox
								.isEnabled()) {
							convertView
									.setBackgroundResource(R.drawable.contact_view_list_item_bg_over);
						}
					}

					if (useType == ContactView.CALL_MODEL_GROUP_CONTACT || selectType==ContactView.ONLY_ON_SELECT) {
						String rowId = (String) holder.checkBox.getTag();

						selection.clear();

						if(selection.indexOf(rowId)==-1)
						selection.add(rowId);
						ContactViewAdapter.this.notifyDataSetChanged();
						sendHandlerMsg();
					} else {

						String rowId = (String) holder.checkBox.getTag();

						int index = selection.indexOf(rowId);

						if (index != -1) {
							selection.remove(rowId);
						} else {
							selection.add(rowId);
						}

						sendHandlerMsg();
					}

				}

			});
		}
		return convertView;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = newView(context, mCursor, parent);
		mCursor.moveToPosition(position);
		bindView(v, context, mCursor);

		return v;
	}

	public void selectAll() {
		if (mCursor == null || mCursor.getCount() <= 0) {
			return;
		}
		mCursor.moveToFirst();

		do {
			String id = "";
			if (condition != null && useType != ContactView.OPERATOR_CONTACT) {// ����״̬
				id = mCursor
						.getString(mCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			} else if (condition != null
					&& useType == ContactView.OPERATOR_CONTACT) {
				id = mCursor.getString(mCursor
						.getColumnIndex(ContactsContract.Contacts._ID));
			} else {
				id = mCursor.getString(mCursor
						.getColumnIndex(ContactsContract.Contacts._ID));
			}
			if (selection != null) {
				int index = selection.indexOf(id);

				if (index != -1) {
					// selection.remove(id);
				} else {
					selection.add(id);
				}
			}
		} while (mCursor.moveToNext());
		sendHandlerMsg();
	}

	public void unSelectAll() {
		if (mCursor == null || mCursor.getCount() <= 0) {
			return;
		}
		mCursor.moveToFirst();

		do {
			String id = "";
			if (condition != null && useType != ContactView.OPERATOR_CONTACT) {// ����״̬
				id = mCursor
						.getString(mCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			} else if (condition != null
					&& useType == ContactView.OPERATOR_CONTACT)

			{
				id = mCursor.getString(mCursor
						.getColumnIndex(ContactsContract.Contacts._ID));
			} else {
				id = mCursor.getString(mCursor
						.getColumnIndex(ContactsContract.Contacts._ID));
			}

			if (selection != null) {
				int index = selection.indexOf(id);

				if (index != -1) {
					selection.remove(id);
				} else {
					// selection.add(id);
				}
			}
		} while (mCursor.moveToNext());
		sendHandlerMsg();
	}

	public void sendHandlerMsg() {
		Message message = new Message();
		message.what = 1;
		message.obj = selection;
		handler.sendMessage(message);
	}

}
