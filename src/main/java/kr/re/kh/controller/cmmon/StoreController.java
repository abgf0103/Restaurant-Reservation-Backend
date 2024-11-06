package kr.re.kh.controller.cmmon;

import io.swagger.annotations.ApiOperation;
import kr.re.kh.model.vo.StoreVO;
import kr.re.kh.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
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
    @PostMapping("/delete")
    public void deleteStore(Long storeId) {
        storeService.deleteStore(storeId);
    }
}
