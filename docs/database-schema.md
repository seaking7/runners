# 데이터베이스 스키마

## 개요
러너스 커뮤니티 애플리케이션의 MySQL 데이터베이스 테이블 구조 및 생성문을 정의합니다.

## 테이블 목록

### 1. r_competitions (대회정보)

대회 정보를 저장하는 테이블입니다.

#### 테이블 구조

| 컬럼명 | 데이터 타입 | 제약조건 | 설명 |
|--------|------------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 대회 고유 ID |
| name | VARCHAR(255) | NOT NULL | 대회명 |
| event_date_time | DATETIME | NOT NULL | 대회 일시 |
| location | VARCHAR(255) | NOT NULL | 대회 장소 |
| course | TEXT | NOT NULL | 대회 코스 설명 |
| gifts | TEXT | NULL | 사은품 정보 |
| participation_fee | INT | NOT NULL | 참가비용 (원) |
| created_at | DATETIME | NOT NULL | 등록일시 |

#### MySQL 테이블 생성문

```sql
CREATE TABLE r_competitions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL COMMENT '대회명',
    event_date_time DATETIME NOT NULL COMMENT '대회 일시',
    location VARCHAR(255) NOT NULL COMMENT '대회 장소',
    course TEXT NOT NULL COMMENT '대회 코스 설명',
    gifts TEXT COMMENT '사은품 정보',
    participation_fee INT NOT NULL COMMENT '참가비용(원)',
    created_at DATETIME NOT NULL COMMENT '등록일시',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='대회정보';
```

#### 테이블 생성 스크립트

```sql
-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS test1 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE test1;
```

#### 인덱스

```sql
-- 대회 일시 검색용 인덱스
CREATE INDEX idx_competitions_event_date_time ON r_competitions(event_date_time);

-- 대회 장소 검색용 인덱스
CREATE INDEX idx_competitions_location ON r_competitions(location);

-- 등록일시 정렬용 인덱스
CREATE INDEX idx_competitions_created_at ON r_competitions(created_at);
```

#### 샘플 데이터

```sql
INSERT INTO r_competitions (name, event_date_time, location, course, gifts, participation_fee, created_at) VALUES
('서울 한강 마라톤 대회', '2024-05-15 08:00:00', '서울특별시 영등포구 한강공원 여의도지구', '여의도 한강공원 출발 → 반포대교 → 잠수교 → 여의도 한강공원 도착\n총 거리: 10km\n평탄한 코스로 초보자도 참가 가능', '완주메달, 기념 티셔츠, 스포츠 타월', 30000, NOW()),
('부산 해운대 달리기 축제', '2024-06-20 07:30:00', '부산광역시 해운대구 해운대해수욕장', '해운대해수욕장 출발 → 동백섬 → 달맞이고개 → 해운대해수욕장 도착\n총 거리: 5km\n바다 전망이 아름다운 코스', '완주메달, 해운대 기념품, 생수', 25000, NOW()),
('제주 올레길 트레일런', '2024-07-10 09:00:00', '제주특별자치도 서귀포시 올레 7코스', '외돌개 출발 → 월평포구 → 서귀포 자연휴양림 도착\n총 거리: 15km\n중급자 이상 추천', '완주메달, 제주 특산품 세트, 기능성 양말', 45000, NOW());
```

### 2. r_posts (커뮤니티 게시글)

커뮤니티 자유게시판의 게시글을 저장하는 테이블입니다.

#### 테이블 구조

| 컬럼명 | 데이터 타입 | 제약조건 | 설명 |
|--------|------------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 게시글 고유 ID |
| title | VARCHAR(255) | NOT NULL | 게시글 제목 |
| content | TEXT | NOT NULL | 게시글 내용 |
| author | VARCHAR(255) | NOT NULL | 작성자 |
| view_count | INT | NOT NULL, DEFAULT 0 | 조회수 |
| like_count | INT | NOT NULL, DEFAULT 0 | 좋아요 수 |
| created_at | DATETIME | NOT NULL | 작성일시 |
| updated_at | DATETIME | NOT NULL | 수정일시 |

