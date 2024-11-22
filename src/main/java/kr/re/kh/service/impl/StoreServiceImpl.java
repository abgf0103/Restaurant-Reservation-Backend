package kr.re.kh.service.impl;

import kr.re.kh.mapper.StoreMapper;
import kr.re.kh.model.vo.StoreVO;
import kr.re.kh.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Slf4j
public class StoreServiceImpl implements StoreService {
    private final StoreMapper storeMapper;

    public StoreServiceImpl(StoreMapper storeMapper) {
        this.storeMapper = storeMapper;
    }


    /**
     * 가게 추가
     * @param storeVO
     */
    @Override
    public void insertStore(StoreVO storeVO) {
        // 파일 ID가 존재하면 파일 이름을 가져와서 저장
        if (storeVO.getFileId() != null) {
            String fileName = storeMapper.getFileNameByFileId(storeVO.getFileId());
            if (fileName != null) {
                storeVO.setSaveFileName(fileName);  // 파일 이름 설정
            }
        }
        storeMapper.insertStore(storeVO);
    }

    /**
     * 파일 ID로 파일 이름을 가져오는 메서드
     */
    @Override
    public String getFileNameByFileId(Long fileId) {
        return storeMapper.getFileNameByFileId(fileId);  // storeMapper에서 파일 이름을 가져옴
    }

    /**
     * 모든 가게 조회
     *
     * @return
     */
    @Override
    public List<StoreVO> selectAllStore() {
        return storeMapper.selectAllStore();
    }

    /**
     * 가게 업데이트
     * @param storeVO
     */
    @Override
    public void updateStore(StoreVO storeVO) {
        storeMapper.updateStore(storeVO);
    }

    /**
     * 가게 삭제
     * @param storeId
     */
    @Override
    public void deleteStore(Long storeId) {
        storeMapper.deleteStore(storeId);
    }

    /**
     * 가게 이름 중복 조회
     * @param storeName
     * @return
     */
    @Override
    public boolean hasStoreName(String storeName) {
        return storeMapper.hasStoreName(storeName);
    }

    @Override
    public Long findStoreIdByStoreName(String storeName) {
        return storeMapper.findStoreIdByStoreName(storeName);
    }

    /**
     * 가게 등록 수락(관리자)
     * @param storeId
     */
    @Override
    public void acceptStoreRegister(Long storeId) {
        storeMapper.acceptStoreRegister(storeId);
    }

    /**
     * 가게 삭제 요청(사업자)
     * @param storeId
     */
    @Override
    public void requestStoreDelete(Long storeId) {
        storeMapper.requestStoreDelete(storeId);
    }

    /**
     * 가게 삭제 수락(관리자)
     * @param storeId
     */
    @Override
    public void acceptStoreDelete(Long storeId) {
        storeMapper.acceptStoreDelete(storeId);
    }

    /**
     * 가게 조회
     * @param storeId
     * @return
     */
    @Override
    public StoreVO viewStore(Long storeId) {
        return storeMapper.viewStore(storeId);
    }

    /**
     * 유저ID로 가게 조회 (내 가게 조회)
     * @param userId
     * @return
     */
    @Override
    public List<StoreVO> selectMyStoreByUserId(Long userId) {
        return storeMapper.selectMyStoreByUserId(userId);
    }

    /**
     * 카테고리ID로 가게 조회
     * @param storeId
     * @return
     */
    @Override
    public List<StoreVO> selectStoreByCategoryId(Long storeId) {
        return storeMapper.selectStoreByCategoryId(storeId);
    }

    /**
     * 키워드로 가게 검색
     * @param searchKeyword
     * @return
     */
    @Override
    public List<StoreVO> searchStore(String searchKeyword) {
        return storeMapper.searchStore(searchKeyword);
    }
}
