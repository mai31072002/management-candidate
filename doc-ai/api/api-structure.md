# Phân tích API

## Tổng quan cấu trúc API

### Base Configuration
- **Base URL**: `/api`
- **Authentication**: Bearer Token (JWT)
- **Content-Type**: application/json
- **Error Handling**: Centralized error responses

### Response Structure
```json
{
    "status": 200,
    "message": "Success",
    "data": {...},
    "pagination": {
        "page": 0,
        "size": 10,
        "totalElements": 100
    }
}
```

## Authentication APIs

### Login
- **Method**: `POST /api/auth/login`
- **Request Body**:
```json
{
    "username": "john.doe",
    "password": "password123"
}
```
- **Response Body**:
```json
{
    "status": 200,
    "message": "Login successful",
    "data": {
        "accessToken": "jwt-token",
        "refreshToken": "refresh-token",
        "username": "john.doe",
    }
}
```

### Refresh Token
- **Method**: `POST /api/auth/refresh-token`
- **Request Body**:
```json
{
    "refreshToken": "refresh-token"
}
```
- **Response Body**:
```json
{
    "status": 200,
    "message": "Token refreshed",
    "data": {
        "accessToken": "new-jwt-token",
        "refreshToken": "new-refresh-token"
    }
}
```

### Logout
- **Method**: `POST /api/auth/logout`
- **Response Body**:
```json
{
    "status": 200,
    "message": "Logout successful"
}
```

## Employee Management APIs

### Get Employee List
- **Method**: `GET /api/users?page={page}&size={size}`
- **Query Parameters**: `page`, `size`
- **Response Body**:
```json
{
    "status": 200,
    "message": "Success",
    "data": {
        "content": [
            {
                "userId": "uuid",
                "username": "john.doe",
                "email": "john.doe@company.com",
                "employee": {
                    "employeeId": "uuid",
                    "firstName": "John",
                    "lastName": "Doe",
                    "fullName": "John Doe",
                    "phone": "0912345678",
                    "cccd": "123456789",
                    "birthday": "1990-01-01",
                    "gender": 1,
                    "age": 34,
                    "province": "Hà Nội",
                    "district": "Ba Đình",
                    "address": "123 Main Street",
                    "positionName": "Developer",
                    "departmentName": "IT",
                    "employeesCode": "EMP001",
                    "baseSalary": 15000000,
                    "allowance": 2000000,
                    "manages": "Jane Smith",
                    "description": "Senior Developer",
                    "startDate": "2020-01-01",
                    "endDate": null,
                    "status": 1
                }
            }
        ],
        "pageable": {
            "page": 0,
            "size": 10
        },
        "totalElements": 100,
        "totalPages": 10
    }
}
```

### Create Employee
- **Method**: `POST /api/employee`
- **Request Body**:
```json
{
    "firstName": "John",
    "lastName": "Doe",
    "username": "john.doe",
    "email": "john.doe@company.com",
    "phone": "0912345678",
    "cccd": "123456789",
    "birthday": "1990-01-01",
    "gender": 1,
    "province": "Hà Nội",
    "district": "Ba Đình",
    "address": "123 Main Street",
    "positionId": "uuid-position-id",
    "departmentId": "uuid-department-id",
    "employeesCode": "EMP001",
    "baseSalary": 15000000,
    "allowance": 2000000,
    "manages": "Jane Smith",
    "description": "Senior Developer",
    "startDate": "2024-01-01",
    "status": 1,
    "roles": ["EMPLOYEE"]
}
```

### Update Employee
- **Method**: `PUT /api/employee/{employeeId}`
- **Request Body**: Similar to Create Employee (partial update allowed)

### Delete Employee
- **Method**: `DELETE /api/employee/{employeeId}`
- **Response Body**:
```json
{
    "status": 200,
    "message": "Employee deleted successfully"
}
```

### Search Employee
- **Method**: `GET /api/users/search?keyword={keyword}&page={page}&size={size}`
- **Query Parameters**: `keyword`, `page`, `size`

## Department Management APIs

### Get Department List
- **Method**: `GET /api/department`
- **Response Body**:
```json
{
    "status": 200,
    "message": "Success",
    "data": {
        "department": [
            {
                "departmentId": "uuid",
                "departmentName": "IT Department",
                "description": "Information Technology"
            }
        ]
    }
}
```

