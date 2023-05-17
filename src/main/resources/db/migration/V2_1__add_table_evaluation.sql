CREATE TABLE IF NOT EXISTS evaluation (
    id              BIGINT          NOT NULL    AUTO_INCREMENT,
    reviewee_id     BIGINT          NOT NULL,
    reviewer_id     BIGINT          NOT NULL,
    review_id       BIGINT          UNIQUE,
    score           FLOAT           NOT NULL DEFAULT 0.0,
    content         VARCHAR(100)    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (reviewer_id) REFERENCES reviewer (id) ON DELETE CASCADE
) ENGINE = InnoDB;