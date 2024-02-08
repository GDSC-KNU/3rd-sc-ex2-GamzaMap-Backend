package GDSC.gamzamap.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j

public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request);
        HttpServletRequest httpRequest = (HttpServletRequest)
                request;
        String requestURI = httpRequest.getRequestURI();

        if ("/auth/general/join".equals(requestURI) || "/auth/login".equals(requestURI)
                || "/auth/refresh".equals(requestURI) || "/auth/logout".equals(requestURI)
                || "/auth/login/kakao".equals(requestURI)){
            log.info("바로 통과하십쇼");
            chain.doFilter(request,response);
            return;
        }

        if (token != null && jwtTokenProvider.validateToken(token)) {
            log.info("유효한 accessToken 입니다!");
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else{
            log.info("유효하지 않지롱");
            // 유효하지 않은 경우 클라이언트에게 응답 전송
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            httpResponse.getWriter().write("nono.. expired. plz refresh~");
            return;
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
