package com.demo.security.filter;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTTokenFilter extends OncePerRequestFilter {

    //this will be used to verify the token
    private final JWTVerifier jwtVerifier;

    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        //get the header which should contain a token
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // If there's no token, just proceed with the chain
        if (authorizationHeader == null || !authorizationHeader.toLowerCase().startsWith("bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = "";
        if (authorizationHeader.toLowerCase().startsWith("bearer ")) {
            token = authorizationHeader.substring(7); // Remove the "Bearer " prefix
            try {
                //validate if request has a header attached
                if (StringUtils.isNotBlank(token)) {
                    //verify the signature and decide the token
                    DecodedJWT jwt = jwtVerifier.verify(token);

                    // Check token expiry
                    Date expiresAt = jwt.getExpiresAt();
                    if (expiresAt != null && expiresAt.before(new Date())) {
                        throw new JWTVerificationException("Token has expired");
                    }

                    //obtain roles and claims present into the token
                    List<String> roles = jwt.getClaim("roles").asList(String.class);
                    String userID = String.valueOf(jwt.getClaim("userID"));

                    /*
                     * create a list of spring roles and add prefix of ROLE_ to it
                     * the spring roles will be used by security context to allow access
                     * based on the roles in security config
                     */
                    List<SimpleGrantedAuthority> rolesList = roles.stream().map(
                            thisRoleString -> new SimpleGrantedAuthority(
                                    String.format("%s%s", "ROLE_", thisRoleString)
                            )
                    ).toList();

                    /*
                     * finally, after all verification,
                     * create a spring user and assign roles and claims to it
                     */
                    UsernamePasswordAuthenticationToken user =
                            new UsernamePasswordAuthenticationToken(jwt, token, rolesList);

                    //set this user to spring security context for the current thread
                    SecurityContextHolder.getContext().setAuthentication(user);
                }
            } catch (JWTVerificationException jwtException) {
                log.debug("Problem in token.", jwtException);
                throw jwtException;
            }
        }
        chain.doFilter(request,response);
    }
}