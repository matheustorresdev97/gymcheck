CREATE TABLE tb_roles (
    id BIGSERIAL PRIMARY KEY,
    authority VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tb_user_role (
    user_id UUID NOT NULL,
    role_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_role_user
        FOREIGN KEY (user_id) REFERENCES tb_users(id),
    CONSTRAINT fk_user_role_role
        FOREIGN KEY (role_id) REFERENCES tb_roles(id)
);