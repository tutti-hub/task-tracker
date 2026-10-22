create table users (
    id bigserial primary key,
    email varchar unique not null,
    password_hash varchar not null,
    created_at timestamptz not null default now()
);