CREATE TABLE IF NOT EXISTS PUser ( -- "User" is a reserved SQL word
  name                VARCHAR(255),
  matriculationNumber VARCHAR(255) PRIMARY KEY,
  studyProgramme      VARCHAR(255),
  isDeleted           BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS Course (
  id bigint auto_increment primary key,
  mark      VARCHAR(255) not null,
  semester  VARCHAR(255) not null,
  name      VARCHAR(255),
  isDeleted BOOLEAN DEFAULT FALSE,
);

CREATE TABLE IF NOT EXISTS PUserCourse (
  matriculationNumber VARCHAR(255) REFERENCES PUser (matriculationNumber),
  courseid             bigint references Course (id),
  isDeleted           BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (matriculationNumber, courseid)
);

CREATE TABLE IF NOT EXISTS Questionnaire (
  courseid  bigint REFERENCES Course (id),
  id        BIGINT  AUTO_INCREMENT PRIMARY KEY,
  name      VARCHAR(255) NOT NULL,
  selected    BOOLEAN DEFAULT FALSE,
  isDeleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS PUserQuestionnaire (
  matriculationNumber VARCHAR(255) REFERENCES PUser (matriculationNumber),
  qid                 BIGINT REFERENCES Questionnaire (id),
  isDeleted           BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (matriculationNumber, qid)
);

CREATE TABLE IF NOT EXISTS LearningQuestionnaire (
  id BIGINT PRIMARY KEY REFERENCES Questionnaire(id),
);

CREATE TABLE IF NOT EXISTS ExamQuestionnaire (
  id BIGINT PRIMARY KEY REFERENCES Questionnaire(id),
  qdate      TIMESTAMP DEFAULT NULL,
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