#### MySQL 테이블 생성문

```sql
CREATE TABLE r_posts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    author VARCHAR(255) NOT NULL COMMENT '작성자',
    view_count INT NOT NULL DEFAULT 0 COMMENT '조회수',
    like_count INT NOT NULL DEFAULT 0 COMMENT '좋아요 수',
    created_at DATETIME NOT NULL COMMENT '작성일시',
    updated_at DATETIME NOT NULL COMMENT '수정일시',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='커뮤니티 게시글';
```


#### 인덱스

```sql
-- 작성일시 정렬용 인덱스
CREATE INDEX idx_posts_created_at ON r_posts(created_at);

-- 제목 검색용 인덱스
CREATE INDEX idx_posts_title ON r_posts(title);

-- 작성자 검색용 인덱스
CREATE INDEX idx_posts_author ON r_posts(author);
```

#### 샘플 데이터

```sql
INSERT INTO r_posts (title, content, author, view_count, created_at, updated_at) VALUES
('첫 5km 완주 후기!', '어제 처음으로 5km를 쉬지 않고 뛰었습니다!&#10;정말 힘들었지만 완주했을 때의 성취감은 정말 대단했어요.&#10;다음 목표는 10km입니다!', '러닝초보', 15, NOW(), NOW()),
('한강 러닝코스 추천', '한강공원 여의도 구간이 정말 좋아요.&#10;평평하고 야경도 아름답습니다.&#10;초보자분들께 추천드려요!', '한강러너', 23, NOW(), NOW()),
('러닝화 추천 부탁드려요', '러닝을 시작한지 1개월 된 초보입니다.&#10;발에 맞는 러닝화를 찾고 있는데 추천 부탁드려요.&#10;예산은 10만원 정도입니다.', '신발고민', 8, NOW(), NOW());
```

### 3. r_comments (댓글)

게시글에 달린 댓글을 저장하는 테이블입니다.

#### 테이블 구조

| 컬럼명 | 데이터 타입 | 제약조건 | 설명 |
|--------|------------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 댓글 고유 ID |
| content | TEXT | NOT NULL | 댓글 내용 |
| author | VARCHAR(255) | NOT NULL | 댓글 작성자 |
| post_id | BIGINT | NOT NULL, FOREIGN KEY | 게시글 ID (참조) |
| created_at | DATETIME | NOT NULL | 작성일시 |
| updated_at | DATETIME | NOT NULL | 수정일시 |

#### MySQL 테이블 생성문

```sql
CREATE TABLE r_comments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    content TEXT NOT NULL COMMENT '댓글 내용',
    author VARCHAR(255) NOT NULL COMMENT '댓글 작성자',
    post_id BIGINT NOT NULL COMMENT '게시글 ID',
    created_at DATETIME NOT NULL COMMENT '작성일시',
    updated_at DATETIME NOT NULL COMMENT '수정일시',
    PRIMARY KEY (id),
    FOREIGN KEY (post_id) REFERENCES r_posts(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='댓글';
```


#### 인덱스

```sql
-- 게시글별 댓글 조회용 인덱스
CREATE INDEX idx_comments_post_id ON r_comments(post_id);

-- 댓글 작성일시 정렬용 인덱스
CREATE INDEX idx_comments_created_at ON r_comments(created_at);

-- 작성자별 댓글 검색용 인덱스
CREATE INDEX idx_comments_author ON r_comments(author);
```

#### 샘플 데이터

```sql
INSERT INTO r_comments (content, author, post_id, created_at, updated_at) VALUES
('축하해요! 저도 첫 5km 완주했을 때가 기억나네요. 정말 뿌듯하죠!', '응원러너', 1, NOW(), NOW()),
('다음엔 10km 도전해보세요. 화이팅!', '마라토너', 1, NOW(), NOW()),
('여의도 코스 정말 좋아요! 저도 자주 뛰는 곳이에요.', '한강사랑', 2, NOW(), NOW()),
('나이키 에어줌 페가수스 추천드려요. 쿠셔닝이 좋아요.', '신발전문가', 3, NOW(), NOW());
```

