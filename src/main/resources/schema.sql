-- BENCHMARKS
CREATE SEQUENCE IF NOT EXISTS benchmarks_id_seq INCREMENT BY 1 MINVALUE 1 START 1;

CREATE TABLE IF NOT EXISTS benchmarks (
    id BIGINT PRIMARY KEY DEFAULT nextval('benchmarks_id_seq'),
    project_name VARCHAR(255) NOT NULL,
    period VARCHAR(255) NOT NULL,
    release_frequency DOUBLE PRECISION NOT NULL,
    lead_time DOUBLE PRECISION NOT NULL,
    time_to_repair DOUBLE PRECISION NOT NULL,
    bug_issues_rate DOUBLE PRECISION NOT NULL
);

-- PROJECTS
CREATE SEQUENCE IF NOT EXISTS projects_id_seq INCREMENT BY 1 MINVALUE 1 START 1;

CREATE TABLE IF NOT EXISTS projects (
    id BIGINT PRIMARY KEY DEFAULT nextval('projects_id_seq'),
    title VARCHAR(255) NOT NULL UNIQUE, 
    name VARCHAR(255) NOT NULL UNIQUE, 
    release_frequency DOUBLE PRECISION,
    lead_time DOUBLE PRECISION,
    time_to_repair DOUBLE PRECISION,
    bug_issues_rate DOUBLE PRECISION
);

--COLOR RANGES
CREATE SEQUENCE IF NOT EXISTS colorRanges_id_seq INCREMENT BY 1 MINVALUE 1 START 1;

CREATE TABLE IF NOT EXISTS colorRanges (
    id BIGINT PRIMARY KEY DEFAULT nextval('colorRanges_id_seq'), 
    metric VARCHAR(255) NOT NULL UNIQUE,
    green VARCHAR(255) NOT NULL, 
    yellow VARCHAR(255) NOT NULL,
    red VARCHAR(255) NOT NULL 
);

-- GITHUB PROJECTS
CREATE SEQUENCE IF NOT EXISTS github_projects_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS github_projects (id BIGINT PRIMARY KEY DEFAULT nextval('github_projects_id_seq'::regclass), address VARCHAR(255) UNIQUE NOT NULL, name VARCHAR(255) UNIQUE, release_frequency DOUBLE PRECISION, lead_time_for_released_changes DOUBLE PRECISION, time_to_repair_code DOUBLE PRECISION, bug_issues_rate DOUBLE PRECISION);

-- REQUESTS
CREATE SEQUENCE IF NOT EXISTS requests_id_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS requests (id BIGINT PRIMARY KEY DEFAULT nextval('requests_id_seq'::regclass), github_project_id BIGINT NOT NULL, FOREIGN KEY (github_project_id) REFERENCES github_projects (id));