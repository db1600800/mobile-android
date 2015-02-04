package com.compoment.contact;

//http://www.cnblogs.com/ruiyi1987/archive/2011/06/20/2084925.html
//http://www.eoeandroid.com/forum.php?mod=viewthread&tid=148105
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.View;

public class ContactProvider {


	private static ContentResolver contentResolver = null;
	static Context context;
	static SharedPreferences sp = null;
	static int sdkversion = 0;
	public static MatrixCursor contactsTableMatrixCursor = null;
	public static boolean firstTimeContactsTableMatrixCursor = true;
	static MatrixCursor phoneTableMatrixCursor = null;
	public boolean msgModelSaveContact=false;
	public boolean msgModelImportContact=false;

	private static ContactProvider instance = null;
	static List headCharList=null;
	public static synchronized ContactProvider getInstance(Context ctxt) {
		context = ctxt;

		if (instance == null) {
			instance = new ContactProvider();
			if (contentResolver == null)
				contentResolver = context.getContentResolver();

			if (sp == null)
				sp = context.getSharedPreferences("haoxie",
						Activity.MODE_PRIVATE);
			if (sdkversion == 0)
				sdkversion = Integer.valueOf(android.os.Build.VERSION.SDK);

		}
		return instance;

	}

	public ContactProvider() {
	}

