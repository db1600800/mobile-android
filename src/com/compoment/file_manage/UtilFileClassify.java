package com.compoment.file_manage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android_demonstrate_abstractcode.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;



/**
 * 文件的分类，打开，删除，扫描，过滤等操作
 * 
 * @author issuser
 * 
 */
public class UtilFileClassify {
	
	/**
	 * 解析xml，读取里面的文件夹信息
	 * 
	 * @param ctx
	 * @param listFile
	 */
	public static void scanDefaultFolder(List<ApkFileInfo> listFolder,
			List<ApkFileInfo> listFile, Context context) {
		String folderPath = null;
		for (int i = 0; i < listFolder.size(); i++) {
			if (CloudDocApkOtherActivity.scandSwitcher == false) {
				break;
			}
			folderPath = listFolder.get(i).folderPath;
			scanSingleFolder(folderPath, listFile, context);
		}
		String sdcardPath = android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath();
		UtilFileClassify.loadFileOnly(sdcardPath, listFile, context);

	}

	/**
	 * 扫描sdcard，将里面的文件写入list中
	 * 
	 * @param folderPath
	 * @param listFile
	 * @param allFile
	 */
	public static List<ApkFileInfo> scanSdcard(String folderPath, 
			Context context) {
		List<ApkFileInfo> listFile=new ArrayList();
		File folder = new File(folderPath);
		File[] files = folder.listFiles();// 列出所有文件

		if (files != null) {
			List folderList = new ArrayList();
			@SuppressWarnings("rawtypes")
			List fileList = new ArrayList();
			for (int i = 0; i < files.length; i++) {

				if (CloudDocApkOtherActivity.scandSwitcher == false) {
					break;
				}

				if (files[i].getName().equals("extSD")) {
					continue;
				}

				ApkFileInfo fileInfo = new ApkFileInfo();
				

				File file = files[i];
				String fileName = file.getName().toLowerCase();
				// if (fileFilter(fileName))
				{ // 过滤文件

					if (files[i].isDirectory()) {
						// 如果是文件夹就显示的图片为文件夹的图片
						fileInfo.icon = context.getResources().getDrawable(
								R.drawable.cloud_folder);
						fileInfo.isFolder = true;
						fileInfo.folderName = file.getName();
						fileInfo.folderPath = file.getPath();
						fileInfo.fileName = file.getName();
						fileInfo.filePath = file.getPath();
						
						if (!file.getPath().toString().startsWith("/")) {
							fileInfo.folderPath = "/" + fileInfo.folderPath;
						}
						fileInfo.lastModifiedTime = getFileLastModifiedTime(file
								.getPath());
						
						
						List<ApkFileInfo> listTemp = null;
						listTemp=UtilFileClassify.scanSdcard(fileInfo.folderPath,  context);

						fileInfo.fileCount= listTemp.size();
						
						
						folderList.add(fileInfo);
					} else {
						
						// 过滤掉小于40K的文件
						if (file.length() > 40 * 1024
								&& fileFilter(file.getName())) {
							fileInfo.isFolder = false;
							fileInfo.fileName = file.getName();
							fileInfo.filePath = file.getPath();
							
							if (file.getName().toLowerCase().endsWith(".apk")) {
								setApkNameAndIcon(context, fileInfo);
							}
							
							if (!file.getPath().toString().startsWith("/")) {
								fileInfo.filePath = "/" + fileInfo.filePath;
							}
							fileInfo.lastModifiedTime = getFileLastModifiedTime(file
									.getPath());
							fileList.add(fileInfo);
						}
					}

				}

			}
			if(folderList!=null && folderList.size()>0)
			{
			UtilFileClassify.sortListByName(folderList, true);
			listFile.addAll(folderList);
			}
			listFile.addAll(fileList);
			
		}
		return listFile;
	}

