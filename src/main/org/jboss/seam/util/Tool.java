/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.seam.util;


/**
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @version $Revision$
 */
public class Tool
{
   public static String filename2FQN(String filename)
   {
      String result = filename.substring(0, filename.lastIndexOf(".class"));
      return result.replace('/', '.').replace('\\', '.');
   }
   
   public static boolean isEmptyOrNull(String string)
   {
      return (string == null || string.trim().length() == 0); 
   }
}


