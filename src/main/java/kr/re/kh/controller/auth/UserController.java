package kr.re.kh.controller.auth;

import io.swagger.annotations.ApiOperation;
import kr.re.kh.annotation.CurrentUser;
import kr.re.kh.event.OnUserLogoutSuccessEvent;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.payload.request.LogOutRequest;
import kr.re.kh.model.payload.response.ApiResponse;
import kr.re.kh.model.payload.response.UserResponse;
import kr.re.kh.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/test")
    public String test(){
        return String.valueOf(userService.findById(1L));
    }

    /**
     * 현재 사용자의 프로필 리턴
     * @param currentUser
     * @return
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> getUserProfile(@CurrentUser CustomUserDetails currentUser) {
        log.info(currentUser.getEmail() + " has role: " + currentUser.getRoles() + " username: " + currentUser.getUsername());
        log.info(currentUser.getPhone());
        UserResponse userResponse = new UserResponse(currentUser.getUsername(), currentUser.getEmail(), currentUser.getRoles(), currentUser.getId(), currentUser.getPhone() , currentUser.getName());
        return ResponseEntity.ok(userResponse);
    }

    /**
     * 로그아웃
     * @param customUserDetails
     * @param logOutRequest
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@CurrentUser CustomUserDetails customUserDetails,
                                     @Valid @RequestBody LogOutRequest logOutRequest) {
        log.info(customUserDetails.toString());
        log.info(logOutRequest.toString());
        userService.logoutUser(customUserDetails, logOutRequest);
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(customUserDetails.getEmail(), credentials.toString(), logOutRequest);
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return ResponseEntity.ok(new ApiResponse(true, "로그아웃 되었습니다."));
    }

    @ApiOperation("사업자인지 확인")
    @GetMapping("/isManagerByUserId")
    public Long isManagerByuserId(@RequestParam Long userId) {
        return userService.isManagerByUserId(userId);
    }

    @ApiOperation("어드민인지 확인")
    @GetMapping("/isAdminByUserId")
    public Long isAdminByUserId(@RequestParam Long userId) {
        return userService.isAdminByUserId(userId);
    }
}
