package kr.re.kh.model.vo;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@Slf4j
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {

    private Long userId; // 회원 아이디
    private String email;  // 회원 이메일
    private boolean emailVerified; // e-mail 인증
    private String username; //회원 이름
    private String password; //비밀번호
    private String name; // 닉네임
    private boolean isActive; //계정 활성화
    private LocalDateTime createdAt; //가입시간
    private LocalDateTime updatedAt; //수정시간

}
