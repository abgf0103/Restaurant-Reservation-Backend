package kr.re.kh.service;

import kr.re.kh.model.vo.MenuVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MenuService {
    void insertMenu(MenuVO menuVO);
    void updateMenu(MenuVO menuVO);
    void deleteMenu(Long menuId);
    MenuVO getMenuById(Long menuId);

    List<MenuVO> getMenuListByStoreId(Long storeId);
}
