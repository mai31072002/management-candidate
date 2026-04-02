package com.candidate.candidate_backend.data.seed;

import com.candidate.candidate_backend.entity.DtbPermission;
import com.candidate.candidate_backend.repositorry.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor // Lombok tạo constructor cho field final
@Slf4j
public class PermissionSeeder {

    private final PermissionRepository permissionRepository;

    @Bean
    public ApplicationRunner initPermissions() {
        return args -> {
            log.info("Bắt đầu quét và tạo permissions...");
            
            Set<String> extractedPermissions = new HashSet<>();
            
            // Scan các controller để tìm @PreAuthorize
            extractedPermissions.addAll(scanControllersForPermissions());

            // Thêm các permissions mặc định nếu cần
            extractedPermissions.addAll(getDefaultPermissions());

            // Lưu vào DB
            int createdCount = 0;
            int updatedCount = 0;
            
            for (String permissionName : extractedPermissions) {
                if (!permissionRepository.existsByPermissionName(permissionName)) {
                    DtbPermission permission = new DtbPermission();
                    permission.setPermissionName(permissionName);
                    permission.setDescription("Auto-generated permission for: " + permissionName);
                    permissionRepository.save(permission);
                    createdCount++;
                    log.info("Đã tạo permission mới: {}", permissionName);
                } else {
                    updatedCount++;
                }
            }

            log.info("Hoàn thành! Tạo {} permissions mới, cập nhật {} permissions có sẵn", 
                    createdCount, updatedCount);
        };
    }

    private Set<String> scanControllersForPermissions() {
        Set<String> permissions = new HashSet<>();
        SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        
        try {
            // Scan tất cả các class trong package controller
            Resource[] resources = resolver.getResources("classpath*:com/candidate/candidate_backend/controller/**/*.class");
            
            // Pattern để extract permission từ @PreAuthorize("@ss.hasPermission('PERMISSION_NAME')")
            Pattern pattern = Pattern.compile("@PreAuthorize\\(\"@ss\\.hasPermission\\('([^']+)'\\)\"\\)");
            
            for (Resource resource : resources) {
                try {
                    MetadataReader metadataReader = factory.getMetadataReader(resource);
                    ClassMetadata classMetadata = metadataReader.getClassMetadata();
                    
                    // Kiểm tra có phải là Controller không
                    if (!isController(classMetadata)) {
                        continue;
                    }
                    
                    log.info("Scanning controller: {}", classMetadata.getClassName());
                    
                    // Đọc annotations của class và methods
                    Set<MethodMetadata> annotatedMethods = metadataReader.getAnnotationMetadata().getAnnotatedMethods("org.springframework.security.access.prepost.PreAuthorize");
                    
                    for (MethodMetadata method : annotatedMethods) {
                        // Lấy giá trị của @PreAuthorize annotation
                        Map<String, Object> attributes = method.getAnnotationAttributes("org.springframework.security.access.prepost.PreAuthorize");
                        if (attributes != null) {
                            String value = (String) attributes.get("value");
                            if (value != null) {
                                Matcher matcher = pattern.matcher(value);
                                while (matcher.find()) {
                                    String permission = matcher.group(1);
                                    permissions.add(permission);
                                    log.info("Found permission: {} in method: {}", permission, method.getMethodName());
                                }
                            }
                        }
                    }
                    
                } catch (Exception e) {
                    log.warn("Không thể đọc controller: {}", resource.getFilename(), e);
                }
            }
            
        } catch (IOException e) {
            log.error("Lỗi khi scan controllers", e);
        }
        
        return permissions;
    }
    
    private boolean isController(ClassMetadata classMetadata) {
        try {
            Class<?> clazz = Class.forName(classMetadata.getClassName());
            return clazz.isAnnotationPresent(RestController.class) || 
                   clazz.isAnnotationPresent(Controller.class);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private Set<String> getDefaultPermissions() {
        return new HashSet<>(Arrays.asList(
            // Auth permissions (public endpoints)
            "AUTH_LOGIN", "AUTH_REFRESH_TOKEN", "AUTH_FORGOT_PASSWORD",
            
            // System permissions
            "SYSTEM_ADMIN", "SYSTEM_VIEW_LOGS"
        ));
    }
}
