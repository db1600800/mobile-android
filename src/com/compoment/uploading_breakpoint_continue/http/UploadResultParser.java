package com.compoment.uploading_breakpoint_continue.http;

import java.io.InputStream;
import java.util.Calendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.compoment.uploading_breakpoint_continue.util.CalendarDealWith;

import android.util.Log;



public class UploadResultParser extends DefaultHandler {
	private static String elementName = new String(""); // 正在遍历的元素名

	private UploadResultBean result = new UploadResultBean();

	public UploadResultParser(InputStream input) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(input, this);
	}

	public void characters(char buffer[], int start, int length) {
		// 取得元素的内容，放进messageSent中
		StringBuffer accumulator = new StringBuffer();
		accumulator.append(buffer, start, length);
		String content = accumulator.toString();

		Log.e(this.getClass().getCanonicalName(), "<" + elementName + ">" + content
				+ "</" + elementName + ">");

		// XML元素 --> JavaBean属性
		if ("msg_id".equals(elementName)) {
			result.setId(content);
		} else if ("file_length".equals(elementName)) {
			result.setFileLength(Integer.valueOf(content));
		} else if ("rcv_length".equals(elementName)) {
			result.setReceivedLength(Integer.valueOf(content));
		} else if ("code".equals(elementName)) {
			result.setCode(content);
		} else if ("error".equals(elementName)) {
			result.setError(content);
		} else if ("snd_time".equals(elementName)) {
			// 解析"2011-11-02 09:10:00" -> Calendar
			Calendar calendar = CalendarDealWith.parseStringToCalendar(content);
			result.setSendDate(calendar);
		}
		elementName = "";
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementName = qName;
	}

	public UploadResultBean getMessageUploadResult() {
		return result;
	}
}
