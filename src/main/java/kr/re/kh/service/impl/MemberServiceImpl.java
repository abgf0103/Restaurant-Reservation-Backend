package kr.re.kh.service.impl;

import kr.re.kh.mapper.MemberMapper;
import kr.re.kh.model.vo.MemberVO;
import kr.re.kh.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    @Override
    public int createMember(MemberVO member) {
        return memberMapper.insertMember(member);
    }

    @Override
    public Optional<MemberVO> getMemberById(Long memberId) {
        return Optional.ofNullable(memberMapper.selectMemberById(memberId));
    }

    @Override
    public List<MemberVO> getAllMembers() {
        return memberMapper.selectAllMembers();
    }

    @Override
    public int updateMember(MemberVO member) {
        return memberMapper.updateMember(member);
    }

    @Override
    public int deleteMember(Long memberId) {
        return memberMapper.deleteMember(memberId);

    }
}
