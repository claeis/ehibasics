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

/** Single point of access to the logging system. 
 * It is a usage error, to have no registered listeners. 
 * @author ce
 * @version $Revision: 1.6 $ $Date: 2006-06-21 13:13:30 $
 */
public class EhiLogger {
	static private EhiLogger instance=null; 
	private java.util.Set logListenerv = new java.util.HashSet();
	/** adds a Listener.
	 */
	public void addListener(LogListener logListener)
	{
		if(logListener==null){
		  // ignore usage error
		  return;
		}
	  getInstance().logListenerv.add(logListener);
	  return;
	}
	/** removes a Listener.
	 */
	public void removeListener(LogListener logListener)
	{
		// null or unknown logListener?
		if(logListener==null || !getInstance().logListenerv.contains(logListener)){
		  // ignore usage error
		  return;
		}
		getInstance().logListenerv.remove(logListener);
		if(getInstance().logListenerv.size()==0){
			StdListener.getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,"no logging listeners left",null,getOrigin()));
		}
	  return;
	}
	/** get unique instance
	 */
	public static EhiLogger getInstance()
	  {
	  if(instance==null){
		instance=new EhiLogger();
		instance.addListener(StdListener.getInstance());
	  }
	  return instance;
	}
	private EhiLogger(){
	}
	private boolean filterTrace=true;
	/** enables/disables filtering of trace events.
	 */
	public void setTraceFiler(boolean enableFilter){
		filterTrace=enableFilter;
	}
	/** gets the current filtering state.
	 */
	public boolean getTraceFiler(){
		return filterTrace;
	}
	/** dispatch any log event.
	 */
	public void logEvent(LogEvent event){
		// filter event
		int kind=event.getEventKind();
		if(getInstance().filterTrace){
			if(kind==LogEvent.DEBUG_TRACE
				|| kind==LogEvent.STATE_TRACE
				|| kind==LogEvent.UNUSUAL_STATE_TRACE
				|| kind==LogEvent.BACKEND_CMD
				){
				return;		
			}
		}
		if(kind<LogEvent.FIRST_KIND || kind>LogEvent.LAST_KIND){
			StdListener.getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,"illegal kind",null,getOrigin()));
			return;
		}
		String msg=event.getEventMsg();
		if((msg==null || msg.trim().length()==0) && event.getException()==null){
			StdListener.getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,"null or empty log-message",null,getOrigin()));
			return;
		}
		// notify event to all registered listeners
		java.util.Iterator it=getInstance().logListenerv.iterator();
		while(it.hasNext()){
		  LogListener listener=(LogListener)it.next();
		  try{
			listener.logEvent(event);
		  }catch(Throwable ex){
		  	StdListener.getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,null,ex,getOrigin()));
		  }
		}
	}
	/**	temporarly log a message to track down bugs. Calls to this function 
	 * should be removed when the bug is fixed.
	 */
	static public void debug(String msg){
		getInstance().logEvent(new StdLogEvent(LogEvent.DEBUG_TRACE,msg,null,getOrigin()));
	}
	/**	temporarly log a stack trace to track down bugs. Calls to this function 
	 * should be removed when the bug is fixed.
	 */
	static public void debugStackTrace(){
		Throwable tr=new Throwable();
		StackTraceElement stack[]=tr.getStackTrace();
		for(int i=1;i<stack.length;i++){
			debug(stack[i].toString()); 
		}
	}
	/** log normal system state (normally not shown to user)
	 */
	static public void traceState(String state){
		getInstance().logEvent(new StdLogEvent(LogEvent.STATE_TRACE,state,null,getOrigin()));
	}
	/** log expected but unusual system state (normally not shown to user)
	 */
	static public void traceUnusualState(String state){
		getInstance().logEvent(new StdLogEvent(LogEvent.UNUSUAL_STATE_TRACE,state,null,getOrigin()));
	}
	/**	log a command to a backend system (e.g. a SQL statement)
	 */
	static public void traceBackendCmd(String cmd){
		getInstance().logEvent(new StdLogEvent(LogEvent.BACKEND_CMD,cmd,null,getOrigin()));
	}
	/** inform user about current system state (e.g. progress information)
	 */
	static public void logState(String state){
		getInstance().logEvent(new StdLogEvent(LogEvent.STATE,state,null,getOrigin()));
	}
	/** inform user about adaptions taken by the code (e.g. ignoring some supefluous input)
	 */ 
	static public void logAdaption(String adaption){
		getInstance().logEvent(new StdLogEvent(LogEvent.ADAPTION,adaption,null,getOrigin()));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(String errmsg){
		getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,errmsg,null,getOrigin()));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(String errmsg,Throwable ex){
		getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,errmsg,ex,getOrigin()));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(Throwable ex){
		getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,null,ex,getOrigin()));
	}
	/** gets the origin of a call to logError() functions.
	 */
	static private StackTraceElement getOrigin(){
		Throwable tr=new Throwable();
		StackTraceElement stack[]=tr.getStackTrace();
		// stack[0]: getOrigin()
		// stack[1]: logError()
		// stack[2]: user code
		if(2<stack.length){
			StackTraceElement st=stack[2]; 
			return st;
		}
		return null;
	}
	/** converts an event kind to a String.
	 */
	static public String kindToString(int eventKind)
	{
		switch(eventKind){
			case LogEvent.DEBUG_TRACE:
				return "DEBUG_TRACE";
			case LogEvent.STATE_TRACE:
				return "STATE_TRACE";
			case LogEvent.UNUSUAL_STATE_TRACE:
				return "UNUSUAL_STATE_TRACE";
			case LogEvent.BACKEND_CMD:
				return "BACKEND_CMD";
			case LogEvent.STATE:
				return "STATE";
			case LogEvent.ADAPTION:
				return "ADAPTION";
			case LogEvent.ERROR:
				return "ERROR";
			default:
				throw new IllegalArgumentException("illegal event kind");
		}
	}
}
