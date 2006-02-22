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

/** A logging listener that just logs to System.err.
 * @author ce
 * @version $Revision: 1.3 $ $Date: 2006-02-22 10:04:27 $
 */
public class StdListener implements LogListener {
	static private StdListener instance=null; 
	public void logEvent(LogEvent event){
		ArrayList msgv=formatOutput(event,true,!EhiLogger.getInstance().getTraceFiler());
		Iterator msgi=msgv.iterator();
		while(msgi.hasNext()){
			String msg=(String)msgi.next();
			if(msg.endsWith("\n")){
				System.err.print(msg);
			}else{
				System.err.println(msg);
			}
		}
	}
	/** get unique instance
	 */
	public static StdListener getInstance()
	  {
	  if(instance==null){
		instance=new StdListener();
	  }
	  return instance;
	}
	private StdListener(){
	}
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

}
