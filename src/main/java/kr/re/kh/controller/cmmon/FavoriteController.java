package kr.re.kh.controller.cmmon;

import io.swagger.annotations.ApiOperation;
import kr.re.kh.model.vo.FavoriteVO;
import kr.re.kh.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
@Slf4j
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @ApiOperation("즐겨찾기 등록")
    @PostMapping("/insertFavorite")
    public void insertFavorite(@RequestBody FavoriteVO favoriteVO){
        favoriteService.insertFavorite(favoriteVO);
    };

    @ApiOperation("유저ID로 즐겨찾기 조회")
    @GetMapping("/getFavoriteByUserId")
    public List<FavoriteVO> getFavoriteByUserId(@RequestParam("userId") Long userId){
        return favoriteService.getFavoriteByUserId(userId);
    };

    @ApiOperation("유저ID와 가게ID로 즐겨찾기 등록 여부 확인")
    @PostMapping("/checkFavoriteByUserStore")
    public Long checkFavoriteByUserStore(@RequestBody FavoriteVO favoriteVO){
        Long userId = favoriteVO.getUserId();
        Long storeId = favoriteVO.getStoreId();
        return favoriteService.checkFavoriteByUserStore(userId, storeId);
    };

    @ApiOperation("아이디로 즐겨찾기 삭제")
    @DeleteMapping("/deleteFavoriteById")
    public void deleteFavoriteById(@RequestParam Long favoriteId){
        favoriteService.deleteFavoriteById(favoriteId);
    };
}
