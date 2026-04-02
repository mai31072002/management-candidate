package com.candidate.candidate_backend.exception;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.util.Helper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // Validate exception res
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<CommonsRep> handleValidate(MethodArgumentNotValidException exception) {
//
//        String errorMessage = exception.getBindingResult().getFieldErrors()
//                .stream()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                .collect(Collectors.joining(", "));
//
//        CommonsRep response = Helper.getServerResponse(
//                HttpStatus.BAD_REQUEST,
//                errorMessage,
//                null
//        );
//
//        return ResponseEntity.status(exception.getStatusCode()).body(response);
//    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<CommonsRep> handleInvalidFormat(HttpMessageNotReadableException ex) {
//
//        String messages = "Dữ liệu gửi lên không đúng định dạng";
//
//        if (ex.getMostSpecificCause() != null) {
//            messages = ex.getMostSpecificCause().getMessage();
//        }
//
//        CommonsRep response = Helper.getServerResponse(
//                HttpStatus.BAD_REQUEST,
//                String.join(", ", messages),
//                null
//        );
//
//        return ResponseEntity.badRequest().body(response);
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<CommonsRep> handleConstraint(ConstraintViolationException ex) {
//
//        String message = ex.getConstraintViolations()
//                .stream()
//                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
//                .collect(Collectors.joining(", "));
//
//        CommonsRep response = Helper.getServerResponse(
//                HttpStatus.BAD_REQUEST,
//                message,
//                null
//        );
//
//        return ResponseEntity.badRequest().body(response);
//    }
//
//    public ResponseEntity<CommonsRep> handleDBException(
//            DataIntegrityViolationException ex) {
//
//        String message = "Dữ liệu vi phạm ràng buộc";
//
//        // Kiểm tra nguyên nhân sâu hơn (optional)
//        if (ex.getRootCause() != null) {
//            String rootMessage = ex.getRootCause().getMessage();
//
//            if (rootMessage.contains("duplicate") ||
//                rootMessage.contains("Unique")) {
//                message = "Dữ liệu đã tồn tại (vi phạm unique)";
//            }
//
//            if (rootMessage.contains("foreign key")) {
//                message = "Không thể xóa vì đang được tham chiếu";
//            }
//        }
//
//        CommonsRep response = new CommonsRep(
//                HttpStatus.BAD_REQUEST.value(),
//                message,
//                null
//        );
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

    // System Exception (Fallback)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<CommonsRep> handleSystem(Exception ex) {
//
//        // Có thể log ở đâyz
//        // log.error("System error", ex);
//
//        CommonsRep response = Helper.getServerResponse(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                "Lỗi hệ thống, vui lòng thử lại sau",
//                null
//        );
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(response);
//    }
}