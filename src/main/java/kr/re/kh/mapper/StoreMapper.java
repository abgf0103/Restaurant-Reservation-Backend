package kr.re.kh.mapper;

import kr.re.kh.model.vo.StoreVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreMapper {

    void insertStore(StoreVO storeVO);                  //가게 추가
    List<StoreVO> selectAllStore();                     //모든 가게 조회
    void updateStore(StoreVO storeVO);                  //가게 수정
    void deleteStore(Long storeId);                     //가게 삭제

    StoreVO viewStore(Long storeId);                    //가게ID로 가게 조회

    List<StoreVO> selectMyStoreByUserId(Long userId);   //유저ID로 가게 조회

}
