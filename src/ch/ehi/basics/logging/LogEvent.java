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
 * @author ce
 * @version $Revision: 1.1 $ $Date: 2005-02-08 11:55:13 $
 */
public interface LogEvent {
	/**	temporary messages to track down bugs. Should be removed 
	 *  when the bug is fixed.
	 */
	static final int DEBUG_TRACE=1;
	/**	log normal system state (normally not shown to user)
	 */
	static final int STATE_TRACE=2;
	/**	log expected but unusual system state (normally not shown to user)
	 */
	static final int UNUSUAL_STATE_TRACE=3;
	/**	log a command to a backend system (e.g. a SQL statement)
	 */
	static final int BACKEND_CMD=4;
	/**	inform user about current system state (e.g. progress information)
	 */
	static final int STATE=5;
	/**	inform user about adaptions take by the code (e.g. ignoring some supefluous input)
	 */
	static final int ADAPTION=6;
	/**	errors (program errors or input errors)
	 */
	static final int ERROR=7;
	static final int FIRST_KIND=DEBUG_TRACE;
	static final int LAST_KIND=ERROR;
	/** gets the kind of this event.
	 * @return kind of event.
	 */
	public int getEventKind();
	/** gets the message to log of this event.
	 * @return message to log. Should not return null or empty message.
	 */
	public String getEventMsg();
	/** gets the exception attached to this event.
	 * @return the attached exception or null if there is no exception attached.
	 */
	public Throwable getException();
	/** gets the source of this event.
	 * @return source of this event or null. (e.g. MyClass.mash(MyClass.java:9))
	 */
	public StackTraceElement getOrigin();
}
