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

/** Default logging listener that just logs to System.err.
 * @author ce
 * @version $Revision: 1.7 $ $Date: 2007-12-12 09:52:02 $
 */
public class StdListener extends AbstractFilteringListener {
	static private StdListener instance=null; 
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
	public void outputMsgLine(int kind,int level,String msg) {
			if(msg.endsWith("\n")){
				System.err.print(msg);
			}else{
				System.err.println(msg);
			}
	}
}
