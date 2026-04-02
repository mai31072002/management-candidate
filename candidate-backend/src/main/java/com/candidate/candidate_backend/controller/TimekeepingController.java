package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.timeKeeping.DtoTimeKeepingReq;
import com.candidate.candidate_backend.service.TimeKeepingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/time-keeping")
public class TimekeepingController {

    @Autowired
    private TimeKeepingService timeKeepingService;

    @PreAuthorize("@ss.hasPermission('TIME_KEEPING_CREATE')")
    @PostMapping
    public CommonsRep createTimekeeping(@RequestBody @Valid DtoTimeKeepingReq dtoTimeKeepingReq) {
        return timeKeepingService.createTimekeeping(dtoTimeKeepingReq);
    }

    @PreAuthorize("@ss.hasPermission('TIME_KEEPING_VIEW')")
    @GetMapping
    public CommonsRep getTimeKeeping(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return timeKeepingService.getTimeKeeping(page, size);
    }

//    @GetMapping("/{timeKeepingId}")
//    public CommonsRep getById(@PathVariable(name = "timeKeepingId") String timeKeepingId) {
//        return timeKeepingService.getById(timeKeepingId);
//    }

    @PreAuthorize("@ss.hasPermission('TIME_KEEPING_VIEW_DETAIL')")
    @GetMapping("/{userId}")
    public CommonsRep getTimeKeepingByEmployee(
            @PathVariable(name = "userId") String userId,
            @RequestParam(name = "yearMonth")
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth
    ) {
        return timeKeepingService.getTimeKeepingByEmployee(userId, yearMonth);
    }

    @PreAuthorize("@ss.hasPermission('TIME_KEEPING_DELETE')")
    @DeleteMapping("/{timeKeepingId}")
    public CommonsRep deleteTimeKeeping(@PathVariable(name = "timeKeepingId") String timeKeepingId) {
        return timeKeepingService.deleteTimeKeeping(timeKeepingId);
    }

    @PreAuthorize("@ss.hasPermission('TIME_KEEPING_UPDATE')")
    @PutMapping("/{timeKeepingId}")
    public CommonsRep updateTimeKeeping(
            @Valid
            @PathVariable(name = "timeKeepingId") String timeKeepingId,
            @RequestBody DtoTimeKeepingReq dtoTimeKeepingReq
    ) {
        return timeKeepingService.updateTimeKeeping(timeKeepingId, dtoTimeKeepingReq);
    }
}
