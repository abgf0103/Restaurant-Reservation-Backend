package kr.re.kh.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StoreVO {
    private Long storeId;       //가게 고유ID
    private Long userId;        //가게 관리자 ID
    private String storeName;   //가게명
    private String address;     //주소
    private String storeHours;  //영업시간


}
