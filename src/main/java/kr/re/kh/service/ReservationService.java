package kr.re.kh.service;

import kr.re.kh.model.vo.ReservationVO;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    void createReservation(ReservationVO reservation);

    Optional<ReservationVO> getReservationById(Long reserved);

    List<ReservationVO> getAllReservationsByUserId(Long userId);

    List<ReservationVO> getAllReservationsByStoreId(Long storeId);

    void updateReservation(ReservationVO reservation);

    void updateReservationStatus(Long reserveId, int status, Long userId);  // 예약 상태 업데이트 후 알림 생성

    void deleteReservation(Long reserveId);

    boolean isReservationOwner(Long reserveId, Long userId); // 사용자 권한 확인 메서드

    void confirmReservation(Long reserveId);    // 예약 확정 후 알림 생성

    void cancelReservation(Long reserveId);     // 예약 취소 후 알림 생성

    void completeReservation(Long reserveId);   // 예약 완료 후 알림 생성
}
