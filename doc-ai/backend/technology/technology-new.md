# Công nghệ Backend - Hệ thống Quản lý Nhân viên

## 1. Core Framework & Libraries

### Spring Boot Ecosystem
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
Spring Boot 3.x : 
- Core framework
- Auto-configuration, 
- application bootstrap
Spring Web 6.x: 
- REST API development
- Controller annotations, HTTP handling
Spring Security 6.x: 
- Authentication & Authorization
- JWT security, method-level security
Spring Data JPA 3.x:
- Database access
- Repository pattern, JPQL queries
Spring Boot Starter Test 3.x: 
- Testing support
- Unit và integration tests |

### Database & Persistence
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
| Spring Data JPA 3.x | ORM framework | Entity management, database operations |
| Hibernate 6.x | JPA implementation | Database dialect, caching mechanisms |
| MySQL/MariaDB | Production database | Primary data storage với connection pooling |

### Security & Authentication
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
| Spring Security 6.x | Security framework | JWT-based authentication, RBAC |
| JWT (io.jsonwebtoken) | Token generation | Access và refresh tokens với HS512 signing |
| BCrypt | Password encryption | Password hashing với salt |

### Validation & Utilities
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
| Jakarta Validation 3.x | Bean validation | @Valid, @NotNull annotations |
| Lombok 1.18.x | Code generation | @Getter, @Setter, @Builder annotations |
| Jackson 2.15.x | JSON processing | Object serialization/deserialization |

## 2. Architecture & Design Patterns

### Layered Architecture
```
┌─────────────────────────────────────┐
│           Controller Layer          │  ← REST API Endpoints
├─────────────────────────────────────┤
│            Service Layer            │  ← Business Logic
├─────────────────────────────────────┤
│           Repository Layer          │  ← Data Access
├─────────────────────────────────────┤
│            Entity Layer             │  ← Database Models
└─────────────────────────────────────┘
```

### Package Structure
```
com.candidate.candidate_backend/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── dto/             # Data Transfer Objects
├── entity/          # JPA entities
├── exception/       # Custom exceptions
├── mapper/          # Object mapping
├── repository/      # Data repositories
├── service/         # Business logic
├── util/           # Utility classes
└── validator/      # Custom validators
```

### Entity Design Pattern
```
// Base entity với common fields
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class EntityBase {
    @Version
    private int version;
    
    @CreatedDate
    private LocalDateTime createAt;
    
    @LastModifiedDate
    private LocalDateTime updateAt;
    
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}

// Entity example
@Entity
public class DtbEmployees extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID employeeId;
    
    // Entity-specific fields với proper fetching
    @ManyToOne(fetch = FetchType.LAZY)
    private DtbPosition position;
    
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private DtbUser user;
}
```

### Repository Pattern
```
// Spring Data JPA repository
@Repository
public interface EmployeeRepository extends JpaRepository<DtbEmployees, UUID> {
    
    // Custom queries với JPQL
    @Query("SELECT e FROM DtbEmployees e WHERE e.isDeleted = false")
    List<DtbEmployees> findAllActive();
    
    // Search functionality
    @Query("SELECT e FROM DtbEmployees e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<DtbEmployees> searchEmployees(@Param("keyword") String keyword, Pageable pageable);
    
    // Soft delete handling
    @Modifying
    @Query("UPDATE DtbEmployees e SET e.isDeleted = true WHERE e.employeeId = :id")
    void softDeleteById(@Param("id") UUID id);
}
```

### Service Layer Pattern
```
@Service
@Transactional
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    // CRUD operations với business logic
    @Transactional(readOnly = true)
    public CommonsRep getByEmployee(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
        Page<DtbEmployees> employees = employeeRepository.findAll(pageable);
        
        return CommonsRep.builder()
                .status(200)
                .message("Success")
                .data(employees)
                .build();
    }
    
    // Custom business validation
    private void validateEmployeeData(DtoEmployeeReq dto) {
        if (dto.getBirthday() != null) {
            int age = Period.between(dto.getBirthday(), LocalDate.now()).getYears();
            if (age < 15 || age > 70) {
                throw new ValidationException("Employee age must be between 15 and 70");
            }
        }
    }
}
```

## 3. Security Implementation

### JWT Configuration
```
@Component
public class JwtTokenProvider {
    
    // Access token generation (15 minutes)
    public String generateAccessToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpiration);
        
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    
    // Refresh token generation (7 days)
    public String generateRefreshToken(Authentication authentication) {
        // Similar pattern với longer expiry
    }
}
```

### Security Configuration
```
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

### Method-Level Security
```
@Component("ss")
public class SecurityService {
    
    // Custom permission checking
    public boolean hasPermission(String permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return permissionService.hasPermission(username, permission);
    }
}

