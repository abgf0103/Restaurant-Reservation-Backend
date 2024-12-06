package kr.re.kh.controller.auth;

import io.swagger.annotations.ApiOperation;
import kr.re.kh.annotation.CurrentUser;
import kr.re.kh.event.OnUserLogoutSuccessEvent;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.User;
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
import java.util.List;

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
        UserResponse userResponse = new UserResponse(currentUser.getUsername(), currentUser.getEmail(), currentUser.getRoles(), currentUser.getId(), currentUser.getPhone() , currentUser.getName() ,  currentUser.getFileId() ,currentUser.getActive() );
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

    @ApiOperation("모든 유저 리스트")
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
    public List<User> getUserList() {
        return userService.findAll();
    }

    @ApiOperation("유저 비활성화(어드민)")
    @GetMapping("/deactiveUser")
    public void deactiveUser(@RequestParam Long userId){
        userService.deactiveUser(userId);
    }

    @ApiOperation("유저 활성 여부")
    @GetMapping("/userIsActive")
    public boolean isUserActive(@RequestParam Long userId){
        return userService.userIsActive(userId);
    }

}
