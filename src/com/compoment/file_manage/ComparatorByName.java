package com.compoment.file_manage;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;


/**
 * ��collection.sort()���õ�comparator
 * @author zigang
 *
 */
public class ComparatorByName implements Comparator<ApkFileInfo>{
	
	boolean isAscend;
	public ComparatorByName(boolean isAscend){
		this.isAscend = isAscend;
	}
	public int compare(ApkFileInfo f1, ApkFileInfo f2) {
		
		if(isAscend == true){
			return Collator.getInstance(Locale.CHINESE).compare(f1.fileName, f2.fileName);
		} else {
			return Collator.getInstance(Locale.CHINESE).compare(f2.fileName, f1.fileName); 
		}
		
	}

}
