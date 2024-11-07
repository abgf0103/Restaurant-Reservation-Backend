package kr.re.kh.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StoreVO {
    private Long storeId;               //가게 고유ID
    private Long userId;                //가게 관리자 ID
    private String storeName;           //가게명
    private String address;             //주소
    private String storeHours;          //영업시간
    private String storeStatus;         //가게 상태 (준비중, 영업중)
    private String phone;               //가게 연락처
    private LocalDateTime createdAt;    //등록 시간
    private LocalDateTime updatedAt;    //수정 시간
}
