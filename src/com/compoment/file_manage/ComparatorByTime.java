package com.compoment.file_manage;

import java.util.Comparator;


public class ComparatorByTime implements Comparator<ApkFileInfo>{
	
	
	boolean isAscend;
	public ComparatorByTime(boolean isAscend){
		this.isAscend = isAscend;
	}
	public int compare(ApkFileInfo f1, ApkFileInfo f2) {
		if(isAscend == false){
			return f2.lastModifiedTime.compareTo(f1.lastModifiedTime);	
		} else {
			return f1.lastModifiedTime.compareTo(f2.lastModifiedTime);	
		}
			
	}

}
