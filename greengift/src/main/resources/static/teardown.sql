drop table if exists user_tb cascade ;

create table festival_tb (
	end_date date not null,
	is_finished boolean not null,
	start_date date not null,
	festival_manager_id bigint,
	id bigint  AUTO_INCREMENT PRIMARY KEY,
	name varchar(255) not null unique,
	image BLOB
);

create table product_tb (
        due_date date not null,
        extra_amount bigint not null,
        festival_id bigint,
        id bigint AUTO_INCREMENT PRIMARY KEY,
        price bigint not null,
        `rank` bigint not null,
        company varchar(255) not null,
        name varchar(255) not null,
        image clob
);

    create table trash_tb (
        created_at date not null,
        id bigint  AUTO_INCREMENT PRIMARY KEY,
        user_festival_id bigint,
        category varchar(255) not null check (category in ('PAPER','PET','CAN','PLASTIC','GLASS','VINYL','GENERAL','ETC')),
        name varchar(255) not null
    );

    create table user_festival_tb (
        festival_id bigint,
        id bigint  AUTO_INCREMENT PRIMARY KEY,
        user_id bigint,
        status varchar(255) not null check (status in ('SUCCESS','FAIL','READY','WAITING'))
    );

    create table user_product_tb (
        id bigint  AUTO_INCREMENT PRIMARY KEY,
        product_id bigint,
        user_festival_id bigint,
        product_category varchar(255) not null check (product_category in ('FESTIVAL','MILEAGE'))
    );
 
    create table user_tb (
        id bigint  AUTO_INCREMENT PRIMARY KEY,
        mileage bigint,
        trash_count bigint,
        dtype varchar(31) not null,
        email varchar(255) not null unique,
        grade varchar(255) check (grade in ('SEED','SPROUT','TREE','FLOWER','FRUIT')),
        password varchar(255) not null,
        username varchar(255) not null
    );
    
select * from user_tb;