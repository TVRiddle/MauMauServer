package maumau.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
@Slf4j
public class MdcFilter extends OncePerRequestFilter {

    /**
     * Setzt Variablen die fuer das Loggen auf Serverseite benoetigt werden
     *
     * @param httpServletRequest  einkommende Request
     * @param httpServletResponse ausgenhende Responce
     * @param filterChain         Liste an Ausnahmen die nicht getrackt werden sollen
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, FilterChain filterChain)
        throws ServletException, IOException {
        log.trace(
            "Request: Method=" + httpServletRequest.getMethod() + " Path=" + httpServletRequest
                .getRequestURI());
        String uuid = UUID.randomUUID().toString().substring(0, 5);
        MDC.put("TRACE_ID", uuid);
        MDC.put("REMOTE_ADDR",
            httpServletRequest.getRemoteAddr() + ":" + httpServletRequest.getRemotePort());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        log.trace("Responce: Code=" + httpServletResponse.getStatus());
        MDC.clear();
    }
}