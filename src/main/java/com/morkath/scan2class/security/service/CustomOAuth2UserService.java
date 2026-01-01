package com.morkath.scan2class.security.service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.repository.auth.UserRepository;
import com.morkath.scan2class.service.AuthService;

public class CustomOAuth2UserService extends OidcUserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public CustomOAuth2UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("========== CUSTOM OIDC SERVICE CALLED ==========");
        System.out.println("UserRepository: " + (userRepository != null ? "INJECTED" : "NULL"));
        System.out.println("AuthService: " + (authService != null ? "INJECTED" : "NULL"));

        OidcUser oidcUser = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        System.out.println("Provider: " + registrationId);
        System.out.println("Email: " + oidcUser.getAttributes().get("email"));

        return processOidcUser(registrationId, oidcUser);
    }

    private OidcUser processOidcUser(String registrationId, OidcUser oidcUser) {
        Map<String, Object> attributes = oidcUser.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        logger.info("Bắt đầu xử lý OIDC Login cho Provider: {}, Email: {}", registrationId, email);

        if (email == null) {
            logger.error("LỖI XÁC THỰC: Google không trả về Email. Attributes: {}", attributes);
            throw new OAuth2AuthenticationException("Email không tìm thấy từ Google Provider");
        }

        try {
            UserEntity user = userRepository.findByEmail(email);
            if (user != null) {
                logger.info("User {} đã tồn tại. Tiến hành cập nhật thông tin.", email);
                user = updateExistingUser(user, name);
            } else {
                logger.warn("User {} chưa có trong hệ thống. Đang tạo tài khoản mới.", email);
                user = registerNewUser(registrationId, email, name);
            }

            logger.debug("Gán quyền cho User {}: {}", email, user.getRoles());
            Set<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getCode()))
                    .collect(Collectors.toSet());

            if (authorities.isEmpty()) {
                authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
            }

            // Return DefaultOidcUser which implements OidcUser interface
            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());

        } catch (Exception ex) {
            logger.error("LỖI HỆ THỐNG khi xử lý lưu User {}: {}", email, ex.getMessage(), ex);
            throw ex;
        }
    }

    private UserEntity registerNewUser(String registrationId, String email, String name) {
        try {
            logger.info("Đang đăng ký User mới thông qua AuthService: {}", email);

            String temporaryPassword = UUID.randomUUID().toString();
            boolean isRegistered = authService.register(email, temporaryPassword, email);

            if (isRegistered) {
                UserEntity savedUser = userRepository.findByEmail(email);
                if (savedUser != null) {
                    if (name != null) {
                        savedUser.setFullname(name);
                        userRepository.save(savedUser);
                    }
                    logger.info("Đăng ký thành công và cập nhật tên cho User ID: {}", savedUser.getId());
                    return savedUser;
                }
            }

            throw new OAuth2AuthenticationException("Không thể đăng ký user mới qua AuthService");

        } catch (Exception ex) {
            logger.error("LỖI HỆ THỐNG khi tạo User mới {}: {}", email, ex.getMessage(), ex);
            throw ex;
        }
    }

    private UserEntity updateExistingUser(UserEntity existingUser, String name) {
        if (name != null && !name.equals(existingUser.getFullname())) {
            existingUser.setFullname(name);
            return userRepository.save(existingUser);
        }
        return existingUser;
    }
}
