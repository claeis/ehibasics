package ch.ehi.basics.logging;

/** A skeleton filtering logging listener.
* To implement a listener, the programmer needs only to extend this 
* class and provide an implementation for the outputMsg() method.
*/
public abstract class AbstractFilteringListener extends AbstractStdListener {

	public boolean skipEvent(LogEvent event)
	{
		int kind=event.getEventKind();
		if(kind==LogEvent.DEBUG_TRACE && skipDebugTrace){
			return true;
		}
		if(kind==LogEvent.STATE_TRACE && skipStateTrace){
			return true;
		}
		if(kind==LogEvent.UNUSUAL_STATE_TRACE && skipUnusualStateTrace){
			return true;
		}
		if(kind==LogEvent.BACKEND_CMD && skipBackendCmd){
			return true;
		}
		if(kind==LogEvent.STATE_TRACE && skipStateTrace){
			return true;
		}
		if(kind==LogEvent.ADAPTION && skipAdaption){
			return true;
		}
		return false;
	}
	private boolean skipDebugTrace=false;
	private boolean skipStateTrace=false;
	private boolean skipUnusualStateTrace=false;
	private boolean skipBackendCmd=false;
	private boolean skipState=false;
	private boolean skipAdaption=false;
	/** Returns true if ADAPTION events are suppressed.
	 * @see LogEvent#ADAPTION
	 */
	public boolean isSkipAdaption() {
		return skipAdaption;
	}
	/** Enables/disables filtering of ADAPTION events.
	 * @see LogEvent#ADAPTION
	 */
	public void skipAdaption(boolean skipAdaption) {
		this.skipAdaption = skipAdaption;
	}
	/** Returns true if BACKEND_CMD events are suppressed.
	 * @see LogEvent#BACKEND_CMD
	 */
	public boolean isSkipBackendCmd() {
		return skipBackendCmd;
	}
	/** Enables/disables filtering of BACKEND_CMD events.
	 * @see LogEvent#BACKEND_CMD
	 */
	public void skipBackendCmd(boolean skipBackendCmd) {
		this.skipBackendCmd = skipBackendCmd;
	}
	/** Returns true if DEBUG_TRACE events are suppressed.
	 * @see LogEvent#DEBUG_TRACE
	 */
	public boolean isSkipDebugTrace() {
		return skipDebugTrace;
	}
	/** Enables/disables filtering of DEBUG_TRACE events.
	 * @see LogEvent#DEBUG_TRACE
	 */
	public void skipDebugTrace(boolean skipDebugTrace) {
		this.skipDebugTrace = skipDebugTrace;
	}
	/** Returns true if STATE events are suppressed.
	 * @see LogEvent#STATE
	 */
	public boolean isSkipState() {
		return skipState;
	}
	/** Enables/disables filtering of STATE events.
	 * @see LogEvent#STATE
	 */
	public void skipState(boolean skipState) {
		this.skipState = skipState;
	}
	/** Returns true if STATE_TRACE events are suppressed.
	 * @see LogEvent#STATE_TRACE
	 */
	public boolean isSkipStateTrace() {
		return skipStateTrace;
	}
	/** Enables/disables filtering of STATE_TRACE events.
	 * @see LogEvent#STATE_TRACE
	 */
	public void skipStateTrace(boolean skipStateTrace) {
		this.skipStateTrace = skipStateTrace;
	}
	/** Returns true if UNUSUAL_STATE_TRACE events are suppressed.
	 * @see LogEvent#UNUSUAL_STATE_TRACE
	 */
	public boolean isSkipUnusualStateTrace() {
		return skipUnusualStateTrace;
	}
	/** Enables/disables filtering of UNUSUAL_STATE_TRACE events.
	 * @see LogEvent#UNUSUAL_STATE_TRACE
	 */
	public void skipUnusualStateTrace(boolean skipUnusualStateTrace) {
		this.skipUnusualStateTrace = skipUnusualStateTrace;
	}
	/** shortcut to skip all events except errors.
	 */
	public void skipInfo(boolean skipInfo) {
		skipDebugTrace=skipInfo;
		skipStateTrace=skipInfo;
		skipUnusualStateTrace=skipInfo;
		skipBackendCmd=skipInfo;
		skipState=skipInfo;
		skipAdaption=skipInfo;
	}

}
