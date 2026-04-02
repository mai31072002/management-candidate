package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.otday.DtoOtDayReq;
import com.candidate.candidate_backend.service.OtDayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/ot-date")
public class OtDateController {

    @Autowired
    private OtDayService otDayService;

    @PreAuthorize("@ss.hasPermission('OT_DATE_CREATE')")
    @PostMapping
    public CommonsRep createOtDay(@Valid @RequestBody DtoOtDayReq dtoOtDayReq) {
        return otDayService.createOtDay(dtoOtDayReq);
    }

    @PreAuthorize("@ss.hasPermission('OT_DATE_VIEW')")
    @GetMapping
    public CommonsRep getOtDay(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,

            @RequestParam(required = false) Integer status
    ) {
        return otDayService.getOtDay(page, size, fromDate, toDate, status);
    }

    @PreAuthorize("@ss.hasPermission('OT_DATE_VIEW_DETAIL')")
    @GetMapping("/{userId}")
    public CommonsRep getOtDayByEmployeeId(
            @PathVariable(name = "userId") String userId,
            @RequestParam(name = "month") YearMonth month
    ) {
        return otDayService.getOtDayByUserId(userId, month);
    }

    @PreAuthorize("@ss.hasPermission('OT_DATE_UPDATE')")
    @PutMapping("/{otId}")
    public CommonsRep updateOtDay(
            @Valid
            @PathVariable(name = "otId") String otId,
            @RequestBody DtoOtDayReq dtoOtDayReq
    ) {
        return otDayService.updateOtDay(otId, dtoOtDayReq);
    }

    @PreAuthorize("@ss.hasPermission('OT_DATE_DELETE')")
    @DeleteMapping("/{otId}")
    public CommonsRep deleteOtDay(@PathVariable(name = "otId") String otId) {
        return otDayService.deleteOtDay(otId);
    }
}
