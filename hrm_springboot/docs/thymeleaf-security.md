# Giới thiệu các thẻ Security trong Thymeleaf

## 1. Mục đích

Thymeleaf không tự bảo vệ URL. Spring Security mới là lớp quyết định request nào được phép đi qua. Thư viện **thymeleaf-extras-springsecurity6** cung cấp namespace **sec** để template:

- Hiển thị hoặc ẩn HTML theo trạng thái đăng nhập, role hoặc authority.
- Hiển thị thông tin của principal hiện tại.
- Kiểm tra quyền truy cập URL trước khi render liên kết.

Dự án đã có dependency:

~~~xml
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity6</artifactId>
</dependency>
~~~

Tham chiếu cấu hình gốc: [SecurityConfig.java](../src/main/java/fu/hr/config/SecurityConfig.java).

> sec:authorize chỉ điều khiển giao diện. Không được xem đây là cơ chế bảo mật duy nhất. Endpoint vẫn phải được bảo vệ bằng SecurityFilterChain, và thao tác quan trọng nên được kiểm tra thêm ở service/controller.

---

## 2. Đọc SecurityConfig.java hiện tại

~~~java
http.authorizeHttpRequests((auth) -> {
    auth.requestMatchers("/public/**", "/css/**", "/auths/**").permitAll()
            .anyRequest().authenticated();
});
~~~

### 2.1. Nhánh permitAll()

Ba mẫu URL sau cho phép truy cập mà không cần đăng nhập:

