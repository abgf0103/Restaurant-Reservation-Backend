package kr.re.kh.model.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.re.kh.model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String email;
    private String username;
    private Set<Role> roles;
    private Long id;
    private boolean active;
    private String name;
    private String phone;
    private Long fileId;


    public UserResponse(String username, String email, Set<Role> roles, Long id, String phone , String name , Long fileId) {
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.fileId = fileId; // 파일아이디 추가
    }

    public UserResponse(String username, String email, Set<Role> roles, Long id, boolean active, String name, String phone , Long fileId) {
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.id = id;
        this.active = active;
        this.name = name;
        this.phone = phone;
        this.fileId = fileId; // 파일아이디 추가
    }
}
