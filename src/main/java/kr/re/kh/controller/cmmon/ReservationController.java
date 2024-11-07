package kr.re.kh.controller.cmmon;

import kr.re.kh.model.vo.ReservationVO;
import kr.re.kh.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations") // 예약 생성
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestBody ReservationVO reservation) {
        reservationService.createReservation(reservation);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<?> storeList(@ModelAttribute ReservationVO reservationVO) {
        return ResponseEntity.ok(reservationService.selectAllReservation());
    }

    @GetMapping("/{reservationId}") // 예약 ID로 예약 조회
    public ResponseEntity<ReservationVO> getReservationById(@PathVariable Long reservationId) {
        Optional<ReservationVO> reservation = reservationService.getReservationById(reservationId);
        return reservation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}") // 사용자 ID로 모든 예약 조회
    public List<ReservationVO> getAllReservationsByUserId(@PathVariable Long userId) {
        return reservationService.getAllReservationsByUserId(userId);
    }

    @GetMapping("/store/{storeId}") // 가게 ID로 모든 예약 조회
    public List<ReservationVO> getAllReservationsByStoreId(@PathVariable Long storeId) {
        return reservationService.getAllReservationsByStoreId(storeId);
    }

    @PutMapping("/{reservationId}") // 예약 정보 업데이트
    public ResponseEntity<Void> updateReservation(@PathVariable Long reservationId, @RequestBody ReservationVO reservation) {
        reservation.setReservationId(reservationId);
        reservationService.updateReservation(reservation);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reservationId}/status") // 예약 상태 업데이트
    public ResponseEntity<Void> updateReservationStatus(@PathVariable Long reservationId, @RequestBody String status) {
        reservationService.updateReservationStatus(reservationId, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reservationId}") // 예약 삭제
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
