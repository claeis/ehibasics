package ch.ehi.basics.io;

import java.io.IOException;

/** Stream utilities
 * 
 */
public class StreamUtility {
    /** do not instantiate
    *
    */
   private StreamUtility(){}

   public static void copyStream(java.io.File outFile, java.io.InputStream in) 
           throws IOException 
   {
       java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outFile));
       byte[] bt = new byte[1024];
       int i;
       while ((i = in.read(bt)) != -1) {
           out.write(bt, 0, i);
       }
       out.close();
   }

}
