package kr.re.kh.controller.admin;

import kr.re.kh.model.vo.MemberVO;
import kr.re.kh.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/member")
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
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberVO> getMemberById(@PathVariable Long memberId) {
        return ResponseEntity.of(memberService.getMemberById(memberId));
    }

    // 모든 회원 조회
    @GetMapping
    public ResponseEntity<List<MemberVO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // 회원 정보 업데이트
    @PutMapping("/{memberId}")
    public ResponseEntity<String> updateMember(@PathVariable Long memberId, @RequestBody MemberVO member) {
        member.setMemberId(memberId); // URL에서 가져온 memberId를 설정
        memberService.updateMember(member);
        return ResponseEntity.ok("Member updated successfully");
    }

    // 회원 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok("Member deleted successfully");
    }
}