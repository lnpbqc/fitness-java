use fitness;

SET FOREIGN_KEY_CHECKS = 0;

SELECT GROUP_CONCAT(CONCAT('`', table_name, '`'))
INTO @tables
FROM information_schema.tables
WHERE table_schema = 'fitness';

SET @sql = CONCAT('DROP TABLE ', @tables);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET FOREIGN_KEY_CHECKS = 1;