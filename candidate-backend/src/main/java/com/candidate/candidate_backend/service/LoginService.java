package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.login.DtoForgotPassword;
import com.candidate.candidate_backend.dto.login.DtoLoginRep;
import com.candidate.candidate_backend.dto.login.DtoLoginReq;
import com.candidate.candidate_backend.dto.login.DtoRefreshToken;
import com.candidate.candidate_backend.entity.DtbRole;
import com.candidate.candidate_backend.entity.DtbUser;
import com.candidate.candidate_backend.exception.BusinessException;
import com.candidate.candidate_backend.repositorry.RefreshTokenRepository;
import com.candidate.candidate_backend.repositorry.UserRepository;
import com.candidate.candidate_backend.util.Helper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.annotation.PostConstruct;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @PostConstruct
    public void validate() {
        if (SIGNER_KEY == null || SIGNER_KEY.isBlank()) {
            throw new IllegalStateException("Thiếu cấu hình: jwt.signerKey là bắt buộc!");
        }
    }

    public CommonsRep login(DtoLoginReq dtoLoginReq) {
        var login = userRepository.findByUsernameOrEmailAndIsDeletedFalse(dtoLoginReq.getUsername(), dtoLoginReq.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("valid.usernameOrPassword"));

//        System.out.println("login" + login);
//        System.out.println("Password raw: " + dtoLoginReq.getPassword());
//        System.out.println("Password encoded in DB: " + login.getPassword());
//
//        System.out.println(passwordEncoder.getClass());
//        System.out.println(passwordEncoder.matches("123567", "$2a$10$Rm1j6IwdhU7PfyGAZDK5P.6BOB9/enQvLeLToxEZW5LR/5FQE.WPe"));

        boolean result = passwordEncoder.matches(dtoLoginReq.getPassword(), login.getPassword());

        if (!result) {
            return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "Vui lòng kiểm tra lại thông tin của tài khoản mà bạn vừa nhập .", null);
        }

        String token;

        try {
            token = generateToken(login);
        } catch (KeyLengthException e) {
            throw new RuntimeException(e);
        }

        String refreshToken;
        try {
            refreshToken = generateRefreshToken(login);
        } catch (KeyLengthException e) {
            throw new RuntimeException(e);
        }

        DtoLoginRep dtoLoginRep = new DtoLoginRep();

//        DtbRefreshToken dtbRefreshToken = refreshTokenService.createRefreshToken(login);
//
//        dtoLoginRep.setRefreshToken(dtbRefreshToken.getTokenId());
        dtoLoginRep.setRefreshToken(refreshToken);
        dtoLoginRep.setAccessToken(token);
        dtoLoginRep.setUsername(login.getUsername());

        return Helper.getServerResponse(HttpStatus.OK, "Login thành công", dtoLoginRep);
    }

    public CommonsRep forgotPassword(DtoForgotPassword dtoForgotPassword) {
        Optional<DtbUser> optionalUser = userRepository.findByUsernameAndEmail(
                dtoForgotPassword.getUsername(),
                dtoForgotPassword.getEmail()
        );

        if (optionalUser.isEmpty()) {
            return Helper.getServerResponse(
                    HttpStatus.BAD_REQUEST,
                    "Username hoặc email không đúng!",
                    null
                    );
        }

        DtbUser dtbUser = optionalUser.get();

        dtbUser.setPassword(passwordEncoder.encode(dtoForgotPassword.getNewPassword()));

        DtbUser save = userRepository.save(dtbUser);

        return Helper.getServerResponse(HttpStatus.OK, "Lấy lại mật khẩu thành công!", null);

    }

//    public CommonsRep refreshToken(DtoRefreshToken dtoRefreshToken) {
//        // Kiểm tra token tồn tại và còn thời hạn sử dụng
//        DtbRefreshToken dtbRefreshToken  = refreshTokenService.verifyRefreshToken(dtoRefreshToken.getTokenId());
//
//        String newAccessToken;
//
//        try {
//            newAccessToken = generateToken(dtbRefreshToken.getUser());
//        } catch (KeyLengthException e) {
//            throw new RuntimeException(e);
//        }
//
//        DtoLoginRep dtoLoginRep = new DtoLoginRep();
//        dtoLoginRep.setAccessToken(newAccessToken);
//        dtoLoginRep.setRefreshToken(dtbRefreshToken.getTokenId());
//
//        return Helper.getServerResponse(HttpStatus.OK, "Refresh token thành công", dtoLoginRep);
//    }

    public CommonsRep refreshToken(DtoRefreshToken dtoRefreshToken) {
        try {
            JWSObject jwsObject = JWSObject.parse(dtoRefreshToken.getRefreshToken());
            jwsObject.verify(new MACVerifier(SIGNER_KEY.getBytes()));

            JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
            String username = claims.getSubject();

            DtbUser user = userRepository.findByUsernameAndIsDeletedFalse(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not exist"));

            String newAccessToken = generateToken(user);

            DtoLoginRep dtoLoginRep = new DtoLoginRep();
            dtoLoginRep.setAccessToken(newAccessToken);
            dtoLoginRep.setRefreshToken(dtoRefreshToken.getRefreshToken()); // vẫn dùng token cũ
            dtoLoginRep.setUsername(user.getUsername());
            return Helper.getServerResponse(HttpStatus.OK, "Refresh token thành công", dtoLoginRep);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token không hợp lệ");
        }
    }

    public String generateToken(DtbUser dtbUser) throws KeyLengthException {

        List<String> roles = dtbUser.getRoles()
            .stream()
            .map(DtbRole::getRoleName)
            .toList();

        List<String> permissions = dtbUser.getRoles()
            .stream()
            .flatMap(role -> role.getPermissionName().stream())
            .map(p -> p.getPermissionName())
            .distinct()
            .toList();

        // body cần gửi đi
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(dtbUser.getUsername()) // đại diện user đăng nhập
                .issuer("chienoq.com")
                .issueTime(new Date()) // Ngày tạo token
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                )) // Khai báo token hết hạn trong bao lâu
                .jwtID(UUID.randomUUID().toString())
//                .claim("UId", buildEmployeeId(dtbUser))
                .claim("UId", dtbUser.getUserId())
                .claim("token_type", "accessToken")
                .claim("scope", roles)
                .claim("permission", permissions)
                .build();

//        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
//
//        JWSObject jwsObject = new JWSObject(header, payload);
//
//        try {
//            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
//            return jwsObject.serialize();
//        } catch (JOSEException e) {
//            log.warn("cannot create token");
//            throw new RuntimeException(e);
//        }
        return signToken(jwtClaimsSet);
    }

//    public String buildEmployeeId(DtbUser dtbUser) {
//        if (dtbUser.getEmployee() != null) {
//            return dtbUser.getEmployee().getId().toString();
//        }
//        return null;
//    }

    public String generateRefreshToken(DtbUser dtbUser) throws KeyLengthException {

        // body cần gửi đi
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(dtbUser.getUsername()) // đại diện user đăng nhập
                .issuer("chienoq.com")
                .issueTime(new Date()) // Ngày tạo
                .expirationTime(new Date(
                        Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()
                )) // Khai báo token hết hạn trong bao lâu
                .claim("token_type", "refreshToken")
                .build();

        return signToken(jwtClaimsSet);
    }

    private String signToken(JWTClaimsSet claims) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256); // truyền vào thuật toán mã hóa
            JWSObject jwsObject = new JWSObject(header, new Payload(claims.toJSONObject()));
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Cannot sign JWT", e);
        }
    }
}
