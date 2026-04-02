package com.candidate.candidate_backend.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// Chỉ định annotation này đc bao gồm trong javaDoc của các lớp sử dụng nó.
// giúp tài liệu hóa rõ ràng khi @HbSizeList đc dùng
@Documented
// Đánh dấu là một bean variabletion
// HbSizeListConstraintValidator là lớp sử lý logic cho annotation này
@Constraint(validatedBy = HbSizeListConstraintValidator.class)
// Xác địch @HbSizeList có thể sử dụng cho các trường field và method
@Target({ElementType.METHOD, ElementType.FIELD})
// Chỉ định rằng annotation này sẽ được giữ lại trong bytecode và có thể truy cập tại runtime (thời điểm chạy chương trình) thông qua reflection. Điều này cần thiết để Bean Validation có thể đọc annotation khi kiểm tra dữ liệu.
@Retention(RetentionPolicy.RUNTIME)
public @interface HbSizeList {

  /** */
  int index() default 0;
  /** */
  String name() default "";
  /** */
  int min() default 1;
  /** */
  int max();
  /** */
  String message();
  /** */
  Class<?>[] groups() default {};
  /** */
  Class<? extends Payload>[] payload() default {};
}
