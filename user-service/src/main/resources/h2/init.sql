create table users (
    id bigint auto_increment,
    name varchar(50),
    balance int,
    primary key (id)
);

create table user_transaction (
    id bigint auto_increment,
    user_id bigint,
    amount int,
    transaction_date timestamp,
    foreign key (user_id) references users(id) on delete cascade -- On delete User, delete all the transactions which refers that key of that user
);

insert into users
    (name, balance)
    values
    ('Rudy', 2000),
    ('Mike', 500),
    ('Jake', 800),
    ('Marc', 1520);