	/**
	 * 打开指定的文件夹，将里面的文件写入list中
	 *  (递归遍历，文件夹下的文件夹的文件，即全部文件)
	 * @param folderPath
	 * @param listFile
	 * @param allFile
	 */
	public static void scanSingleFolder(String folderPath,
			List<ApkFileInfo> listFile, Context context) {
		// listFile.size();
		File folder = new File(folderPath);
		File[] files = folder.listFiles();// 列出所有文件
		// 将所有文件存入list中
		if (files != null) {
			int count = files.length;// 文件个数
			ApkFileInfo fileInfo = null;
			for (int i = 0; i < count; i++) {
				// 当按了返回键之后 就跳出扫描
				if (CloudDocApkOtherActivity.scandSwitcher == false) {
					break;
				}
				File file = files[i];
				// 如果不为文件夹就将文件添加到listFile中
				if (!file.isDirectory()) {
					if (file.length() < (40 * 1024)) {// 过滤掉小于40K的文件
						continue;
					}

					String fileName = file.getName().toLowerCase();
					if (fileFilter(fileName)) { // 过滤文件
						fileInfo = new ApkFileInfo();
						fileInfo.fileName = file.getName();
						fileInfo.filePath = file.getPath();

						if (!file.getPath().toString().startsWith("/")) {
							fileInfo.filePath = "/" + fileInfo.filePath;
						}

						if (file.getName().toLowerCase().endsWith(".apk")) {
							setApkNameAndIcon(context, fileInfo);
						}
						fileInfo.lastModifiedTime = getFileLastModifiedTime(file
								.getPath());
						fileInfo.isFolder = false;

						listFile.add(fileInfo);
					}

				} else {

					if (CloudDocApkOtherActivity.scandSwitcher == false) {// 如果按了返回键
						// 跳出扫描
						break;
					}
					// 为文件夹就 递归扫描
					String folderName = file.getName().toLowerCase();
					if (folderFilter(folderName)) {// 文件夹过滤
						scanSingleFolder(file.getPath(), listFile, context);
					}
				}
			}
		} else {
			// folder is null
		}
	}

	/**
	 * 文件过滤
	 * 
	 * @param fileName
	 * @return
	 */
	private static boolean fileFilter(String fileName) {
		String filePostfix = getFilePostfix(fileName);
		// 略掉不想要的后缀文件
		String postfix = ".xml.log.dll.mf.db.sys.info.asec.tmp0.tmp1.addr.cache"
				+ ".clsd.lck.indicate.dz.clsid.shd.mm.bak.arsc.dex.pak.sf.lock"
				+ ".lang.css.js.tpl.pcap.vmag.shd.bin.htm.plx.pdt.cachever.fdb.lst.dat"
				+ ".key.ini.tmc.bat.irf.pat.nomedia.tmp2.db3.idx.1.rsa.json.ifo.reflect.scel";
		if (null != filePostfix && postfix.indexOf(filePostfix) == -1 // 过滤掉指定后缀的文件
				// && filePostfix.length() < 6 // 过滤掉后缀长度大于6位的文件
				// && fileName.length() < 58 //过滤掉文件名长度大于58(两行)的文件
				&& fileName.indexOf("http") == -1 // 过滤掉文件名中含有http的文件
				&& fileName.charAt(0) != '.' // 过滤掉以.开头的文件
				&& fileName.indexOf("tmp") == -1 // 过滤掉文件名中含有tem的文件
				&& fileName.indexOf("log") == -1 // 过滤掉文件名中含有Log的文件
				&& fileName.indexOf("temp") == -1 // 过滤掉文件名中含有temp的文件
				&& fileName.indexOf("cache") == -1) { // 过滤掉文件名中含有cache的文件
			return true;
		}
		return false;
	}

	/**
	 * 文件夹过滤
	 * 
	 * @param folderName
	 * @return
	 */
	private static boolean folderFilter(String folderName) {
		if (folderName.indexOf("temp") == -1 // 过滤掉文件夹名中含有temp的文件夹
				&& folderName.indexOf("tmp") == -1 // 过滤掉文件夹名中含有tem的文件夹
				&& folderName.indexOf("cache") == -1 // 过滤掉文件夹名中含有cache的文件夹
				&& folderName.indexOf("log") == -1 // 过滤掉文件夹名中含有log的文件夹
				&& folderName.charAt(0) != '.' // 过滤掉以.开头的文件夹
				&& !folderName.equals("android") // 过滤掉系统文件夹 android
				// && !folderName.equals("dcim") //过滤掉照相目录dcim
				&& !folderName.equals("data")) { // 过滤掉数据目录data
			return true;
		}
		return false;
	}


