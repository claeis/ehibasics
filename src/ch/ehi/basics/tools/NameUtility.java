package ch.ehi.basics.tools;

public class NameUtility {
    private NameUtility() {};
    
    public static String shortcutName(String aname, int maxlen) {
        StringBuffer name = new StringBuffer(aname);
        // number of characters to remove
        int stripc = name.length() - maxlen;
        if (stripc <= 0)
            return aname;
        // remove vocals
        for (int i = name.length() - 4; i >= 3; i--) {
            char c = name.charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'A' || c == 'E' || c == 'I' || c == 'O'
                    || c == 'U') {
                name.deleteCharAt(i);
                stripc--;
                if (stripc == 0)
                    return name.toString();
            }
        }
        // still to long
        // remove from the middle of the name
        int start = (name.length() - stripc) / 2;
        name.delete(start, start + stripc);
        // ASSERT(!name.IsEmpty());
        return name.toString();
    }

    public static String shortcutName(String modelName, String attrName, int maxSqlNameLength) {
        StringBuffer ret = new StringBuffer();
        String modelSqlName = modelName;
        String attrSqlName = attrName;
        int maxClassNameLength = maxSqlNameLength - 5;
        ret.append(shortcutName(modelSqlName, maxClassNameLength / 2 - 2));
        ret.append("_");
        ret.append(shortcutName(attrSqlName, maxSqlNameLength - ret.length()));
        return ret.toString();
    }

}
