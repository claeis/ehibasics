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
 * @version $Revision: 1.3 $ $Date: 2006-06-21 12:57:59 $
 */
public class StdLogEvent implements LogEvent {
	private int kind=0;
	private String msg=null;
	private Throwable ex=null;
	private StackTraceElement origin=null;
	private int customLevel=LEVEL_UNDEFINED;
	public StdLogEvent(int kind,String msg,Throwable ex,StackTraceElement origin,int customLevel){
		this.kind=kind;
		this.msg=msg;
		this.ex=ex;
		this.origin=origin;
		this.customLevel=customLevel;
	}
	public StdLogEvent(int kind,String msg,Throwable ex,StackTraceElement origin){
		this.kind=kind;
		this.msg=msg;
		this.ex=ex;
		this.origin=origin;
	}
	public int getEventKind(){
		return kind;
	}
	public String getEventMsg(){
		return msg;
	}
	public Throwable getException(){
		return ex;
	}
	public StackTraceElement getOrigin(){
		return origin;
	}
	public int getCustomLevel()
	{
		return customLevel;
	}
}
