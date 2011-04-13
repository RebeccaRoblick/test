package net.sf.exlp.parser.impl;

import java.util.List;
import java.util.regex.Matcher;

import net.sf.exlp.event.LogEventHandler;
import net.sf.exlp.parser.AbstractLogParser;
import net.sf.exlp.parser.LogParser;
import net.sf.exlp.util.xml.JDomUtil;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;

public class XmlParser extends AbstractLogParser implements LogParser  
{
	static Log logger = LogFactory.getLog(XmlParser.class);

	public XmlParser(LogEventHandler leh)
	{
		super(leh);
	}
	
	@Override
	public void parseItem(List<String> item)
	{
		
		StringBuffer sb = new StringBuffer();
		for(String s : item)
		{
			sb.append(s+SystemUtils.LINE_SEPARATOR);
		}
		Document doc;
		JDomUtil jd;
	}
	
	public void event(Matcher m)
	{
		logger.debug(m.group(0));
	}
}