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

import javax.swing.JTextArea;

/** A logging listener that logs to a JTextArea.
 * @author ce
 * @version $Revision: 1.2 $ $Date: 2007-12-12 09:52:02 $
 */
public class TextAreaListener extends ch.ehi.basics.logging.AbstractFilteringListener {
	public void outputMsgLine(int arg0, int arg1, String msg) {
		if(errOutput!=null){
			if(msg.endsWith("\n")){
				errOutput.append(msg);
			}else{
				errOutput.append(msg+"\n");
			}
		}
	}
	private JTextArea  errOutput=null;
	/** Defines the output GUI control.
	 */
	public void setOutputArea(JTextArea  err)
	{
		errOutput=err;
	}
}
