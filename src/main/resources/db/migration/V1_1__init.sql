create table IF NOT EXISTS member (
    id              BIGINT          NOT NULL    AUTO_INCREMENT,
    github_id       VARCHAR(255)    NOT NULL    UNIQUE,
    username        VARCHAR(255)    NOT NULL    UNIQUE,
    email           VARCHAR(255)    UNIQUE,
    image_url       VARCHAR(255),
    profile_url     VARCHAR(255)    NOT NULL    UNIQUE,
    is_reviewer     BOOLEAN         DEFAULT 0,
    PRIMARY KEY (id),
) ENGINE = InnoDB

create table IF NOT EXISTS reviewer (
    id              BIGINT          NOT NULL    AUTO_INCREMENT,
    member_id       BIGINT          NOT NULL,
    job             VARCHAR(255),
    career          VARCHAR(255),
    introduction    VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
) ENGINE = InnoDB

CREATE TABLE IF NOT EXISTS refresh_token (
    member_id       BIGINT          PRIMARY KEY,
    token           VARCHAR(255)    NOT NULL    UNIQUE,
    issued_at       DATETIME        NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
)

create table IF NOT EXISTS category (
    id              BIGINT          NOT NULL    AUTO_INCREMENT,
    name            VARCHAR(255)    NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB

create table IF NOT EXISTS tag (
    id              BIGINT          NOT NULL    AUTO_INCREMENT,
    category_id     BIGINT          NOT NULL,
    name            VARCHAR(255)    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
) ENGINE = InnoDB

create table IF NOT EXISTS reviewer_tag (
    id              BIGINT          NOT NULL    AUTO_INCREMENT,
    reviewer_id     BIGINT          NOT NULL,
    tag_id          BIGINT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (reviewer_id) REFERENCES reviewer (id),
    FOREIGN KEY (tag_id) REFERENCES tag (id)
) ENGINE = InnoDB

create table IF NOT EXISTS review (
    id              BIGINT          NOT NULL    AUTO_INCREMENT,
    reviewee_id     BIGINT          NOT NULL,
    reviewer_id     BIGINT          NOT NULL,
    title           VARCHAR(50)     NOT NULL,
    content         VARCHAR(1500)   NOT NULL,
    pr_url          VARCHAR(255)    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (reviewee_id) REFERENCES member (id),
    FOREIGN KEY (reviewer_id) REFERENCES member (id),
) ENGINE = InnoDB