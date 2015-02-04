package com.compoment.login;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;



public class UserLoginParser extends DefaultHandler {
	
	/*
	 * 
<userinfo><id></id><imsinum></imsinum><phone></phone></userinfo>
	 */
	
	private StringBuffer accumulator;
	private UserBean userRegister;
	
	public UserLoginParser(InputStream input) throws Exception {
    	SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(input, this);
	}

	@Override
	public void characters(char buffer[], int start, int length) {
		accumulator.append(buffer, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if(localName.equals("id")) {
			userRegister.setId(accumulator.toString());
		} else if(localName.equals("imsinum")) {
			userRegister.setImsi(accumulator.toString());
		} else if(localName.equals("phone")) {
			userRegister.setPhone(accumulator.toString());
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		accumulator = new StringBuffer();
		userRegister = new UserBean();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		accumulator.setLength(0);
		
	}
	
	public void warning(SAXParseException exception) {
		
	}

	public void error(SAXParseException exception) {
		
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		throw exception;
	}

	public UserBean getUserRegister() {
		return userRegister;
	}

}

