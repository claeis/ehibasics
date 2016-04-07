package ch.ehi.basics.view;

import java.io.File;

import javax.swing.filechooser.*;

/** Example usage:
 *    JFileChooser fc = new JFileChooser();
 *    fc.addChoosableFileFilter(new GenericFileFilter("INTERLIS models (*.ili)","ili"));
 */
public class GenericFileFilter extends FileFilter implements java.io.FileFilter {
	private static java.util.ResourceBundle resources = ch.ehi.basics.i18n.ResourceBundle.getBundle(GenericFileFilter.class);
    private String description;
    private String extension;
    public GenericFileFilter(String description,String extension){
      this.description=description;
      this.extension=extension;
    }
    /** Accept all directories, with or without extension, and all files with given extension
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
 * Comma Separated Values (ASCII).
 * @return specific File-Filter.
 */
public static GenericFileFilter createCsvFilter() {
	return new GenericFileFilter(resources.getString("CICsvFilter"), "csv");//$NON-NLS-2$ //$NON-NLS-1$
}
/**
 * MS Excel Spreadsheet (binary).
 * @return specific File-Filter.
 */
public static GenericFileFilter createXlsFilter() {
    return new GenericFileFilter("MS Excel"/*resources.getString("CICsvFilter")*/, "xls");//$NON-NLS-2$ //$NON-NLS-1$
}
/**
 * HTML (ASCII).
 * @return specific File-Filter.
 */
public static GenericFileFilter createHtmlFilter() {
	return new GenericFileFilter(resources.getString("CIHtmlFilter"), "html");//$NON-NLS-2$ //$NON-NLS-1$
}
/**
 * SQL (ASCII).
 * @return specific File-Filter.
 */
public static GenericFileFilter createSqlFilter() {
	return new GenericFileFilter(resources.getString("CISqlFilter"), "sql");//$NON-NLS-2$ //$NON-NLS-1$
}
/**
 * XML (ASCII).
 * @return specific File-Filter.
 */
public static GenericFileFilter createXmlFilter() {
	return new GenericFileFilter(resources.getString("CIXmlFilter"), "xml");//$NON-NLS-2$ //$NON-NLS-1$
}
/**
 * XSD (ASCII).
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
	/** Get the extension of a file name.
	 * @return e.g. "xml"
	 */
	public static String getFileExtension(String s) {
		String ext = null;
		int i = s.lastIndexOf('.');

		if (i >= 0 &&  i < s.length()) {
			ext = s.substring(i+1);
		}
		return ext;
	}
	/** Get the extension of a file.
     * @return e.g. "xml"
     */
    public static String getFileExtension(File f) {
        String s = f.getName();
        return getFileExtension(s);
    }
	/** Get the filename without extension.
	 * @return e.g. "export"
	 */
	public static String stripFileExtension(String s) {
		String name = s;
		int i = s.lastIndexOf('.');

		if (i >= 0 &&  i < s.length()) {
			name = s.substring(0,i);
		}
		return name;
	}
	/** Get the filename without extension.
	 * @return e.g. "export"
	 */
	public static String stripFileExtension(File f) {
		String s = f.getName();
		return stripFileExtension(s);
	}
}
