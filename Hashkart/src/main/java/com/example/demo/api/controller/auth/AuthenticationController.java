package com.example.demo.api.controller.auth;

import com.example.demo.api.model.LoginBody;
import com.example.demo.api.model.LoginResponse;
import com.example.demo.api.model.RegistrationBody;
import com.example.demo.exception.EmailFailureException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotVerifiedException;
import com.example.demo.model.LocalUser;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for handling authentication requests.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  /** The user service. */
  private UserService userService;

  /**
   * Spring injected constructor.
   * @param userService
   */
  public AuthenticationController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Post Mapping to handle registering users.
   * @param registrationBody The registration information.
   * @return Response to front end.
   */
  @PostMapping("/register")
  public ResponseEntity registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
    try {
      userService.registerUser(registrationBody);
      return ResponseEntity.ok().build();
    } catch (UserAlreadyExistsException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (EmailFailureException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Post Mapping to handle user logins to provide authentication token.
   * @param loginBody The login information.
   * @return The authentication token if successful.
   */
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) {
    String jwt = null;
    try {
      jwt = userService.loginUser(loginBody);
    } catch (UserNotVerifiedException ex) {
      LoginResponse response = new LoginResponse();
      response.setSuccess(false);
      String reason = "USER_NOT_VERIFIED";
      if (ex.isNewEmailSent()) {
        reason += "_EMAIL_RESENT";
      }
      response.setFailureReason(reason);
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    } catch (EmailFailureException ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    if (jwt == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } else {
      LoginResponse response = new LoginResponse();
      response.setJwt(jwt);
      response.setSuccess(true);
      return ResponseEntity.ok(response);
    }
  }

 
  @PostMapping("/verify")
  public ResponseEntity verifyEmail(@RequestParam String token) {
    if (userService.verifyUser(token)) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

 
  @GetMapping("/me")
  public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user) {
    return user;
  }

}