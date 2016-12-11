package ch.unibe.ese.team3.interceptors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.ese.team3.model.enums.PageMode;

public class UriInterceptorTest {
	@Test
	public void preHandleRent() throws Exception{
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/rent/results");
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		UriInterceptor interceptor = new UriInterceptor();
				
		assertTrue(interceptor.preHandle(request, response, new Object()));
		verify(request).setAttribute("pageMode", PageMode.RENT);
	}
	
	@Test
	public void preHandleBuy() throws Exception{
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/buy/results");
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		UriInterceptor interceptor = new UriInterceptor();
				
		assertTrue(interceptor.preHandle(request, response, new Object()));
		verify(request).setAttribute("pageMode", PageMode.BUY);
	}
	
	@Test
	public void preHandleRoot() throws Exception{
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/");
		when(request.getContextPath()).thenReturn("http://localhost:8080");
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		UriInterceptor interceptor = new UriInterceptor();
				
		assertFalse(interceptor.preHandle(request, response, new Object()));
		verify(response).sendRedirect("http://localhost:8080/buy/");
	}
	
	@Test
	public void preHandleNone() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/strange/thing");
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		
		UriInterceptor interceptor = new UriInterceptor();
				
		assertTrue(interceptor.preHandle(request, response, new Object()));
		verify(request, never()).setAttribute("pageMode", PageMode.BUY);
	}
	
	@Test
	public void postHandleWithModelAndView() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/buy/results");
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		ModelAndView model = mock(ModelAndView.class);
		
		UriInterceptor interceptor = new UriInterceptor();
				
		assertTrue(interceptor.preHandle(request, response, new Object()));
		verify(request).setAttribute("pageMode", PageMode.BUY);
		interceptor.postHandle(request, response, new Object(), model);
		verify(model).addObject("pagemode", PageMode.BUY.getParameter());
	}
	
	@Test
	public void postHandleNoneWithModelAndView() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/strange/thing");
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		ModelAndView model = mock(ModelAndView.class);
		
		UriInterceptor interceptor = new UriInterceptor();
				
		assertTrue(interceptor.preHandle(request, response, new Object()));
		verify(request, never()).setAttribute("pageMode", PageMode.BUY);
		interceptor.postHandle(request, response, new Object(), model);
		verify(model).addObject("pagemode", PageMode.NONE.getParameter());
	}
	
	@Test
	public void postHandleWithoutModelAndView() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/buy/results");
		
		HttpServletResponse response = mock(HttpServletResponse.class);
		ModelAndView model = mock(ModelAndView.class);
		
		UriInterceptor interceptor = new UriInterceptor();
				
		assertTrue(interceptor.preHandle(request, response, new Object()));
		verify(request).setAttribute("pageMode", PageMode.BUY);
		interceptor.postHandle(request, response, new Object(), null);
		verify(model, never()).addObject("pagemode", PageMode.BUY.getParameter());
	}
}
