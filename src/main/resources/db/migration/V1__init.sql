
-- ============================================
-- 1. 기초 테이블 생성 (참조하는 테이블이 없는 독립적인 테이블)
-- ============================================

-- 학과 테이블
CREATE TABLE IF NOT EXISTS department (
                                          department_id BIGINT PRIMARY KEY,
                                          department_name VARCHAR(100) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL
    );

-- 강의 그룹 테이블
CREATE TABLE IF NOT EXISTS lecture_group (
                                             lecture_group_id BIGINT PRIMARY KEY,
                                             lecture_group_name VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL
    );

-- 이메일 인증 테이블
CREATE TABLE IF NOT EXISTS email_verifications (
                                                   email_verification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                   email VARCHAR(255) NOT NULL UNIQUE,
    verification_code VARCHAR(6) NOT NULL,
    expires_at DATETIME(6) NOT NULL,
    verified BOOLEAN NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL
    );

-- ============================================
-- 2. 참조 테이블 생성 (외래 키 의존성이 있는 테이블)
-- ============================================

-- 강의 테이블 (lecture_group_id 외래 키 참조)
CREATE TABLE IF NOT EXISTS lecture (
                                       lecture_id BIGINT PRIMARY KEY,
                                       lecture_name VARCHAR(255) NOT NULL,
    credit INT NOT NULL,
    course_classification VARCHAR(50) NOT NULL, -- MAJOR_REQUIREMENT, LIBERAL_REQUIREMENT 등
    liberal_category VARCHAR(50) NULL,          -- AI_BASIC, COMMUNICATION 등 (NULL 가능)
    semester INT NOT NULL,
    lecture_year INT NOT NULL,
    grade INT NULL,                             -- NULL 입력 대비 NULL 허용
    classroom VARCHAR(100) NULL,                -- NULL 입력 대비 NULL 허용
    professor VARCHAR(100) NULL,                -- NULL 입력 대비 NULL 허용
    course_code VARCHAR(50) NOT NULL,
    lecture_group_id BIGINT,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    FOREIGN KEY (lecture_group_id) REFERENCES lecture_group(lecture_group_id) ON DELETE SET NULL
    );

-- 강의 시간표 테이블 (lecture_id 외래 키 참조)
CREATE TABLE IF NOT EXISTS lecture_schedule (
                                                lecture_schedule_id BIGINT PRIMARY KEY,
                                                lecture_id BIGINT NOT NULL,
                                                day_of_week VARCHAR(10) NOT NULL,           -- MON, TUE, WED, THU, FRI 등
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    FOREIGN KEY (lecture_id) REFERENCES lecture(lecture_id) ON DELETE CASCADE
    );

-- 커리큘럼 테이블 (lecture_id, department_id 외래 키 참조)
CREATE TABLE IF NOT EXISTS curriculum (
                                          curriculum_id BIGINT PRIMARY KEY,
                                          lecture_id BIGINT NOT NULL,
                                          department_id BIGINT NOT NULL,
                                          grade INT NOT NULL,
                                          requirement BOOLEAN NOT NULL,
                                          created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    FOREIGN KEY (lecture_id) REFERENCES lecture(lecture_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department(department_id) ON DELETE CASCADE
    );

-- 유저 테이블 (department_id 외래 키 참조)
CREATE TABLE IF NOT EXISTS users (
                                     user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     department_id BIGINT NOT NULL,
                                     student_id BIGINT NOT NULL,
                                     email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    grade INT NOT NULL,
    email_verified BOOLEAN NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
    );

CREATE TABLE IF NOT EXISTS timetable (
                                         timetable_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         user_id BIGINT NOT NULL,
                                         candidate_name VARCHAR(50) NOT NULL,
    department_id BIGINT NOT NULL,
    preferred_free_days VARCHAR(50) NULL,       -- WeekListConverter가 변환한 문자열 저장
    total_credits INT NOT NULL,
    target_credits INT NOT NULL,
    grade INT NOT NULL,
    semester INT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    -- 외래키 제약조건 (유저 및 학과 참조)
    CONSTRAINT fk_timetable_user
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_timetable_department
    FOREIGN KEY (department_id) REFERENCES department(department_id)
    );

-- 시간표-강의 매핑 (다대다 해소용 연관 테이블)
CREATE TABLE IF NOT EXISTS timetable_lecture (
                                                 timetable_lecture_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                 timetable_id BIGINT NOT NULL,
                                                 lecture_id BIGINT NOT NULL,
                                                 created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    -- 외래키 제약조건 (시간표 및 강의 참조)
    CONSTRAINT fk_tl_timetable
    FOREIGN KEY (timetable_id) REFERENCES timetable(timetable_id) ON DELETE CASCADE,
    CONSTRAINT fk_tl_lecture
    FOREIGN KEY (lecture_id) REFERENCES lecture(lecture_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS refresh_tokens (
                                              refresh_token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              user_id BIGINT NOT NULL UNIQUE,
                                              token VARCHAR(512) NOT NULL,
    expired_at DATETIME(6) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    CONSTRAINT fk_refresh_token_user
    FOREIGN KEY (user_id)
    REFERENCES users(user_id)
    ON DELETE CASCADE
    );
