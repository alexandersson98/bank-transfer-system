DROP TABLE IF EXISTS transaction_log;
DROP TABLE IF EXISTS account;


create table account (
                         account_id bigserial primary key,
                         owner_name varchar(100) not null,
                         balance decimal (19,4) not null check (balance >= 0)
);

create table transaction_log(
                                transaction_log_id bigserial primary key,
                                from_account_id bigint not null,
                                to_account_id bigint not null,
                                amount decimal(19,4) not null,
                                status varchar(20) not null check(status in ('SUCCESS', 'FAILED')),
                                error_message text,
                                created_at timestamp not null default current_timestamp,
                                foreign key (from_account_id) references account (account_id),
                                foreign key (to_account_id) references account (account_id));