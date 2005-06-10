package ch.ehi.basics.i18n;
/*
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
import java.net.URL;
/**
 * Alternative ResourceBundle.
 * @author ce
 * @version $Revision: 1.3 $ $Date: 2005-06-10 16:07:21 $
 */
public class ResourceBundle {
	private final static String RESOURCES_DIRECTORY = "resources";
  private ResourceBundle() {
  }
  /** Get the appropriate ResourceBundle.
   * uses transformName to get the right bundle name.
   */
  public static java.util.ResourceBundle getBundle(Class baseClass){
    return java.util.ResourceBundle.getBundle(transformName(baseClass.getName()));
  }
  /** 
   * Get the appropriate ResourceBundle for a given Locale.
   * Uses transformName() to get the right bundle name.
   */
  public static java.util.ResourceBundle getBundle(java.lang.Class baseClass, java.util.Locale locale) {
    return java.util.ResourceBundle.getBundle(transformName(baseClass.getName()), locale);
  }
  /** 
   * Get the appropriate ResourceBundle for a given Locale and ClassLoader.
   * Uses transformName() to get the right bundle name.
   */
  public static java.util.ResourceBundle getBundle(java.lang.Class baseClass, java.util.Locale locale, ClassLoader loader) {
    return java.util.ResourceBundle.getBundle(transformName(baseClass.getName()), locale, loader);
  }
/**
 * Return an ImageIcon.
 * @return ImageIcon
 * @see #getURL()
 */
public static javax.swing.ImageIcon getImageIcon(Class aClass, String imageFileName) {
	return new javax.swing.ImageIcon(getURL(aClass, imageFileName));
}
/**
 * Return URL of given filename in a Package named "resources" relative to
 * the given Class' path.
 * @param aClass
 * @param fileName
 * @return URL
 */
public static URL getURL(Class aClass, String fileName) {
	String className = aClass.getName();
	int index = className.lastIndexOf('.');
	String file = className.substring(0, index).replace('.', '/') + "/" + RESOURCES_DIRECTORY + "/" + fileName;	
	
	// var I) from IDE with relative FileSystem or compiled within Jar
	java.net.URL url = aClass.getResource("/" + file);

	// var II) from JAR with External FileSystem
	//	java.net.URL url = getClass().getResource(file);

	return url;
}
  /** transform a class name into a corresponding resource bundle name.
   * insert 'resources' into the given fully qualified classname.
   * @param 'ch.ehi.Text'
   * @returns 'ch.ehi.resources.Text'
   */
  public static String transformName(String className) {
    StringBuffer resourceName = new StringBuffer(className);
    resourceName.insert(className.lastIndexOf('.'), "." + RESOURCES_DIRECTORY);
    return resourceName.toString();
  }
  /** transform a class into a corresponding package path.
   * @param 'ch.ehi.Text'
   * @returns '/ch/ehi'
   */
  public static String class2packagePath(Class aClass){
	String className = aClass.getName();
	int index = className.lastIndexOf('.');
	String file = "/"+className.substring(0, index).replace('.', '/');	
	return file;
  }
}
