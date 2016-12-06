package ch.unibe.ese.team3.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ch.unibe.ese.team3.model.enums.PageMode;

/**
 * Analyzes the url provided by the user and detects the mode
 * of the application (buy or rent). 
 * Makes this information available for all controllers.
 *
 */
public class UriInterceptor extends HandlerInterceptorAdapter {
	
	private PageMode pageMode = PageMode.NONE;
	
	/**
	 * Passes the page mode to all controllers.
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null){
			modelAndView.addObject("pagemode", pageMode.getParameter());
		}
	}
	
	/**
	 * Detects the page mode of the application based on the url.
	 * Redirects user to buy mode if no page mode is set.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		
		String uri = request.getRequestURI();
		
		if (uri.startsWith("/rent/")){
			pageMode = PageMode.RENT;
		}
		if (uri.startsWith("/buy/")){
			pageMode = PageMode.BUY;
		}
		
		//Redirect users to default page mode
		if (uri.equals("/")){
			String url = request.getContextPath() + "/buy/";
			response.sendRedirect(url);
			return false;
		}
		
		//This will pass the pagemode to all controllers
		if (pageMode != PageMode.NONE){
			request.setAttribute("pageMode", pageMode);
		}
		
		return true;
	}

}