	public synchronized static Bitmap loadImageFromUrl(Context ct,
			String photo_id) {
		Bitmap d = null;
		if (photo_id == null || photo_id.equals(""))
			return d;
		try {
			String[] projection = new String[] { ContactsContract.Data.DATA15 };
			String selection = "ContactsContract.Data._ID = " + photo_id;
			Cursor cur = ct.getContentResolver().query(
					ContactsContract.Data.CONTENT_URI, projection, selection,
					null, null);
			cur.moveToFirst();
			byte[] contactIcon = null;
			if (cur != null
					&& cur.getCount() > 0
					&& cur.getBlob(cur
							.getColumnIndex(ContactsContract.Data.DATA15)) != null) {
				contactIcon = cur.getBlob(cur
						.getColumnIndex(ContactsContract.Data.DATA15));

				// ͼƬ���65*65�ı������Ŵ�С
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				options.inSampleSize = computeSampleSize(options, -1, 65 * 65);
				d = BitmapFactory.decodeByteArray(contactIcon, 0,
						contactIcon.length, options);

				// Bitmap temp = BitmapFactory.decodeByteArray(contactIcon, 0,
				// contactIcon.length, opts);
				// d = Bitmap.createScaledBitmap(d,65,65,false);
				// d.recycle();
			}
			cur.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,

		maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	// ȡ�绰����
	public static List getPhoneNumber(String contactId) {
		List numbers = new ArrayList();
		Cursor cursor2 = contentResolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
				new String[] { "" + contactId }, null);
		if (cursor2.moveToFirst()) {
			do {
				String number = cursor2
						.getString(cursor2
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				numbers.add(number);
			} while (cursor2.moveToNext());
		}
		cursor2.close();

		return numbers;
	}

	public static long getRawContactId(long contactId) {
		Cursor cursor = contentResolver.query(
				ContactsContract.RawContacts.CONTENT_URI,
				new String[] { ContactsContract.RawContacts._ID },
				ContactsContract.RawContacts.CONTACT_ID + "=?",
				new String[] { "" + contactId }, null);
		if (cursor == null) {
			cursor.close();
			return -1;

		}
		if (cursor.moveToFirst()) {
			long rawId = cursor.getLong(0);
			cursor.close();
			return rawId;
		}
		cursor.close();
		return -1;
	}

	// 
	public static List createOrGetVicesGroupId(Context context) {
		
		List vicesGroupId = new ArrayList();
		
		return vicesGroupId;
	}

	// 
	public static Map getGroupContactNotIn(String currentGroupName) {// ����id

		Editor editor = sp.edit();
		Cursor cursor = null;
		String sortOrder = null;
		Map contain = new HashMap();

		sortOrder = "sort_key COLLATE LOCALIZED ASC";

		// ���ŷ���
		if (currentGroupName.equals("main")) {
			List vicesId = createOrGetVicesGroupId(context);

			String s = "(";
			// ��ȡEditor����

			for (int i = 0; i < vicesId.size(); i++) {
				s += sp.getString(vicesId.get(i).toString(), "");
			}
			s += " )";

			// ȥ���һ������
			int index = s.indexOf(",");
			String viceContactIds = "";
			if (index == -1) {
				return null;
			} else {
				viceContactIds = "  in ";
				viceContactIds += s.substring(0, index);
				viceContactIds += s.substring(index + 1, s.length());

			}

			String where = ContactsContract.Contacts._ID + viceContactIds;
			
				Cursor tempCur = (Cursor) queryAllInContactsTable().get(
						"cursor");
				contain = queryPartInAllContacts(where, tempCur);
		

		}

		// if (cursor.getCount() <= 0) {
		//
		// return null;
		// }

		return contain;
	}

	// ĳ��������Щ��ϵ��
	public static Map getGroupContactForCall() {

		Cursor cursor = null;
		String sortOrder = null;
		Map contain = new HashMap();

		sortOrder = "sort_key COLLATE LOCALIZED ASC";

		
			contain = queryAllInContactsTable();
		

		// if (cursor.getCount() <= 0) {
		// return null;
		// }

		return contain;
	}

	// ĳ��������Щ��ϵ��
	public static Map getGroupContact(String viceContactGroupName, int useType) {// ����id

		Editor editor = sp.edit();
		Cursor cursor = null;
		String sortOrder = null;
		Map contain = new HashMap();

		sortOrder = "sort_key COLLATE LOCALIZED ASC";

		// ���ŷ���
		if (viceContactGroupName.equals("main")) {
			List vicesId = createOrGetVicesGroupId(context);

			String s = "(";
			// ��ȡEditor����

			for (int i = 0; i < vicesId.size(); i++) {
				s += sp.getString(vicesId.get(i).toString(), "");
			}
			s += " )";

			// ȥ���һ������
			int index = s.indexOf(",");
			String viceContactIds = "";
			String where = null;
			if (index == -1) {

			} else {
				viceContactIds = " not in ";
				viceContactIds += s.substring(0, index);
				viceContactIds += s.substring(index + 1, s.length());

				if (useType == ContactView.OTHER_MODEL_GROUP_CONTACT) {

					where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
							+ viceContactIds;
				} else {

					where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + viceContactIds;

				}
			}

			
				if (useType == ContactView.OTHER_MODEL_GROUP_CONTACT) {

					Cursor tempCur = (Cursor) queryAllInContactsTable().get(
							"cursor");
					contain = queryPartInAllContacts(where, tempCur);
				} else {
					Cursor tempCur = (Cursor) queryAllInContactsTable().get(
							"cursor");
					contain = queryPartInAllContacts(where, tempCur);
				}
			

		} 

		return contain;
	}

	// ����ϵ�˴Ӹ��ŷ����Ƴ�
	public static void removFromViceGroup(long personId, String groupName) {
		Editor editor = sp.edit();
		String oldContactIds = sp.getString(groupName, "");
		if (!oldContactIds.equals("")
				|| oldContactIds.indexOf(String.valueOf(personId)) != -1) {
			int start = oldContactIds.indexOf(String.valueOf(personId));
			String newContactIds = "";
			newContactIds += oldContactIds.substring(0, start - 1);
			newContactIds += oldContactIds.substring(start
					+ String.valueOf(personId).length());
			editor.putString(groupName, newContactIds);
			editor.commit();
		}
	}

	// ����ϵ�˴Ӹ��ŷ����Ƴ�
	public static void removAllFromViceGroup(String personIds, String groupName) {
		Editor editor = sp.edit();
		String oldContactIds = sp.getString(groupName, "");
		if (!oldContactIds.equals("") || oldContactIds.indexOf(personIds) != -1) {

			int start = oldContactIds.indexOf(personIds);
			String newContactIds = "";
			newContactIds += oldContactIds.substring(0, start);
			newContactIds += oldContactIds
					.substring(start + personIds.length());
			editor.putString(groupName, newContactIds);
			editor.commit();
		}
	}

	// ��ĳ��ϵ�˼���ĳ����
	public static Long addGroupContact(long personId, String currentGroupName,
			String toGroupName) {

		if (toGroupName.equals("main")) {
			removFromViceGroup(personId, currentGroupName);
		} else if (!currentGroupName.equals("main")
				&& !toGroupName.equals("main")) {
			removFromViceGroup(personId, currentGroupName);
			Editor editor = sp.edit();
			String oldContactIds = sp.getString(toGroupName, "");
			if (oldContactIds.indexOf(String.valueOf(personId)) == -1) {
				editor.putString(toGroupName, oldContactIds + "," + personId);
				editor.commit();
			}
		} else {

			Editor editor = sp.edit();
			String oldContactIds = sp.getString(toGroupName, "");
			if (oldContactIds.indexOf(String.valueOf(personId)) == -1) {
				editor.putString(toGroupName, oldContactIds + "," + personId);
				editor.commit();
			}
		}

		return null;
	}

	// 
	public static String getContactInWhichGroup(long contactId) {
		Editor editor = sp.edit();
		List vicesId = createOrGetVicesGroupId(context);

		boolean viceFlag = false;
		String viceName = "";
		for (int i = 0; i < vicesId.size(); i++) {
			String oldContactIds = sp.getString(vicesId.get(i).toString(), "");
			if (oldContactIds.indexOf(String.valueOf(contactId)) == -1) {
				viceFlag = false;
				continue;
			} else {
				viceFlag = true;
				viceName = vicesId.get(i).toString();
				break;
			}
		}

		if (viceFlag) {// ����
			return viceName;
		} else {
			return "main";
		}

	}

	public static void removeContact(final long contactId) {

		String groupName = getContactInWhichGroup(contactId);
		if (groupName.equals("main")) {

		} else {
			removFromViceGroup(contactId, groupName);
		}

		long rawContactId = getRawContactId(contactId);
		String str = ContactsContract.RawContacts.CONTENT_URI.toString() + "?"
				+ ContactsContract.CALLER_IS_SYNCADAPTER + " = true";
		contentResolver.delete(Uri.parse(str), ContactsContract.RawContacts._ID
				+ " = " + rawContactId, null);
	}

	public static void removeAllContact(String contactIds,
			String contactRawIds, String groupName) {

		// String groupName = getContactInWhichGroup(contactId);
		if (groupName.equals("main")) {

		} else {
			String ids[] = contactIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				if (!ids[i].equals("")) {
					removFromViceGroup(Integer.valueOf(ids[i]), groupName);
				}
			}
		}

		// long rawContactId = getRawContactId(contactId);
		int p = contactRawIds.indexOf(",");
		contactRawIds = contactRawIds.substring(p + 1);
		String str = ContactsContract.RawContacts.CONTENT_URI.toString() + "?"
				+ ContactsContract.CALLER_IS_SYNCADAPTER + " = true";
		contentResolver.delete(Uri.parse(str), ContactsContract.RawContacts._ID
				+ " In( " + contactRawIds + ")", null);
	}

	public static void delContactWhenDeleteVice(String groupNumber) {

//		ArrayList<ViceNumberInfo> vices = (ArrayList<ViceNumberInfo>) NumberNameProvider
//				.Query((Activity) context);
//
//		List vicesGroupId = new ArrayList();
//		// ���ŷ����Ƿ��� ��û�ͽ�
//		for (int i = 0; i < vices.size(); i++) {
//			ViceNumberInfo vice = ((ViceNumberInfo) vices.get(i));
//			if (vice.Number.equals(groupNumber)) {
//				Editor editor = sp.edit();
//				editor.remove(vice.NickName);
//				editor.commit();
//				break;
//			}
//
//		}

	}

	public static void whenRenameViceName(String oldGroupName,
			String newGroupName) {

		Editor editor = sp.edit();
		String oldContactIds = sp.getString(oldGroupName, "");
		editor.putString(newGroupName, oldContactIds);
		editor.commit();
		editor.remove(oldGroupName);
		editor.commit();
	}

	// 
	public  Cursor getGroupContactForSearchNotIn(
			String viceContactGroupName, String condition, Cursor sourceCursor) {// ����id

		Editor editor = sp.edit();
		Cursor cursor = null;
		String sortOrder = null;

		
			sortOrder = ContactsContract.Contacts.DISPLAY_NAME
					+ " COLLATE LOCALIZED ASC";

		
		//
		if (viceContactGroupName.equals("main")) {
			List vicesId = createOrGetVicesGroupId(context);

			String s = "(";
			// 

			for (int i = 0; i < vicesId.size(); i++) {
				s += sp.getString(vicesId.get(i).toString(), "");
			}
			s += " )";

			// 
			int index = s.indexOf(",");
			String viceContactIds = "";
			String where = null;
			ContactUtil queryContact = new ContactUtil(context);
			if (index == -1) {
				where = "  ("
						+ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
						+ " like '%" + condition + "%' or " + "sort_key"
						+ " like '% "
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%' or " + "sort_key" + " like '"
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%')";
			} else {
				viceContactIds = "  in ";
				viceContactIds += s.substring(0, index);
				viceContactIds += s.substring(index + 1, s.length());
				where = ContactsContract.Contacts._ID + viceContactIds
						+ " AND ("
						+ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
						+ " like '%" + condition + "%' or " + "sort_key"
						+ " like '% "
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%' or " + "sort_key" + " like '"
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%')";
			}

			
			cursor = queryCursorForSearch(condition, sourceCursor);
			

		} 

		if (cursor.getCount() <= 0) {
			MatrixCursor c = new MatrixCursor(
					new String[] {
							ContactsContract.Contacts._ID,
							ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
							"sort_key",
							ContactsContract.CommonDataKinds.Phone.PHOTO_ID });

			return c;
		}

		return cursor;
	}

	public synchronized Cursor getGroupContactSearchForCall(String condition,
			Cursor sourceCursor) {
		String sortOrder = "sort_key COLLATE LOCALIZED ASC";

		ContactUtil queryContact = new ContactUtil(context);

		String where = " ("
				+ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
				+ " like '%" + condition + "%' or " + "sort_key" + " like '% "
				+ queryContact.getPYSearchRegExp(condition, "%") + "%' or "
				+ "sort_key" + " like '"
				+ queryContact.getPYSearchRegExp(condition, "%") + "%')";
		Cursor cursor = null;

		
			cursor = queryCursorForSearch(condition, sourceCursor);
		

		if (cursor.getCount() <= 0) {
			MatrixCursor c = new MatrixCursor(
					new String[] {
							ContactsContract.Contacts._ID,
							ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
							"sort_key",
							ContactsContract.CommonDataKinds.Phone.PHOTO_ID });

			return c;
		}

		return cursor;
	}

	// 
	public  Cursor getGroupContactForSearch(String viceContactGroupName,
			String condition, Cursor sourceCursor) {// 

		Editor editor = sp.edit();
		condition = condition.replace("'", "");
		String sortOrder = "sort_key COLLATE LOCALIZED ASC";
		Cursor cursor = null;
		//
		if (viceContactGroupName.equals("main")) {
			List vicesId = createOrGetVicesGroupId(context);

			String s = "(";
			//

			for (int i = 0; i < vicesId.size(); i++) {
				s += sp.getString(vicesId.get(i).toString(), "");
			}
			s += " )";

			// 
			int index = s.indexOf(",");
			String viceContactIds = "";
			ContactUtil queryContact = new ContactUtil(context);
			String where = null;
			if (index == -1) {
				where = " ("
						+ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
						+ " like '%" + condition + "%' or " + "sort_key"
						+ " like '% "
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%' or " + "sort_key" + " like '"
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%')";
			} else {
				viceContactIds = " not in ";
				viceContactIds += s.substring(0, index);
				viceContactIds += s.substring(index + 1, s.length());
				where = ContactsContract.Contacts._ID + viceContactIds
						+ " AND ("
						+ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
						+ " like '%" + condition + "%' or " + "sort_key"
						+ " like '% "
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%' or " + "sort_key" + " like '"
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%')";
			}

			
				cursor = queryCursorForSearch(condition, sourceCursor);
			

		} 

		if (cursor.getCount() <= 0) {
			MatrixCursor c = new MatrixCursor(
					new String[] {
							ContactsContract.Contacts._ID,
							ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
							"sort_key",
							ContactsContract.CommonDataKinds.Phone.PHOTO_ID });

			return c;
		}

		return cursor;
	}

	// 
	public synchronized  Cursor getGroupContactForSearchHaveNumber(
			String viceContactGroupName, String condition, Cursor sourceCursor) {// ����id

		Editor editor = sp.edit();
		Cursor cursor = null;
		String sortOrder = null;

			sortOrder = ContactsContract.Contacts.DISPLAY_NAME
					+ " COLLATE LOCALIZED ASC";

		

		condition = condition.replace("'", "");

		// ���ŷ���
		if (viceContactGroupName.equals("main")) {
			List vicesId = createOrGetVicesGroupId(context);

			String s = "(";
			// 

			for (int i = 0; i < vicesId.size(); i++) {
				s += sp.getString(vicesId.get(i).toString(), "");
			}
			s += " )";

			//
			int index = s.indexOf(",");
			String viceContactIds = "";
			ContactUtil queryContact = new ContactUtil(context);
			String where = null;
			if (index == -1) {
				where = " (" + ContactsContract.CommonDataKinds.Phone.NUMBER
						+ " like '%" + condition + "%' or "
						+ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
						+ " like '%" + condition + "%' or " + "sort_key"
						+ " like '% "
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%' or " + "sort_key" + " like '"
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%')";
			} else {
				viceContactIds = " not in ";
				viceContactIds += s.substring(0, index);
				viceContactIds += s.substring(index + 1, s.length());
				where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
						+ viceContactIds + " AND ("
						+ ContactsContract.CommonDataKinds.Phone.NUMBER
						+ " like '%" + condition + "%' or "
						+ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
						+ " like '%" + condition + "%' or " + "sort_key"
						+ " like '% "
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%' or " + "sort_key" + " like '"
						+ queryContact.getPYSearchRegExp(condition, "%")
						+ "%')";
			}

			
				cursor = queryCursorForSearch(condition, sourceCursor);
			

		} 

		if (cursor.getCount() <= 0) {
			MatrixCursor c = new MatrixCursor(
					new String[] {
							ContactsContract.CommonDataKinds.Phone._ID,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
							ContactsContract.CommonDataKinds.Phone.NUMBER,
							ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
							"sort_key",
							ContactsContract.CommonDataKinds.Phone.PHOTO_ID });
			return c;
		}

		return cursor;
	}

	public static void reFreshTotalCursor() {
		contactsTableMatrixCursor = null;
		phoneTableMatrixCursor = null;

	}

	public static synchronized Map queryAllInContactsTable() {

		Map contain = new HashMap();

		if (contactsTableMatrixCursor != null) {
			contain.put("cursor", contactsTableMatrixCursor);
			contain.put("headCharList", headCharList);
			return contain;
		}

		contactsTableMatrixCursor = new MatrixCursor(new String[] {
				ContactsContract.Contacts._ID, Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				"sort_key", "spy",ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID });

		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";
		String[] project;
		if(sdkversion>=14)
		{
		 project = new String[]{ Phone.CONTACT_ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				Phone.NUMBER,"sort_key",
				ContactsContract.Contacts.PHOTO_ID };
		}else{
		 project =new String[] { Phone.CONTACT_ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				Phone.NUMBER,
				ContactsContract.Contacts.PHOTO_ID };
		}
		Cursor mCursor = context.getContentResolver().query(
				Phone.CONTENT_URI, project, null, null,
				sortOrder);

		if (mCursor == null) {
			firstTimeContactsTableMatrixCursor = false;
			contain.put("cursor", contactsTableMatrixCursor);
			contain.put("headCharList", null);
			return contain;
		} else if (mCursor != null && mCursor.getCount() <= 0) {
			firstTimeContactsTableMatrixCursor = false;
			contain.put("cursor", contactsTableMatrixCursor);
			contain.put("headCharList", null);
			return contain;
		}

		if (mCursor.moveToFirst() == false) {
			firstTimeContactsTableMatrixCursor = false;
			contain.put("cursor", contactsTableMatrixCursor);
			contain.put("headCharList", null);
			return contain;
		}

		List sortList = new ArrayList();
		HashSet sortSet=new HashSet();
		for(int i=0;i<mCursor.getCount();i++)
		{
			mCursor.moveToPosition(i);

			ContactBean contact = new ContactBean();
			String id = "";
			String name = "";
			String number = "";
			int photoid = 0;
			String pingyin = "";
			String spy="";

			id = mCursor.getString(mCursor
					.getColumnIndex(Phone.CONTACT_ID));
			contact.contactId = id;
			name = mCursor
					.getString(mCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

			contact.name = name;
			photoid = mCursor
					.getInt(mCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
			contact.photoId = photoid;
			
			number=mCursor
					.getString(mCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			
			
			if(number==null)
			{
				continue;
			}
			
	
			
			if(sdkversion>=14)
			{
				contact.pingyin=mCursor
					.getString(mCursor
							.getColumnIndex("sort_key"));
				
				 if(contact.pingyin==null || contact.pingyin.length()<1)
		           {
		        	   continue;
		           }
				
				String temp[]=contact.pingyin.split(" ");
				
				if(temp!=null)
				{
					if(!containCn(contact.pingyin))
					{
						contact.sbSimple.append(contact.pingyin.toUpperCase());
					}else
					{
				for(int m=0;m<temp.length;m++)
				{
					if(containCn(temp[m]))
					{
						if(temp[m-1]!=null && temp[m-1].length()>=1)
						{
						contact.sbSimple.append(temp[m-1].substring(0, 1).toUpperCase());
						}
					}
				}
					}
					
					
					
				}
			}else
			{
			
			pingyin = contact.chinese2pinyin(name);
			 if(pingyin==null || pingyin.length()<1)
	           {
	        	   continue;
	           }
			contact.pingyin = pingyin;
			}

			contact.number = number;
			sortList.add(contact);
	

		} while (mCursor.moveToNext());
		  
		contactsTableMatrixCursor = (MatrixCursor) sort(sortList);

		firstTimeContactsTableMatrixCursor = false;

		contain.put("cursor", contactsTableMatrixCursor);
		contain.put("headCharList", headCharList);
		mCursor.close();
		return contain;
	}

	// ��ѯȫ��
	public static synchronized Cursor queryAllInPhoneTable() {
		if (phoneTableMatrixCursor != null)
			return phoneTableMatrixCursor;

		phoneTableMatrixCursor = new MatrixCursor(new String[] {
				ContactsContract.Contacts._ID, Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				"sort_key","spy", ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID });

		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";
		String[] project = { Phone.CONTACT_ID, Phone.DISPLAY_NAME,
				Phone.NUMBER, Phone.PHOTO_ID };
		Cursor mCursor = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, project,
				null, null, sortOrder);

		if (mCursor == null || mCursor.getCount() <= 0) {
			return phoneTableMatrixCursor;
		}
		if (mCursor.moveToFirst() == false) {
			return phoneTableMatrixCursor;
		}

		
		
		for(int i=0;i<mCursor.getCount();i++)
		{
			mCursor.moveToPosition(i);

			if (mCursor.isLast()) {
				String tem = "";
			}

			String id = "";
			String name = "";
			String number = "";
			int photoid = 0;
			String pingyin = "";
			String spy="";

			id = mCursor
					.getString(mCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

			name = mCursor
					.getString(mCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

			number = mCursor
					.getString(mCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			photoid = mCursor
					.getInt(mCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
			ContactBean contactBean=new ContactBean();
			pingyin = contactBean.chinese2pinyin(name);
			spy=contactBean.sbSimple.toString();
			phoneTableMatrixCursor.addRow(new Object[] { id, id, name, pingyin,spy,
					number, photoid });

		} while (mCursor.moveToNext());

		return phoneTableMatrixCursor;
	}

	public  synchronized Cursor queryCursorForSearch(String condition,
			Cursor mCursor) {

		MatrixCursor matrixCursor = new MatrixCursor(new String[] {
				ContactsContract.Contacts._ID, Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				"sort_key", ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID });

		if (mCursor == null || mCursor.getCount() <= 0) {
			return matrixCursor;
		}
		mCursor.moveToFirst();
		int count=mCursor.getCount();
		

		List heads = new ArrayList();
		ContactUtil queryContact = new ContactUtil(context);

		// ��ѯ���� �Ƿ�Ϊƴ����ƣ�true��
		boolean isCn=queryContact.containCn(condition);
		boolean isSimpleForm=false;
		if(!isCn)
		 isSimpleForm = queryContact.isSimpleFormPingyin(condition);
		
		
		for(int i=0;i<count;i++)
		{
			
			mCursor.moveToPosition(i);
			String sortkey = mCursor.getString(mCursor.getColumnIndex("sort_key"));
			
		
			
			String number = mCursor
					.getString(mCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			
			String name =mCursor
					.getString(mCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				
		
			
			String spy =  mCursor.getString(mCursor.getColumnIndex("spy"));
			String id =mCursor.getString(mCursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			int photoid = mCursor
					.getInt(mCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
		
			 if (isSimpleForm) {//�û�����Ϊƴ���������
				if (spy.contains(condition.toUpperCase()) || number.contains(condition)) {
					matrixCursor.addRow(new Object[] { id, id, name, sortkey,
							number, photoid });
				}
			}
			else 
			{
				if (sortkey.contains(condition.toLowerCase()) || name.contains(condition.toUpperCase()) || name.contains(condition.toLowerCase()) ) {
			
					matrixCursor.addRow(new Object[] { id, id, name, sortkey,
							number, photoid });
				}
			}

			 
		}
		
	
		

		return matrixCursor;

	}

	public static synchronized Map queryPartInAllContacts(String inOrNotIn,
			Cursor mCursor)// mCursor
	// ΪphoneTableMatrixCursor
	// ��
	// contactsTableMatrixCursor
	{

		List headCharList = new ArrayList();
		Map contain = new HashMap();

		Boolean isIn = false;
		if (inOrNotIn == null) {
			isIn = null;
		} else if (inOrNotIn.contains("not in")) {
			int p = inOrNotIn.indexOf("not in");
			if (p != -1) {
				inOrNotIn = inOrNotIn.substring(p + 6).trim();
				inOrNotIn = inOrNotIn.replace("(", ",");
				inOrNotIn = inOrNotIn.replace(" )", ",");
				isIn = false;
			}
		} else if (inOrNotIn.contains("in")) {
			int p = inOrNotIn.indexOf("in");
			if (p != -1) {
				inOrNotIn = inOrNotIn.substring(p + 2).trim();
				inOrNotIn = inOrNotIn.replace("(", ",");
				inOrNotIn = inOrNotIn.replace(" )", ",");
				isIn = true;
			}
		}

		MatrixCursor matrixCursor = new MatrixCursor(new String[] {
				ContactsContract.Contacts._ID, Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				"sort_key","spy", ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID });

		if (mCursor == null) {
			contain.put("cursor", matrixCursor);
			contain.put("headCharList", headCharList);
			return contain;
		} else if (mCursor != null && mCursor.getCount() <= 0) {
			contain.put("cursor", matrixCursor);
			contain.put("headCharList", headCharList);
			return contain;
		}

		if (mCursor.moveToFirst() == false) {
			contain.put("cursor", matrixCursor);
			contain.put("headCharList", headCharList);
			return contain;
		}
		// if (isIn == null) {
		//
		// contain.put("cursor", mCursor);
		// contain.put("headCharList", headCharList);
		// return contain;
		//
		// }

		for(int i=0;i<mCursor.getCount();i++)
		{
			mCursor.moveToPosition(i);
			String id = "";

			id = mCursor.getString(mCursor
					.getColumnIndex(ContactsContract.Contacts._ID));

			if (isIn == null) {
				String name = mCursor
						.getString(mCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				String sortkey = mCursor.getString(mCursor
						.getColumnIndex("sort_key"));

				String spy = mCursor.getString(mCursor
						.getColumnIndex("spy"));
				
				String headChar = "";
				if (sortkey != null && sortkey.length() > 0) {
					headChar = sortkey.substring(0, 1).toUpperCase();
				}

				String number = mCursor
						.getString(mCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				int photoid = mCursor
						.getInt(mCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
				matrixCursor.addRow(new Object[] { id, id, name, sortkey,spy,
						number, photoid });
				headCharList.add(headChar);

			} else if (isIn) {

				if (inOrNotIn.contains("," + id + ",")) {

					String name = mCursor
							.getString(mCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					String sortkey = mCursor.getString(mCursor
							.getColumnIndex("sort_key"));
					String spy = mCursor.getString(mCursor
							.getColumnIndex("spy"));
					String headChar = "";
					if (sortkey != null && sortkey.length() > 0) {
						headChar = sortkey.substring(0, 1).toUpperCase();
					}

					String number = mCursor
							.getString(mCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					int photoid = mCursor
							.getInt(mCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
					matrixCursor.addRow(new Object[] { id, id, name, sortkey,spy,
							number, photoid });
					headCharList.add(headChar);

				}
			} else if (isIn == false) {
				if (!inOrNotIn.contains("," + id + ",")) {

					String name = mCursor
							.getString(mCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					String sortkey = mCursor.getString(mCursor
							.getColumnIndex("sort_key"));
					String spy = mCursor.getString(mCursor
							.getColumnIndex("spy"));
					String headChar = "";
					if (sortkey != null && sortkey.length() > 0) {
						headChar = sortkey.substring(0, 1).toUpperCase();
					}

					String number = mCursor
							.getString(mCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					int photoid = mCursor
							.getInt(mCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
					matrixCursor.addRow(new Object[] { id, id, name, sortkey,spy,
							number, photoid });
					headCharList.add(headChar);

				}
			}
		}

		contain.put("cursor", matrixCursor);
		contain.put("headCharList", headCharList);
		return contain;

	}

	public static Cursor sort(List<ContactBean> sortList) {

		final Comparator cmp = Collator.getInstance(java.util.Locale.ENGLISH);

		Comparator<ContactBean> comparator = new Comparator<ContactBean>() {
			@Override
			public int compare(ContactBean entry1, ContactBean entry2) {
				return cmp.compare(entry1.sbSimple.toString().subSequence(0, 1).toString().toUpperCase(), entry2.sbSimple.toString().subSequence(0, 1).toString().toUpperCase());
			}
		};

		// ����
		Collections.sort(sortList, comparator);

		MatrixCursor matrixCursor = new MatrixCursor(new String[] {
				ContactsContract.Contacts._ID, Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				"sort_key","spy",ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID });
		headCharList=new ArrayList();
		for (int i = 0; i < sortList.size(); i++) {
			String id = "";
			String name = "";
			String number = "";
			int photoid = 0;
			String pingyin = "";
			String spy="";

			ContactBean contact =null;
			contact = (ContactBean) sortList.get(i);
			id = contact.contactId;
			name = contact.name;
			number = contact.number;
			photoid = contact.photoId;
			pingyin = contact.pingyin;
	        spy=contact.sbSimple.toString().toUpperCase();
			
			headCharList.add(spy.substring(0, 1).toUpperCase());
			matrixCursor.addRow(new Object[] { id, id, name, pingyin,spy, number,
					photoid });
		}

		return matrixCursor;

	}

	
	public static boolean containCn(String str) {
		Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
		return pattern.matcher(str).find();
	}
	
}
