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
import ch.ehi.basics.logging.*;
import java.io.PrintWriter;
import java.io.File;

/** A logging listener that just logs to a file.
 * @author ce
 * @version $Revision: 1.1 $ $Date: 2007-03-08 10:56:08 $
 */
public class FileListener implements LogListener {
	private PrintWriter out=null; 
	private File file=null;
	private boolean doTimeStamp=false;
	public void logEvent(LogEvent event){
		String stmp="";
		if(doTimeStamp){
			stmp=Long.toString(System.currentTimeMillis())+": ";
		}
		ArrayList msgv=StdListener.formatOutput(event,true,!EhiLogger.getInstance().getTraceFiler());
		Iterator msgi=msgv.iterator();
		while(msgi.hasNext()){
			if(out==null){
				try{
					out=new PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(file)));
				}catch(java.io.IOException ex){
					StdListener.getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,null,ex,null));
				}
			}
			String msg=(String)msgi.next();
			if(msg.endsWith("\n")){
				out.print(stmp+msg);
			}else{
				out.println(stmp+msg);
			}
		}
		if(out!=null){
			out.flush();
		}
	}
	public FileListener(File file1){
		file=file1;
	}
	public FileListener(File file1,boolean doTimeStamp1){
		file=file1;
		doTimeStamp=doTimeStamp1;
	}
	public void close()
	{
		if(out!=null){
			out.close();
			out=null;
		}
	}
}
