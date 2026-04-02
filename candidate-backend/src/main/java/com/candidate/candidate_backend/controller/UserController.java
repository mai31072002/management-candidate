package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.user.DtoCreateUserEmployee;
import com.candidate.candidate_backend.dto.user.DtoUserReq;
import com.candidate.candidate_backend.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userServicen;

    @PreAuthorize("@ss.hasPermission('USER_CREATE')")
    @PostMapping
    public CommonsRep dtoCreateUserEmployee(@RequestBody @Valid DtoCreateUserEmployee dtoCreateUserEmployee) {
        return userServicen.createUserEmployee(dtoCreateUserEmployee);
    }

    @PreAuthorize("@ss.hasPermission('USER_VIEW')")
    @GetMapping
    public CommonsRep getUser(
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "size", defaultValue = "10") @Min(0) @Max(100) int size
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.warn("username: {}", authentication.getName());
        log.warn("role: {}", authentication.getAuthorities());

        return userServicen.getUsers(page, size);
    }

    @PreAuthorize("@ss.hasPermission('USER_VIEW_DETAIL')")
    @GetMapping("/{userId}")
    public CommonsRep getUserById(@PathVariable("userId") String userId) {
        return userServicen.getUserById(userId);
    }

    @PreAuthorize("@ss.hasPermission('USER_VIEW_DETAIL')")
    @GetMapping("/info")
    public CommonsRep getInfo() {
        return userServicen.getInfo();
    }

    @PreAuthorize("@ss.hasPermission('USER_UPDATE')")
    @PutMapping("/{userId}")
    public CommonsRep updateUser(
            @Valid
            @RequestBody DtoUserReq dtoUserReq,
            @PathVariable("userId") String userId ) {
        return userServicen.updateUser(userId, dtoUserReq);
    }

    @PreAuthorize("@ss.hasPermission('USER_DELETE')")
    @DeleteMapping("/{userId}")
    public CommonsRep deleteUser(@PathVariable("userId") String userId) {
        return userServicen.deleteUser(userId);
    }

    @PreAuthorize("@ss.hasPermission('USER_SEARCH')")
    @GetMapping("/search")
    public CommonsRep searchUsername(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return userServicen.searchUsername(keyword, page, size);
    }

    @PreAuthorize("@ss.hasPermission('USER_IMPORT')")
    @PostMapping("/import-excel")
    public CommonsRep importExcel(@RequestParam("file") MultipartFile file) {
        return userServicen.importExcel(file);
    }

    @GetMapping("/download-template")
    public org.springframework.http.ResponseEntity<byte[]> downloadTemplate() {
        return userServicen.downloadTemplate();
    }
}