	/*
	 * load SDcard file(not include folder) 
	 *  (锟斤拷锟斤拷前目录锟铰碉拷全锟斤拷锟侥硷拷锟斤拷锟角递癸拷锟斤拷去)
	 */
	public static void loadFileOnly(String folderPath, List<ApkFileInfo> listFile,
			Context context) {
		File folder = new File(folderPath);
		File[] files = folder.listFiles();// 列出所有文件
		if (files != null) {
			int count = files.length;// 文件个数
			ApkFileInfo fileInfo = null;
			for (int i = 0; i < count; i++) {
				File file = files[i];
				// 如果不为文件夹就将文件添加到listFile中

				if (!file.isDirectory() && file.length() > 40 * 1024
						&& fileFilter(file.getName())) {
					fileInfo = new ApkFileInfo();
					fileInfo.fileName = file.getName();
					fileInfo.filePath = file.getPath();
					fileInfo.lastModifiedTime = getFileLastModifiedTime(file
							.getPath());
					if (file.getName().endsWith(".apk")) {
						setApkNameAndIcon(context, fileInfo);
					}

					listFile.add(fileInfo);
				}

			}
		}

	}

	/**
	 * Get the last modify time of the file
	 */
	public static String getFileLastModifiedTime(String filePath) {
		String path = filePath.toString();
		File f = new File(path);
		Calendar calendar = Calendar.getInstance();
		long time = f.lastModified();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		calendar.setTimeInMillis(time);

		// 输出：修改时间2009-08-17 10:32:38
		return formatter.format(calendar.getTime());
	}

	/**
	 * 对文件进行分类
	 */
	public static List<ApkFileInfo> classifyFile(List<ApkFileInfo> listFile,
			String postfix) {
		List<ApkFileInfo> list = new ArrayList<ApkFileInfo>();
		String filePostfix = null;
		for (int i = 0; i < listFile.size(); i++) {
			filePostfix = getFilePostfix(listFile.get(i).fileName);
			if (null == filePostfix) {
				continue;
			}
			if (postfix.indexOf(filePostfix) != -1) {
				list.add(listFile.get(i));
			}
		}

		return list;

	}

	/**
	 * 其他类单独写
	 */
	public static List<ApkFileInfo> classifyFileForOther(List<ApkFileInfo> listFile,
			String postfix) {
		List<ApkFileInfo> list = new ArrayList<ApkFileInfo>();
		String filePostfix = null;
		for (int i = 0; i < listFile.size(); i++) {
			filePostfix = getFilePostfix(listFile.get(i).fileName);
			if (null == filePostfix || postfix.indexOf(filePostfix) == -1) {
				list.add(listFile.get(i));
			}
		}
		return list;
	}

	/**
	 * 根据文件名获取文件的后缀
	 */
	public static String getFilePostfix(String filename) {
		String filePostfix = null;
		if (filename.lastIndexOf(".") != -1) {
			filePostfix = filename.substring(filename.lastIndexOf("."))
					.toLowerCase();
		}
		return filePostfix;
	}

