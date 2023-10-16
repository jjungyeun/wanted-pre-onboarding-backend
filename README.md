# 원티드 프리온보딩 백엔드 인턴십 선발과제 (2023.10)

## 목차
- [개요](#개요)
- [Database 설계](#database)
- [API 설계](#api)
- [개발 과정](#개발-과정)


## 개요

- [과제 상세 안내 페이지](https://www.notion.so/wonwonjung/1850bca26fda4e0ca1410df270c03409?pvs=4)

### 서비스 소개

- 본 서비스는 기업의 채용을 위한 웹 서비스 입니다.
- 회사는 채용공고를 생성하고, 이에 사용자는 지원합니다.

### 요구사항

1. 채용공고 등록
2. 채용공고 수정
3. 채용공고 삭제
4. 채용공고 리스트 검색
5. 채용공고 조회
6. 채용공고에 지원

### 기술 스택

- Java & Spring
- MySQL

## Database

### Entity

#### 회사 - **Company**

- id (pk)
- name
- country
- region

#### 사용자 - **Member**

- id (pk)
- name

#### 채용공고 - **Recruitment**

- id (pk)
- company id (fk)
- position
- reward
- contents
- skill

#### 지원내역 - **CompanyApplication**

- id (pk)
- recruitment id (fk)
- member id (fk)

### ERD
![ERD](https://github.com/jjungyeun/wanted-pre-onboarding-backend/blob/master/wanted%20erd.png?raw=true)

### Table SQL

```sql
CREATE TABLE COMPANY (
	company_id	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	company_name	NVARCHAR(255)	NOT NULL,
	country	NVARCHAR(255)	NOT NULL,
	region	NVARCHAR(255)	NOT NULL
);

CREATE TABLE MEMBER (
	member_id	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	member_name	NVARCHAR(255)	NOT NULL
);

CREATE TABLE RECRUITMENT (
	recruitment_id	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	company_id	BIGINT	NOT NULL,
	position	NVARCHAR(255)	NOT NULL,
	reward	INT	NOT NULL,
	contents	NVARCHAR(255)	NOT NULL,
	skill	NVARCHAR(255)	NOT NULL
);

CREATE TABLE COMPANY_APPLICATION (
	company_application_id	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	member_id	BIGINT	NOT NULL,
	recruitment_id	BIGINT	NOT NULL,
	application_time TIMESTAMP NOT NULL
);

ALTER TABLE RECRUITMENT ADD CONSTRAINT FK_COMPANY_TO_RECRUITMENT_1 FOREIGN KEY (
	company_id
)
REFERENCES COMPANY (
	company_id
);

ALTER TABLE COMPANY_APPLICATION ADD CONSTRAINT FK_MEMBER_TO_COMPANY_APPLICATION_1 FOREIGN KEY (
	member_id
)
REFERENCES MEMBER (
	member_id
);

ALTER TABLE COMPANY_APPLICATION ADD CONSTRAINT FK_RECRUITMENT_TO_COMPANY_APPLICATION_1 FOREIGN KEY (
	recruitment_id
)
REFERENCES RECRUITMENT (
	recruitment_id
);
```

## API

### 1. 채용공고 등록

- Method: POST
- URL: /recruitments

#### Request Body (JSON)

| Path | Type | Description | Required | ETC |
| --- | --- | --- | --- | --- |
| company_id | number | 회사 아이디 | Y |  |
| position | string | 채용포지션 | Y |  |
| reward | string | 채용보상금 | Y |  |
| contents | string | 채용내용 | Y |  |
| skill | string | 사용기술 | Y |  |

```json
{
  "company_id":회사_id,
  "position":"백엔드 주니어 개발자",
  "reward":1000000,
  "contents":"원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
  "skill":"Python"
}
```

#### Response

```json
{ 
	"status" : 200,
	"message": "OK",
	"body" : {
		"created": 12 // 생성된 채용공고 아이디
	},
}
```

### 2. 채용공고 수정

- Method: PATCH
- URL: /recruitments/{recruitment_id}

#### Path Variable

| Variable | Description |
| --- | --- |
| {recruitment_id} | 채용공고 아이디 |

#### Request Body (JSON)

| Path | Type | Description | Required | ETC |
| --- | --- | --- | --- | --- |
| position | string | 채용포지션 | N |  |
| reward | string | 채용보상금 | N |  |
| contents | string | 채용내용 | N |  |
| skill | string | 사용기술 | N |  |

```json
{
  "position":"백엔드 주니어 개발자",
  "reward":1500000, # 변경됨
  "contents":"원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
  "skill":"Python"
}
```

#### Response

```json
{ 
	"status" : 200,
	"message": "OK"
}
```

### 3. 채용공고 삭제

- Method: DELETE
- URL: /recruitments/{recruitment_id}

#### Path Variable

| Variable | Description |
| --- | --- |
| {recruitment_id} | 채용공고 아이디 |

#### Response

```json
{ 
	"status" : 200,
	"message": "OK"
}
```

### 4. 채용공고 목록 조회/검색

- Method: GET
- URL: /recruitments?search=원티드

#### Query Parameter

| Variable | Description |
| --- | --- |
| search | 검색어 |

#### Response

```json
{ 
	"status" : 200,
	"message": "OK",
	"body": [
			{
				"recruitment_id": 채용공고_id,
			  "company_name":"원티드랩",
			  "country":"한국",
			  "region":"서울",
			  "position":"백엔드 주니어 개발자",
			  "reward":1500000,
			  "skill":"Python"
			},
			{
				"recruitment_id": 채용공고_id,
			  "company_name":"원티드코리아",
			  "country":"한국",
			  "region":"부산",
			  "position":"프론트엔드 개발자",
			  "reward":500000,
			  "skill":"javascript"
			}
		]
}
```

### 5. 채용공고 상세 조회

- Method: GET
- URL: /recruitments/{recruitment_id}

#### Path Variable

| Variable | Description |
| --- | --- |
| {recruitment_id} | 채용공고 아이디 |

#### Response

```json
{ 
	"status" : 200,
	"message": "OK",
	"body" : {
		"recruitment_id": 채용공고_id,
	  "company_name":"원티드랩",
	  "country":"한국",
	  "region":"서울",
	  "position":"백엔드 주니어 개발자",
	  "reward":1500000,
	  "skill":"Python",
		"contents": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
		"other_recruitments":[채용공고_id, 채용공고_id, ..]
	},
}
```

### 6. 채용공고 지원

- Method: POST
- URL: /recruitments/{recruitment_id}

#### Path Variable

| Variable | Description |
| --- | --- |
| {recruitment_id} | 채용공고 아이디 |

#### Request Body (JSON)

| Path | Type | Description | Required | ETC |
| --- | --- | --- | --- | --- |
| member_id | number | 사용자 아이디 | Y |  |

```json
{
	"member_id": 사용자_id
}
```

#### Response

```json
{ 
	"status" : 200,
	"message": "OK"
}
```

## 개발 과정
1. Entity 구현 https://github.com/jjungyeun/wanted-pre-onboarding-backend/issues/1
2. Controller 구현 https://github.com/jjungyeun/wanted-pre-onboarding-backend/issues/2
3. Service 구현 https://github.com/jjungyeun/wanted-pre-onboarding-backend/issues/3
4. API 테스트 작성 https://github.com/jjungyeun/wanted-pre-onboarding-backend/issues/4
