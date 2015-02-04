package com.compoment.file_manage;

import java.io.File;
import java.util.Comparator;




/**
 *
 * @author zigang
 *
 */
public class ComparatorBySize implements Comparator<ApkFileInfo>{
	boolean isAscend;
	public ComparatorBySize(boolean isAscend){
		this.isAscend = isAscend;
	}
	public int compare(ApkFileInfo f1, ApkFileInfo f2) {
		long temp =  new File(f2.filePath).length() - new File(f1.filePath).length();
		
		if(isAscend == false){
			if(temp > 0){
				return 1;
			}else if(temp == 0 ){
				return 0;
			}else{
				return -1;
			}
		}else{
			if(temp > 0){
				return -1;
			}else if(temp == 0 ){
				return 0;
			}else{
				return 1;
			}
		}
		
	}

}
