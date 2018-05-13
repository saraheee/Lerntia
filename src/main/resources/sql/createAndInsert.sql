DROP TABLE IF EXISTS QuestionnaireQuestion;
DROP TABLE IF EXISTS Question;
DROP TABLE IF EXISTS LearningQuestionnaire;
DROP TABLE IF EXISTS ExamQuestionnaire;
DROP TABLE IF EXISTS PUserQuestionnaire;
DROP TABLE IF EXISTS Questionnaire;
DROP TABLE IF EXISTS PUserCourse;
DROP TABLE IF EXISTS Course;
DROP TABLE IF EXISTS PUser;

CREATE TABLE IF NOT EXISTS PUser ( -- "User" is a reserved SQL word
  name                VARCHAR(255),
  matriculationNumber VARCHAR(255) PRIMARY KEY,
  studyProgramme      VARCHAR(255),
  isDeleted           BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS Course (
  mark      VARCHAR(255) primary key,
  semester  VARCHAR(255),
  name      VARCHAR(255),
  isDeleted BOOLEAN DEFAULT FALSE,
  -- PRIMARY KEY (mark, semester) kein many to one relation hier möglich
);

CREATE TABLE IF NOT EXISTS PUserCourse (
  matriculationNumber VARCHAR(255) REFERENCES PUser (matriculationNumber),
  cmark               VARCHAR(255) REFERENCES Course (mark),
  semester            VARCHAR(255) REFERENCES Course (semester),
  isDeleted           BOOLEAN DEFAULT FALSE,
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
  isDeleted           BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (matriculationNumber, qid)
);
--
CREATE TABLE IF NOT EXISTS LearningQuestionnaire (
  id BIGINT PRIMARY KEY REFERENCES Questionnaire(id),
  name      VARCHAR(255) NOT NULL,
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

--name, matriculationNumber, studyProgramme, isDeleted
INSERT INTO PUser
   SELECT * FROM (
      SELECT * FROM PUser WHERE FALSE
        UNION SELECT 'testuser', '15', 'keineahnung', false
  )
WHERE NOT EXISTS (SELECT * FROM PUser);

--mark, semester, name, isDeleted
INSERT INTO Course
   SELECT * FROM (
      SELECT * FROM Course WHERE FALSE
        UNION SELECT '1', '4', 'TIL', false
  )
WHERE NOT EXISTS (SELECT * FROM Course);

--matriculationNumber REF PUser (matriculationNumber),
--cmark               REF Course (mark),
--semester            REF Course (semester),
--isDeleted
INSERT INTO PUserCourse
   SELECT * FROM (
      SELECT * FROM PUserCourse WHERE FALSE
        UNION SELECT '15', '1', '4', false
  )
WHERE NOT EXISTS (SELECT * FROM PUserCourse);

--cmark     REF Course (mark),
--semester  REF Course (Semester),
--id,
--isDeleted
--INSERT INTO Questionnaire
--   SELECT * FROM (
--      SELECT * FROM Questionnaire WHERE FALSE
--        UNION SELECT '1', '4', '1', false
--  )
--WHERE NOT EXISTS (SELECT * FROM Questionnaire);
