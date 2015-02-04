
package com.compoment.contact;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.compoment.contact.HanziToPinyin.Token;



import android.util.Log;



public class ContactBean {
	/* ��ϵ��id */
	public String contactId="" ;
	/* ��ϵ����� */
	public String name="";
	
	/* ��ϵ�˺��� */
	public String number="";
	
	public String pingyin="";
	
	public int photoId=0;
	
	public StringBuilder sbSimple=new StringBuilder();
	

	

	 
	 	public  String chinese2pinyin(String input) {
	 		ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
	 		StringBuilder sb = new StringBuilder();
	 		
	 		if (tokens != null && tokens.size() > 0) {
	 			for (Token token : tokens) {
	 				if (Token.PINYIN == token.type) {
	 					sb.append(token.target+" "+token.source+" ");
	 					if(token.target.length()>0)
	 					{
	 					sbSimple.append(token.target.substring(0, 1));
	 					}
	 				} else {
	 					sb.append(token.source+" ");
	 					if(token.source.length()>0)
	 					{
	 					sbSimple.append(token.source);
	 					}
	 				
	 				}
	 			}
	 		}
	 	
	 		return sb.toString().toLowerCase();
	 	}
	 

	    public int hashCode()
	     {
	         return name.hashCode();
	     }
	     
	     @Override
	     public boolean equals(Object st)
	     {
	    	 ContactBean tempStudent= (ContactBean) st;
	         if (name==tempStudent.name) return true;
	         else return false;
	     } 

	 
}