| Matcher | Ví dụ URL | Ý nghĩa |
|---|---|---|
| /public/** | /public/home, /public/jobs/1 | Trang và tài nguyên công khai |
| /css/** | /css/admin.css | File CSS công khai |
| /auths/** | /auths/login, /auths/register | Endpoint thuộc khu vực xác thực |

/** là wildcard theo nhiều cấp. Vì vậy /auths/** cũng bao phủ mọi URL bắt đầu bằng /auths/, kể cả endpoint mới được thêm sau này.

Ví dụ:

~~~text
GET  /public/home              -> được phép khi chưa đăng nhập
GET  /css/admin.css            -> được phép khi chưa đăng nhập
GET  /auths/login              -> được phép khi chưa đăng nhập
POST /auths/register           -> được phép khi chưa đăng nhập
GET  /departments              -> phải đăng nhập
POST /departments              -> phải đăng nhập
POST /departments/delete/10    -> phải đăng nhập
~~~

### 2.2. Nhánh anyRequest().authenticated()

Mọi URL không khớp ba matcher trên đều yêu cầu một Authentication hợp lệ. Trong mã hiện tại, điều này gồm:

- /departments và /departments/detail.
- Request POST tạo/xóa department.
- /, /admin/**, /jobs/** nếu controller được bổ sung.
- Tài nguyên tĩnh khác như /js/**, /images/** nếu chưa được khai báo permitAll().

Spring Security xét quy tắc theo thứ tự và dùng quy tắc phù hợp đầu tiên. anyRequest() nên đặt sau các quy tắc cụ thể.

### 2.3. Bảng hành vi

| Người dùng | /public/home | /auths/login | /departments |
|---|---:|---:|---:|
| Anonymous | 200 | 200 | 302 đến login hoặc 401 tùy client |
| Đã đăng nhập | 200 | 200 | 200 |

permitAll() không cấm người đã đăng nhập; nó chỉ có nghĩa URL không bắt buộc phải đăng nhập.

---

## 3. Khai báo namespace sec

Mỗi HTML sử dụng thuộc tính sec:* cần khai báo:

~~~html
<!DOCTYPE html>
<html lang="vi"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
~~~

Nếu có Layout Dialect:

~~~html
<html lang="vi"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
~~~

public-layout/header.html đã khai báo namespace này. Các file như admin-header.html và admin-sidebar.html đang có đoạn sec:* bị comment; khi bật lại phải bổ sung xmlns:sec.

---

## 4. sec:authorize: hiển thị theo điều kiện bảo mật

### 4.1. Đã đăng nhập

~~~html
<div sec:authorize="isAuthenticated()">
    Nội dung này chỉ xuất hiện sau khi đăng nhập.
</div>
~~~

Mẫu header đầy đủ:

~~~html
<div sec:authorize="isAuthenticated()" class="d-flex align-items-center gap-3">
    <span sec:authentication="name">username</span>
    <form th:action="@{/logout}" method="post">
        <button type="submit" class="btn btn-link">Đăng xuất</button>
    </form>
</div>
~~~

### 4.2. Chưa đăng nhập

~~~html
<div sec:authorize="isAnonymous()">
    <a th:href="@{/auths/login}" class="btn btn-primary">Đăng nhập</a>
</div>
~~~

Đây là mẫu phù hợp cho header công khai: anonymous thấy Login, user đã đăng nhập thấy thông tin tài khoản và Logout.

### 4.3. Xác thực đầy đủ và Remember-Me

~~~html
<p sec:authorize="isFullyAuthenticated()">
    Phiên đăng nhập đã được xác thực đầy đủ.
</p>

<p sec:authorize="isRememberMe()">
    Bạn đang đăng nhập bằng Remember-Me.
</p>
~~~

Hai biểu thức này chỉ hữu ích khi ứng dụng thực sự cấu hình Remember-Me. isFullyAuthenticated() loại trừ authentication được khôi phục bằng Remember-Me.

### 4.4. Phủ định điều kiện

~~~html
<a sec:authorize="!isAuthenticated()"
   th:href="@{/auths/login}">
    Login
</a>
~~~

Trong layout, isAnonymous() thường dễ đọc hơn !isAuthenticated().

---

## 5. Kiểm tra role và authority

### 5.1. hasRole

~~~html
<a sec:authorize="hasRole('ADMIN')"
   th:href="@{/admin/users}"
   class="admin-nav-item">
    Quản lý người dùng
</a>
~~~

hasRole('ADMIN') thường được ánh xạ tới authority ROLE_ADMIN vì Spring Security mặc định thêm tiền tố ROLE_.

Hiển thị vai trò:

~~~html
<span sec:authorize="hasRole('ADMIN')">Administrator</span>
<span sec:authorize="hasRole('RECRUITER')">Recruiter</span>
~~~

Trong comment hiện tại của dự án có hasRole('ROLE_ADMIN'). Nên thống nhất dùng hasRole('ADMIN'), hoặc dùng hasAuthority('ROLE_ADMIN') nếu muốn kiểm tra chính xác chuỗi authority.

### 5.2. hasAnyRole

~~~html
<button sec:authorize="hasAnyRole('ADMIN', 'HR_MANAGER')"
        type="submit"
        class="btn btn-primary">
    Lưu department
</button>
~~~

Nút xuất hiện với user có một trong hai role. Endpoint xử lý POST /departments vẫn phải được bảo vệ ở server.

### 5.3. hasAuthority

~~~html
<button sec:authorize="hasAuthority('DEPARTMENT_CREATE')"
        type="submit"
        class="btn btn-primary">
    Tạo department
</button>
~~~

hasAuthority so sánh chính xác giá trị và không tự thêm ROLE_. Nó phù hợp với permission chi tiết như DEPARTMENT_CREATE, DEPARTMENT_DELETE.

### 5.4. hasAnyAuthority

~~~html
<div sec:authorize="hasAnyAuthority('DEPARTMENT_READ', 'DEPARTMENT_ADMIN')">
    Bạn có thể xem dữ liệu department.
</div>
~~~

### 5.5. Phân biệt role và authority

| Biểu thức | Giá trị thường kiểm tra | Dùng cho |
|---|---|---|
| hasRole('ADMIN') | ROLE_ADMIN | Nhóm vai trò lớn |
| hasAuthority('ROLE_ADMIN') | ROLE_ADMIN | Kiểm tra chính xác một role authority |
| hasAuthority('DEPARTMENT_DELETE') | DEPARTMENT_DELETE | Permission cụ thể |

Entity User hiện có trường role kiểu String. Khi tạo UserDetails, cần thống nhất dữ liệu trong database với giá trị trả về từ getAuthorities(). Nếu database lưu ADMIN, adapter nên trả về ROLE_ADMIN khi template dùng hasRole('ADMIN').

---

## 6. sec:authentication: thông tin user hiện tại

### 6.1. Username hoặc tên principal

~~~html
<span sec:authentication="name">anonymousUser</span>
<span sec:authentication="principal.username">admin@example.com</span>
~~~

public-layout/header.html hiện dùng:

~~~html
<a sec:authentication="principal.username"
   class="nav-link dropdown-toggle"
   href="#">
</a>
~~~

Cách này yêu cầu principal có thuộc tính username. Với principal tùy chỉnh không có property đó, dùng sec:authentication="name" hoặc expose đúng property trong UserDetails.

### 6.2. Authorities

~~~html
<span sec:authentication="principal.authorities">[ROLE_USER]</span>
~~~

Không nên hiển thị toàn bộ authority trên production nếu không cần thiết; nên dùng sec:authorize để hiển thị nhãn cụ thể.

### 6.3. Avatar theo ký tự đầu

~~~html
<div class="avatar"
     th:text="${#authentication.name != null
              ? #strings.substring(#authentication.name, 0, 1).toUpperCase()
              : 'U'}">
    U
</div>
~~~

#authentication là object Authentication trong Thymeleaf context. Nên kiểm tra isAuthenticated() trước khi đọc principal trong vùng chỉ dành cho user đã đăng nhập.

---

## 7. sec:authorize-url: hiển thị link theo URL

~~~html
<a sec:authorize-url="/admin/users"
   th:href="@{/admin/users}"
   class="admin-nav-item">
    User Management
</a>
~~~

Có thể chỉ rõ HTTP method:

~~~html
<button sec:authorize-url="POST /departments"
        type="submit">
    Save
</button>
~~~

Attribute này hữu ích cho navigation dùng chung, nhưng chỉ là logic render. Request trực tiếp từ Postman vẫn phải bị chặn bởi backend.

---

## 8. Ví dụ hoàn chỉnh cho public header

~~~html
<!DOCTYPE html>
<html lang="vi"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<body>
<header th:fragment="header" class="navbar navbar-expand-lg">
    <a th:href="@{/public/home}" class="navbar-brand">HRM</a>

    <nav class="ms-auto d-flex align-items-center gap-3">
        <a th:href="@{/public/home}">Trang chủ</a>

        <div sec:authorize="isAnonymous()">
            <a th:href="@{/auths/login}" class="btn btn-outline-primary">
                Đăng nhập
            </a>
        </div>

        <div sec:authorize="isAuthenticated()" class="d-flex align-items-center gap-2">
            <span>
                Xin chào, <strong sec:authentication="name">user</strong>
            </span>

            <a sec:authorize="hasRole('ADMIN')"
               th:href="@{/admin/users}">
                Quản trị
            </a>

            <form th:action="@{/logout}" method="post">
                <button type="submit" class="btn btn-outline-danger">
                    Đăng xuất
                </button>
            </form>
        </div>
    </nav>
</header>
</body>
</html>
~~~

---

## 9. Ví dụ cho màn hình Department

### 9.1. Chỉ role phù hợp được tạo

~~~html
<a sec:authorize="hasAnyRole('ADMIN', 'HR_MANAGER')"
   class="btn btn-primary"
   th:href="@{/departments/detail}">
    Add department
</a>
~~~

### 9.2. Chỉ permission phù hợp được xóa

~~~html
<form sec:authorize="hasAuthority('DEPARTMENT_DELETE')"
      th:action="@{/departments/delete/{id}(id=${dept.id})}"
      method="post"
      class="d-inline">
    <button type="submit" class="btn btn-sm btn-outline-danger">
        Delete
    </button>
</form>
~~~

### 9.3. Hiển thị thông báo khi chỉ có quyền xem

~~~html
<div sec:authorize="isAuthenticated()"
     th:unless="${#authorization.expression('hasAuthority(\'DEPARTMENT_CREATE\')')}"
     class="alert alert-info">
    Tài khoản của bạn chỉ có quyền xem department.
</div>
~~~

Thông thường nên dùng trực tiếp sec:authorize trên element cần ẩn/hiện. Cách kết hợp th:unless và #authorization chỉ cần khi điều kiện security phải lồng vào biểu thức Thymeleaf khác.

### 9.4. User trong admin layout

~~~html
<html lang="vi"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<body>
<aside sec:authorize="isAuthenticated()">
    <div class="user-name" sec:authentication="name">User</div>
    <div sec:authorize="hasRole('ADMIN')" class="user-role">
        Administrator
    </div>
    <div sec:authorize="hasRole('RECRUITER')" class="user-role">
        Recruiter
    </div>
</aside>
</body>
</html>
~~~

---

## 10. CSRF cho form POST

Spring Security thường bật CSRF protection mặc định cho ứng dụng web. Với form Thymeleaf gửi POST, nên đưa token vào form:

~~~html
<form th:action="@{/departments}" method="post">
    <input type="hidden"
           th:name="${_csrf.parameterName}"
           th:value="${_csrf.token}">
    <button type="submit">Save</button>
</form>
~~~

Form logout:

~~~html
<form th:action="@{/logout}" method="post">
    <input type="hidden"
           th:name="${_csrf.parameterName}"
           th:value="${_csrf.token}">
    <button type="submit">Logout</button>
</form>
~~~

Nếu dùng formLogin() và logout() của Spring Security, form login mặc định thường gửi tới /login, logout tới /logout. Nếu muốn dùng /auths/login và /auths/logout, phải cấu hình rõ các URL đó và bảo đảm controller/template dùng cùng convention.

---

## 11. Đối chiếu với mã hiện tại

### 11.1. URL login/logout chưa thống nhất

Hiện có các URL khác nhau:

| Vị trí | URL |
|---|---|
| AuthController GET | /auths/login |
| login.html form POST | /auths/login |
| AuthController POST | /auths |
| public-layout/header.html logout | /auths/logout |
| admin-header.html logout | /auth/logout |

Trong khi SecurityConfig chưa cấu hình formLogin() hoặc logout(). Có thể chọn endpoint chuẩn và cấu hình như sau:

~~~java
http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/public/**", "/css/**",
                         "/auths/login", "/auths/register").permitAll()
        .anyRequest().authenticated()
    )
    .formLogin(form -> form
        .loginPage("/auths/login")
        .loginProcessingUrl("/auths/login")
        .defaultSuccessUrl("/departments", true)
        .permitAll()
    )
    .logout(logout -> logout
        .logoutUrl("/auths/logout")
        .logoutSuccessUrl("/public/home")
        .permitAll()
    );
~~~

Khi dùng cấu hình trên, POST /auths/login do Spring Security xử lý; không nên đồng thời tự xử lý cùng request bằng controller khác.

### 11.2. /auths/** đang mở rộng hơn cần thiết

Vì toàn bộ /auths/** là permitAll(), mọi endpoint mới thêm dưới prefix này cũng trở thành public. Có thể giới hạn rõ hơn:

~~~java
auth.requestMatchers(
        "/public/**",
        "/css/**",
        "/auths/login",
        "/auths/register"
    ).permitAll()
    .anyRequest().authenticated();
~~~

Nếu logout do Spring Security xử lý, cấu hình riêng logoutUrl thay vì mở toàn bộ prefix.

### 11.3. CSS không phải quyền nghiệp vụ

/css/** chỉ là tài nguyên tĩnh. Nếu dùng JavaScript, ảnh hoặc font, khai báo đúng thư mục:

~~~java
auth.requestMatchers("/css/**", "/js/**", "/images/**").permitAll();
~~~

Không đưa API nghiệp vụ vào nhóm public chỉ vì một trang HTML cần gọi API đó.

### 11.4. Namespace trong admin layout

Các đoạn comment trong admin-header.html và admin-sidebar.html có sec:authorize, nhưng file hiện chưa khai báo xmlns:sec. Khi bật chúng, sửa thẻ html:

~~~html
<html lang="vi"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
~~~

---

## 12. Checklist kiểm thử

- Anonymous truy cập được /public/home, /css/admin.css, /auths/login.
- Anonymous bị chặn khi truy cập /departments.
- User đã đăng nhập thấy isAuthenticated() và username.
- Anonymous thấy isAnonymous(); user đã đăng nhập không thấy block đó.
- User có ROLE_ADMIN thấy hasRole('ADMIN').
- User không có DEPARTMENT_DELETE không thấy nút Delete; request trực tiếp vẫn bị chặn ở server.
- Tất cả form POST có CSRF token khi CSRF được bật.
- URL trong th:action của login/logout trùng với loginProcessingUrl/logoutUrl.
- Không dựa vào việc ẩn nút trên Thymeleaf để thay thế authorization ở backend.

---

## 13. Tóm tắt nhanh

| Thẻ/biểu thức | Công dụng |
|---|---|
| sec:authorize="isAuthenticated()" | Chỉ hiện khi đã đăng nhập |
| sec:authorize="isAnonymous()" | Chỉ hiện khi chưa đăng nhập |
| sec:authorize="isFullyAuthenticated()" | Đã xác thực đầy đủ, không phải Remember-Me |
| sec:authorize="isRememberMe()" | Đang dùng Remember-Me |
| sec:authorize="hasRole('ADMIN')" | Có role ROLE_ADMIN |
| sec:authorize="hasAnyRole('ADMIN', 'HR_MANAGER')" | Có ít nhất một role |
| sec:authorize="hasAuthority('DEPARTMENT_DELETE')" | Có đúng permission chỉ định |
| sec:authorize-url="/admin/users" | URL được phép theo authorization hiện tại |
| sec:authentication="name" | Tên principal hiện tại |
| sec:authentication="principal.username" | Thuộc tính username của principal |
| sec:authentication="principal.authorities" | Danh sách authority |

