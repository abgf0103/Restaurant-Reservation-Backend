package kr.re.kh.mapper;

import kr.re.kh.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    String test ();
    void removeUser(Long userId);

    Long isManagerByUserId(Long userId);
    Long isAdminByUserId(Long userId);

    Long insertManager(Long userId,Long isManager);

    User findUserByInfo(Map<String, String> params);

    void updatePassword(User user);

    List<User> findAllUsers(); // 모든 유저 조회(어드민)

    void deactiveUser(Long userId); //유저 비활성화(어드민)

    boolean userIsActive(Long userId); //유저 활성여부

    void userPwUpdate(User user);
    }
