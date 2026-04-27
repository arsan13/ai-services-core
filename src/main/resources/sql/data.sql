-- DROP TABLE IF EXISTS token_usage_audit;
-- DROP TABLE IF EXISTS user_roles;
-- DROP TABLE IF EXISTS app_user;

INSERT INTO app_user (full_name, username, password, provider_type, provider_id, created_date, updated_date)
VALUES ('Anbarsan P', 'arsan', '$2a$12$HMOMeKWbuPgRiMDPCEZtv.SkguASLdtezd8HoXI/xqB91tQsLIfNy', 'LOCAL', NULL,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), -- password: admin123
       ('John May', 'johnmay', '$2a$12$PuWTCiu.Bama.4X0ysVeTuerBD.0Y5JUQDm73gr6AE4jUSUuhV4ku', 'LOCAL', NULL,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- password: test123


INSERT INTO user_roles (user_id, roles)
VALUES (1, 'ROLE_USER'),
       (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER');

INSERT INTO user_permissions (user_id, permissions)
VALUES (1, 'USER_READ'),
       (1, 'USER_WRITE'),
       (1, 'USER_DELETE'),
       (1, 'USER_MANAGE'),
       (1, 'ADMIN_READ'),
       (1, 'ADMIN_WRITE'),
       (1, 'ADMIN_DELETE'),
       (1, 'TOKEN_USAGE_READ'),
       (2, 'USER_READ');


INSERT INTO token_usage_audit (id, user_id, model, provider, prompt_tokens, completion_tokens, total_tokens, cost_in_usd, latency_sec, input_summary, output_summary, created_date)
VALUES (1, 1, 'gpt-4', 'openai', 120, 80, 200, 0.004, 1.1, 'Login request', 'Login success', CURRENT_TIMESTAMP),
       (2, 1, 'gpt-4', 'openai', 220, 140, 360, 0.008, 1.6, 'Generate report', 'Report output', CURRENT_TIMESTAMP),
       (3, 1, 'gpt-3.5', 'openai', 90, 60, 150, 0.002, 0.9, 'Summarize text', 'Short summary', CURRENT_TIMESTAMP),
       (4, 1, 'gpt-4', 'openai', 300, 200, 500, 0.010, 2.0, 'Analyze data', 'Insights', CURRENT_TIMESTAMP),
       (5, 1, 'gpt-3.5', 'openai', 110, 70, 180, 0.003, 1.1, 'Translate text', 'Translated', CURRENT_TIMESTAMP),
       (6, 1, 'gpt-4', 'openai', 200, 150, 350, 0.007, 1.5, 'Search query', 'Search results', CURRENT_TIMESTAMP),
       (7, 1, 'gpt-3.5', 'openai', 130, 90, 220, 0.004, 1.2, 'Chat', 'Response', CURRENT_TIMESTAMP),

       (11, 2, 'gpt-3.5', 'openai', 100, 50, 150, 0.002, 0.8, 'Grammar check', 'Corrected', CURRENT_TIMESTAMP),
       (12, 2, 'gpt-4', 'openai', 350, 250, 600, 0.012, 2.2, 'Email draft', 'Draft', CURRENT_TIMESTAMP);
