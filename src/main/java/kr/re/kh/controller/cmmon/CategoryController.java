package kr.re.kh.controller.cmmon;

import io.swagger.annotations.ApiOperation;
import kr.re.kh.model.vo.CategoryVO;
import kr.re.kh.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 카테고리 관련 Controller
 */
@RestController
@RequestMapping("/api/category")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation("카테고리 리스트 조회")
    @GetMapping("/list")
    public ResponseEntity<?> getCategoryList(@ModelAttribute CategoryVO categoryVO) {
        return ResponseEntity.ok(categoryService.getCategoryList());
    }

    @ApiOperation("가게 ID로 카테고리 조회")
    @GetMapping("/getCategoryIdByStoreId")
    public Long getCategoryByStoreId(Long storeId) {
        return categoryService.getCategoryIdByStoreId(storeId);
    }
}
