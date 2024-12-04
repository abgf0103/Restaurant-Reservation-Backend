package kr.re.kh.controller.cmmon;

import io.swagger.annotations.ApiOperation;
import kr.re.kh.annotation.CurrentUser;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.vo.StoreVO;
import kr.re.kh.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 가게 정보 Controller
 */
@RestController
@RequestMapping("/api/store")
@Slf4j
public class StoreController {
    private final StoreService storeService;
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @ApiOperation("가게 추가")
    @PostMapping("/insert")
    public void insertStore(@RequestBody StoreVO storeVO) {
        storeService.insertStore(storeVO);
    }

    @ApiOperation("모든 가게 조회")
    @GetMapping("/list")
    public ResponseEntity<?> storeList(@ModelAttribute StoreVO storeVO) {
        return ResponseEntity.ok(storeService.selectAllStore());
    }

    @ApiOperation("비슷한 가게 조회")
    @GetMapping("/getSimilarStoreList")
    public ResponseEntity<?> getSimilarStoreList(@RequestParam Long categoryId,
                                                 @RequestParam Long storeId) {
        Map<String, Long> params = Map.of("categoryId", categoryId, "storeId", storeId);
        return ResponseEntity.ok(storeService.getSimilarStoreList(params));
    }

    @ApiOperation("가게 수정")
    @PostMapping("/update")
    public void updateStore(@RequestBody StoreVO storeVO) {
        storeService.updateStore(storeVO);
    }

    @ApiOperation("가게 삭제")
    @DeleteMapping("/delete")
    public void deleteStore(Long storeId) {
        storeService.deleteStore(storeId);
    }

    @ApiOperation("가게 정보 조회")
    @GetMapping("/view/{storeId}")
    public StoreVO viewStore(@PathVariable Long storeId) {
        return storeService.viewStore(storeId);
    }

    @ApiOperation("내 가게 조회")
    @GetMapping("/mystore")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    //사업자 사용자만 요청 가능하게 수정
    public ResponseEntity<?> getMyStore(@CurrentUser CustomUserDetails currentUser) {
        Long userId = currentUser.getId();
        List<StoreVO> myStores = storeService.selectMyStoreByUserId(userId);
        return ResponseEntity.ok(myStores);
    }

    @ApiOperation("가게 이름 중복 조회")
    @GetMapping("/hasStoreName")
    public boolean hasStoreName(@RequestParam String storeName) {
        return storeService.hasStoreName(storeName);
    }

    @ApiOperation("가게 이름으로 가게 ID 조회")
    @GetMapping("/findStoreIdByStoreName")
    public Long findStoreIdByStoreName(@RequestParam String storeName) {
        return storeService.findStoreIdByStoreName(storeName);
    }

    @ApiOperation("가게 등록 수락(관리자)")
    @GetMapping("/acceptStoreRegister")
    public void acceptStoreRegister(@RequestParam Long storeId) {
        storeService.acceptStoreRegister(storeId);
    }

    @ApiOperation("가게 삭제 요청(사업자)")
    @GetMapping("/requestStoreDelete")
    public void requestStoreDelete(@RequestParam Long storeId) {
        storeService.requestStoreDelete(storeId);
    }

    @ApiOperation("가게 삭제 수락(관리자)")
    @GetMapping("/acceptStoreDelete")
    public void acceptStoreDelete(@RequestParam Long storeId) {
        storeService.acceptStoreDelete(storeId);
    }

    @ApiOperation("카테고리ID로 가게 조회")
    @GetMapping("/selectStoreByCategoryId")
    public ResponseEntity<?> selectStoreByCategoryId(@RequestParam Long categoryId) {
        List<StoreVO> storeList = storeService.selectStoreByCategoryId(categoryId);
        return ResponseEntity.ok(storeList);
    }


    @ApiOperation("키워드로 가게 검색")
    @GetMapping("/search")
    public ResponseEntity<?> searchStore(@RequestParam String searchKeyword) {
        return ResponseEntity.ok(storeService.searchStore(searchKeyword));
    }

    @ApiOperation("유저ID로 즐겨찾기 등록된 가게 리스트 조회")
    @GetMapping("/getFavoriteStoreList")
    public ResponseEntity<?> getFavoriteStoreList(@RequestParam Long userId) {
        return ResponseEntity.ok(storeService.getFavoriteStoreList(userId));
    }

    @ApiOperation("가게 리스트 조회(어드민)")
    @GetMapping("getStoreListForAdmin")
    public ResponseEntity<?> getStoreListForAdmin() {
        return ResponseEntity.ok(storeService.getStoreListForAdmin());
    }

}
