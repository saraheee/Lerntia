CREATE TABLE IF NOT EXISTS PUser ( -- "User" is a reseved SQL word
  name                VARCHAR(255),
  matriculationNumber VARCHAR(255) PRIMARY KEY,
  studyProgramme      VARCHAR(255),
  isDeleted           BOOLEAN DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS Course (
  mark      VARCHAR(255),
  semester  VARCHAR(255),
  isDeleted BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (id, semester)
);

CREATE TABLE IF NOT EXISTS Questionnaire (
  mark      VARCHAR(255) REFERENCES Course (mark),
  semester  VARCHAR(255) REFERENCES Course (Semester),
  id        BIGINT AUTO_INCREMENT,
  isDeleted BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (cmark, semester, id)
);

CREATE TABLE IF NOT EXISTS ExamQuestionnaire (
  mark      VARCHAR(255) REFERENCES Questionnaire (mark),
  semester  VARCHAR(255) REFERENCES Questionnaire (Semester),
  qid       BIGINT REFERENCES Questionnaire (id),
  date      TIMESTAMP,
  isDeleted BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (cmark, semester, qid)
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
  optionalFeedback TEXT         DEFAULT NULL,
  isDeleted        BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS QuestionnaireQuestion (
  cmark      VARCHAR(255) REFERENCES Questionnaire (mark),
  semester   VARCHAR(255) REFERENCES Questionnaire (Semester),
  qid        BIGINT REFERENCES Questionnaire (id),
  questionid BIGINT REFERENCES Question (id),
  isDeleted  BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (cmark, semester, qid, questionid)
);