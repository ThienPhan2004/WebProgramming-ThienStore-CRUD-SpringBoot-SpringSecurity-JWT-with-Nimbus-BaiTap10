# JWT Login – Spring Boot 4 + Security 7 + Nimbus JWT + Thymeleaf + MapStruct + MySQL

## Chạy
1. Cài MySQL 8+ tại localhost:3306 (database `jwt_login` được tự tạo).
2. Đặt biến môi trường (hoặc sửa `application.properties`):
   - `DB_USERNAME`, `DB_PASSWORD`
   - `JWT_SECRET` (Base64, >= 32 byte): `openssl rand -base64 32`
3. `mvn spring-boot:run` (JDK 21) → http://localhost:8005

## Trang web
`/register` → `/login` → `/profile` (cần đăng nhập)

## API (Postman)
| Method | URL | Ghi chú |
|---|---|---|
| POST | /api/auth/signup | `{"fullName","email","password"}` |
| POST | /api/auth/login | trả `{token, tokenType, expiresIn}` + cookie HttpOnly `JWT` |
| POST | /api/auth/logout | xóa cookie |
| GET | /api/users/me | header `Authorization: Bearer <token>` |
| GET | /api/users | header `Authorization: Bearer <token>` |
