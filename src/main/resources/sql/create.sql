CREATE TABLE IF NOT EXISTS User (
  name                VARCHAR(255),
  matriculationNumber VARCHAR(255) PRIMARY KEY,
  studyProgramme      VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS Course (
  id       VARCHAR(255),
  semester VARCHAR(255),
  PRIMARY KEY (id, semester)
);

CREATE TABLE IF NOT EXISTS Questionnaire (
  cid      VARCHAR(255) REFERENCES Course (id),
  semester VARCHAR(255) REFERENCES Course (Semester),
  id       BIGINT AUTO_INCREMENT,
  PRIMARY KEY (cid, semester, id)
);

CREATE TABLE IF NOT EXISTS ExamQuestionnaire (
  cid      VARCHAR(255) REFERENCES Questionnaire (cid),
  semester VARCHAR(255) REFERENCES Questionnaire (Semester),
  qid      BIGINT REFERENCES Questionnaire (id),
  date    TIMESTAMP,
  PRIMARY KEY (cid, semester, qid)
);

-- LearningQuestionnaire does not contain any additional arguments and
-- thus does not require an additional table (will be saved to the table
-- Questionnaire


CREATE TABLE IF NOT EXISTS Question (
  id               BIGINT       AUTO_INCREMENT PRIMARY KEY,
  questionText     TEXT       NOT NULL,
  picture          VARCHAR(255) DEFAULT NULL,
  answer1          TEXT       NOT NULL,
  answer2          TEXT       NOT NULL,
  answer3          TEXT         DEFAULT NULL,
  answer4          TEXT         DEFAULT NULL,
  answer5          TEXT         DEFAULT NULL,
  correctAnswers   VARCHAR(5) NOT NULL,
  optionalFeedback TEXT         DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS QuestionnaireQuestion (
  cid        VARCHAR(255) REFERENCES Questionnaire (cid),
  semester   VARCHAR(255) REFERENCES Questionnaire (Semester),
  qid        BIGINT REFERENCES Questionnaire (id),
  questionid BIGINT REFERENCES Question (id),
  PRIMARY KEY (cid, semester, qid, questionid)
);

