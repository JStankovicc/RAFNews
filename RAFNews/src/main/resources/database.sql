create table users
(
    id         int auto_increment,
    first_name text not null,
    last_name  text not null,
    email      varchar(256) not null,
    password   text not null,
    status enum('active', 'inactive'),
    type enum('admin', 'content creator'),
    constraint id
        primary key (id),
    constraint email
        unique (email)
);

create table category
(
    id          int auto_increment,
    name        varchar(256) not null,
    description text not null,
    constraint id
        primary key (id),
    constraint name
        unique (name)
);

create table news
(
    id          int auto_increment,
    title       text not null,
    author      text not null,
    content     text not null,
    createdAt   text null,
    category_id int  null,
    user_id     int  null,
    constraint id
        primary key (id),
    constraint news_user_id_fk
        foreign key (user_id) references users (id) on delete set null
);

create table tag
(
    id  int auto_increment,
    tag text not null,
    constraint id
        primary key (id)
);

create table news_tags
(
    id      int auto_increment,
    news_id int not null,
    tag_id  int null,
    constraint id
        primary key (id),
    constraint news_tags_news_id_fk
        foreign key (news_id) references news (id),
    constraint tag_id
        foreign key (tag_id) references tag (id)
);


create table comment
(
    id       int auto_increment,
    author   text not null,
    text     text not null,
    postedAt text null,
    news_id  int  null,
    constraint id
        primary key (id),
    constraint comment_news_id_fk
        foreign key (news_id) references news (id)
);

CREATE TABLE likes (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       user_ip TEXT NOT NULL,
                       post_or_comment BOOLEAN NOT NULL,
                       post_id INT NOT NULL,
                       like_value INT NOT NULL
);

CREATE TABLE views (
                       id INT PRIMARY KEY auto_increment,
                       news_id INT  NULL,
                       viewer_ip text  NULL,
                       created_at text null,
                       FOREIGN KEY (news_id) REFERENCES News(id)
);
