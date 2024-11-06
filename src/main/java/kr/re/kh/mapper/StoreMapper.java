package kr.re.kh.mapper;

import kr.re.kh.model.vo.StoreVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreMapper {

    void insertStore(StoreVO storeVO);
    List<StoreVO> selectAllStore();
    void updateStore(StoreVO storeVO);
    void deleteStore(Long storeId);

}