	/**
	 * 打开文件
	 * 
	 * @param ctx
	 * @param file
	 */
	public static void openFile(Context ctx, ApkFileInfo file) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 设置标记，说明是启动一个新任务
		intent.setAction(android.content.Intent.ACTION_VIEW);// intent告诉activity，这个动作是查看
		String type = UtilFileClassify.getMIMEType(file.fileName);
		// Uri fileUri = Uri.parse(URLDecoder.decode("file://" +
		// file.filePath)); //.replaceAll(" ", "%20")
		Uri fileUri = Uri.fromFile(new File(file.filePath));
		intent.setDataAndType(fileUri, type);
		try {
			ctx.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(ctx, "没有安装对应的程序来打开此文件", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 删除指定文件
	 * 
	 * @param filePath
	 */
	public static void deleFile(String filePath) {
		File file = new File(filePath);
		file.delete();
	}

	/**
	 * 对list进行排序
	 * 
	 * @param list
	 */

	// public static void sortList(List<FileInfo> list){
	// if(list.size() == 0)return;
	//
	// String sortBy = ActivityFileManager.sortByWhat;
	// if(sortBy.equals("time")){
	// sortListByTime(list);
	// } else if(sortBy.equals("size")){
	// sortListBySize(list);
	// }else if(sortBy.equals("name")){
	// sortListByName(list);
	// }else{
	// Log.e("","please check the string sortByWhat");
	// }
	// }

	/***
	 * 按最后修改时间排序
	 * 
	 * @param list
	 * @param isAscend
	 */
	public static void sortListByTime(List<ApkFileInfo> list, boolean isAscend) {
		// 对ListView中数据list排序
		ComparatorByTime comparator = new ComparatorByTime(isAscend);
		if (!list.isEmpty()) {
			synchronized (list) {
				Collections.sort(list, comparator);
			}

		}
	}

	/**
	 * 按文件大小排序
	 * 
	 * @param list
	 */
	public static void sortListBySize(List<ApkFileInfo> list, boolean isAscend) {
		// 对ListView中数据list排序
		ComparatorBySize comparator = new ComparatorBySize(isAscend);
		if (!list.isEmpty()) {
			synchronized (list) {
				Collections.sort(list, comparator);
			}

		}
	}

	/**
	 * 按文件名称排序
	 * 
	 * @param list
	 */
	public static void sortListByName(List<ApkFileInfo> list, boolean isAscend) {
		// 对ListView中数据list排序
		ComparatorByName comparator = new ComparatorByName(isAscend);
		if (!list.isEmpty()) {
			synchronized (list) {
				Collections.sort(list, comparator);
			}

		}
	}

	/**
	 * 判断要添加的目录 是否已经包含在默认扫描目录里面
	 */
	public static boolean isFolderPathExisted(String folderPath) {
		String listFolderPath = null;
		for (int i = 0; i < CloudDocApkOtherActivity.listFolder.size(); i++) {
			listFolderPath = CloudDocApkOtherActivity.listFolder.get(i).folderPath;
			if (folderPath.indexOf(listFolderPath) != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取文件的MIME类型
	 */
	public static String getMIMEType(String fileName) {
		String type = null;
		String filePostfix = getFilePostfix(fileName);
		if (null == filePostfix) {
			return "*/*";
		}

		String videoType = ".3gp.asf.avi.m4u.m4v.mov.mp4.mpe.mpeg.mpg.mpg4.swf.flv";
		String applicationType = ".apk.bin.class.doc.exe.gstar.gz.jar.js.mpc.msg.pdf.pps.ppt.rar.rtf.tar.tgz.wps.z.zip.docx.xlsx.pptx";
		String imageType = ".bmp.gif.jpeg.jpg.png.imag";
		String textType = ".c.conf.cpp.h.htm.html.java.log.prop.rc.sh.txt.xml.chm.mth";
		String audioType = ".m3u.m4a.m4b.m4p.mp2.mp3.mpga.ogg.wav.wma.wmv.rmvb.aac.mid";
		if (videoType.indexOf(filePostfix) != -1) {
			if (filePostfix.equals(".3gp")) {
				type = "video/3gpp";
			} else if (filePostfix.equals(".asf")) {
				type = "video/x-ms-asf";
			} else if (filePostfix.equals(".avi")) {
				type = "video/x-msvideo";
			} else if (filePostfix.equals(".m4u")) {
				type = "video/vnd.mpegurl";
			} else if (filePostfix.equals(".m4v")) {
				type = "video/x-m4v";
			} else if (filePostfix.equals(".mov")) {
				type = "video/quicktime";
			} else if (filePostfix.equals(".mp4")) {
				type = "video/quicktime";
			} else if (filePostfix.equals(".mpe")) {
				type = "video/mpeg";
			} else if (filePostfix.equals(".mpeg")) {
				type = "video/mpeg";
			} else if (filePostfix.equals(".mpg")) {
				type = "video/mpeg";
			} else if (filePostfix.equals(".mpg4")) {
				type = "video/mp4";
			} else {
				type = "*/*";
			}

		} else if (audioType.indexOf(filePostfix) != -1) {
			if (filePostfix.equals(".m3u")) {
				type = "audio/x-mpegurl";
			} else if (filePostfix.equals(".m4a")) {
				type = "audio/mp4a-latm";
			} else if (filePostfix.equals(".m4b")) {
				type = "audio/mp4a-latm";
			} else if (filePostfix.equals(".m4p")) {
				type = "audio/mp4a-latm";
			} else if (filePostfix.equals(".mp2")) {
				type = "audio/x-mpeg";
			} else if (filePostfix.equals(".mp3")) {
				type = "audio/x-mpeg";
			} else if (filePostfix.equals(".mpga")) {
				type = "audio/mpeg";
			} else if (filePostfix.equals(".ogg")) {
				type = "audio/ogg";
			} else if (filePostfix.equals(".wav")) {
				type = "audio/x-wav";
			} else if (filePostfix.equals(".wma")) {
				type = "audio/x-ms-wma";
			} else if (filePostfix.equals(".wmv")) {
				type = "audio/x-ms-wmv";
			} else if (filePostfix.equals(".rmvb")) {
				type = "audio/x-pn-realaudio";
			} else {
				type = "*/*";
			}
		} else if (applicationType.indexOf(filePostfix) != -1) {
			if (filePostfix.equals(".apk")) {
				type = "application/vnd.android.package-archive";
			} else if (filePostfix.equals(".bin")) {
				type = "application/octet-stream";
			} else if (filePostfix.equals(".class")) {
				type = "application/octet-stream";
			} else if (filePostfix.equals(".doc")) {
				type = "application/msword";
			} else if (filePostfix.equals(".exe")) {
				type = "application/octet-stream";
			} else if (filePostfix.equals(".gstar")) {
				type = "application/x-gtar";
			} else if (filePostfix.equals(".gz")) {
				type = "application/x-gzip";
			} else if (filePostfix.equals(".jar")) {
				type = "application/java-archive";
			} else if (filePostfix.equals(".js")) {
				type = "application/x-javascript";
			} else if (filePostfix.equals(".mpc")) {
				type = "application/vnd.mpohun.certificate";
			} else if (filePostfix.equals(".msg")) {
				type = "application/vnd.ms-outlook";
			} else if (filePostfix.equals(".pdf")) {
				type = "application/pdf";
			} else if (filePostfix.equals(".ppt")) {
				type = "application/vnd.ms-powerpoint";
			} else if (filePostfix.equals(".pps")) {
				type = "application/vnd.ms-powerpoint";
			} else if (filePostfix.equals(".rar")) {
				type = "application/x-rar-compressed";
			} else if (filePostfix.equals(".rtf")) {
				type = "application/rtf";
			} else if (filePostfix.equals(".tar")) {
				type = "application/x-tar";
			} else if (filePostfix.equals(".tgz")) {
				type = "application/x-compressed";
			} else if (filePostfix.equals(".wps")) {
				type = "application/vnd.ms-works";
			} else if (filePostfix.equals(".z")) {
				type = "application/x-compress";
			} else if (filePostfix.equals(".zip")) {
				type = "application/zip";
			} else {
				type = "*/*";
			}
		} else if (imageType.indexOf(filePostfix) != -1) {
			if (filePostfix.equals(".bmp")) {
				type = "image/bmp";
			} else if (filePostfix.equals(".gif")) {
				type = "image/gif";
			} else if (filePostfix.equals(".imag")) {
				type = "image/*";
			} else if (filePostfix.equals(".jpeg")) {
				type = "image/jpeg";
			} else if (filePostfix.equals(".jpg")) {
				type = "image/jpeg";
			} else if (filePostfix.equals(".png")) {
				type = "image/png";
			} else {
				type = "*/*";
			}
		} else if (textType.indexOf(filePostfix) != -1) {
			if (filePostfix.equals(".c")) {
				type = "text/plain";
			} else if (filePostfix.equals(".conf")) {
				type = "text/plain";
			} else if (filePostfix.equals(".cpp")) {
				type = "text/plain";
			} else if (filePostfix.equals(".h")) {
				type = "text/plain";
			} else if (filePostfix.equals(".htm")) {
				type = "text/html";
			} else if (filePostfix.equals(".html")) {
				type = "text/html";
			} else if (filePostfix.equals(".java")) {
				type = "text/plain";
			} else if (filePostfix.equals(".log")) {
				type = "text/plain";
			} else if (filePostfix.equals(".prop")) {
				type = "text/plain";
			} else if (filePostfix.equals(".rc")) {
				type = "text/plain";
			} else if (filePostfix.equals(".sh")) {
				type = "text/plain";
			} else if (filePostfix.equals(".txt")) {
				type = "text/plain";
			} else if (filePostfix.equals(".xml")) {
				type = "text/plain";
			} else {
				type = "text/*";
			}
		} else {
			type = "*/*";
		}
		return type;
	}

	public static String getFileTypeForItemImage(String postfix) {
		String videoType = ".3gp.asf.avi.m4u.m4v.mov.mp4.mpe.mpeg.mpg.mpg4.swf.flv.rmvb";
		String applicationType = ".doc.pdf.ppt.rar.zip.docx.xlsx.pptx.xls";
		String imageType = ".bmp.gif.jpeg.jpg.png.imag.psd";
		String textType = ".c.conf.cpp.h.htm.html.java.log.prop.rc.sh.txt.xml.chm.mth.wps";
		String audioType = ".m3u.m4a.m4b.m4p.mp2.mp3.mpga.ogg.wav.wma.wmv.aac.mid.amr";
		if (videoType.indexOf(postfix) != -1) {
			return "video";
		} else if (applicationType.indexOf(postfix) != -1) {
			return "application";
		} else if (postfix.equals(".apk")) {
			return "apk";
		} else if (imageType.indexOf(postfix) != -1) {
			return "image";
		} else if (textType.indexOf(postfix) != -1) {
			return "text";
		} else if (audioType.indexOf(postfix) != -1) {
			return "audio";
		}
		return "other";
	}

	/**
	 * 加载文件弹出的进度条
	 * 
	 * @param progressDialog
	 * @param title
	 * @param msg
	 */

	public static void showProgressDailg(ProgressDialog progressDialog,
			String title, String msg) {
		// 实例化
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置进度条风格，风格为圆形，旋转的
		progressDialog.setTitle(title);
		// 设置ProgressDialog 标题
		progressDialog.setMessage(msg);
		progressDialog.setIndeterminate(false);
		// 设置ProgressDialog 的进度条是否不明确
		progressDialog.setCancelable(false);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}
	
	/**
	 * 取消进度条
	 */
	public static void cancleProgressDialog(ProgressDialog progressDialog) {
		if (null != progressDialog && progressDialog.isShowing()) {
			try {
				progressDialog.dismiss();
			} catch (Exception e) {

			}

		}
	}

	public static void setApkNameAndIcon(Context context, ApkFileInfo fileInfo) {
		String PATH_PackageParser = "android.content.pm.PackageParser";
		String PATH_AssetManager = "android.content.res.AssetManager";
		String apkPath = fileInfo.filePath;
		try {
			// apk包的文件路径
			// 这是一个Package 解释器, 是隐藏的
			// 构造函数的参数只有一个, apk文件的路径
			Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
			Class[] typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			Object pkgParser = pkgParserCt.newInstance(valueArgs);
			// 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();

			typeArgs = new Class[4];
			typeArgs[0] = File.class;
			typeArgs[1] = String.class;
			typeArgs[2] = DisplayMetrics.class;
			typeArgs[3] = Integer.TYPE;
			Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
					"parsePackage", typeArgs);
			valueArgs = new Object[4];
			valueArgs[0] = new File(apkPath);
			valueArgs[1] = apkPath;
			valueArgs[2] = metrics;
			valueArgs[3] = 0;
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
					valueArgs);
			// 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
			Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"applicationInfo");
			ApplicationInfo info = (ApplicationInfo) appInfoFld
					.get(pkgParserPkg);
			// uid 输出为"-1"，原因是未安装，系统未分配其Uid。

			Class<?> assetMagCls = Class.forName(PATH_AssetManager);
			Constructor<?> assetMagCt = assetMagCls
					.getConstructor((Class[]) null);
			Object assetMag = assetMagCt.newInstance((Object[]) null);
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(
					"addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
			Resources res = context.getResources();
			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = res.getDisplayMetrics().getClass();
			typeArgs[2] = res.getConfiguration().getClass();
			Constructor<Resources> resCt = Resources.class
					.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = res.getDisplayMetrics();
			valueArgs[2] = res.getConfiguration();

			res = resCt.newInstance(valueArgs);

			// set app name
			if (info.labelRes != 0) {
				fileInfo.fileName = (String) res.getText(info.labelRes)
						+ ".apk";// 锟斤拷锟斤拷
			}

			// set app icon
			if (info.icon != 0) {
				fileInfo.icon = res.getDrawable(info.icon);
			} else {
				fileInfo.icon = context.getResources().getDrawable(
						R.drawable.cloud_icon);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
