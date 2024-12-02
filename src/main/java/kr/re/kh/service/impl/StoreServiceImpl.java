package kr.re.kh.service.impl;

import kr.re.kh.mapper.StoreMapper;
import kr.re.kh.model.vo.StoreVO;
import kr.re.kh.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;


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
     * 즐겨찾기한 가게 리스트 조회
     * @param userId
     * @return
     */
    @Override
    public List<StoreVO> getFavoriteStoreList(Long userId) {
        List<StoreVO> stores = storeMapper.getFavoriteStoreList(userId);  // 가게 목록 조회

        // 각 가게마다 파일 이름을 추가
        for (StoreVO storeVO : stores) {
            if (storeVO.getFileId() != null) {
                String fileName = storeMapper.getFileNameByFileId(storeVO.getFileId());  // 파일 이름 조회
                if (fileName != null) {
                    storeVO.setSaveFileName(fileName);  // 파일 이름 설정
                }
            }
        }

        return stores;
    }

    /**
     * 모든 가게 리스트 조회(어드민)
     * @return
     */
    @Override
    public List<StoreVO> getStoreListForAdmin() {
        return storeMapper.getStoreListForAdmin();
    }

    /**
     * 모든 가게 조회
     *
     * @return
     */
    @Override
    public List<StoreVO> selectAllStore() {
        List<StoreVO> stores = storeMapper.selectAllStore();  // 가게 목록 조회

        // 각 가게마다 파일 이름을 추가
        for (StoreVO storeVO : stores) {
            if (storeVO.getFileId() != null) {
                String fileName = storeMapper.getFileNameByFileId(storeVO.getFileId());  // 파일 이름 조회
                if (fileName != null) {
                    storeVO.setSaveFileName(fileName);  // 파일 이름 설정
                }
            }
        }

        return stores;
    }

    /**
     * 비슷한 가게 조회
     * @return
     */
    @Override
    public List<StoreVO> getSimilarStoreList(Map<String, Long> params) {
        List<StoreVO> stores = storeMapper.getSimilarStoreList(params.get("categoryId"), params.get("storeId")); //비슷한 가게 목록 조회

        // 각 가게마다 파일 이름을 추가
        for (StoreVO storeVO : stores) {
            if (storeVO.getFileId() != null) {
                String fileName = storeMapper.getFileNameByFileId(storeVO.getFileId());  // 파일 이름 조회
                if (fileName != null) {
                    storeVO.setSaveFileName(fileName);  // 파일 이름 설정
                }
            }
        }

        return stores;
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

    /**
     * 가게 이름으로 가게 ID 조회
     * @param storeName
     * @return
     */
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
     * @param storeId 가게 ID
     * @return StoreVO 가게 정보 객체
     * @fileName StoreServiceImpl.java  // 이 파일에서 정의된 메서드입니다.
     */
    @Override
    public StoreVO viewStore(Long storeId) {
        StoreVO storeVO = storeMapper.viewStore(storeId);  // 가게 정보 조회

        //  파일 이름을 가져와서 설정
        if (storeVO != null && storeVO.getFileId() != null) {
            String fileName = storeMapper.getFileNameByFileId(storeVO.getFileId());
            String fileTarget = storeMapper.getFileTargetByFileId(storeVO.getFileId());
            if (fileName != null) {
                storeVO.setSaveFileName(fileName);// 파일 이름 설정
                storeVO.setSaveFileTarget((fileTarget));// 파일 타겟 설정
            }
        }

        return storeVO;
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
        List<StoreVO> stores = storeMapper.selectStoreByCategoryId(storeId);  // 가게 목록 조회

        // 각 가게마다 파일 이름을 추가
        for (StoreVO storeVO : stores) {
            if (storeVO.getFileId() != null) {
                String fileName = storeMapper.getFileNameByFileId(storeVO.getFileId());  // 파일 이름 조회
                if (fileName != null) {
                    storeVO.setSaveFileName(fileName);  // 파일 이름 설정
                }
            }
        }

        return stores;
    }

    /**
     * 키워드로 가게 검색
     * @param searchKeyword
     * @return
     */
    @Override
    public List<StoreVO> searchStore(String searchKeyword) {
        List<StoreVO> stores = storeMapper.searchStore(searchKeyword);  // 가게 목록 조회

        // 각 가게마다 파일 이름을 추가
        for (StoreVO storeVO : stores) {
            if (storeVO.getFileId() != null) {
                String fileName = storeMapper.getFileNameByFileId(storeVO.getFileId());  // 파일 이름 조회
                if (fileName != null) {
                    storeVO.setSaveFileName(fileName);  // 파일 이름 설정
                }
            }
        }

        return stores;
    }
}
