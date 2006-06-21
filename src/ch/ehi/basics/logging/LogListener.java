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

/** The interface that each listener to EhiLogger should implement.
 * @author ce
 * @version $Revision: 1.2 $ $Date: 2006-06-21 13:44:35 $
 * @see EhiLogger
 */
public interface LogListener {
	/** logs a logging event. called by EhiLogger.
	 * @param event the event to log.
	 */
	public void logEvent(LogEvent event);
}
