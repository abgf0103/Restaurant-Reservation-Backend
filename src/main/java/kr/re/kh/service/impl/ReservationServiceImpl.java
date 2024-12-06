package kr.re.kh.service.impl;

import kr.re.kh.mapper.ReservationMapper;
import kr.re.kh.model.vo.NotificationVO;
import kr.re.kh.model.vo.ReservationVO;
import kr.re.kh.service.NotificationService;
import kr.re.kh.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final NotificationService notificationService;

    @Override
    public void createReservation(ReservationVO reservation) {
        reservationMapper.insertReservation(reservation);
    }

    @Override
    public Optional<ReservationVO> getReservationById(Long reserveId) {
        Optional<ReservationVO> reservation = reservationMapper.findReservationById(reserveId);
        log.info("Reservation fetched: {}", reservation); // 쿼리 결과 로그 추가
        return reservation;
    }

    @Override
    public List<ReservationVO> getAllReservationsByUserId(Long userId) {
        return reservationMapper.findAllReservationsByUserId(userId);
    }

    @Override
    public List<ReservationVO> getAllReservationsByStoreId(Long storeId) {
        return reservationMapper.findAllReservationsByStoreId(storeId);
    }

    @Override
    public void updateReservation(ReservationVO reservation) {
        reservationMapper.updateReservation(reservation);
    }

    @Override
    public void updateReservationStatus(Long reserveId, int status, Long userId) {
        reservationMapper.updateReservationStatus(reserveId, status);

        // 예약 정보 가져오기
        Optional<ReservationVO> reservationOpt = reservationMapper.findReservationById(reserveId);
        if (!reservationOpt.isPresent()) {
            throw new RuntimeException("예약을 찾을 수 없습니다: ID = " + reserveId);
        }

        ReservationVO reservation = reservationOpt.get();

        // 알림 메시지 설정
        String message;
        switch (status) {
            case 1:
                message = reservation.getStoreName() + "의 예약이 확정되었습니다.";
                break;
            case 3:
                message = reservation.getStoreName() + "의 예약이 취소되었습니다.";
                break;
            case 2:
                message = reservation.getStoreName() + "의 예약이 완료되었습니다.";
                break;
            default:
                message = reservation.getStoreName() + "의 예약 상태가 업데이트되었습니다.";
                break;
        }

        // 알림 생성
        NotificationVO notification = NotificationVO.builder()
                .userId(userId)
                .message(message)
                .build();
        notificationService.createNotification(notification);
    }

    @Override
    public void deleteReservation(Long reserveId) {
        reservationMapper.deleteReservation(reserveId);
    }

    @Override
    public boolean isReservationOwner(Long reserveId, Long userId) {
        Optional<ReservationVO> reservation = reservationMapper.findReservationById(reserveId);
        return reservation.map(res -> res.getUserId().equals(userId)).orElse(false);
    }

    @Override
    public void confirmReservation(Long reserveId) {
        reservationMapper.confirmReservation(reserveId);
    }

    @Override
    public void cancelReservation(Long reserveId) {
        reservationMapper.cancelReservation(reserveId);
    }

    @Override
    public void completeReservation(Long reserveId) {
        reservationMapper.completeReservation(reserveId);
    }
}
