package ch.ehi.basics.tools;

/** String utilities
 *
 */
public class StringUtility {
  /** do not instantiate
   *
   */
  private StringUtility(){}

  /** Returns a string of the given length, consisting of a repetition of the specified filler character.
   *
   */
  public static String STRING(int length,char c)
  {
    StringBuffer buf=new StringBuffer(length);
    for(int i=0;i<length;i++){
      buf.append(c);
    }
    return buf.toString();
  }
	/** removes leading and trailing white space and returns null if nothing left.
	 */
	public static String purge(String value){
		if(value==null)return null;
		String ret=value.trim();
		if(ret.length()==0){
			return null;
		}
		return ret;
	}
}