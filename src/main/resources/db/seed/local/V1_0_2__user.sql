-- ============================================
-- 어드민 계정 (1개)
-- ============================================
INSERT INTO users (
    user_id,
    department_id,
    student_id,
    email,
    password,
    grade,
    email_verified,
    created_at,
    updated_at
)
VALUES (
           1,
           1,
           202612345,
           'admin1234',
           '$2a$12$qXfrOTVa6KDsuVUmKhjcq.xZ76MN5Zkd6FW1.mVJlgBCYIgIwQ/42',
           4,
           true,
           NOW(),
           NOW()
       )
    ON DUPLICATE KEY UPDATE
                         password = '$2a$12$qXfrOTVa6KDsuVUmKhjcq.xZ76MN5Zkd6FW1.mVJlgBCYIgIwQ/42',
                         email_verified = true,
                         updated_at = NOW();