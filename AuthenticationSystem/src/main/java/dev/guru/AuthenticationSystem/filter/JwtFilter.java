package dev.guru.AuthenticationSystem.filter;

import dev.guru.AuthenticationSystem.service.AuthService;
import dev.guru.AuthenticationSystem.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private AuthService authService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    private final List<String> PUBLIC_URLS = new ArrayList<>(List.of("/login", "/register","/resend-otp","/verify-account","/reset-password"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if(PUBLIC_URLS.contains(path)){
            filterChain.doFilter(request,response);
            return;
        }

        String jwt = null;
        String email = null;

        //try to extract token from authorization
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")){
            jwt = authorization.substring(7);
        }

        //if authorization fails, try to get the token via cookie.
        if(jwt == null){
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for (Cookie cookie : cookies) {
                    if(!cookie.getName().equals("JWT_TOKEN"))
                        continue;
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        email = jwtUtil.getEmailFromToken(jwt);

        //validate the token and set the security context.
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetail = authService.loadUserByUsername(email);
            if(jwtUtil.validateToken(jwt,userDetail)){
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail,null,userDetail.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request,response);
    }
}
