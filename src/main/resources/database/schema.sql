# Structure required to accomodate authentication information
create table api_token(auto_id serial PRIMARY KEY, token varchar(64) not null, created_at timestamp not null);
create table api_user(auto_id serial PRIMARY KEY, email text, password varchar (64), salt varchar (32), token_id integer references api_token (auto_id));
