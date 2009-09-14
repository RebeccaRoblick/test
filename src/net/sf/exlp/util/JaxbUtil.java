package net.sf.exlp.util;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.sf.exlp.io.resourceloader.MultiResourceLoader;

import org.apache.log4j.Logger;

public class JaxbUtil
{
	private static Logger logger = Logger.getLogger(JaxbUtil.class);
	
	public static synchronized Object loadJAXB(String xmlFile, Class<?> c)
	{
		MultiResourceLoader mrl = new MultiResourceLoader();
		Object result = null;
		try
		{
			JAXBContext jc = JAXBContext.newInstance(c);
			Unmarshaller u = jc.createUnmarshaller();
			result = u.unmarshal(mrl.searchIs(xmlFile));
		}
		catch (JAXBException e) {logger.error(e);}
		catch (FileNotFoundException e) {logger.error(e);}
		return result;
	}
}