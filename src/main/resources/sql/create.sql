CREATE TABLE IF NOT EXISTS PUser ( -- "User" is a reserved SQL word
  name                VARCHAR(255),
  matriculationNumber VARCHAR(255) PRIMARY KEY,
  studyProgramme      VARCHAR(255),
  isDeleted           BOOLEAN DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS Course (
  mark      VARCHAR(255),
  semester  VARCHAR(255),
  isDeleted BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (mark, semester)
);

CREATE TABLE IF NOT EXISTS PUserCourse (
  matriculationNumber VARCHAR(255) REFERENCES PUser (matriculationNumber),
  cmark               VARCHAR(255) REFERENCES Course (mark),
  semester            VARCHAR(255) REFERENCES Course (semester),
  PRIMARY KEY (matriculationNumber, cmark, semester)
);

CREATE TABLE IF NOT EXISTS Questionnaire (
  cmark     VARCHAR(255) REFERENCES Course (mark),
  semester  VARCHAR(255) REFERENCES Course (Semester),
  id        BIGINT  AUTO_INCREMENT PRIMARY KEY,
  isDeleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS PUserQuestionnaire (
  matriculationNumber VARCHAR(255) REFERENCES PUser (matriculationNumber),
  qid                 BIGINT REFERENCES Questionnaire (id),
  PRIMARY KEY (matriculationNumber, qid)
);

CREATE TABLE IF NOT EXISTS LearningQuestionnaire (
  id BIGINT PRIMARY KEY REFERENCES Questionnaire(id),
  name      VARCHAR(255) NOT NULL,
);

CREATE TABLE IF NOT EXISTS ExamQuestionnaire (
  id BIGINT PRIMARY KEY REFERENCES Questionnaire(id),
  date      TIMESTAMP DEFAULT NULL,
);

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
  qid        BIGINT REFERENCES Questionnaire (id),
  questionid BIGINT REFERENCES Question (id),
  isDeleted  BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (qid, questionid)
);