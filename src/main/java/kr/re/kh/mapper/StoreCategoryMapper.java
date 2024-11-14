package kr.re.kh.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreCategoryMapper {
    String getCategoryById(Long storeId);
}
