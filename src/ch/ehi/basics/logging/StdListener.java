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

/** A logging listener that just logs to System.err.
 * @author ce
 * @version $Revision: 1.1 $ $Date: 2005-02-08 11:55:13 $
 */
public class StdListener implements LogListener {
	static private StdListener instance=null; 
	public void logEvent(LogEvent event){
		int kind=event.getEventKind();
		if(kind==LogEvent.DEBUG_TRACE
			|| kind==LogEvent.STATE_TRACE
			|| kind==LogEvent.UNUSUAL_STATE_TRACE
			|| kind==LogEvent.BACKEND_CMD
			){
			StackTraceElement origin=event.getOrigin();
			if(origin!=null){
				System.err.println(fmtOriginMsg(origin,event.getEventMsg()));
				return;		
			}
		}

		System.err.println(event.getEventMsg());
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

}
