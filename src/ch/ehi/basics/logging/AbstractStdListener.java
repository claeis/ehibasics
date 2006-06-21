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
 * @author ce
 * @version $Revision: 1.2 $ $Date: 2006-06-21 13:00:18 $
 */
public abstract class AbstractStdListener implements LogListener {
	private boolean errors=false;
	public void logEvent(LogEvent event){
		if(event.getEventKind()==LogEvent.ERROR){
			errors=true;
		}
		ArrayList msgv=formatOutput(event,true,!EhiLogger.getInstance().getTraceFiler());
		Iterator msgi=msgv.iterator();
		while(msgi.hasNext()){
			String msg=(String)msgi.next();
			outputMsgLine(event.getEventKind(),event.getCustomLevel(),msg);
		}
	}
	/** print one line of a logging event.
	 */
	abstract public void outputMsgLine(int eventKind,int eventCustomLevel,String msg);
	/** helper, to decorate a message with origin information
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
	/** helper, to format an event into list of output messages.
	 * @param event
	 * @param doOrigin add origin information to trace messages
	 * @param doStacktrace add the stacktrace to exceptions
	 * @return list of output messages
	 */
	public static ArrayList formatOutput(LogEvent event,boolean doOrigin,boolean doStacktrace){
		ArrayList out=new ArrayList();
		String msg=event.getEventMsg();
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
	/** clear flag of seen errors.
	 */
	public void clearErrors(){
		errors=false;
	}

}
