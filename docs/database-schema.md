# 데이터베이스 스키마

## 개요
러너스 커뮤니티 애플리케이션의 데이터베이스 테이블 구조 및 생성문을 정의합니다.

## 테이블 목록

### 1. competitions (대회정보)

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
CREATE TABLE competitions (
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

#### H2 테이블 생성문 (개발용)

```sql
CREATE TABLE competitions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    event_date_time TIMESTAMP NOT NULL,
    location VARCHAR(255) NOT NULL,
    course CLOB NOT NULL,
    gifts CLOB,
    participation_fee INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
```

#### 인덱스

```sql
-- 대회 일시 검색용 인덱스
CREATE INDEX idx_competitions_event_date_time ON competitions(event_date_time);

-- 대회 장소 검색용 인덱스
CREATE INDEX idx_competitions_location ON competitions(location);

-- 등록일시 정렬용 인덱스
CREATE INDEX idx_competitions_created_at ON competitions(created_at);
```

#### 샘플 데이터

```sql
INSERT INTO competitions (name, event_date_time, location, course, gifts, participation_fee, created_at) VALUES
('서울 한강 마라톤 대회', '2024-05-15 08:00:00', '서울특별시 영등포구 한강공원 여의도지구', '여의도 한강공원 출발 → 반포대교 → 잠수교 → 여의도 한강공원 도착\n총 거리: 10km\n평탄한 코스로 초보자도 참가 가능', '완주메달, 기념 티셔츠, 스포츠 타월', 30000, NOW()),
('부산 해운대 달리기 축제', '2024-06-20 07:30:00', '부산광역시 해운대구 해운대해수욕장', '해운대해수욕장 출발 → 동백섬 → 달맞이고개 → 해운대해수욕장 도착\n총 거리: 5km\n바다 전망이 아름다운 코스', '완주메달, 해운대 기념품, 생수', 25000, NOW()),
('제주 올레길 트레일런', '2024-07-10 09:00:00', '제주특별자치도 서귀포시 올레 7코스', '외돌개 출발 → 월평포구 → 서귀포 자연휴양림 도착\n총 거리: 15km\n중급자 이상 추천', '완주메달, 제주 특산품 세트, 기능성 양말', 45000, NOW());
```

## 향후 확장 예정 테이블

### 2. users (사용자)
- 사용자 정보 관리

### 3. running_records (달리기 기록)
- 개인 달리기 기록 저장

### 4. community_posts (커뮤니티 게시글)
- 커뮤니티 게시글 저장

### 5. competition_participants (대회 참가자)
- 대회 참가 신청 정보

## 변경 이력

| 날짜 | 버전 | 변경 내용 | 작성자 |
|------|------|-----------|--------|
| 2024-09-15 | 1.0 | 초기 competitions 테이블 생성 | Claude |

## 주의사항

1. **문자 인코딩**: 한글 데이터 저장을 위해 utf8mb4 charset 사용 (MySQL)
2. **DateTime 처리**: 애플리케이션에서 LocalDateTime으로 처리
3. **텍스트 필드**: course, gifts 필드는 긴 텍스트 저장을 위해 TEXT/CLOB 타입 사용
4. **참가비용**: 정수형으로 저장 (원 단위)
5. **자동 생성 필드**: created_at은 JPA @PrePersist로 자동 설정