package de.adorsys.ledgers.app.server.auth;

import de.adorsys.ledgers.middleware.rest.resource.DataMgmtStaffAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DisableEndpointFilter extends OncePerRequestFilter {
    private static final List<String> EXCLUDED_URLS;
    private static final List<String> DEVELOP_PROFILES = Arrays.asList("develop", "sandbox");
    private static final AntPathMatcher matcher = new AntPathMatcher();
    private final Environment environment;

    static {
        EXCLUDED_URLS = Arrays.asList(
                DataMgmtStaffAPI.BASE_PATH + "/currencies",
                DataMgmtStaffAPI.BASE_PATH + "/branch"
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isAccessibleEndpoint(request)) {
            filterChain.doFilter(request, response);
        } else {
            log.info("This endpoint is not accessible");
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    private boolean isAccessibleEndpoint(HttpServletRequest request) {
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        boolean isDevelopProfile = DEVELOP_PROFILES.stream()
                                           .anyMatch(activeProfiles::contains);
        return isDevelopProfile || isExcludedEndpoint(request);
    }

    private boolean isExcludedEndpoint(HttpServletRequest request) {
        return !matcher.matchStart(request.getServletPath(), DataMgmtStaffAPI.BASE_PATH)
                       || EXCLUDED_URLS.stream()
                                  .anyMatch(p -> matcher.match(request.getServletPath(), p));
    }
}
