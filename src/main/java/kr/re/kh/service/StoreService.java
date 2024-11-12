package kr.re.kh.service;


import kr.re.kh.model.vo.StoreVO;

import java.util.List;

public interface StoreService {
    void insertStore(StoreVO store);
    List<StoreVO> selectAllStore();
    void updateStore(StoreVO store);
    void deleteStore(Long storeId);

    StoreVO viewStore(Long storeId);

    List<StoreVO> selectMyStoreByUserId(Long userId);
}
