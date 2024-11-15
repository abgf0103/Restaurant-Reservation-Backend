package kr.re.kh.service.impl;

import kr.re.kh.mapper.CategoryMapper;
import kr.re.kh.model.vo.CategoryVO;
import kr.re.kh.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 카테고리 조회
     * @return
     */
    @Override
    public List<CategoryVO> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    @Override
    public Long getCategoryIdByStoreId(Long storeId) {
        return categoryMapper.getCategoryIdByStoreId(storeId);
    }
}
