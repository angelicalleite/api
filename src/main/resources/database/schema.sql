# Structure required to accomodate authentication information
create table api_token(auto_id serial PRIMARY KEY, token varchar(64) unique not null, created_at timestamp not null);
create table api_user(auto_id serial PRIMARY KEY, email text unique, password varchar (64), salt varchar (32) unique, authorized boolean default false, token_id integer references api_token (auto_id));
insert into api_user (email, password, salt, authorized) values ('admin@sibbr.gov.br', '51b0306073744ff0b7a5f740cff00160a4b2c984492251df702ba033475c2856', '43dd770ebb1ce390162b4386d358cab6', true);
create index api_user_email_index on api_user (email);
create index api_token_token_index on api_token (token);


#apt-get install postgresql-contrib
#CREATE EXTENSION unaccent;

create index ocurrence_rid_index on occurrence (resource_id);
create index ocurrence_up_sc_name_index on occurrence (upper(scientificname));
create index ocurrence_up_taxr_index on occurrence (upper(taxonrank));
		