package com.compoment.file_manage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




import android.content.Context;
import android.util.Log;
import android.widget.Toast;



public class UtilFolderOperate {

	
	static String sdcardPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
	public final static String MY_FOLDER_PATH = sdcardPath + "/MyFolder";
	public static Context context;
	/**
	 * 新建文件夹
	 */
	
	public static void createNewFolder(Context ctx, String folderName) {
		File folder = new File(MY_FOLDER_PATH +"/"+ folderName);
		if (!folder.exists()) {
			boolean createOk = folder.mkdirs();

			if (createOk) {
				showToast("收藏夹:"+folderName+"已创建。", ctx);
			} else {
				showToast("收藏夹名称不合法！", ctx);
			}
		}
	}
	
	
	/**
	 * 重命名文件夹
	 * @param folderPath
	 * @param newFolderName
	 * @param ctx 
	 */
	public static void renameFolder(String folderPath,String newFolderName, Context ctx) {
		File folder = new File(folderPath);
		String path = folder.getAbsolutePath();
		File newFolder = new File(path.substring(0, path.lastIndexOf("/")+1)+newFolderName);
		if(folder.renameTo(newFolder) == true){
			showToast("重命名成功。", ctx);
		}else{
			showToast("名称不合法。", ctx);
		}
			
	}
	
	/**
	 * 重命名文件
	 * @param oldFile
	 * @param newFileName
	 * @param ctx
	 * @return 0为重命名失败不刷新列表，1为重命名成功，2为文件已不存在
	 */
	public static Integer renameFile(ApkFileInfo oldFile,String newFileName, Context ctx) {
		
		File folder = new File(oldFile.filePath);
		if(!folder.exists()){
			showToast("文件已不存在。", ctx);
			return 2;
		}
		
		String path = folder.getAbsolutePath();
		File newFolder = new File(path.substring(0, path.lastIndexOf("/")+1)+newFileName);
		if(!newFolder.exists()){
			if(folder.renameTo(newFolder) == true){
				showToast("文件重命名成功。", ctx);
				return 1;
			}else{
				showToast("重命名失败", ctx);
				return 0;
			}
		}else{
			showToast("重命名失败，文件所在目录已有相同的文件名。", ctx);
			return 0;
		}
			

		
		
//		File folder = new File(oldFile.filePath);
//		if(!folder.exists()){
//			showToast("�ļ��Ѳ����ڡ�", ctx);
//			return false;
//		}
//		
//		String path = folder.getAbsolutePath();
//		File newFolder = new File(path.substring(0, path.lastIndexOf("/")+1)+newFileName);
//		if(folder.renameTo(newFolder) == true){
//			showToast("�ļ�������ɹ���", ctx);
//			return true;
//		}else{
//			showToast("�ļ�����Ѿ����ڡ�", ctx);
//			return false;
//		}
//			
	}
	  

	/**
	 * 删除文件夹
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
				showToast("文件夹删除失败。", ctx);
				return;
			}
		}
		showToast("删除成功。", ctx);

	}

	/**
	 * 判断文件夹是否为空
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
	 * 拷贝文件
	 */
	static String filePathForCopy;
	public static void copyFile(String filePath) {
		filePathForCopy = filePath;
	}

	/**
	 * 粘贴文件操作
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
	 * 移动文件到指定目录
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
		file.listFiles();
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
	 * 将MyFolder文件夹中文件夹信息放到List中
	 * @return
	 */
	
	public static List<ApkFileInfo> getMyFolderList(){
		List<ApkFileInfo> listFolder = new ArrayList<ApkFileInfo>();
		File folder = new File(MY_FOLDER_PATH);
		if(folder.exists() == false){
			boolean	createOk = folder.mkdirs();
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
					
					if(context!=null)
					{
					List<ApkFileInfo> listTemp = null;
					listTemp=UtilFileClassify.scanSdcard(fileInfo.folderPath,  context);

					fileInfo.fileCount= listTemp.size();
					}
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
