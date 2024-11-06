package kr.re.kh.service;

import kr.re.kh.model.payload.request.SpRequest;
import kr.re.kh.model.payload.response.SpResponse;
import kr.re.kh.model.vo.MemberVO;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    // 회원 등록
    int createMember(MemberVO member);

    // 특정 회원 조회 by ID
    Optional<MemberVO> getMemberById(Long memberId);

    // 모든 회원 조회
    List<MemberVO> getAllMembers();

    // 회원 정보 업데이트
    int updateMember(MemberVO member);

    // 회원 삭제
    int deleteMember(Long memberId);
}