### 4. r_likes (좋아요)

게시글에 대한 좋아요 정보를 저장하는 테이블입니다.

#### 테이블 구조

| 컬럼명 | 데이터 타입 | 제약조건 | 설명 |
|--------|------------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 좋아요 고유 ID |
| post_id | BIGINT | NOT NULL, FOREIGN KEY | 게시글 ID (참조) |
| user_identifier | VARCHAR(255) | NOT NULL | 사용자 식별자 (IP 주소) |
| created_at | DATETIME | NOT NULL | 좋아요 생성일시 |

#### MySQL 테이블 생성문

```sql
CREATE TABLE r_likes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    post_id BIGINT NOT NULL COMMENT '게시글 ID',
    user_identifier VARCHAR(255) NOT NULL COMMENT '사용자 식별자 (IP 주소)',
    created_at DATETIME NOT NULL COMMENT '좋아요 생성일시',
    PRIMARY KEY (id),
    FOREIGN KEY (post_id) REFERENCES r_posts(id) ON DELETE CASCADE,
    UNIQUE KEY unique_like_per_user (post_id, user_identifier)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='좋아요';
```

#### 인덱스

```sql
-- 게시글별 좋아요 조회용 인덱스
CREATE INDEX idx_likes_post_id ON r_likes(post_id);

-- 사용자별 좋아요 조회용 인덱스
CREATE INDEX idx_likes_user_identifier ON r_likes(user_identifier);

-- 좋아요 생성일시 정렬용 인덱스
CREATE INDEX idx_likes_created_at ON r_likes(created_at);
```

#### 샘플 데이터

```sql
INSERT INTO r_likes (post_id, user_identifier, created_at) VALUES
(1, '192.168.1.100', NOW()),
(1, '192.168.1.101', NOW()),
(2, '192.168.1.100', NOW()),
(3, '192.168.1.102', NOW());
```

### 5. r_tips (팁&노하우)

팁&노하우 게시판의 팁 정보를 저장하는 테이블입니다.

#### 테이블 구조

| 컬럼명 | 데이터 타입 | 제약조건 | 설명 |
|--------|------------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 팁 고유 ID |
| title | VARCHAR(255) | NOT NULL | 팁 제목 |
| content | TEXT | NOT NULL | 팁 내용 |
| author | VARCHAR(255) | NOT NULL | 작성자 |
| view_count | INT | NOT NULL, DEFAULT 0 | 조회수 |
| like_count | INT | NOT NULL, DEFAULT 0 | 좋아요 수 |
| created_at | DATETIME | NOT NULL | 작성일시 |
| updated_at | DATETIME | NOT NULL | 수정일시 |

#### MySQL 테이블 생성문

```sql
CREATE TABLE r_tips (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL COMMENT '팁 제목',
    content TEXT NOT NULL COMMENT '팁 내용',
    author VARCHAR(255) NOT NULL COMMENT '작성자',
    view_count INT NOT NULL DEFAULT 0 COMMENT '조회수',
    like_count INT NOT NULL DEFAULT 0 COMMENT '좋아요 수',
    created_at DATETIME NOT NULL COMMENT '작성일시',
    updated_at DATETIME NOT NULL COMMENT '수정일시',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='팁&노하우';
```

#### 인덱스

```sql
-- 작성일시 정렬용 인덱스
CREATE INDEX idx_tips_created_at ON r_tips(created_at);

-- 제목 검색용 인덱스
CREATE INDEX idx_tips_title ON r_tips(title);

-- 작성자 검색용 인덱스
CREATE INDEX idx_tips_author ON r_tips(author);

-- 조회수 정렬용 인덱스
CREATE INDEX idx_tips_view_count ON r_tips(view_count);

-- 좋아요 수 정렬용 인덱스
CREATE INDEX idx_tips_like_count ON r_tips(like_count);
```

#### 샘플 데이터

