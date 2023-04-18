-- BENCHMARKS
CREATE SEQUENCE IF NOT EXISTS benchmarks_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS benchmarks (id BIGINT PRIMARY KEY DEFAULT nextval('benchmarks_id_seq'::regclass), name VARCHAR(255) UNIQUE NOT NULL, release_frequency VARCHAR(255) NOT NULL, lead_time_for_released_changes VARCHAR(255) NOT NULL, time_to_repair_code VARCHAR(255) NOT NULL, bug_issues_rate VARCHAR(255) NOT NULL);

-- GITHUB PROJECTS
CREATE SEQUENCE IF NOT EXISTS github_projects_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS github_projects (id BIGINT PRIMARY KEY DEFAULT nextval('github_projects_id_seq'::regclass), address VARCHAR(255) UNIQUE NOT NULL, name VARCHAR(255) UNIQUE, release_frequency DOUBLE PRECISION, lead_time_for_released_changes DOUBLE PRECISION, time_to_repair_code DOUBLE PRECISION, bug_issues_rate DOUBLE PRECISION);

-- REQUESTS
CREATE SEQUENCE IF NOT EXISTS requests_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS requests (id BIGINT PRIMARY KEY DEFAULT nextval('requests_id_seq'::regclass), github_project_id BIGINT NOT NULL, FOREIGN KEY (github_project_id) REFERENCES github_projects (id));