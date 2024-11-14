package kr.re.kh.controller.cmmon;

import io.swagger.annotations.ApiOperation;
import kr.re.kh.model.vo.StoreCategoryVO;
import kr.re.kh.service.StoreCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storeCategory")
@Slf4j
public class StoreCategoryController {
    private final StoreCategoryService storeCategoryService;

    @Autowired
    public StoreCategoryController(StoreCategoryService storeCategoryService) {
        this.storeCategoryService = storeCategoryService;
    }

    @ApiOperation("가게의 카테고리를 저장")
    @PostMapping("/save")
    public StoreCategoryVO save(@RequestBody StoreCategoryVO storeCategoryVO) {
        return storeCategoryService.save(storeCategoryVO);
    }

    // StoreCategory 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<StoreCategoryVO>> getAll() {
        List<StoreCategoryVO> categories = storeCategoryService.findAll();
        return ResponseEntity.ok(categories);
    }

}
