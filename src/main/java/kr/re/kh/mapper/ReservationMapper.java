package kr.re.kh.mapper;

import kr.re.kh.model.vo.ReservationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReservationMapper {
    // 예약 저장
    void insertReservation(ReservationVO reservation);

    // 특정 예약 조회 by ID
    Optional<ReservationVO> findReservationById(@Param("reserveId") Long reserveId);

    // 특정 사용자의 모든 예약 조회
    List<ReservationVO> findAllReservationsByUserId(@Param("userId") Long userId);

    // 특정 가게의 모든 예약 조회
    List<ReservationVO> findAllReservationsByStoreId(@Param("storeId") Long storeId);

    // 예약 업데이트
    void updateReservation(ReservationVO reservation);

    // 예약 삭제
    void updateReservationStatus(@Param("reserveId") Long reserveId, @Param("status") String status);
    
    // 가게 회원 예약 상태 업데이트
    void deleteReservation(@Param("reserveId") Long reserveId);

    List<ReservationVO> selectAllReservation();

    // 예약 확정
    void confirmReservation(Long reserveId);

    // 예약 취소
    void cancelReservation(Long reserveId);
}