```sql
INSERT INTO r_tips (title, content, author, view_count, like_count, created_at, updated_at) VALUES
('초보자를 위한 러닝 시작 가이드', '러닝을 처음 시작하는 분들을 위한 단계별 가이드입니다.

1. 목표 설정하기
- 처음에는 거창한 목표보다는 작고 달성 가능한 목표를 세우세요
- 예: 일주일에 3번, 20분씩 걷기부터 시작

2. 적절한 장비 준비하기  
- 러닝화: 발에 맞는 편안한 운동화 선택
- 의류: 통기성 좋은 운동복 착용

3. 워밍업과 쿨다운
- 운동 전후 5-10분간 스트레칭 필수
- 부상 예방을 위해 절대 생략하지 마세요', '러닝코치김', 245, 18, NOW(), NOW()),

('효과적인 러닝화 선택 팁', '올바른 러닝화 선택으로 부상을 예방하고 러닝 효율을 높이세요!

🏃‍♂️ 발 타입 파악하기
1. 평발 vs 높은 아치 vs 정상 아치
2. 발의 폭 (넓은 발 vs 좁은 발)

👟 러닝화 선택 기준
1. 쿠셔닝: 체중과 러닝 스타일에 맞게
2. 사이즈: 평소보다 0.5-1cm 큰 사이즈
3. 브랜드별 특성 이해하기

💡 구매 팁
- 오후에 발이 부은 상태에서 구매
- 실제 러닝할 때 신는 양말 착용 후 피팅
- 전문 러닝샵에서 발 분석 후 구매 추천', '신발전문가', 189, 23, NOW(), NOW()),

('마라톤 완주를 위한 12주 훈련 계획', '마라톤 완주를 목표로 하는 러너들을 위한 체계적인 훈련 계획입니다.

📅 1-4주차: 기초 체력 다지기
- 주 3-4회, 30-40분 LSD(Long Slow Distance)
- 1주일에 1회 인터벌 트레이닝

📅 5-8주차: 지구력 향상
- 주 4-5회 훈련
- 장거리 러닝 거리 점진적 증가 (주말 20-25km)

📅 9-12주차: 대회 준비
- 테이퍼링(tapering) 시작
- 실전 페이스 연습

⚠️ 주의사항
- 무리하지 말고 몸의 신호를 듣기
- 충분한 휴식과 영양 섭취
- 부상 징후 발견 시 즉시 휴식', '마라톤러너', 156, 31, NOW(), NOW());
```

## 향후 확장 예정 테이블

### 6. users (사용자)
- 사용자 정보 관리

### 7. running_records (달리기 기록)
- 개인 달리기 기록 저장

### 8. competition_participants (대회 참가자)
- 대회 참가 신청 정보

## 변경 이력

| 날짜 | 버전 | 변경 내용 | 작성자 |
|------|------|-----------|--------|
| 2024-09-15 | 1.0 | 초기 competitions 테이블 생성 | Claude |
| 2024-09-15 | 1.1 | posts 테이블 추가 (커뮤니티 게시판) | Claude |
| 2024-09-15 | 1.2 | comments 테이블 추가 (댓글 기능) | Claude |
| 2024-09-15 | 1.3 | H2에서 MySQL로 데이터베이스 전환 | Claude |
| 2024-09-15 | 1.4 | r_likes 테이블 추가 및 r_posts에 like_count 필드 추가 (좋아요 기능) | Claude |
| 2024-09-15 | 1.5 | r_tips 테이블 추가 (팁&노하우 게시판) | Claude |

## 주의사항

1. **데이터베이스**: MySQL 8.0 이상 사용 권장
2. **문자 인코딩**: 한글 데이터 저장을 위해 utf8mb4 charset 사용
3. **DateTime 처리**: 애플리케이션에서 LocalDateTime으로 처리, 타임존은 Asia/Seoul 설정
4. **텍스트 필드**: course, gifts, content 필드는 긴 텍스트 저장을 위해 TEXT 타입 사용
5. **참가비용**: 정수형으로 저장 (원 단위)
6. **자동 생성 필드**: created_at, updated_at은 JPA @PrePersist, @PreUpdate로 자동 설정
7. **환경변수**: DB_USERNAME, DB_PASSWORD 환경변수 설정 필요