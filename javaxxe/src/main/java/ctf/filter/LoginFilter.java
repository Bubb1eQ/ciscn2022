package ctf.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


public class LoginFilter implements Filter {
    private String forwardUrl;
    private String[] excludedPages;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.forwardUrl = filterConfig.getInitParameter("forwardUrl");
        String excludedPage = filterConfig.getInitParameter("ignores");
        if (excludedPage != null && excludedPage.length() > 0) {
            this.excludedPages = excludedPage.split(",");
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String servletPath = req.getServletPath();
        HttpSession session = req.getSession();

        for (String excludedPage: excludedPages) {
            if (servletPath.contains(excludedPage)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        if (!servletPath.contains(this.forwardUrl)) {
            if (session != null && session.getAttribute("user") != null) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                ((HttpServletResponse)servletResponse).sendRedirect(req.getContextPath() + "/login.jsp");
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
