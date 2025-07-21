UPDATE topicos SET autor_id = 1 WHERE autor_id IS NULL OR autor_id = 0;

ALTER TABLE topicos MODIFY COLUMN autor_id BIGINT NOT NULL;

ALTER TABLE topicos ADD CONSTRAINT fk_topicos_autor_id
FOREIGN KEY (autor_id) REFERENCES usuarios(id);