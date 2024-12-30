
package org.server.socialnetworkserver.test;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestTimingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        long startTime = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            long endTime = System.currentTimeMillis();
            String requestUri = ((HttpServletRequest) request).getRequestURI();
            System.out.println("Request to " + requestUri + " took " + (endTime - startTime) + " ms");
        }
    }
}
