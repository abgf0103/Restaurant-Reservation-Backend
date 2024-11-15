package kr.re.kh.controller.admin;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.re.kh.model.vo.MemberVO;
import kr.re.kh.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member2")
@Slf4j
@AllArgsConstructor
public class CommonMemberController {

    private final MemberService memberService;

    // 회원 등록

    @ApiOperation(value = "회원 등록", notes = "회원을 등록합니다.")
    @PostMapping("/save")
    public ResponseEntity<String> createMember(@RequestBody MemberVO member) {
        memberService.createMember(member);
        return ResponseEntity.ok("Member created successfully");
    }

    // 특정 회원 조회 by ID
    @ApiOperation(value = "특정 회원 조회", notes = "특정 회원 조회를 조회합니다.")
    @ApiImplicitParam(name = "userId" , dataType = "Long" , required = true)
    @GetMapping("/info/{userId}")
    public ResponseEntity<MemberVO> getMemberById(@PathVariable Long userId) {

        return ResponseEntity.of(memberService.getMemberById(userId));
    }

    // 모든 회원 조회
    @ApiOperation(value = "모든 회원 조회", notes = "모든 회원 조회를 조회합니다.")
    @GetMapping("/allmember")
    public ResponseEntity<List<MemberVO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // 회원 정보 업데이트
    @ApiOperation(value = "회원 정보 업데이트", notes = "특정 회원의 정보를 업데이트합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", dataType = "Long", required = true),
            @ApiImplicitParam(name = "member", dataType = "MemberVO", required = true),
    })
    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateMember(@PathVariable Long userId, @RequestBody MemberVO member) {
        member.setUserId(userId);
        memberService.updateMember(member);
        return ResponseEntity.ok("Member updated successfully");
    }

    // 회원 삭제
    @ApiOperation(value = "회원 삭제", notes = "특정 회원을 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", dataType = "Long", required = true),

    })
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long userId) {
        memberService.deleteMember(userId);
        return ResponseEntity.ok("Member deleted successfully");
    }
}
