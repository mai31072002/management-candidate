package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.rewardPenalty.DtoRewardPenaltyRep;
import com.candidate.candidate_backend.dto.rewardPenalty.DtoRewardPenaltyReq;
import com.candidate.candidate_backend.entity.DtbEmployees;
import com.candidate.candidate_backend.entity.DtbRewardPenalty;
import com.candidate.candidate_backend.entity.DtbUser;
import com.candidate.candidate_backend.exception.BusinessException;
import com.candidate.candidate_backend.mapper.RewardPenaltyMapper;
import com.candidate.candidate_backend.repositorry.EmployeeRepository;
import com.candidate.candidate_backend.repositorry.RewardPenaltyRepository;
import com.candidate.candidate_backend.repositorry.UserRepository;
import com.candidate.candidate_backend.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class RewardPenaltyService {

    @Autowired
    private RewardPenaltyRepository rewardPenaltyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    public CommonsRep createRewardPenalty(DtoRewardPenaltyReq req) {
//        try {
            if (rewardPenaltyRepository.existsByUser_UserIdAndMonthAndType
                    (req.getUserId(), req.getMonth(), req.getType())) {
                return Helper.getServerResponse(HttpStatus.BAD_GATEWAY, "Nhân viên đã có thưởng/phạt cùng loại trong " +
                        "tháng này", null);
            }

            DtbUser user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Nhân viên không tồn tại"));

            // Validate type
//            if (!"BONUS".equals(req.getType()) && !"PENALTY".equals(req.getType())) {
//                throw new BusinessException(HttpStatus.BAD_REQUEST, "Invalid type");
//            }

            DtbRewardPenalty entity =
                    RewardPenaltyMapper.toEntity(req, user);

            rewardPenaltyRepository.save(entity);

            return Helper.getServerResponse(
                    HttpStatus.OK,
                    "Tạo thưởng/phạt thành công",
                    RewardPenaltyMapper.toDto(entity)
            );

//        } catch (Exception e) {
//            log.error("Create reward/penalty error", e);
//            return Helper.getServerResponse(
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    "Lỗi hệ thống",
//                    null
//            );
//        }
    }

//    public CommonsRep getByEmployeeAndMonth(String employeeId, LocalDate month) {
//
//        UUID uuidEmployeeId = UUID.fromString(employeeId);
//
//        List<DtbRewardPenalty> list =
//                rewardPenaltyRepository.findByEmployeeIdAndMonth(uuidEmployeeId, month);
//
//        return Helper.getServerResponse(
//                HttpStatus.OK,
//                "Danh sách thưởng/phạt",
//                list.stream()
//                        .map(RewardPenaltyMapper::toDto)
//                        .toList()
//        );
//    }

    @Transactional(readOnly = true)
    public CommonsRep getRewardPenaltyByEmployee(
            String userId,
            LocalDate fromDate,
            LocalDate toDate,
            Integer type
    ) {
        try {
            UUID empId = UUID.fromString(userId);

            LocalDate from = fromDate != null
                ? fromDate
                : LocalDate.of(1900, 1, 1);

            LocalDate to = toDate != null
                ? toDate
                : LocalDate.of(9999, 12, 31);

            List<DtbRewardPenalty> list =
                    rewardPenaltyRepository.findByUserAndFilter(
                            empId, from, to, type
                    );

            List<DtoRewardPenaltyRep> data = list.stream()
                    .map(RewardPenaltyMapper::toDto)
                    .toList();

            return Helper.getServerResponse(
                    HttpStatus.OK,
                    "Lấy danh sách thưởng / phạt thành công",
                    data
            );

        } catch (IllegalArgumentException e) {
            return Helper.getServerResponse(
                    HttpStatus.BAD_REQUEST,
                    "employeeId không hợp lệ",
                    null
            );
        }
//        catch (Exception e) {
//            log.warn("Lỗi lấy danh sách thưởng / phạt", e);
//            return Helper.getServerResponse(
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    "Lỗi hệ thống",
//                    null
//            );
//        }
    }

    public CommonsRep updateRewardPenalty(String rewardPenaltyId, DtoRewardPenaltyReq dtoRewardPenaltyReq) {
//        try {
            UUID uuidRewardPenaltyId = UUID.fromString(rewardPenaltyId);

            DtbRewardPenalty dtbRewardPenalty = rewardPenaltyRepository.findByIdAndUser_UserId(
                    uuidRewardPenaltyId, dtoRewardPenaltyReq.getUserId()
            ).orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "rewardPenaltyId not found"));

            if (!hasChangeRewardPenalty(dtbRewardPenalty, dtoRewardPenaltyReq)) {
                return Helper.getServerResponse(HttpStatus.OK, "Không có thay đổi rewardPenalty", null);
            }

            dtbRewardPenalty.setMonth(dtoRewardPenaltyReq.getMonth());
            dtbRewardPenalty.setAmount(dtoRewardPenaltyReq.getAmount());
            dtbRewardPenalty.setType(dtoRewardPenaltyReq.getType());
            dtbRewardPenalty.setReason(dtoRewardPenaltyReq.getReason());

            // 3. Save
            rewardPenaltyRepository.save(dtbRewardPenalty);

            return Helper.getServerResponse(
                    HttpStatus.OK,
                    "Cập nhật thưởng / phạt thành công",
                    null
            );
//        } catch (Exception e) {
//            log.warn("Lỗi cập nhật thưởng / phạt", e);
//            return Helper.getServerResponse(
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    "Lỗi hệ thống",
//                    null
//            );
//        }
    }

    public CommonsRep deleteRewardPenalty(String rewardPenaltyId) {
//        try {
            UUID uuidRewardPenaltyId = UUID.fromString(rewardPenaltyId);

            DtbRewardPenalty dtbRewardPenalty =
                    rewardPenaltyRepository.findByIdAndIsDeletedFalse(uuidRewardPenaltyId).orElseThrow(
                            () -> new BusinessException(HttpStatus.BAD_REQUEST, "rewardPenaltyId not found")
                    );

            dtbRewardPenalty.setDeleted(true);

            rewardPenaltyRepository.save(dtbRewardPenalty);

            return Helper.getServerResponse(
                    HttpStatus.OK,
                    "Xóa" + dtbRewardPenalty.getReason() + "Thành công",
                    null
            );
//        } catch (Exception e) {
//            log.warn("Lỗi xóa thưởng / phạt", e);
//            return Helper.getServerResponse(
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    "Lỗi hệ thống",
//                    null
//            );
//        }
    }

    public boolean hasChangeRewardPenalty (DtbRewardPenalty dtbRewardPenalty, DtoRewardPenaltyReq dtoRewardPenaltyReq){
        return !Objects.equals(dtbRewardPenalty.getMonth(), dtoRewardPenaltyReq.getMonth()) ||
                !Objects.equals(dtbRewardPenalty.getAmount(), dtoRewardPenaltyReq.getAmount()) ||
                !Objects.equals(dtbRewardPenalty.getType(), dtoRewardPenaltyReq.getType()) ||
                !Objects.equals(dtbRewardPenalty.getReason(), dtoRewardPenaltyReq.getReason());
    }
}
