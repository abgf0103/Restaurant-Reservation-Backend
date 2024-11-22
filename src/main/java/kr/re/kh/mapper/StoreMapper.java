package kr.re.kh.mapper;

import kr.re.kh.model.vo.StoreVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreMapper {

    void insertStore(StoreVO storeVO);                      //가게 추가

    List<StoreVO> selectAllStore();                         //모든 가게 조회

    void updateStore(StoreVO storeVO);                      //가게 수정

    void deleteStore(Long storeId);                         //가게 삭제

    boolean hasStoreName(String storeName);                 //가게 이름 중복 조회

    Long findStoreIdByStoreName(String storeName);

    void acceptStoreRegister(Long storeId);                 //가게 등록 수락(관리자)

    void requestStoreDelete(Long storeId);                  //가게 삭제 요청(사업자)

    void acceptStoreDelete(Long storeId);                   //가게 삭제 수락(관리자)

    StoreVO viewStore(Long storeId);                        //가게ID로 가게 조회

    List<StoreVO> selectMyStoreByUserId(Long userId);       //유저ID로 가게 조회

    List<StoreVO> selectStoreByCategoryId(Long categoryId); //카테고리 ID로 가게 조회

    List<StoreVO> searchStore(String searchKeyword);        //키워드로 가게 검색

    String getFileNameByFileId(Long fileId);

    List<StoreVO> getFavoriteStoreList(Long userId);

}
