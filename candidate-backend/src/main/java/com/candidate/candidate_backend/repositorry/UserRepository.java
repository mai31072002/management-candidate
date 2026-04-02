package com.candidate.candidate_backend.repositorry;

import com.candidate.candidate_backend.entity.DtbUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<DtbUser, UUID>, JpaSpecificationExecutor<DtbUser> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Boolean> existsByEmployeeId(UUID employeeId);

    // Optional cho trả về null và bắt buộc phải sử lý lỗi không tồn tại
    Optional<DtbUser> findByUsernameAndIsDeletedFalse(String username);

    Optional<DtbUser> findFirstByUserIdAndIsDeletedIsFalse(UUID loginId);

    Optional<DtbUser> findByUsernameOrEmailAndIsDeletedFalse(String username, String email);

//    Page<DtbUser> findByUsernameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query(
        value = """
            SELECT *
            FROM dtb_user u
            WHERE u.is_deleted = false
            AND unaccent(lower(u.username))
                LIKE '%' || unaccent(lower(:keyword)) || '%'
            """,
        countQuery = """
            SELECT count(*)
            FROM dtb_user u
            WHERE u.is_deleted = false
            AND unaccent(lower(u.username))
                LIKE '%' || unaccent(lower(:keyword)) || '%'
            """,
        nativeQuery = true
    )
    Page<DtbUser> searchByUsernameIgnoreCaseAndAccent(
        @Param("keyword") String keyword,
        Pageable pageable
    );


    Optional<DtbUser> findByUsernameAndEmail(String username, String email);

    Page<DtbUser> findAllByIsDeletedFalse(Pageable pageable);
}
