package com.candidate.candidate_backend.repositorry;

import com.candidate.candidate_backend.entity.DtbEmployees;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<DtbEmployees, UUID> {
    Optional<DtbEmployees> findByEmployeesCodeAndIsDeletedFalse(String employeesCode);

    Page<DtbEmployees> findAllByIsDeletedFalse(Pageable pageable);

    Optional<DtbEmployees> findByIdAndIsDeletedFalse(UUID employeeId);

    long countByDepartmentIdAndIsDeletedFalse(UUID departmentId);

    long countByPositionIdAndIsDeletedFalse(UUID positionId);

//    // Containing tương đương LIKE để tìm kiếm
//    Page<DtbEmployees> findByFullNameContainingAndIsDeletedFalse(String keyword, Pageable pageable);

    @Query(
        value = """
            SELECT *
            FROM dtb_employees e
            WHERE e.is_deleted = false
            AND unaccent(lower(e.full_name))
                LIKE '%' || unaccent(lower(:keyword)) || '%'
            """,
        countQuery = """
            SELECT count(*)
            FROM dtb_employees e
            WHERE e.is_deleted = false
            AND unaccent(lower(e.full_name))
                LIKE '%' || unaccent(lower(:keyword)) || '%'
            """,
        nativeQuery = true
    )
    Page<DtbEmployees> searchByFullNameIgnoreCaseAndAccent(
        @Param("keyword") String keyword,
        Pageable pageable
    );
}
