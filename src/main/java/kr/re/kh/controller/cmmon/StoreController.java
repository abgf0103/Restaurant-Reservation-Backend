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

    @PostMapping("/search")
    public ResponseEntity<?> searchStore(@ModelAttribute StoreVO storeVO) {

        return null;
    }

}
