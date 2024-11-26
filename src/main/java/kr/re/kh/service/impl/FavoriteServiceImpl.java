package kr.re.kh.service.impl;

import kr.re.kh.mapper.FavoriteMapper;
import kr.re.kh.mapper.StoreMapper;
import kr.re.kh.model.vo.FavoriteVO;
import kr.re.kh.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteMapper favoriteMapper;

    public FavoriteServiceImpl(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    /**
     * 즐겨찾기 추가
     * @param favoriteVO
     */
    @Override
    public void insertFavorite(FavoriteVO favoriteVO) {
        favoriteMapper.insertFavorite(favoriteVO);
    }

    /**
     * 유저ID로 즐겨찾기 리스트 조회
     * @param userId
     * @return
     */
    @Override
    public List<FavoriteVO> getFavoriteByUserId(Long userId) {
        return favoriteMapper.getFavoriteByUserId(userId);
    }

    /**
     * 즐겨찾기 여부 체크
     * @param userId
     * @param storeId
     * @return
     */
    @Override
    public Long checkFavoriteByUserStore(Long userId, Long storeId) {
        return favoriteMapper.checkFavoriteByUserStore(userId, storeId);
    }

    /**
     * 즐겨찾기 취소
     * @param favoriteId
     */
    @Override
    public void deleteFavoriteById(Long favoriteId) {
        favoriteMapper.deleteFavoriteById(favoriteId);
    }
}
