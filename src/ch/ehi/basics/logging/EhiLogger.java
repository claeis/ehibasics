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
 * Initially all events are written to System.err. To remove 
 * this output, remove the StdListener after you added your own listener by
 * <code>EhiLogger.getInstance().removeListener(ch.ehi.basics.logging.StdListener.getInstance());</code> 
 * @author ce
 * @version $Revision: 1.9 $ $Date: 2007-12-12 09:52:02 $
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
	 *  The filtering is done on the kind of event. 
	 *  DEBUG_TRACE, STATE_TRACE, UNUSUAL_STATE_TRACE, BACKEND_CMD
	 *  are considered as trace events.
	 *  The indent of this filtering is to reduce calls 
	 *  to listeners. A user oriented filtering should be 
	 *  implemented in listeners.
	 * 	@see LogEvent#DEBUG_TRACE
	 *	@see LogEvent#STATE_TRACE
	 *	@see LogEvent#UNUSUAL_STATE_TRACE
	 *	@see LogEvent#BACKEND_CMD
	 *
	 */
	public void setTraceFilter(boolean enableFilter){
		filterTrace=enableFilter;
	}
	/** gets the current filtering state.
	 */
	public boolean getTraceFilter(){
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
	/**	temporarly log a message to track down bugs. Calls to this function 
	 * should be removed when the bug is fixed.
	 */
	static public void debug(String msg,int level){
		getInstance().logEvent(new StdLogEvent(LogEvent.DEBUG_TRACE,msg,null,getOrigin(),level));
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
	/**	temporarly log a stack trace to track down bugs. Calls to this function 
	 * should be removed when the bug is fixed.
	 */
	static public void debugStackTrace(int level){
		Throwable tr=new Throwable();
		StackTraceElement stack[]=tr.getStackTrace();
		for(int i=1;i<stack.length;i++){
			debug(stack[i].toString(),level); 
		}
	}
	/** log normal system state (normally not shown to user)
	 */
	static public void traceState(String state){
		getInstance().logEvent(new StdLogEvent(LogEvent.STATE_TRACE,state,null,getOrigin()));
	}
	/** log normal system state (normally not shown to user)
	 */
	static public void traceState(String state,int level){
		getInstance().logEvent(new StdLogEvent(LogEvent.STATE_TRACE,state,null,getOrigin(),level));
	}
	/** log expected but unusual system state (normally not shown to user)
	 */
	static public void traceUnusualState(String state){
		getInstance().logEvent(new StdLogEvent(LogEvent.UNUSUAL_STATE_TRACE,state,null,getOrigin()));
	}
	/** log expected but unusual system state (normally not shown to user)
	 */
	static public void traceUnusualState(String state,int level){
		getInstance().logEvent(new StdLogEvent(LogEvent.UNUSUAL_STATE_TRACE,state,null,getOrigin(),level));
	}
	/**	log a command to a backend system (e.g. a SQL statement)
	 */
	static public void traceBackendCmd(String cmd){
		getInstance().logEvent(new StdLogEvent(LogEvent.BACKEND_CMD,cmd,null,getOrigin()));
	}
	/**	log a command to a backend system (e.g. a SQL statement)
	 */
	static public void traceBackendCmd(String cmd,int level){
		getInstance().logEvent(new StdLogEvent(LogEvent.BACKEND_CMD,cmd,null,getOrigin(),level));
	}
	/** inform user about current system state (e.g. progress information)
	 */
	static public void logState(String state){
		getInstance().logEvent(new StdLogEvent(LogEvent.STATE,state,null,getOrigin()));
	}
	/** inform user about current system state (e.g. progress information)
	 */
	static public void logState(String state,int level){
		getInstance().logEvent(new StdLogEvent(LogEvent.STATE,state,null,getOrigin(),level));
	}
	/** inform user about adaptions taken by the code (e.g. ignoring some supefluous input)
	 */ 
	static public void logAdaption(String adaption){
		getInstance().logEvent(new StdLogEvent(LogEvent.ADAPTION,adaption,null,getOrigin()));
	}
	/** inform user about adaptions taken by the code (e.g. ignoring some supefluous input)
	 */ 
	static public void logAdaption(String adaption,int level){
		getInstance().logEvent(new StdLogEvent(LogEvent.ADAPTION,adaption,null,getOrigin(),level));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(String errmsg){
		getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,errmsg,null,getOrigin()));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(String errmsg,int level){
		getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,errmsg,null,getOrigin(),level));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(String errmsg,Throwable ex){
		getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,errmsg,ex,getOrigin()));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(String errmsg,Throwable ex,int level){
		getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,errmsg,ex,getOrigin(),level));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(Throwable ex){
		getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,null,ex,getOrigin()));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(Throwable ex,int level){
		getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,null,ex,getOrigin(),level));
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
