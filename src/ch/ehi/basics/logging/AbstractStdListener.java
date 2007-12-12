/* This file is part of the ehibasics project.
 * For more information, please see <http://www.umleditor.org>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package ch.ehi.basics.logging;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;

/** A skeleton logging listener.
 * To implement a listener, the programmer needs only to extend this 
 * class and provide an implementation for the outputMsg() method.
 * A second extension point is getMessageTag()/getTimestamp().
 * A third extension point is skipEvent().
 * @author ce
 * @version $Revision: 1.4 $ $Date: 2007-12-12 09:52:02 $
 */
public abstract class AbstractStdListener implements LogListener {
	private boolean errors=false;
	public void logEvent(LogEvent event){
		// is event an error?
		if(event.getEventKind()==LogEvent.ERROR){
			errors=true;
		}
		// suppress event?
		if(skipEvent(event)){
			return;
		}
		// convert event to a list of lines
		ArrayList msgv=formatOutput(event,true,!EhiLogger.getInstance().getTraceFilter());
		// do output of lines
		outputEvent(event,msgv);
	}
	/** Extension point to print the list of lines of an event.
	 * @param event Logging event.
	 * @param msgv list of lines (String) without tag or timestamp.
	 */
	public void outputEvent(LogEvent event,ArrayList msgv)
	{
		// get event tag
		String msgTag=getMessageTag(event);
		if(msgTag==null){
			msgTag="";
		}else{
			msgTag=msgTag+": ";
		}
		
		// get timetstamp
		String msgTimestamp=getTimestamp();
		if(msgTimestamp==null){
			msgTimestamp="";
		}else{
			msgTimestamp=msgTimestamp+": ";
		}
		
		// output all lines
		Iterator msgi=msgv.iterator();
		while(msgi.hasNext()){
			String msg=(String)msgi.next();
			outputMsgLine(event.getEventKind(),event.getCustomLevel(),msgTimestamp+msgTag+msg);
		}
	}
	/** Extension point to filter events.
	 * @return true if the event should be suppressed. This listener always returns false.
	 */
	public boolean skipEvent(LogEvent event)
	{
		return false;
	}
	/** Extension point to print one line of a logging event.
	 */
	abstract public void outputMsgLine(int eventKind,int eventCustomLevel,String msg);
	
	/** Extension point to customize the tagging of a log-event in the output.
	 * @return tag or null. This listener returns null for errors and "Info" for all others.
	 */
	public String getMessageTag(LogEvent event){
		return event.getEventKind()==LogEvent.ERROR ? null : "Info";
	}
	/** Extension point to customize the timestamp of a log-event in the output.
	 * @return timestamp or null. This listener always returns null.
	 */
	public String getTimestamp(){
		return null;
	}

	/** Helper, to decorate a message with origin information
	 * @param st origin
	 * @param msg message
	 * @return decorated message
	 */
	public static String fmtOriginMsg(StackTraceElement st,String msg){
		StringBuffer ret=new StringBuffer();
		ret.append(st.getMethodName());
		ret.append("(): ");
		ret.append(msg);
		ret.append(" (");
		String filename=st.getFileName();
		if(filename!=null){
			ret.append(filename);
			int line=st.getLineNumber();
			if(line>=0){
				ret.append(":");
				ret.append(Integer.toString(line));
			}
		}else{
			ret.append(st.getClassName());
		}
		ret.append(")");
		return ret.toString();
		
	}
	/** Helper, to format an event into a list of output messages.
	 * @param event
	 * @param doOrigin add origin information to trace messages
	 * @param doStacktrace add the stacktrace to exceptions
	 * @return list of output messages
	 */
	public static ArrayList formatOutput(LogEvent event,boolean doOrigin,boolean doStacktrace){
		ArrayList out=new ArrayList();
		String msg=event.getEventMsg();
		if(msg!=null){
			msg=msg.trim();
			if(msg.length()==0){
				msg=null;
			}
		}
		String sep="";
		if(msg!=null){
			if(doOrigin){
				int kind=event.getEventKind();
				if(kind==LogEvent.DEBUG_TRACE
					|| kind==LogEvent.STATE_TRACE
					|| kind==LogEvent.UNUSUAL_STATE_TRACE
					|| kind==LogEvent.BACKEND_CMD
					){
					StackTraceElement origin=event.getOrigin();
					if(origin!=null){
						msg=fmtOriginMsg(origin,msg);
					}
				}
			}
			out.add(msg);
			sep="  ";
		}
		Throwable ex=event.getException();
		if(ex!=null){
			logThrowable(out,sep,ex,doStacktrace);
		}
		return out;
	}
	private static void logThrowable(ArrayList out,String ind,Throwable ex,boolean doStacktrace){
		String msg=ex.getLocalizedMessage();
		if(msg!=null){
			msg=msg.trim();
			if(msg.length()==0){
				msg=null;
			}
		}
		if(msg==null){
			msg=ex.getClass().getName();
		}
		out.add(ind+msg);
		if(doStacktrace){
			StackTraceElement[] stackv=ex.getStackTrace();
			for(int i=0;i<stackv.length;i++){
				out.add(ind+"    "+stackv[i].toString());
			}
		}
		Throwable ex2=ex.getCause();
		if(ex2!=null){
			logThrowable(out,ind+"  ",ex2,doStacktrace);
		}
		if(ex instanceof java.sql.SQLException){
			java.sql.SQLException exTarget=(java.sql.SQLException)ex;
			java.sql.SQLException exTarget2=exTarget.getNextException();
			if(exTarget2!=null){
				logThrowable(out,ind+"  ",exTarget2,doStacktrace);
			}
		}
		if(ex instanceof InvocationTargetException){
			InvocationTargetException exTarget=(InvocationTargetException)ex;
			Throwable exTarget2=exTarget.getTargetException();
			if(exTarget2!=null){
				logThrowable(out,ind+"  ",exTarget2,doStacktrace);
			}
		}
	}
	/** have there been errors logged?
	 */
	public boolean hasSeenErrors(){
		return errors;
	}
	/** Clears flag of seen errors.
	 */
	public void clearErrors(){
		errors=false;
	}

}
