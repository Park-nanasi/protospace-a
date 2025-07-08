CREATE TABLE IF NOT EXISTS sns_links (
  id SERIAL NOT NULL PRIMARY KEY,
  -- x_url: 
    -- https://x.com/ 14
    -- <account_name> 50
  -- facebook_url:
    -- https://www.facebook.com/ 25
    -- <account_name> 50
  x_url VARCHAR(64),
  facebook_url VARCHAR(75)
);
