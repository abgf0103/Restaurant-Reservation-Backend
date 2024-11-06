package kr.re.kh.mapper;

import kr.re.kh.model.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    // 회원 생성
    int insertMember(MemberVO member);

    // 회원 조회 by ID
    MemberVO selectMemberById(Long memberId);

    // 모든 회원 조회
    List<MemberVO> selectAllMembers();

    // 회원 정보 수정
    int updateMember(MemberVO member);

    // 회원 삭제 (비활성화)
    int deleteMember(Long memberId);
}
