package kr.re.kh.service.impl;

import kr.re.kh.mapper.MenuMapper;
import kr.re.kh.model.vo.MenuVO;
import kr.re.kh.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MenuServiceImpl implements MenuService {
    private final MenuMapper menuMapper;

    public MenuServiceImpl(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    /**
     * 가게 추가
     * @param menuVO
     */
    @Override
    public void insertMenu(MenuVO menuVO) {
        menuMapper.insertMenu(menuVO);
    }

    /**
     * 가게 수정
     * @param menuVO
     */
    @Override
    public void updateMenu(MenuVO menuVO) {
        menuMapper.updateMenu(menuVO);
    }

    /**
     * 가게 삭제 (status를 변경)
     * @param menuId
     */
    @Override
    public void deleteMenu(Long menuId) {
        menuMapper.deleteMenu(menuId);
    }

    /**
     * 가게ID로 메뉴 리스트 조회
     * @param storeId
     * @return
     */
    @Override
    public List<MenuVO> getMenuListByStoreId(Long storeId) {
        return menuMapper.getMenuListByStoreId(storeId);
    }
}
