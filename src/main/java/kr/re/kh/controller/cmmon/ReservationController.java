package kr.re.kh.controller.cmmon;

import kr.re.kh.annotation.CurrentUser;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.vo.ReservationVO;
import kr.re.kh.model.payload.response.ApiResponse;
import kr.re.kh.service.ReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@Slf4j
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 생성
    @PostMapping("/save")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> createReservation(@RequestBody ReservationVO reservation,
                                               @CurrentUser CustomUserDetails currentUser) {
        reservation.setUserId(currentUser.getId());  // 현재 로그인한 사용자의 ID 설정
        reservationService.createReservation(reservation);
        return ResponseEntity.ok(new ApiResponse(true, "예약이 생성되었습니다."));
    }

    // 사용자의 모든 예약 조회
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<List<ReservationVO>> getUserReservations(@CurrentUser CustomUserDetails currentUser) {
        List<ReservationVO> reservations = reservationService.getAllReservationsByUserId(currentUser.getId());
        return ResponseEntity.ok(reservations);
    }

    // 가게 ID로 모든 예약 조회 (가게 측에서 사용)
    @GetMapping("/store/{storeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<List<ReservationVO>> getAllReservationsByStoreId(@PathVariable Long storeId) {
        List<ReservationVO> reservations = reservationService.getAllReservationsByStoreId(storeId);
        return ResponseEntity.ok(reservations);
    }

    // 예약 ID로 특정 예약 조회
    @GetMapping("/view/{reserveId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> getReservationById(@PathVariable Long reserveId,
                                                @CurrentUser CustomUserDetails currentUser) {
        Optional<ReservationVO> reservation = reservationService.getReservationById(reserveId);
        return reservation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 예약 상태 업데이트 (가게 회원 전용)
    @PutMapping("/update-status/{reserveId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> updateReservationStatus(@PathVariable Long reserveId,
                                                     @RequestBody String status) {
        reservationService.updateReservationStatus(reserveId, status);
        return ResponseEntity.ok(new ApiResponse(true, "예약 상태가 업데이트되었습니다."));
    }

    // 예약 삭제
    @DeleteMapping("/delete/{reserveId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<?> deleteReservation(@PathVariable Long reserveId,
                                               @CurrentUser CustomUserDetails currentUser) {
        if (!reservationService.isReservationOwner(reserveId, currentUser.getId())) {
            return ResponseEntity.status(403).body(new ApiResponse(false, "권한이 없습니다."));
        }
        reservationService.deleteReservation(reserveId);
        return ResponseEntity.ok(new ApiResponse(true, "예약이 삭제되었습니다."));
    }
}
