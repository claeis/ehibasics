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
 * @version $Revision: 1.2 $ $Date: 2007-12-12 09:52:02 $
 */
public class FileListener extends AbstractFilteringListener {
	private PrintWriter out=null; 
	private File file=null;
	private boolean doTimeStamp=false;
	public void outputMsgLine(int arg0, int arg1, String msg) {
		if(out==null){
			try{
				out=new PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(file)));
			}catch(java.io.IOException ex){
				StdListener.getInstance().logEvent(new StdLogEvent(LogEvent.ERROR,null,ex,null));
			}
		}
		if(out!=null){
			if(msg.endsWith("\n")){
				out.print(msg);
			}else{
				out.println(msg);
			}
			out.flush();
		}
	}
	public String getTimestamp(){
		if(!doTimeStamp){
			return null;
		}
		java.text.DateFormat dfm = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
		dfm.setTimeZone(java.util.TimeZone.getTimeZone("GMT+0"));
		// Date a = parseDfm.parse("2007-10-24 20:15:00.00");
		String stmp=dfm.format(new java.util.Date())+"Z"; // add timesone indicator (Z)
		return stmp;
	}
	/** Creates a new FileListener, doing output to the given file.
	 */
	public FileListener(File file1){
		file=file1;
	}
	/** Creates a new FileListener, adding timestamps to each output-line.
	 */
	public FileListener(File file1,boolean doTimeStamp1){
		file=file1;
		doTimeStamp=doTimeStamp1;
	}
	/** Closes the underlying output stream.
	 */
	public void close()
	{
		if(out!=null){
			out.close();
			out=null;
		}
	}
}
