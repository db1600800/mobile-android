package com.compoment.downloading_breakpoint_continue.db;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.compoment.downloading_breakpoint_continue.util.CalendarDealWith;


public class MessageReceivedParser extends DefaultHandler {
	private static final String ITEM_NAME = "message"; // 一条消息元素名
	private static String elementName = new String(""); // 正在遍历的元素名

	private List<MessageReceived> messageReceivedList = new ArrayList<MessageReceived>();
	private MessageReceived messageReceived;

	public MessageReceivedParser(InputStream input) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(input, this);
	}

	public void characters(char buffer[], int start, int length) {
		// 取得元素的内容，放进messageReceived中
		StringBuffer accumulator = new StringBuffer();
		accumulator.append(buffer, start, length);
		String content = accumulator.toString();

		Log.e(this.getClass().getCanonicalName(), "<" + elementName + ">" + content + "</"
				+ elementName + ">");

		// XML元素 --> JavaBean属性
		if ("id".equals(elementName)) {
			messageReceived.setId(content);
		} else if ("file_len".equals(elementName)) {
			messageReceived.setFileLength(Integer.valueOf(content));
		} else if ("type".equals(elementName)) {
			messageReceived.setType(content);
		} else if ("duration".equals(elementName)) {
			messageReceived.setDuration(Integer.valueOf(content));
		} else if ("prev_img_url".equals(elementName)) {
			messageReceived.setPreviewImageUrl(content);
		} else if ("url".equals(elementName)) {
			messageReceived.setUrl(content);
		} else if ("is_readed".equals(elementName)) {
			// 网络传输使用"t"和"f"，本地存储使用"true"和"false"
			if ("t".equals(content)) {
				messageReceived.setReaded(true);
			} else if ("f".equals(content)) {
				messageReceived.setReaded(false);
			} else {
				messageReceived.setReaded(new Boolean(content));
			}
		} else if ("snder_num".equals(elementName)) {
			messageReceived.setSenderPhoneNumber(content);
		} else if ("snd_time".equals(elementName)) {
			// 解析"2011-11-02 09:10:00" -> Calendar
			Calendar calendar = CalendarDealWith.parseStringToCalendar(content);
			messageReceived.setSendTime(calendar);
		} else if ("path".equals(elementName)) {
			messageReceived.setPath(content);
		} else if ("prev_img_path".equals(elementName)) {
			messageReceived.setPreviewImagePath(content);
		} else if ("is_download_finished".equals(elementName)) {
			messageReceived.setDownloadFinished(Boolean.valueOf(content));
		}
		elementName = "";
	}

	public void endDocument() throws SAXException {
		super.endDocument();
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (qName.equals(ITEM_NAME)) {
			messageReceivedList.add(messageReceived);
		}
	}

	public void startDocument() throws SAXException {
		super.startDocument();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		elementName = qName;

		if (qName.equals(ITEM_NAME)) {
			messageReceived = new MessageReceived();
		}
	}

	public List<MessageReceived> getMessageReceivedList() {
		return messageReceivedList;
	}
}
