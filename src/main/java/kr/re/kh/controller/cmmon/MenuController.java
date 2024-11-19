package kr.re.kh.controller.cmmon;

import io.swagger.annotations.ApiOperation;
import kr.re.kh.model.vo.MenuVO;
import kr.re.kh.model.vo.StoreVO;
import kr.re.kh.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/api/store/menu")
@Slf4j
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @ApiOperation("메뉴 추가")
    @PostMapping("/insert")
    public void insertMenu(@RequestBody MenuVO menuVO) {
        menuService.insertMenu(menuVO);
    }

    @ApiOperation("메뉴 정보 조회")
    @GetMapping("/getMenuById")
    public MenuVO getMenuById(@RequestParam Long menuId) {
        return menuService.getMenuById(menuId);
    }

    @ApiOperation("메뉴 수정")
    @PostMapping("/update")
    public void updateMenu(@RequestBody MenuVO menuVO) {
        menuService.updateMenu(menuVO);
    }

    @ApiOperation("메뉴 삭제 (사업자, status를 변경해 실제로 삭제하진 않고 조회되지 않게 함)")
    @GetMapping("/delete")
    public void requestStoreDelete(@RequestParam Long menuId) {
        menuService.deleteMenu(menuId);
    }

    @ApiOperation("가게ID로 메뉴 리스트 조회")
    @GetMapping("/getMenuListByStoreId")
    public List<MenuVO> getMenuListByStoreId(@RequestParam("storeId") Long storeId) {
        return menuService.getMenuListByStoreId(storeId);
    }
}
