package kr.re.kh.repository;

import kr.re.kh.model.vo.StoreCategoryVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreCategoryVORepository extends JpaRepository<StoreCategoryVO, Long> {
    //전체 데이터 반환
}