### Create Department
- **Method**: `POST /api/department`
- **Request Body**:
```json
{
    "departmentName": "HR Department",
    "description": "Human Resources"
}
```

### Update Department
- **Method**: `PUT /api/department/{departmentId}`
- **Request Body**:
```json
{
    "departmentName": "Updated Department",
    "description": "Updated description"
}
```

### Delete Department
- **Method**: `DELETE /api/department/{departmentId}`

## Position Management APIs

### Get Position List
- **Method**: `GET /api/position`
- **Response Body**:
```json
{
    "status": 200,
    "message": "Success",
    "data": {
        "position": [
            {
                "positionId": "uuid",
                "positionName": "Developer",
                "description": "Software Developer"
            }
        ]
    }
}
```

### Create Position
- **Method**: `POST /api/position`
- **Request Body**:
```json
{
    "positionName": "Senior Developer",
    "description": "Senior Software Developer"
}
```

### Update Position
- **Method**: `PUT /api/position/{positionId}`

### Delete Position
- **Method**: `DELETE /api/position/{positionId}`

## Overtime Management APIs

### Get OT List by Employee
- **Method**: `GET /api/ot-date/{employeeId}?month={month}`
- **Query Parameters**: `month` (format: YYYY-MM)
- **Response Body**:
```json
{
    "status": 200,
    "message": "Success",
    "data": [
        {
            "otDateId": "uuid",
            "userId": "uuid",
            "workDate": "2024-01-15",
            "startTime": "18:00",
            "endTime": "22:00",
            "otType": 1,
            "status": 1,
            "approvedAt": "2024-01-16T09:00:00",
            "fullName": "John Doe"
        }
    ]
}
```

### Create OT Request
- **Method**: `POST /api/ot-date`
- **Request Body**:
```json
{
    "userId": "uuid",
    "workDate": "2024-01-15",
    "startTime": "18:00",
    "endTime": "22:00",
    "otType": 1,
    "status": 1
}
```

### Update OT Request
- **Method**: `PUT /api/ot-date/{otDateId}`

### Delete OT Request
- **Method**: `DELETE /api/ot-date/{otDateId}`

## Reward & Penalty Management APIs

### Get Reward/Penalty List by Employee
- **Method**: `GET /api/reward-penalty/{employeeId}?fromDate={fromDate}&toDate={toDate}&type={type}`
- **Query Parameters**: `fromDate`, `toDate`, `type` (REWARD/PENALTY)
- **Response Body**:
```json
{
    "status": 200,
    "message": "Success",
    "data": [
        {
            "rewardPenaltyId": "uuid",
            "employeeId": "uuid",
            "type": "REWARD",
            "amount": 1000000,
            "reason": "Excellent performance",
            "date": "2024-01-15"
        }
    ]
}
```

### Create Reward/Penalty
- **Method**: `POST /api/reward-penalty`
- **Request Body**:
```json
{
    "employeeId": "uuid",
    "type": "REWARD",
    "amount": 1000000,
    "reason": "Excellent performance",
    "date": "2024-01-15"
}
```

### Update Reward/Penalty
- **Method**: `PUT /api/reward-penalty/{rewardPenaltyId}`

### Delete Reward/Penalty
- **Method**: `DELETE /api/reward-penalty/{rewardPenaltyId}`

## Error Response Format

### Validation Errors (400)
```json
{
    "status": 400,
    "message": "Validation failed",
    "errors": [
        {
            "field": "firstName",
            "message": "First name is required"
        },
        {
            "field": "email",
            "message": "Invalid email format"
        }
    ]
}
```

### Authentication Errors (401)
```json
{
    "status": 401,
    "message": "Unauthorized",
    "error": "Invalid or expired token"
}
```

### Authorization Errors (403)
```json
{
    "status": 403,
    "message": "Access denied",
    "error": "Insufficient permissions"
}
```

### Not Found Errors (404)
```json
{
    "status": 404,
    "message": "Resource not found",
    "error": "Employee not found"
}
```

### Server Errors (500)
```json
{
    "status": 500,
    "message": "Internal server error",
    "error": "Database connection failed"
}
```

---

Tài liệu này cung cấp cấu trúc API ngắn gọn và các mẫu request/response cho hệ thống quản lý nhân viên.
