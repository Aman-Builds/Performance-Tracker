-- Employee Performance Tracker — Schema

-- Drop child tables first to avoid foreign key constraint violations.

DROP TABLE IF EXISTS goals;
DROP TABLE IF EXISTS performance_reviews;
DROP TABLE IF EXISTS review_cycles;
DROP TABLE IF EXISTS employees;

-- Using VARCHAR for flexibility and easier schema evolution.

CREATE TABLE employees (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL,
    department  VARCHAR(100)    NOT NULL,
    role        VARCHAR(100)    NOT NULL,
    joining_date DATE           NOT NULL,
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    -- Prevent blank employee names.
    CONSTRAINT chk_employee_name CHECK (TRIM(name) <> '')
);

-- Optimizes employee searches by department.
CREATE INDEX idx_employees_department ON employees(department);

-- Review cycle date validation enforced through a check constraint.

CREATE TABLE review_cycles (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)    NOT NULL UNIQUE,
    start_date  DATE            NOT NULL,
    end_date    DATE            NOT NULL,
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_cycle_dates CHECK (end_date > start_date)
);

-- Allow multiple reviews per employee per cycle while preventing duplicate
-- submissions from the same reviewer.

CREATE TABLE performance_reviews (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id     BIGINT          NOT NULL,
    cycle_id        BIGINT          NOT NULL,
    rating          SMALLINT        NOT NULL,
    reviewer_id     VARCHAR(100)    NOT NULL,
    reviewer_notes  TEXT,
    submitted_at    TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    -- Restrict ratings to the supported range.
    CONSTRAINT chk_rating_range CHECK (rating >= 1 AND rating <= 5),

    -- Prevent duplicate reviews from the same reviewer within a cycle.
    CONSTRAINT uq_review_per_reviewer UNIQUE (employee_id, cycle_id, reviewer_id),

    CONSTRAINT fk_review_employee
        FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_cycle
        FOREIGN KEY (cycle_id) REFERENCES review_cycles(id) ON DELETE CASCADE
);

-- Supports employee review lookups and cycle reporting queries.
CREATE INDEX idx_reviews_employee_cycle ON performance_reviews(employee_id, cycle_id);

-- Optimizes cycle-level review aggregations.
CREATE INDEX idx_reviews_cycle ON performance_reviews(cycle_id);

-- Goals are tracked per employee and review cycle.

CREATE TABLE goals (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT          NOT NULL,
    cycle_id    BIGINT          NOT NULL,
    title       VARCHAR(255)    NOT NULL,
    status      VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_goal_status CHECK (status IN ('PENDING', 'COMPLETED', 'MISSED')),

    -- Prevent blank goal titles.
    CONSTRAINT chk_goal_title CHECK (TRIM(title) <> ''),

    CONSTRAINT fk_goal_employee
        FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    CONSTRAINT fk_goal_cycle
        FOREIGN KEY (cycle_id) REFERENCES review_cycles(id) ON DELETE CASCADE
);

-- Optimizes goal status counts for cycle summary reports.
CREATE INDEX idx_goals_cycle_status ON goals(cycle_id, status);

-- Supports goal lookups for an employee within a review cycle.
CREATE INDEX idx_goals_employee_cycle ON goals(employee_id, cycle_id);