package kr.re.kh.model.vo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MenuVO {

    private Long menuId;
    private Long storeId;
    private String menuName;
    private String description;
    private int price;

}
