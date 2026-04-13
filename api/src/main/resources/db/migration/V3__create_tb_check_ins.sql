CREATE TABLE check_ins (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    validated_at TIMESTAMP,

    user_id VARCHAR(36) NOT NULL,
    gym_id VARCHAR(36) NOT NULL,

    CONSTRAINT fk_checkins_user
        FOREIGN KEY (user_id)
        REFERENCES tb_users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_checkins_gym
        FOREIGN KEY (gym_id)
        REFERENCES tb_gyms(id)
        ON DELETE CASCADE
);