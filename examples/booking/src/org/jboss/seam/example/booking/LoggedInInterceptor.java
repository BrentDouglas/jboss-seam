//$Id$
package org.jboss.seam.example.booking;

import javax.ejb.InvocationContext;

import org.jboss.logging.Logger;
import org.jboss.seam.annotations.Around;
import org.jboss.seam.annotations.Within;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.interceptors.AbstractInterceptor;
import org.jboss.seam.interceptors.BijectionInterceptor;
import org.jboss.seam.interceptors.ConversationInterceptor;
import org.jboss.seam.interceptors.RemoveInterceptor;
import org.jboss.seam.interceptors.ValidationInterceptor;

@Around({BijectionInterceptor.class, ValidationInterceptor.class, ConversationInterceptor.class})
@Within(RemoveInterceptor.class)
public class LoggedInInterceptor extends AbstractInterceptor<LoggedIn>
{
   private static final Logger log = Logger.getLogger(LoggedInInterceptor.class);

   @Override
   public Object aroundInvoke(InvocationContext invocation) throws Exception
   {
      boolean isLoggedIn = Contexts.getSessionContext().get("loggedIn")!=null;
      if (isLoggedIn) 
      {
         log.info("User is already logged in");
         return invocation.proceed();
      }
      else 
      {
         log.info("User is not logged in");
         return "login";
      }
   }

}
