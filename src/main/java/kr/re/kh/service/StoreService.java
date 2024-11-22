package kr.re.kh.service;


import kr.re.kh.model.vo.StoreVO;

import java.util.List;

public interface StoreService {
    void insertStore(StoreVO store);                        //가게 등록
    List<StoreVO> selectAllStore();                         //모든 가게 조회
    void updateStore(StoreVO store);                        //가게 수정
    void deleteStore(Long storeId);                         //가게 삭제

    boolean hasStoreName(String storeName);                 //가게 이름 중복 조회
    Long findStoreIdByStoreName(String storeName);

    void acceptStoreRegister(Long storeId);                 //가게 등록 수락(관리자)
    void requestStoreDelete(Long storeId);                  //가게 삭제 요청(사업자)
    void acceptStoreDelete(Long storeId);                   //가게 삭제 수락(관리자)

    StoreVO viewStore(Long storeId);                        //특정 가게 조회
    List<StoreVO> selectMyStoreByUserId(Long userId);       //내 가게 조회
    List<StoreVO> selectStoreByCategoryId(Long storeId);    //카테고리ID로 가게 조회
    List<StoreVO> searchStore(String searchKeyword);        //키워드로 가게 검색

    String getFileNameByFileId(Long fileId);                // 파일 이름 가져오는 메서드

    List<StoreVO> getFavoriteStoreList(Long userId);        // 유저ID로 즐겨찾기 한 가게 리스트 가져오기
}
