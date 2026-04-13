-- Seed Users
-- password123: $2a$10$7R0Zf9N1o1L1y/e6/Y/e.u4X0gP0O.p.F.q.m.s.C.G.H.I.J.K.L.M
INSERT INTO tb_users (id, name, email, password_hash, created_at) VALUES 
('49323797-2a5c-4e6c-9226-9d62d2940263', 'Admin Gym', 'admin@gympass.com', '$2a$10$7R0Zf9N1o1L1y/e6/Y/e.u4X0gP0O.p.F.q.m.s.C.G.H.I.J.K.L.M', CURRENT_TIMESTAMP),
('98be0e5f-144a-4d76-880c-0335eef43de2', 'John Doe', 'member@example.com', '$2a$10$7R0Zf9N1o1L1y/e6/Y/e.u4X0gP0O.p.F.q.m.s.C.G.H.I.J.K.L.M', CURRENT_TIMESTAMP);

-- Assign Roles
-- Admin User -> ROLE_ADMIN and ROLE_MEMBER
INSERT INTO tb_user_role (user_id, role_id) VALUES 
('49323797-2a5c-4e6c-9226-9d62d2940263', (SELECT id FROM tb_roles WHERE authority = 'ROLE_ADMIN')),
('49323797-2a5c-4e6c-9226-9d62d2940263', (SELECT id FROM tb_roles WHERE authority = 'ROLE_MEMBER')),
-- Member User -> ROLE_MEMBER
('98be0e5f-144a-4d76-880c-0335eef43de2', (SELECT id FROM tb_roles WHERE authority = 'ROLE_MEMBER'));

-- Seed Gyms
INSERT INTO tb_gyms (id, title, description, phone, latitude, longitude) VALUES 
('7350cb44-8848-43d8-a1e6-df15349da390', 'JS Academy', 'A melhor academia de JS', '1199999999', -27.2092052, -49.6401091),
('21915951-46da-498c-9029-7973711d9501', 'Java Strong', 'Academia para programadores Java', '1188888888', -27.2092052, -49.6401091),
('a059b139-ef03-4554-b580-0a256d9539d2', '遠くのジム', 'Academia bem longe para testes', '1177777777', -23.5505, -46.6333);

-- Seed Check-ins
INSERT INTO check_ins (id, created_at, validated_at, user_id, gym_id) VALUES 
('dbf6e7c1-0c5a-4f51-8b77-336365440001', CURRENT_TIMESTAMP - INTERVAL '1' DAY, CURRENT_TIMESTAMP - INTERVAL '1' DAY, '98be0e5f-144a-4d76-880c-0335eef43de2', '7350cb44-8848-43d8-a1e6-df15349da390'),
('dbf6e7c1-0c5a-4f51-8b77-336365440002', CURRENT_TIMESTAMP - INTERVAL '2' DAY, CURRENT_TIMESTAMP - INTERVAL '2' DAY, '98be0e5f-144a-4d76-880c-0335eef43de2', '21915951-46da-498c-9029-7973711d9501');
