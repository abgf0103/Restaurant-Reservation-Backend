package kr.re.kh.mapper;

import kr.re.kh.model.User;
import org.apache.ibatis.annotations.Mapper;

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
    }
