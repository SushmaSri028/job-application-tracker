-- Users
CREATE TABLE users (
                       id          BIGSERIAL PRIMARY KEY,
                       email       VARCHAR(255) UNIQUE NOT NULL,
                       password    VARCHAR(255) NOT NULL,
                       full_name   VARCHAR(255),
                       created_at  TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);

-- Applications
CREATE TABLE applications (
                              id             BIGSERIAL PRIMARY KEY,
                              company        VARCHAR(255) NOT NULL,
                              role           VARCHAR(255) NOT NULL,
                              location       VARCHAR(255),
                              job_url        VARCHAR(1000),
                              notes          VARCHAR(2000),
                              status         VARCHAR(50) NOT NULL,
                              applied_date   DATE,
                              user_id        BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                              created_at     TIMESTAMP,
                              updated_at     TIMESTAMP
);

CREATE INDEX idx_applications_user_id ON applications(user_id);
CREATE INDEX idx_applications_status ON applications(status);