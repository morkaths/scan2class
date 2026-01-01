package com.morkath.scan2class.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.morkath.scan2class.security.model.AuthUser;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	@ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(NoHandlerFoundException ex) {
        return "errors/404";
    }

    @ModelAttribute
    public void globalAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String name = "Anonymous User";
            String email = "";
            String avatar = "/assets/compiled/jpg/1.jpg";

            if (principal instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) principal;
                name = oauth2User.getAttribute("name");
                email = oauth2User.getAttribute("email");
                avatar = oauth2User.getAttribute("picture");
            } else if (principal instanceof AuthUser) {
            	AuthUser authUser = (AuthUser) principal;
                name = authUser.getUsername();
                email = authUser.getUser().getEmail();
            }

            model.addAttribute("displayName", name);
            model.addAttribute("displayEmail", email);
            model.addAttribute("displayAvatar", avatar);
        }
    }
}
