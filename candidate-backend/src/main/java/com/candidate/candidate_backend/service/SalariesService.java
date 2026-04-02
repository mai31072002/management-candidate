package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.entity.DtbEmployees;
import com.candidate.candidate_backend.entity.DtbSalaries;
import com.candidate.candidate_backend.entity.DtbUser;
import com.candidate.candidate_backend.exception.BusinessException;
import com.candidate.candidate_backend.repositorry.*;
import com.candidate.candidate_backend.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SalariesService {
    @Autowired
    private SalariesRepository salariesRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeKeepingRepository timeKeepingRepository;

    @Autowired
    private OtDayRepository otDayRepository;

    @Autowired
    private RewardPenaltyRepository rewardPenaltyRepository;

    public CommonsRep CountSalary(String userId, YearMonth yearMonth) {

//        try {
            UUID uuidUserId = UUID.fromString(userId);

            DtbUser dtbUser = userRepository.findById(uuidUserId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "employeeId not found"));

            int m = yearMonth.getMonthValue();
            int y = yearMonth.getYear();

            // Lấy ra tổng ngày công
            Integer workDays = timeKeepingRepository.countWorkDays(uuidUserId, m, y);

            // Lấy lương cơ bản
            BigDecimal baseSalary = dtbUser.getEmployee().getBaseSalary();

            // Lương cho một ngày
            BigDecimal dailySalary = baseSalary.divide(BigDecimal.valueOf(22), 2, RoundingMode.HALF_UP);

            // Lương cho 1 giờ
            BigDecimal salaryHour = dailySalary.divide(BigDecimal.valueOf(8), 2, RoundingMode.HALF_UP);

            // Lấy số giờ ot trong một tháng (đã nhân hệ số lương)
            BigDecimal otHour = otDayRepository.sumOtHour(uuidUserId, m, y, 1);

            // Tính lương cho những ngày công đã làm
            BigDecimal actualBaseSalary = dailySalary.multiply(BigDecimal.valueOf(workDays));

            // Tính Lương Ot
            BigDecimal salaryOt = salaryHour
                .multiply(otHour).setScale(2, RoundingMode.HALF_UP);

            String yearMonthStr = yearMonth.toString();

            LocalDate fromDate = yearMonth.atDay(1);
            LocalDate toDate   = yearMonth.atEndOfMonth();

            // Tổng lương thưởng
            BigDecimal reward = rewardPenaltyRepository.sumByAmountType(
                    uuidUserId,
                    fromDate,
                    toDate,
                    0);

            // Phụ cấp
            BigDecimal allowance =
                    dtbUser.getEmployee().getAllowance() == null
                            ? BigDecimal.ZERO
                            : dtbUser.getEmployee().getAllowance();

            BigDecimal deductions = BigDecimal.ZERO;

            BigDecimal netSalary = actualBaseSalary
                .add(allowance)
                .add(salaryOt)
                .add(reward)
                .subtract(deductions);

            DtbSalaries dtbSalaries = salariesRepository.findByUserIdAndMonth(uuidUserId, yearMonth)
                    .orElseGet(DtbSalaries::new);

            dtbSalaries.setUserId(uuidUserId);
            dtbSalaries.setBaseSalary(baseSalary);
            dtbSalaries.setMonth(yearMonth);
            dtbSalaries.setTotalWorkDays(workDays);
            dtbSalaries.setOtAmount(salaryOt);
            dtbSalaries.setAllowance(allowance);
            dtbSalaries.setDeductions(deductions);
            dtbSalaries.setNetSalary(netSalary);

            DtbSalaries save = salariesRepository.save(dtbSalaries);

            return Helper.getServerResponse(HttpStatus.OK, "Tính lương thành công", dtbSalaries);
//        }
//        catch (Exception e) {
//            log.error( "Error tính lương", e);
//            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
//        }
    }

    public CommonsRep getSalary() {
        try {
            List<DtbSalaries> dtbSalaries = salariesRepository.findAll();

            return Helper.getServerResponse(HttpStatus.OK, "Lấy ra danh sách thành công", dtbSalaries);
        } catch (Exception e) {
            log.error( "Error lấy danh sách nhân viên", e);
            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
        }
    }

    public CommonsRep getSalaryByEmployeeId(String userId, YearMonth yearMonth) {
//        try {
            UUID uuidUserId = UUID.fromString(userId);

            DtbSalaries dtbSalaries = salariesRepository.findByUserIdAndMonth(uuidUserId, yearMonth)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "nhân viên hoặc tháng năm không " +
                            "đúng"));

            return Helper.getServerResponse(HttpStatus.OK, "Lấy ra lương thành công", dtbSalaries);
//        } catch (Exception e) {
//            log.error( "Error lấy lương chi tiết theo nhân viên", e);
//            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
//        }
    }

}
