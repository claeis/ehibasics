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

/**
 * log normal system state (normally not shown to user)
 * log expected but unusual system state (normally not shown to user)
 * inform user about current system state (e.g. progress information)
 * inform user about adaptions take by the code (e.g. ignoring some supefluous input) 
 * errors (program errors or input errors)
 * keep references (no output in release/production code)
 * @author ce
 * @version $Revision: 1.1 $ $Date: 2005-02-08 11:55:13 $
 */
public class EhiLogger {
	static private EhiLogger instance=null; 
	private java.util.Set logListenerv = new java.util.HashSet();
	/** adds a Listener.
	 */
	public void addListener(LogListener logListener)
	{
	  getInstance().logListenerv.add(logListener);
	  return;
	}
	/** removes a Listener.
	 */
	public void removeListener(LogListener logListener)
	{
		if(logListener==null || !getInstance().logListenerv.contains(logListener)){
		  throw new java.lang.IllegalArgumentException("cannot remove null or unknown logListener");
		}
		getInstance().logListenerv.remove(logListener);
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
	/** dispatch any log event.
	 */
	static public void logEvent(LogEvent event){
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
			throw new java.lang.IllegalArgumentException("illegal kind");
		}
		String msg=event.getEventMsg();
		if(msg==null || msg.trim().length()==0){
			throw new java.lang.IllegalArgumentException("null or empty log-message");
		}
		// notify event to all registered listeners
		java.util.Iterator it=getInstance().logListenerv.iterator();
		while(it.hasNext()){
		  LogListener listener=(LogListener)it.next();
		  listener.logEvent(event);
		}
	}
	/**	log a temporary message to track down bugs. Should be removed 
	 *  when the bug is fixed.
	 */
	static public void debug(String msg){
		logEvent(new StdLogEvent(LogEvent.DEBUG_TRACE,msg,null,getOrigin()));
	}
	/** log normal system state (normally not shown to user)
	 */
	static public void traceState(String state){
		logEvent(new StdLogEvent(LogEvent.STATE_TRACE,state,null,getOrigin()));
	}
	/** log expected but unusual system state (normally not shown to user)
	 */
	static public void traceUnusualState(String state){
		logEvent(new StdLogEvent(LogEvent.UNUSUAL_STATE_TRACE,state,null,getOrigin()));
	}
	/**	log a command to a backend system (e.g. a SQL statement)
	 */
	static public void traceBackendCmd(String cmd){
		logEvent(new StdLogEvent(LogEvent.BACKEND_CMD,cmd,null,getOrigin()));
	}
	/** inform user about current system state (e.g. progress information)
	 */
	static public void logState(String state){
		logEvent(new StdLogEvent(LogEvent.STATE,state,null,getOrigin()));
	}
	/** inform user about adaptions taken by the code (e.g. ignoring some supefluous input)
	 */ 
	static public void logAdaption(String adaption){
		logEvent(new StdLogEvent(LogEvent.ADAPTION,adaption,null,getOrigin()));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(String errmsg){
		logEvent(new StdLogEvent(LogEvent.ERROR,errmsg,null,getOrigin()));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(String errmsg,Throwable ex){
		logEvent(new StdLogEvent(LogEvent.ERROR,errmsg,ex,getOrigin()));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(Throwable ex){
		String msg=ex.getLocalizedMessage();
		if(msg==null){
			msg=ex.getClass().getName();
		}
		logEvent(new StdLogEvent(LogEvent.ERROR,msg,ex,getOrigin()));
	}
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
}
