package kr.re.kh.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CategoryVO {
    private Long categoryId;        //카테고리 고유ID
    private String categoryTitle;   //카테고리 제목
}
