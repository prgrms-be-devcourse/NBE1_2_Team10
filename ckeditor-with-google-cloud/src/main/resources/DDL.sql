-- auto-generated definition
create table posting_content_table
(
    posting_id bigint auto_increment
        primary key,
    title      varchar(100) not null,
    content    longtext     not null
);

