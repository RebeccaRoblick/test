package net.sf.exlp.util.net.jms.listener;

import java.util.Enumeration;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextMessageRespondDebugListener implements MessageListener
{
	final static Logger logger = LoggerFactory.getLogger(TextMessageRespondDebugListener.class);
	
	private QueueSession session;
	private Random rnd;

	public TextMessageRespondDebugListener(InitialContext ctx)
	{	
		try
		{
			rnd = new Random();
			QueueConnectionFactory fact;
			fact = (QueueConnectionFactory)ctx.lookup( "ConnectionFactory" );
			QueueConnection connect = fact.createQueueConnection();
			session = connect.createQueueSession( false, Session.AUTO_ACKNOWLEDGE );
		}
		catch (NamingException e) {logger.error("",e);}
		catch (JMSException e) {logger.error("",e);}
	}
	
	public void stop()
	{
		try
		{
			session.close();
		}
		catch (JMSException e) {logger.error("",e);}
	}
	
	@SuppressWarnings("unchecked")
	public void onMessage(Message msg)
	{
		try
		{
			TextMessage tm = (TextMessage)msg;
			logger.debug("New Message: "+tm.getText());
			
			StringBuffer sb = new StringBuffer();
			sb.append("JMSMessageID="+msg.getJMSMessageID());
			sb.append(" Properties: ");
			Enumeration<String> enu = msg.getPropertyNames();
			while(enu.hasMoreElements())
			{
				String prop=(String)enu.nextElement();
				sb.append(" "+prop+"="+msg.getStringProperty(prop));
			}
			logger.debug("\t"+sb.toString());
			TextMessage answer = session.createTextMessage();
			answer.setText(tm.getText()+" "+rnd.nextInt(100));
			
			try {Thread.sleep(1000*rnd.nextInt(15));}
			catch (InterruptedException e) {logger.error("",e);}
			
			Queue queueReply = (Queue)msg.getJMSReplyTo();
			QueueSender qSender = session.createSender(queueReply);
			qSender.send(answer);
		}
		catch (JMSException e) {e.printStackTrace();}	
	}
}