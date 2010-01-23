package net.sf.exlp.io.spawn;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import net.sf.exlp.io.arch.EnvironmentParameter;
import net.sf.exlp.parser.LogParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Spawn extends Thread
{
	static Log logger = LogFactory.getLog(Spawn.class);
	public final static String exitValueIdentifier = "Exit-Code:";
	
	private String command, charSet;
	private Process p;
	private Writer writer;
	private LogParser lp;
	private File workingDir;
	private EnvironmentParameter envParameter;
	
	private int exitValue;
	
	public Spawn(String command)
	{
		this.command=command;
	}
	
	private void checkPreRequisites()
	{
		checkWorkingDir();
		if(envParameter==null){envParameter = new EnvironmentParameter();}
	}
	
	private void checkWorkingDir()
	{
		if(workingDir==null)
		{
			workingDir = new File(".");
		}
		else
		{
			if(!workingDir.exists()){logger.warn(workingDir.getAbsoluteFile()+" does not exist!");}
			if(!workingDir.isDirectory()){logger.warn(workingDir.getAbsoluteFile()+" is no directory!");}
		}

	}
	
	public void cmd()
	{
		checkPreRequisites();
		try
		{	
			logger.debug("Spawn: "+command+" (wd="+workingDir.getAbsolutePath()+")");
						
			p = Runtime.getRuntime().exec(command, envParameter.get(), workingDir);
			logger.trace("Process started");
			
			InputStreamReader isrStdIn,isrStdErr;
			if(charSet==null)
			{
				isrStdIn  = new InputStreamReader(p.getInputStream());
				isrStdErr = new InputStreamReader(p.getErrorStream());
			}
			else
			{
				isrStdIn = new InputStreamReader(p.getInputStream(),charSet);
				isrStdErr = new InputStreamReader(p.getErrorStream(),charSet);
			}
			
			SpawnLineHandler slhIn=new SpawnLineHandler("I:",isrStdIn);
				slhIn.setWriter(writer);
				slhIn.setLp(lp);
			SpawnLineHandler slhErr=new SpawnLineHandler("E:",isrStdErr);
				slhErr.setWriter(writer);
				slhErr.setLp(lp);
				
			logger.trace("ThreadHandler will be started");
			slhIn.start();
			slhErr.start();
			logger.trace("Waiting for Process End");
			
			p.waitFor();
			exitValue=p.exitValue();
			String exitCode = exitValueIdentifier+exitValue;
			logger.trace("Process finished with "+exitCode);
		}
		catch (UnsupportedEncodingException uee){logger.error("UnsupportedEncodingException. charsets.jar in Path?",uee);}
		catch (IOException e) {logger.error("Fehler beim ausf�hren von: "+command,e);}
		catch (InterruptedException e) {logger.error(e);}
	}
	
	public void run()
	{
		cmd();
	}
	
	public void kill()
	{
		logger.debug("Kill wird ausgef�hrt");
		p.destroy();
	}
	
	public int getExitValue() {return exitValue;}
	public void setCommand(String command) {this.command = command;}
	public void setCharSet(String charSet) {this.charSet = charSet;}
	public void setWriter(Writer writer) {this.writer = writer;}
	public void setLp(LogParser lp) {this.lp = lp;}
	public void setWorkingDir(File workingDir) {this.workingDir = workingDir;}
	public void setEnvParameter(EnvironmentParameter envParameter) {this.envParameter = envParameter;}
}