// Usage trong controllers
@PreAuthorize("@ss.hasPermission('EMPLOYEE_CREATE')")
@PostMapping
public CommonsRep createEmployee(@RequestBody @Valid DtoEmployeeReq dtoEmployeeReq) {
    return employeeService.createEmployee(dtoEmployeeReq);
}
```

## 4. Data Validation & Error Handling

### Bean Validation
```
// DTO với validation annotations
public class DtoEmployeeReq {
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be 2-50 characters")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "First name must contain only letters")
    private String firstName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    private LocalDate birthday;
}
```

### Custom Validators
```
// Custom annotation
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)
public @interface UniqueUsername {
    String message() default "Username already exists";
}

// Validator implementation
@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !userRepository.findByUsername(username).isPresent();
    }
}
```

### Global Exception Handling
```
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonsRep> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.badRequest()
                .body(CommonsRep.builder()
                        .status(400)
                        .message("Validation failed")
                        .errors(errors)
                        .build());
    }
}
```

## 5. Database Design & Optimization

### Entity Relationships
```
// Many-to-Many relationship
@Entity
public class DtbUser extends EntityBase {
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "dtb_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<DtbRole> roles;
}

// One-to-Many relationship
@Entity
public class DtbDepartment extends EntityBase {
    
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DtbEmployees> employees;
}
```

### Query Optimization
```
// Efficient queries với proper fetching
@Repository
public interface EmployeeRepository extends JpaRepository<DtbEmployees, UUID> {
    
    // Eager fetching cho specific use cases
    @Query("SELECT e FROM DtbEmployees e " +
           "LEFT JOIN FETCH e.position " +
           "LEFT JOIN FETCH e.department " +
           "WHERE e.employeeId = :id")
    Optional<DtbEmployees> findByIdWithRelations(@Param("id") UUID id);
    
    // Pagination với sorting
    @Query("SELECT e FROM DtbEmployees e WHERE e.isDeleted = false")
    Page<DtbEmployees> findAllActive(Pageable pageable);
}
```

## 6. Performance & Caching

### Caching Strategy
```
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000));
        return cacheManager;
    }
}

@Service
public class DepartmentService {
    
    @Cacheable(value = "departments", key = "#root.methodName")
    public List<DtbDepartment> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
```

### Async Processing
```
@Configuration
@EnableAsync
public class AsyncConfig {
    
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.initialize();
        return executor;
    }
}

@Service
public class NotificationService {
    
    @Async("taskExecutor")
    public void sendEmailNotification(String to, String subject, String body) {
        emailService.send(to, subject, body);
    }
}
```

## 7. Testing Strategy

### Unit Testing
```
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    
    @Mock
    private EmployeeRepository employeeRepository;
    
    @InjectMocks
    private EmployeeService employeeService;
    
    @Test
    void createEmployee_ValidData_ReturnsSuccess() {
        // Arrange
        DtoEmployeeReq request = new DtoEmployeeReq();
        request.setFirstName("John");
        
        DtbEmployees employee = new DtbEmployees();
        when(employeeRepository.save(employee)).thenReturn(employee);
        
        // Act
        CommonsRep result = employeeService.createEmployee(request);
        
        // Assert
        assertThat(result.getStatus()).isEqualTo(200);
        verify(employeeRepository).save(employee);
    }
}
```

### Integration Testing
```
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void createEmployee_ValidRequest_ReturnsCreated() {
        // Arrange
        DtoEmployeeReq request = new DtoEmployeeReq();
        request.setFirstName("John");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getValidToken());
        HttpEntity<DtoEmployeeReq> entity = new HttpEntity<>(request, headers);
        
        // Act
        ResponseEntity<CommonsRep> response = restTemplate.postForEntity(
            "/api/employee", entity, CommonsRep.class);
        
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
```

## 8. Best Practices & Patterns

### Code Organization
- **Separation of Concerns**: Clear separation giữa layers
- **Dependency Injection**: Constructor injection preferred
- **Immutable Objects**: Sử dụng final fields where possible
- **Exception Handling**: Centralized exception handling

### Security Best Practices
- **Input Validation**: Comprehensive validation ở tất cả layers
- **SQL Injection Prevention**: Sử dụng parameterized queries
- **XSS Prevention**: Input sanitization và output encoding
- **Authentication**: JWT với proper expiration và refresh

### Performance Best Practices
- **Lazy Loading**: Sử dụng FetchType.LAZY cho relationships
- **Batch Operations**: Sử dụng bulk operations where possible
- **Connection Pooling**: Configure proper connection pool
- **Caching**: Cache frequently accessed data

---

Tài liệu này cung cấp cái nhìn tổng quan về stack công nghệ backend và các patterns được áp dụng trong hệ thống quản lý nhân viên.
