package kr.re.kh.mapper;

import kr.re.kh.model.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryVO> getCategoryList();

    Long getCategoryIdByStoreId(Long storeId);
}
