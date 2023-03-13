CREATE TABLE IF NOT EXISTS member
(
    id              BIGINT          PRIMARY KEY AUTO_INCREMENT,
    github_id       BIGINT          NOT NULL UNIQUE,
    username        VARCHAR(30)     NOT NULL UNIQUE,
    email           VARCHAR(100)    NOT NULL UNIQUE,
    image_url       VARCHAR(255),
    github_url      VARCHAR(255)    NOT NULL UNIQUE,
    introduction    VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    member_id       BIGINT          PRIMARY KEY,
    token    VARCHAR(255)    NOT NULL UNIQUE,
    issued_at       DATETIME        NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);