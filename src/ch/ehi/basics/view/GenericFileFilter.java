package ch.ehi.basics.view;

import java.io.File;
import javax.swing.filechooser.*;

/**
 *    JFileChooser fc = new JFileChooser();
 *    fc.addChoosableFileFilter(new GenericFileFilter("INTERLIS models (*.ili)","ili"));
 */
public class GenericFileFilter extends FileFilter {
	private static java.util.ResourceBundle resources = ch.ehi.basics.i18n.ResourceBundle.getBundle(GenericFileFilter.class);
    private String description;
    private String extension;
    public GenericFileFilter(String description,String extension){
      this.description=description;
      this.extension=extension;
    }
    /** Accept all directories and all files with given extension
     *
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String ext = getFileExtension(f);
	if (ext != null) {
            if (ext.equalsIgnoreCase(extension)) {
                    return true;
            } else {
                return false;
            }
    	}

        return false;
    }
/**
 * @return specific File-Filter.
 */
public static GenericFileFilter createCsvFilter() {
	return new GenericFileFilter(resources.getString("CICsvFilter"), "csv");//$NON-NLS-2$ //$NON-NLS-1$
}
/**
 * @return specific File-Filter.
 */
public static GenericFileFilter createHtmlFilter() {
	return new GenericFileFilter(resources.getString("CIHtmlFilter"), "html");//$NON-NLS-2$ //$NON-NLS-1$
}
/**
 * @return specific File-Filter.
 */
public static GenericFileFilter createSqlFilter() {
	return new GenericFileFilter(resources.getString("CISqlFilter"), "sql");//$NON-NLS-2$ //$NON-NLS-1$
}
/**
 * @return specific File-Filter.
 */
public static GenericFileFilter createXmlFilter() {
	return new GenericFileFilter(resources.getString("CIXmlFilter"), "xml");//$NON-NLS-2$ //$NON-NLS-1$
}
/**
 * @return specific File-Filter.
 */
public static GenericFileFilter createXmlSchemaFilter() {
	return new GenericFileFilter(resources.getString("CIXsdFilter"), "xsd");
}
    /** The description of this filter
     *
     */
    public String getDescription() {
        return description;
    }
    /** The file extension used by this filter
     *
     */
    public String getExtension() {
        return extension;
    }
    /** Get the extension of a file.
     *
     */
    public static String getFileExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i >= 0 &&  i < s.length()) {
            ext = s.substring(i+1);
        }
        return ext;
    }
}
