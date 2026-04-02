package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.repositorry.EmployeeRepository;
import com.candidate.candidate_backend.repositorry.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalaryScheduler {

    private final UserRepository userRepository;
    private final SalariesService salariesService;

    @Scheduled(cron = "0 0 7,19 * * *")
    public void recalculateAllSalaries() {
        YearMonth currentMonth = YearMonth.now();
        userRepository.findAll()
                .forEach(e ->
                        salariesService.CountSalary(
                                e.getUserId().toString(),
                                currentMonth
                        )
                );
    }
}

