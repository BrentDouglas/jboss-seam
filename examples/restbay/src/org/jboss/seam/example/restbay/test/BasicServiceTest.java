package org.jboss.seam.example.restbay.test;

import org.jboss.seam.example.restbay.test.fwk.ResourceSeamTest;
import org.jboss.seam.example.restbay.test.fwk.MockHttpServletResponse;
import org.jboss.seam.example.restbay.test.fwk.MockHttpServletRequest;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static org.testng.Assert.assertEquals;

import javax.servlet.http.Cookie;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.HashMap;

public class BasicServiceTest extends ResourceSeamTest
{

   @Override
   public Map<String, Object> getDefaultHeaders()
   {
      return new HashMap<String, Object>()
      {{
            put("Accept", "text/plain");
      }};
   }

   @DataProvider(name = "queryPaths")
   public Object[][] getData()
   {
      return new String[][] {
            { "/restv1/plainTest" },
            { "/restv1/eventComponentTest" },
            { "/restv1/statelessEjbTest" }
      };
   }

   @Test(dataProvider = "queryPaths")
   public void testExeptionMapping(final String resourcePath) throws Exception
   {
      new ResourceRequest(Method.GET, resourcePath + "/trigger/unsupported")
      {

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 501;
            assert response.getStatusMessage().equals("The request operation is not supported: foo");
         }

      }.run();

   }

   @Test(dataProvider = "queryPaths")
   public void testEchos(final String resourcePath) throws Exception
   {
      new ResourceRequest(Method.GET, resourcePath + "/echouri")
      {

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 200;
            assert response.getContentAsString().endsWith("/echouri");
         }

      }.run();

      new ResourceRequest(Method.GET, resourcePath + "/echoquery")
      {

         @Override
         protected void prepareRequest(MockHttpServletRequest request)
         {
            request.setQueryString("asdf=123");
            request.addQueryParameter("bar", "bbb");
            request.addQueryParameter("baz", "bzzz");
         }

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 200;
            assert response.getContentAsString().equals("bbb");
         }

      }.run();

      new ResourceRequest(Method.GET, resourcePath + "/echoheader")
      {

         @Override
         protected void prepareRequest(MockHttpServletRequest request)
         {
            request.addHeader("bar", "baz");
         }

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 200;
            assert response.getContentAsString().equals("baz");
         }

      }.run();

      new ResourceRequest(Method.GET, resourcePath + "/echocookie")
      {

         @Override
         protected void prepareRequest(MockHttpServletRequest request)
         {
            request.addCookie(new Cookie("bar", "baz"));
         }

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 200;
            assert response.getContentAsString().equals("baz");
         }

      }.run();

      new ResourceRequest(Method.GET, resourcePath + "/foo/bar/asdf")
      {

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {

            assert response.getStatus() == 200;
            assert response.getContentAsString().equals("bar: asdf");
         }

      }.run();

      new ResourceRequest(Method.GET, resourcePath + "/echotwoparams/foo/bar")
      {

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 200;
            assert response.getContentAsString().equals("foobar");
         }

      }.run();

   }

   @Test(dataProvider = "queryPaths")
   public void testEncoding(final String resourcePath) throws Exception
   {
      new ResourceRequest(Method.GET, resourcePath + "/echoencoded/foo bar")
      {

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 200;
            assert response.getContentAsString().equals("foo%20bar");
         }

      }.run();
   }

   @Test(dataProvider = "queryPaths")
   public void testFormHandling(final String resourcePath) throws Exception
   {
      new ResourceRequest(Method.POST, resourcePath + "/echoformparams")
      {

         @Override
         protected void prepareRequest(MockHttpServletRequest request)
         {
            request.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            request.addParameter("foo", new String[]{"bar", "baz"});
         }

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 200;
            assert response.getContentAsString().equals("barbaz");
         }

      }.run();

      new ResourceRequest(Method.POST, resourcePath + "/echoformparams2")
      {

         @Override
         protected void prepareRequest(MockHttpServletRequest request)
         {
            request.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            request.addParameter("foo", new String[]{"bar", "baz"});
         }

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 200;
            assert response.getContentAsString().equals("barbaz");
         }

      }.run();

      new ResourceRequest(Method.POST, resourcePath + "/echoformparams3")
      {

         @Override
         protected void prepareRequest(MockHttpServletRequest request)
         {
            request.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            request.addHeader("bar", "foo");
            request.addParameter("foo", new String[]{"bar", "baz"});
         }

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 200;
            assert response.getContentAsString().equals("foobarbaz");
         }

      }.run();

   }

   @Test(dataProvider = "queryPaths")
   public void testStringConverter(final String resourcePath) throws Exception
   {
      final String ISO_DATE = "2007-07-10T14:54:56-0500";
      final String ISO_DATE_MILLIS = "1184097296000";

      new ResourceRequest(Method.GET, resourcePath + "/convertDate/" + ISO_DATE)
      {

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 200;
            assertEquals(response.getContentAsString(), ISO_DATE_MILLIS);
         }

      }.run();

   }

   @Test(dataProvider = "queryPaths")
   public void testProvider(final String resourcePath) throws Exception
   {

      new ResourceRequest(Method.GET, resourcePath + "/commaSeparated")
      {

         @Override
         protected void prepareRequest(MockHttpServletRequest request)
         {
            request.addHeader("Accept", "text/csv");
         }

         @Override
         protected void onResponse(MockHttpServletResponse response)
         {
            assert response.getStatus() == 200;
            assert response.getContentAsString().equals("foo,bar\r\nasdf,123\r\n");
         }

      }.run();

   }
}