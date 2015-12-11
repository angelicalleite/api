# Structure required to accomodate authentication information
create table api_token(auto_id serial PRIMARY KEY, token varchar(64) unique not null, created_at timestamp not null);
create table api_user(auto_id serial PRIMARY KEY, email text unique, password varchar (64), salt varchar (32) unique, authorized boolean default false, token_id integer references api_token (auto_id));
insert into api_user (email, password, salt, authorized) values ('admin@sibbr.gov.br', 'a45ef5caf288ee1430a081b0d98f7f66a8b48a69b930c6c7740965875a3f4e1d', '21232f297a57a5a743894a0e4a801fc3', true);
create index api_user_email_index on api_user (email);
create index api_token_token_index on api_token (token);

