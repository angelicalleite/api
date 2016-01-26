# Structure required to accomodate cities and states from Brazil data
DROP TABLE IF EXISTS estados CASCADE;
DROP TABLE IF EXISTS cidades CASCADE;

DROP INDEX IF EXISTS estados_id_index CASCADE;
DROP INDEX IF EXISTS cidades_id_index CASCADE;
DROP INDEX IF EXISTS estados_nome_index CASCADE;
DROP INDEX IF EXISTS cidades_nome_index CASCADE;


create table estados(id int unique not null, sigla char(2) unique not null, nome varchar (25) not null, nome_unaccent varchar (25));
create table cidades(id int unique not null, estado_id int references estados(id), nome varchar (50) not null, nome_unaccent varchar (50));


create index cidades_id_index on cidades (id);
create index cidades_nome_index on cidades (nome);
create index cidades_nome_unacc_index on cidades (nome_unaccent);

create index estados_id_index on estados (id);
create index estados_nome_index on estados (nome);
create index estados_nome_index_unac on estados (nome_unaccent);

