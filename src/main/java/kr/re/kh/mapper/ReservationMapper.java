package kr.re.kh.mapper;

import kr.re.kh.model.vo.ReservationVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReservationMapper {

    // 예약 저장
    void insertReservation(ReservationVO reservation);

    // 특정 예약 조회 by ID
    Optional<ReservationVO> findReservationById(Long reservationId);

    // 특정 사용자의 모든 예약 조회
    List<ReservationVO> findAllReservationsByUserId(Long userId);

    // 특정 가게의 모든 예약 조회
    List<ReservationVO> findAllReservationsByStoreId(Long storeId);

    // 예약 업데이트
    void updateReservation(ReservationVO reservation);

    // 예약 삭제
    void deleteReservation(Long reservationId);

    // 가게 회원 예약 상태 업데이트
    void updateReservationStatus(Long reservationId, String status);

    List<ReservationVO> selectAllReservation();
}
