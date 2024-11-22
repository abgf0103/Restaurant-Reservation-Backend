package kr.re.kh.service;

import kr.re.kh.model.vo.FavoriteVO;

import java.util.List;

public interface FavoriteService {
    void insertFavorite(FavoriteVO favoriteVO);             //즐겨찾기 등록

    List<FavoriteVO> getFavoriteByUserId(Long userId);                  //유저ID로 즐겨찾기 조회

    Long checkFavoriteByUserStore(Long userId, Long storeId);         //유저ID와 가게ID로 즐겨찾기 등록 여부 확인

    void deleteFavoriteById(Long favoriteId);               //고유ID로 즐겨찾기 삭제
}
