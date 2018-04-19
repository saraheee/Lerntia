CREATE TABLE IF NOT EXISTS qanda (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  question TEXT NOT NULL,
  answer TEXT NOT NULL
);

INSERT INTO qanda
  SELECT * FROM (
  SELECT * FROM qanda WHERE FALSE
    UNION SELECT 1, 'question of life, the universe, and everything', '42'
  )
WHERE NOT EXISTS(SELECT * FROM qanda);