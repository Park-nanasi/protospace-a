## users テーブル

| Column   | Type   | Options  |
| -------- | ------ | -------- |
| id       | SERIAL | NOT NULL, PRIMARY KEY |
| sns_links_id       | SERIAL | NOT NULL |
| username | VARCHAR(50)    | NOT NULL |
| email | VARCHAR(128)    | NOT NULL UNIQUE |
| password | VARCHAR(128)    | NOT NULL |
| profile | VARCHAR(128)    | NOT NULL |
| profile_image | VARCHAR(256) | |;


### Option
- PRIMARY KEY (id)
- FOREIGN KEY (sns_links_id) REFERENCES sns_links(id) ON DELETE CASCADE


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
| count_likes     | INT | DEFAULT 0|

## prototype_likes table

| Column | Type | Options |
| ------ | ------ | ------ |
| id     | SERIAL | NOT NULL, PRIMARY KEY|
| user_id     | INT | NOT NULL|
| prototype_id     | INT | NOT NULL|
| created_at     | TIMESTAMP  | NOT NULL|

### Option
- PRIMARY KEY(id)
- FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE

## comments table

| Column | Type | Options |
| ------ | ------ | ------ |
| id     | SERIAL | NOT NULL|
| content    | text | NULL|
| user_id     | INT | NOT NULL|
| title     | text | NOT NULL|
| prototype_id     | INT | NOT NULL|
| created_at     | TIMESTAMP  | NOT NULL|

### Option
- PRIMARY KEY(id)
- FOREIGN KEY (user_id) REFERENCES users(id)
- FOREIGN KEY (prototype_id) REFERENCES prototypes(id) ON DELETE CASCADE

## sns links table
| Column | Type | Options |
| ------ | ------ | ------ |
| id     | SERIAL | NOT NULL|
| x_url   | String | |
| facebook_url     | String  | |

### Option
- PRIMARY KEY(id)
