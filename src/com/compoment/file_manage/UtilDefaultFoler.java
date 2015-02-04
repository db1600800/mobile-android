package com.compoment.file_manage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import android.os.Environment;
import android.widget.Toast;


/**
 * 
 * @author zigang
 * 默认扫描路劲的读写操�?
 */
public class UtilDefaultFoler {
	
	public final static String FILE_NAME="data/data/com.gmcc.downloadmanager/defaultPath.txt";
	public static void writeDefaultFolderFile(List<ApkFileInfo> listFoler) throws IOException{
		
//		Log.e("writeInfo", "writeInfo");
		
		OutputStream fis = new FileOutputStream(new File(FILE_NAME));
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i < listFoler.size();i++){
			sb.append( listFoler.get(i).folderName);
			sb.append("--");
			sb.append(listFoler.get(i).folderPath);
			sb.append("\r\n");
			//Log.e("","write:"+i);
		}
		fis.write(sb.toString().getBytes("utf_8"));
		fis.flush();	
		fis.close(); 
	}
	
	/**
	 * 閻犲洤顭烽崢銈囩磾椤旇姤鐎ù鐘虫构娣囧﹪骞侀敓锟�?* @return
	 */
	public static List<ApkFileInfo> readDefaultFolderFile(){
		//閻犲洤顭烽妴宥夋儎椤旂偓鐣遍梺鏉跨Ф閻ゅ棝寮崶锔筋偨
		List<ApkFileInfo> listFolder = new ArrayList<ApkFileInfo>();
//		Log.e("readInfo","readInfo");
		 try 
	      {
			 FileReader reader = new FileReader(FILE_NAME);
			 BufferedReader br = new BufferedReader(reader);
			 String item = br.readLine();
			 if(null == item || item.trim().equals("")){
				 return null;
			 }
			 
			 ApkFileInfo folderInfo = null;
			 do {
//				 Log.e("readDefaultFolderFile()", "folderItem:"+item);
				try {
					folderInfo = new ApkFileInfo();
					String temp[] = item.split("--");
					folderInfo.folderName = temp[0];
					folderInfo.folderPath = temp[1];
					listFolder.add(folderInfo);
				} catch (Exception e) {
				}
				
			}while((item = br.readLine()) != null && !item.equals(""));
			 br.close();
			 reader.close();
	    }
	    catch(IOException e)
	    {
	     e.printStackTrace();   
	    }
		return listFolder;
	}
	
	/**
	 * 闁告帗绋戠紓鎾绘�??ュ洨鏋傞柡鍌氭矗濞嗭�?	 */
	public static void createDefaultFolderFile(){
//		Log.e("createPrefFile", "createPrefFile");
		File f = new File(FILE_NAME);
		if(f.exists() == false){
			try {
				f.createNewFile();
			} catch (IOException e) {
			}
		}
	}
	
	
	/**
	 * 闁告帇鍊栭弻鍥棘閸ワ附顐介柡鍕靛灠閹胶锟藉Ο鐑樿含
	 */
	public static boolean isDefaultFolderFileExist(){
		File f = new File(FILE_NAME);
		return f.exists();
	}
	
	/**
	 * 闁告帞濞�▍搴ㄦ�??ュ洨鏋傞柡鍌氭矗濞嗭�?	 */
	public static void deleteDefaultFolderFile(){
		File f = new File(FILE_NAME);
		f.delete();
//		Log.e("deletePrefFile", "deletePrefFile");
	}	
	
	public final static String MY_FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";
	/**
	 * �½��ļ���
	 */
	
	public static void createNewFolder(Context ctx, String folderName) {
		File folder = new File(MY_FOLDER_PATH +"/"+ folderName);
//		Log.e("bkship", "folderPath:" + folder.getPath());
		if (!folder.exists()) {
			boolean createOk = folder.mkdirs();

			if (createOk) {
				showToast("�ղؼ�:"+folderName+"�Ѵ�����", ctx);
			} else {
				showToast("�ղؼ���Ʋ��Ϸ���", ctx);
			}
		}
	}
	
	/**
	 * �������ļ���
	 * @param folderPath
	 * @param newFolderName
	 * @param ctx 
	 */
	public static void renameFolder(String folderPath,String newFolderName, Context ctx) {
		File folder = new File(folderPath);
		String path = folder.getAbsolutePath();
		File newFolder = new File(path.substring(0, path.lastIndexOf("/")+1)+newFolderName);
		if(folder.renameTo(newFolder) == true){
			showToast("������ɹ���", ctx);
		}else{
			showToast("��Ʋ��Ϸ���", ctx);
		}
			
	}
	  

	/**
	 * ɾ���ļ���
	 * @param ctx
	 * @param folderPath
	 */
	public static void deleteFolder(Context ctx, String folderPath) {
		File folder = new File(folderPath);
		String[] fileList = folder.list();
		if (isFolderEmpty(folderPath) == true) {
			if (!folder.delete()) {
				showToast("Fail,Delete folder", ctx);
				return;
			}
		} else {
			for (int i = 0; i < fileList.length; i++) {
				String fileName = fileList[i];
				String filePath = folderPath + File.separator + fileName;
				File file = new File(filePath);
				if (file.exists() && file.isFile()) {
					if (!file.delete()) {
						showToast("Fail,Delete folder", ctx);
						return;
					}
				} else if (file.exists() && file.isDirectory()) {
					deleteFolder(ctx, filePath);
				}
			}
			if (!folder.delete()) {
				showToast("�ļ���ɾ��ʧ�ܡ�", ctx);
				return;
			}
		}
		showToast("ɾ��ɹ���", ctx);

	}

	/**
	 * �ж��ļ����Ƿ�Ϊ��
	 * 
	 * @param folderPath
	 * @return
	 */
	public static boolean isFolderEmpty(String folderPath) {
		File folder = new File(folderPath);
		String[] fileList = folder.list();
		if (null == fileList || fileList.length == 0) {
			return true;
		}
		return false;

	}

	/**
	 * �����ļ�
	 */
	static String filePathForCopy;
	public static void copyFile(String filePath) {
		filePathForCopy = filePath;
	}

	/**
	 * ճ���ļ�����
	 * 
	 * @param filePath
	 * @param folderPath
	 * @throws IOException
	 */
	public static void plastFile(String filePath, String folderPath)
			throws IOException {
		byte[] buffer = new byte[1024 * 8];

		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		String fileName = file.getName();
//		Log.e("fileName", "fileName:" + fileName);
		File newFile = new File(folderPath + File.separator + fileName);

		if (newFile.exists()) {
			newFile.delete();
		}
		newFile.createNewFile();

		FileOutputStream fos = new FileOutputStream(newFile);
		int read = 0;
		while ((read = fis.read(buffer)) > 0) {
			fos.write(buffer, 0, read);
			fos.flush();
		}

		if (null != fis) {
			fis.close();
		}
		if (null != fos) {
			fos.close();
		}
		buffer = null;
	}

	/**
	 * �ƶ��ļ���ָ��Ŀ¼
	 * 
	 * @param filePath
	 * @param folderPath
	 */
	public static void moveFile(String filePath, String folderPath) {
		try {
			plastFile(filePath, folderPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File(filePath);
		if (file.delete()) {
//			Log.e("bkship", "Successfully,move file to folder!");
			
		}
	}

	/**
	 * showToast
	 * @param str
	 * @param ctx
	 */
	public static void showToast(String str, Context ctx) {
		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * ��MyFolder�ļ������ļ�����Ϣ�ŵ�List��
	 * @return
	 */
	
	public static List<ApkFileInfo> getMyFolderList(){
		List<ApkFileInfo> listFolder = new ArrayList<ApkFileInfo>();
		File folder = new File(MY_FOLDER_PATH);
		if(folder.exists() == false){
			boolean	createOk = folder.mkdirs();
//			Log.e("","creat MyFolder:"+createOk);
			return listFolder;
		}
		File[] files = folder.listFiles();
		ApkFileInfo fileInfo;
		if(files != null){
			for(int i = 0;i<files.length;i++){
				File file = files[i];
				if(file.isDirectory() == true){
					fileInfo = new ApkFileInfo();
					fileInfo.folderName = file.getName();
					fileInfo.folderPath = file.getAbsolutePath();
					fileInfo.isFolder = true;
					
					listFolder.add(fileInfo);
				}
			}
		}
		return listFolder;
	}
	
	public static boolean isFolderNameExist(String str){
		File folder = new File(MY_FOLDER_PATH);
		File[] files = folder.listFiles();
		for(int i = 0;i<files.length;i++){
			if(files[i].getName().equals(str)){
				return true;
			}
		}
		return false;
	}
}
