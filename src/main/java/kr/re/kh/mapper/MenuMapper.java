package kr.re.kh.mapper;

import kr.re.kh.model.vo.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    void insertMenu(MenuVO menuVO);                     //메뉴 추가
    void updateMenu(MenuVO menuVO);                     //메뉴 수정
    void deleteMenu(Long menuId);                       //메뉴 삭제

    List<MenuVO> getMenuListByStoreId(Long storeId);     //가게 ID로 메뉴 조회

}
