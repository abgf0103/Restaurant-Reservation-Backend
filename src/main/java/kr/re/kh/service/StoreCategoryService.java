package kr.re.kh.service;

import kr.re.kh.model.vo.StoreCategoryVO;
import kr.re.kh.repository.StoreCategoryVORepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StoreCategoryService {
    private final StoreCategoryVORepository storeCategoryVORepository;

    @Autowired
    public StoreCategoryService(StoreCategoryVORepository storeCategoryVORepository) {
        this.storeCategoryVORepository = storeCategoryVORepository;
    }

    // StoreCategory 저장
    public StoreCategoryVO save(StoreCategoryVO storeCategoryVO) {
        return storeCategoryVORepository.save(storeCategoryVO);
    }

    // StoreCategory 전체 조회
    public List<StoreCategoryVO> findAll() {
        return storeCategoryVORepository.findAll();
    }

    // StoreCategory ID로 조회
    public Optional<StoreCategoryVO> findById(Long id) {
        return storeCategoryVORepository.findById(id);
    }

    // StoreCategory 삭제
    public void deleteById(Long id) {
        storeCategoryVORepository.deleteById(id);
    }

}
