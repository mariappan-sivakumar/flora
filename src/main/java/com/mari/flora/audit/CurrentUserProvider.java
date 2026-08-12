package com.mari.flora.audit;

import com.mari.flora.entity.User;
import com.mari.flora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Resolves the acting user from the SecurityContext populated by JwtAuthenticationFilter.
 * JwtAuthenticationFilter authenticates by EMAIL (jwtTokenProvider.getEmailFromJwtToken),
 * so Authentication#getName() returns the email — used here only to look up the user row.
 * The actual display username (User.username column) is what gets stored in audit_log,
 * for a more readable admin audit trail.
 * Falls back to "SYSTEM" for unauthenticated/internal calls so audit rows are never null.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public CurrentUserContext resolve() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return new CurrentUserContext(null, "SYSTEM");
        }

        // auth.getName() returns EMAIL here, since that's what JwtAuthenticationFilter authenticates with
        String email = auth.getName();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            log.warn("Authenticated principal '{}' not found in users table during audit", email);
            // fall back to email itself if user row somehow missing (shouldn't happen post-auth)
            return new CurrentUserContext(null, email);
        }

        // ✅ store the actual username column, not email, for audit readability
        return new CurrentUserContext(user.get().getId(), user.get().getUsername());
    }

    public record CurrentUserContext(Long userId, String username) {}
}
