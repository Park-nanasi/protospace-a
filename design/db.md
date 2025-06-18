## users テーブル

| Column   | Type   | Options  |
| -------- | ------ | -------- |
| id       | SERIAL | NOT NULL, PRIMARY KEY |
| username | VARCHAR(50)    | NOT NULL |
| email | VARCHAR(128)    | NOT NULL UNIQUE |
| password | VARCHAR(128)    | NOT NULL |
| profile | VARCHAR(128)    | NOT NULL |
| company | VARCHAR(50)    | NOT NULL |
| role | VARCHAR(50)    | NOT NULL |


### Option
- PRIMARY KEY (id)


## prototypes table

| Column | Type | Options |
| ------ | ------ | ------ |
| id     | SERIAL | NOT NULL, PRIMARY KEY|
| user_id     | INT | NOT NULL|
| name     | VARCHAR(50) | NOT NULL|
| catchphrase     | VARCHAR(128) | NOT NULL|
| concept     | VARCHAR(512)  | NOT NULL|
| image    | VARCHAR(256) | NOT NULL|
| created_at     | TIMESTAMP  | NOT NULL|
| updated_at     | TIMESTAMP  | NOT NULL|

### Option
- PRIMARY KEY(id)
- FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE

## comments table

| Column | Type | Options |
| ------ | ------ | ------ |
| id     | SERIAL | NOT NULL|
| content    | text | NULL|
| user_id     | INT | NOT NULL|
| prototype_id     | INT | NOT NULL|
| created_at     | TIMESTAMP  | NOT NULL|

### Option
- PRIMARY KEY(id)
- FOREIGN KEY (user_id) REFERENCES users(id)
- FOREIGN KEY (prototype_id) REFERENCES prototypes(id) ON DELETE CASCADE
