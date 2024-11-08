package kr.re.kh.controller.admin;

import kr.re.kh.model.vo.MemberVO;
import kr.re.kh.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@Slf4j
@AllArgsConstructor
public class CommonMemberController {

    private final MemberService memberService;

    // 회원 등록
    @PostMapping
    public ResponseEntity<String> createMember(@RequestBody MemberVO member) {
        memberService.createMember(member);
        return ResponseEntity.ok("Member created successfully");
    }

    // 특정 회원 조회 by ID
    @GetMapping("/{userId}")
    public ResponseEntity<MemberVO> getMemberById(@PathVariable Long userId) {
        // 수정: memberId -> userId로 변경하여 MemberVO와 일치시킴
        return ResponseEntity.of(memberService.getMemberById(userId));
    }

    // 모든 회원 조회
    @GetMapping("/allmember")
    public ResponseEntity<List<MemberVO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // 회원 정보 업데이트
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateMember(@PathVariable Long userId, @RequestBody MemberVO member) {
        member.setUserId(userId); // URL에서 가져온 userId를 설정
        memberService.updateMember(member);
        return ResponseEntity.ok("Member updated successfully");
    }

    // 회원 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long userId) {
        memberService.deleteMember(userId);
        return ResponseEntity.ok("Member deleted successfully");
    }
}
