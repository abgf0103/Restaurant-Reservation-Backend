    package kr.re.kh.controller.admin;

    import kr.re.kh.model.User;
    import kr.re.kh.model.payload.request.UserRegisterRequest;
    import kr.re.kh.model.payload.response.ApiResponse;
    import kr.re.kh.service.UserService;
    import lombok.AllArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;

    import javax.validation.Valid;
    import java.util.Optional;

    @RestController
    @RequestMapping("/api/member")
    @Slf4j
    @AllArgsConstructor
    public class MemberController {

        private final UserService userService;

        /**
         * 사용자 목록 호출, admin 권한 필요
         * @param searchType (username, email, name) : 미지정시 name으로 검색
         * @param searchKeyword
         * @return
         */
        @GetMapping("/list")
        @PreAuthorize("hasRole('SYSTEM')")
        public ResponseEntity<?> list(
                @RequestParam(value = "searchType", defaultValue = "") String searchType,
                @RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword
        ) {
            return ResponseEntity.ok(userService.userSearch(searchType, searchKeyword));
        }

        /**
         * 사용자 조회시 권한 목록 호출
         * @param id
         * @return
         */
        @GetMapping("/roleList")
        @PreAuthorize("hasRole('SYSTEM')")
        public ResponseEntity<?> roleList(@RequestParam(value = "id", defaultValue = "") Long id) {
            return ResponseEntity.ok(userService.roleSearch(id));
        }

        // 회원가입
        @PostMapping("/save")
        public ResponseEntity<?> save(@Valid @RequestBody UserRegisterRequest registrationRequest) {
            return ResponseEntity.ok(userService.saveUser(registrationRequest));
        }

        @GetMapping("/check/username")
        @PreAuthorize("hasRole('SYSTEM')")
        public ResponseEntity<?> checkUsernameInUse(@RequestParam("username") String username) {
            boolean usernameExists = userService.usernameAlreadyExists(username);
            return ResponseEntity.ok(new ApiResponse(!usernameExists, usernameExists ? "이미 사용중인 아이디입니다." : "사용 가능한 아이디입니다."));
        }

        @GetMapping("/check/email")
        @PreAuthorize("hasRole('SYSTEM')")
        public ResponseEntity<?> checkEmailInUse(@RequestParam("email") String email) {
            boolean emailExists = userService.emailAlreadyExists(email);
            return ResponseEntity.ok(new ApiResponse(!emailExists, emailExists ? "이미 사용중인 이메일입니다." : "사용 가능한 이메일입니다."));
        }
        //수정

        @PutMapping("/user/update")
        public ResponseEntity<?> updateUser(@RequestBody UserRegisterRequest registrationRequest) {
            boolean result = userService.saveUser(registrationRequest);
            if (result) {
                return ResponseEntity.ok("사용자 정보가 성공적으로 수정되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("사용자 정보 수정에 실패했습니다.");
            }
        }
        //id찾기
        @GetMapping("/user/findID")
        public ResponseEntity<?> findUserId(@RequestParam("email") String email) {
            Optional<User> userOpt = userService.findByEmail(email);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // ApiResponse 객체 생성 시 success=true, data=user.getUsername(), cause=null, path=null로 전달
                ApiResponse response = new ApiResponse(true, "아이디 찾기 성공", user.getUsername(), null);
                return ResponseEntity.ok(response);
            } else {

                ApiResponse response = new ApiResponse(false, "이메일에 해당하는 아이디가 없습니다.", null, null);
                return ResponseEntity.ok(response);
            }
        }
        // 수정 창 가기 전에 비밀번호 확인
        @PostMapping("/user/checkUserEdit")
        public ResponseEntity<?> verifyPassword(@RequestBody UserRegisterRequest request) {
            // 요청에서 id와 password를 가져옵니다.
            Long userId = request.getId();
            String password = request.getPassword();//

            // 사용자 정보를 가져옵니다.
            Optional<User> userOpt = userService.findById(userId);

            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "사용자를 찾을 수 없습니다.", null, null));
            }

            User user = userOpt.get();

            // 비밀번호 비교
            boolean passwordMatches = userService.checkPassword(password, user.getPassword());

            if (passwordMatches) {
                return ResponseEntity.ok(new ApiResponse(true, "비밀번호가 일치합니다.", null, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "비밀번호가 일치하지 않습니다.", null, null));
            }
        }

    }
