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
        storeMapper.insertStore(storeVO);

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
}