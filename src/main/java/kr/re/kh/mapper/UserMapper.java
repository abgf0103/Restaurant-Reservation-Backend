package kr.re.kh.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    String test ();
    void removeUser(Long userId);

    Long isManagerByUserId(Long userId);
    Long isAdminByUserId(Long userId);
    }
