package ch.ehi.basics.settings;

import java.util.ArrayList;
import java.util.HashMap;

/** This class represents a set of properties (name-value pairs). 
 * Some of the properties are persistent, others may be transient. 
 * Persistent properties are of type String, transient properties may be of any type.
 * The persistent properties can be saved to a stream or loaded from a stream.
 * Default value handling is left to the apllication.
 * This class differs from java.util.Properties in that it includes transient properties and excludes default value handling. 
 * This class differs from java.util.prefs in that it's focus is not storeing or retrieving settings. The provided load() and 
 * store() operations are just convenience. 
 * 
 * @author ceis
 *
 */
public class Settings {
	private HashMap<String,String> values=new HashMap<String,String>();
	private ArrayList<String> orderedKeys=null;
	private HashMap<String,Object> transientValues=new HashMap<String,Object>();
	public Settings()
	{
		this(false);
	}
	public Settings(boolean keepOrderOfDefs)
	{
		if(keepOrderOfDefs){
			orderedKeys=new ArrayList<String>();
		}
	}
	public Settings(Settings src)
	{
		if(src!=null){
			java.util.Iterator<String> it=src.getValuesIterator();
			while(it.hasNext()){
				String name=it.next();
				String obj=src.values.get(name);
				setValue(name,obj);
			}
			it=transientValues.keySet().iterator();
			while(it.hasNext()){
				String name=(String)it.next();
				Object obj=src.transientValues.get(name);
				transientValues.put(name,obj);
			}
		}
		
	}
	/** gets a property value.
	 * @param name of property.
	 * @return value or null. Never returns an empty String.
	 */
	public String getValue(String name) {
		String value=(String)values.get(name);
		return ch.ehi.basics.tools.StringUtility.purge(value);
	}
	public void setValue(String name,String value) {
		value=ch.ehi.basics.tools.StringUtility.purge(value);
		if(value==null){
			if(orderedKeys!=null && orderedKeys.contains(name)){
				orderedKeys.remove(name);
			}
			values.remove(name);
		}else{
			if(orderedKeys!=null && !orderedKeys.contains(name)){
				orderedKeys.add(name);
			}
			values.put(name, value);
		}
	}
	/** gets a transient property value.
	 * @param name of property.
	 * @return value or null. Never returns an empty String.
	 */
	public Object getTransientObject(String name) {
		Object value=transientValues.get(name);
		if(value!=null && value instanceof String){
			return ch.ehi.basics.tools.StringUtility.purge((String)value);
		}
		return value;
	}
	public void setTransientObject(String name,Object value) {
		if(value!=null && value instanceof String){
			value=ch.ehi.basics.tools.StringUtility.purge((String)value);
		}
		if(value==null){
			transientValues.remove(name);
		}else{
			transientValues.put(name, value);
		}
	}
	/** gets a transient property value.
	 * @param name of property.
	 * @return value or null. Never returns an empty String.
	 */
	public String getTransientValue(String name) {
		return (String)getTransientObject(name);
	}
	public void setTransientValue(String name,String value) {
		setTransientObject(name, value);
	}
	public void store(java.io.File out,String header)
	throws java.io.IOException
	{
		java.io.OutputStream stream=null;
		try{
			stream=new java.io.BufferedOutputStream(new java.io.FileOutputStream(out));
			store(stream, header);
		}finally{
			if(stream!=null){
				stream.close();
				stream=null;
			}
		}
	}
	public void store(java.io.OutputStream out,String header)
	throws java.io.IOException
	{
		java.util.Properties prop=new java.util.Properties();
		prop.putAll(values);
		prop.store(out, header);
	}
	public void load(java.io.File inFile)
	throws java.io.IOException
	{
		java.io.InputStream stream=null;
		try{
			stream=new java.io.BufferedInputStream(new java.io.FileInputStream(inFile));
			load(stream);
		}finally{
			if(stream!=null){
				stream.close();
				stream=null;
			}
		}
	}
	public void load(java.io.InputStream inStream)
	throws java.io.IOException
	{
		java.util.Properties prop=new java.util.Properties();
		prop.load(inStream);
		for(String key:prop.stringPropertyNames()){
			setValue(key,prop.getProperty(key));
		}
	}
	/** get the list of property names.
	 * @return set<String valueName>
	 */
	public java.util.Set<String> getValues(){
		return values.keySet();
	}
	public java.util.Iterator<String> getValuesIterator(){
		if(orderedKeys!=null){
			return orderedKeys.iterator();
		}
		return values.keySet().iterator();
	}
	/** get the list of transient property names.
	 * @return set<String valueName>
	 */
	public java.util.Set<String> getTransientValues(){
		return transientValues.keySet();
	}
	public String toString()
	{
		StringBuffer ret=new StringBuffer();
		java.util.Iterator<String> it=getValuesIterator();
		java.util.Iterator it2=transientValues.keySet().iterator();
		if(it.hasNext() || it2.hasNext()){
			String sep="";
			ret.append("Settings{");
			while(it.hasNext()){
				String name=it.next();
				Object obj=values.get(name);
				ret.append(sep+name+"="+obj.toString());
				sep=";";
			}
			while(it2.hasNext()){
				String name=(String)it2.next();
				Object obj=transientValues.get(name);
				ret.append(sep+name+"="+obj.toString());
				sep=";";
			}
			ret.append("}");
		}
		return ret.toString();
	}
}