/*usage <path>/cqlsh < src/core/cql/schema.sql*/

/*Key space*/
/*Use your keyspace name*/
CREATE KEYSPACE centipair WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
USE centipair;
/*users*/
CREATE TABLE user_account (user_id timeuuid PRIMARY KEY, username text, email text, password text, first_name text, last_name text, active boolean);
CREATE TABLE user_profile (user_id timeuuid PRIMARY KEY, first_name text, middle_name text, last_name text, email text, website text, phone_mobile text, phone_fixed text,
       address_line1 text, street text, city text, state text, country text, profile_photo text, 
       gender text, birth_date text, birth_year int, chat_channel text, chat_id text);
CREATE TABLE user_login_username(username text PRIMARY KEY, user_id timeuuid);
CREATE TABLE user_login_email(email text PRIMARY KEY, user_id timeuuid);
CREATE TABLE user_session (auth_token text PRIMARY KEY, session_expire_time timestamp, user_id timeuuid);
CREATE TABLE user_session_index (user_id timeuuid, auth_token text, PRIMARY KEY(user_id, auth_token));

/*CMS*/
CREATE TABLE site (site_id uuid PRIMARY KEY, site_name text, domain_name text, active boolean);
CREATE TABLE site_domain(domain_name text PRIMARY KEY, site_id uuid);
CREATE TABLE site_admin (user_id timeuuid, site_id uuid, PRIMARY KEY (user_id, site_id));
CREATE TABLE page (url text, site_id uuid, title text, content text, meta_tags text, meta_description text, PRIMARY KEY (url, site_id));

/*MOBILE API*/
CREATE TABLE api_app_key(app_key text PRIMARY KEY, platform text, version text);
CREATE TABLE api_request_token(request_token text PRIMARY KEY, app_key text, device_id text);

CREATE TABLE api_device_request_token(request_token text PRIMARY KEY, platform text, model text, version text);
CREATE TABLE api_device_auth_token(auth_token text PRIMARY KEY, platform text, model text, version text);
