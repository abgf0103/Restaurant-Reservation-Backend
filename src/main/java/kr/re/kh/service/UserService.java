/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kr.re.kh.service;

import kr.re.kh.annotation.CurrentUser;
import kr.re.kh.exception.BadRequestException;
import kr.re.kh.exception.UserLogoutException;
import kr.re.kh.mapper.UserMapper;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.Role;
import kr.re.kh.model.User;
import kr.re.kh.model.UserDevice;
import kr.re.kh.model.payload.DeviceInfo;
import kr.re.kh.model.payload.request.LogOutRequest;
import kr.re.kh.model.payload.request.RegistrationRequest;
import kr.re.kh.model.payload.request.UserRegisterRequest;
import kr.re.kh.model.payload.response.PagedResponse;
import kr.re.kh.model.payload.response.UserListResponse;
import kr.re.kh.model.payload.response.UserResponse;
import kr.re.kh.model.vo.DeviceType;
import kr.re.kh.repository.RoleRepository;
import kr.re.kh.repository.UserRepository;
import kr.re.kh.util.ModelMapper;
import kr.re.kh.util.ValidatePageNumberAndSize;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserDeviceService userDeviceService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;


    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * username으로 찾기
     * @param username
     * @return
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * email로 찾기
     * @param email
     * @return
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * id로 찾기
     * @param Id
     * @return
     */
    public Optional<User> findById(Long Id) {
        return userRepository.findById(Id);
    }

    /**
     * 사용자 저장
     * @param user
     * @return
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * 이메일 중복 체크
     * @param email
     * @return
     */
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * username 중복 체크
     * @param username
     * @return
     */
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    /**
     * 사용자 생성
     * @param registerRequest
     * @return
     */
    public User createUser(RegistrationRequest registerRequest) {
        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setUsername(registerRequest.getUsername());
        newUser.setActive(false);
        newUser.setEmailVerified(true);
        newUser.setName(registerRequest.getName());
        newUser.addRoles(getRolesForNewUser(false));
        newUser.setPhone(registerRequest.getPhone());
        return newUser;
    }

    /**
     * 사용자에게 확인할 수 있는 권한 확인
     * @param isToBeMadeAdmin
     * @return
     */
    private Set<Role> getRolesForNewUser(Boolean isToBeMadeAdmin) {
        Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
        if (!isToBeMadeAdmin) {
            newUserRoles.removeIf(Role::isAdminRole);
            newUserRoles.removeIf(Role::isUserRole);
        }
        log.info("Setting user roles: " + newUserRoles);
        return newUserRoles;
    }

    /**
     * 관리자가 사용자를 등록할때 권한 구성
     * @param roleName
     * @return
     */
    private Set<Role> getUserRoles(String roleName) {
        Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
        if (roleName.equals("ADMIN")) {
            newUserRoles.removeIf(Role::isSystemRole);
        } else if (roleName.equals("USER")) {
            newUserRoles.removeIf(Role::isSystemRole);
            newUserRoles.removeIf(Role::isAdminRole);
        }
        log.info("Setting user roles: " + newUserRoles);
        return newUserRoles;
    }

    /**
     * 로그 아웃
     * @param currentUser
     * @param logOutRequest
     */
    public void logoutUser(@CurrentUser CustomUserDetails currentUser, LogOutRequest logOutRequest) {
        String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
        log.info(deviceId);
        log.info(currentUser.toString());
        UserDevice userDevice = userDeviceService.findByUserIdAndDeviceId(currentUser.getId(), deviceId)
                .filter(device -> device.getDeviceId().equals(deviceId))
                .orElseThrow(() -> new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "해당정보가 없습니다."));

        log.info("Removing refresh token associated with device [" + userDevice + "]");
        refreshTokenService.deleteById(userDevice.getRefreshToken().getId());
    }

    public List<User> getAllUserList() {
        return userRepository.findAll();
    }

    public PagedResponse<UserListResponse> getUserListByPaging(String searchType, String searchKeyword, int page, int size) {
        ValidatePageNumberAndSize.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "user_id");
        log.info(">>>>>>>>>>>> " + searchType);
        Page<User> userLists = null;
        if(searchType.equals("email")) {
            userLists = userRepository.findByUserEmail(searchKeyword, pageable);
        } else {
            userLists = userRepository.findByUsername(searchKeyword, pageable);
        }


        List<UserListResponse> userResponses = userLists.map(ModelMapper::mapUserToUserResponse).getContent();

        return new PagedResponse<>(userResponses, userLists.getNumber(), userLists.getSize(), userLists.getTotalElements(), userLists.getTotalPages(), userLists.isLast());
    }

    /**
     * 사용자 검색
     * @param searchType
     * @param searchKeyword
     * @return
     */
    public List<UserResponse> userSearch(String searchType, String searchKeyword) {
        if(searchType.equals("email")) {
            List<User> list = userRepository.findByEmailIsContaining(searchKeyword);
            return ModelMapper.mapUserListToUserResponseList(list);
        } else if (searchType.equals("username")) {
            List<User> list = userRepository.findByUsernameIsContaining(searchKeyword);
            return ModelMapper.mapUserListToUserResponseList(list);
        } else if (searchType.equals("name")) {
            List<User> list = userRepository.findByNameIsContaining(searchKeyword);
            return ModelMapper.mapUserListToUserResponseList(list);
        } else {
            List<User> list = userRepository.findAll();
            log.info(String.valueOf(list.size()));
            return ModelMapper.mapUserListToUserResponseList(list);
        }
    }

    /**
     * 사용자 권한 조회
     * @param id
     * @return
     */
    public Set<Role> roleSearch(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(User::getRoles).orElse(null);
    }

    /**
     * '아이디 중복 체크'에서 사용
     * @param username
     * @return
     */
    public boolean usernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }


    /**
     * '비밀번호 재확인 / 정보수정 들어갈때 사용
     * @param
     * @return
     */
    public boolean checkPassword(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);//
    }

    public boolean checkPassword(Long userId, String password) {
            Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new BadRequestException("사용자를 찾을 수 없습니다.");
        }
                User user = userOpt.get();
        return checkPassword(password, user.getPassword());
    }

    /**
     *  회원탈퇴
     */
    public boolean deleteUser(Long userId ) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // 사용자 삭제 처리
            userMapper.removeUser(userId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * '이메일 중복 체크'에서 사용
     * @param email
     * @return
     */
    public boolean emailAlreadyExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean saveUser(UserRegisterRequest registrationRequest) {
        log.info(registrationRequest.toString());
        if (registrationRequest.getId() == 0) {
            // 저장
            if (registrationRequest.getUsername().isBlank()) {
                throw new BadRequestException("아이디를 입력해주세요.");
            }
            if (registrationRequest.getEmail().isBlank()) {
                throw new BadRequestException("이메일을 입력해주세요.");
            }
            if (registrationRequest.getName().isBlank()) {
                throw new BadRequestException("이름을 입력해주세요.");
            }
            if (registrationRequest.getPassword().isBlank()) {
                throw new BadRequestException("비밀번호를 입력해주세요.");
            }
            if (registrationRequest.getRoleNum().isBlank()) {
                throw new BadRequestException("권한을 선택해주세요.");
            }
            if (!Objects.equals(registrationRequest.getPassword(), registrationRequest.getPasswordConfirm())) {
                throw new BadRequestException("비밀번호가 일치하지 않습니다.");
            }
            boolean usernameExists = userRepository.existsByUsername(registrationRequest.getUsername());
            boolean emailExists = userRepository.existsByEmail(registrationRequest.getEmail());
            log.info(String.valueOf(usernameExists));
            log.info(String.valueOf(emailExists));
            if (usernameExists) {
                throw new BadRequestException("이미 사용중인 아이디입니다.");
            } else if (emailExists) {
                throw new BadRequestException("이미 사용중인 이메일입니다.");
            } else {
                User user = new User();
                user.setUsername(registrationRequest.getUsername());
                user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                user.setEmail(registrationRequest.getEmail());
                user.setName(registrationRequest.getName());
                user.setActive(registrationRequest.isActive());
                user.setEmailVerified(true);
                user.setBusinessNum(registrationRequest.getBusinessNum());
                String roleNum = registrationRequest.getRoleNum();
                String roleName = "USER";
                if (roleNum.equals("3")) {
                    roleName = "SYSTEM";
                } else if (roleNum.equals("2")) {
                    roleName = "ADMIN";
                }
                user.addRoles(getUserRoles(roleName));
                user.setPhone(registrationRequest.getPhone());
                log.info(user.toString());
                userRepository.save(user);
                return true;
            }
        } else if (registrationRequest.getId() > 0) {
            // 수정
//            if (registrationRequest.getPassword().isBlank()) {
//                throw new BadRequestException("비밀번호를 입력해 주세요.");
//            }
            if (registrationRequest.getRoleNum().isBlank()) {
                throw new BadRequestException("권한을 선택해 주세요.");
            }
//            if (!Objects.equals(registrationRequest.getPassword(), registrationRequest.getPasswordConfirm())) {
//                throw new BadRequestException("비밀번호가 일치하지 않습니다.");
//            }
            User user = new User();
            user.setId(registrationRequest.getId());
            user.setUsername(registrationRequest.getUsername());
            if (!registrationRequest.getPassword().isEmpty()) user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            if (!registrationRequest.getEmail().isEmpty()) user.setEmail(registrationRequest.getEmail());
            user.setName(registrationRequest.getName());
            user.setActive(registrationRequest.isActive());
            user.setEmailVerified(true);
            user.setPhone(registrationRequest.getPhone());
            user.setBusinessNum(registrationRequest.getBusinessNum());
            String roleNum = registrationRequest.getRoleNum();
            String roleName = "USER";
            if (roleNum.equals("3")) {
                roleName = "SYSTEM";
            } else if (roleNum.equals("2")) {
                roleName = "ADMIN";
            }
            user.addRoles(getUserRoles(roleName));
            userRepository.save(user);
            return true;
        } else {
            throw new BadRequestException("잘못된 요청입니다.");
        }
    }
}
