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
public class StdLogEvent implements LogEvent {
	private int kind=0;
	private String msg=null;
	private Throwable ex=null;
	private StackTraceElement origin=null;
	public StdLogEvent(int kind,String msg,Throwable ex,StackTraceElement origin){
		this.kind=kind;
		if(msg==null){
			throw new IllegalArgumentException("error message shouldn't be null");
		}
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
}
