
CREATE TABLE IF NOT EXISTS stats (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app VARCHAR(30) NOT NULL,
    uri VARCHAR(50) NOT NULL,
    ip VARCHAR(39) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
