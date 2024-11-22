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
    @Override
    public void insertFavorite(FavoriteVO favoriteVO) {
        favoriteMapper.insertFavorite(favoriteVO);
    }

    @Override
    public List<FavoriteVO> getFavoriteByUserId(Long userId) {
        return favoriteMapper.getFavoriteByUserId(userId);
    }

    @Override
    public Long checkFavoriteByUserStore(Long userId, Long storeId) {
        return favoriteMapper.checkFavoriteByUserStore(userId, storeId);
    }

    @Override
    public void deleteFavoriteById(Long favoriteId) {
        favoriteMapper.deleteFavoriteById(favoriteId);
    }
}
