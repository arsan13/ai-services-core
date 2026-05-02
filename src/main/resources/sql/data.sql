-- DROP TABLE IF EXISTS token_usage_audit;
-- DROP TABLE IF EXISTS user_roles;
-- DROP TABLE IF EXISTS user_permissions;
-- DROP TABLE IF EXISTS app_user;

INSERT INTO app_user (full_name, email, password, provider_type, provider_id, verified, verified_date, created_date,
                      updated_date, password_reset_date)
VALUES ('Anbarsan P', 'arsan@gmail.com', '$2a$12$HMOMeKWbuPgRiMDPCEZtv.SkguASLdtezd8HoXI/xqB91tQsLIfNy', 'LOCAL', NULL,
        true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL), -- password: admin123
       ('John May', 'johnmay@gmail.com', '$2a$12$PuWTCiu.Bama.4X0ysVeTuerBD.0Y5JUQDm73gr6AE4jUSUuhV4ku', 'LOCAL', NULL,
        false, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL); -- password: test123


INSERT INTO user_roles (user_id, roles)
VALUES (1, 'ROLE_USER'),
       (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER');

INSERT INTO user_permissions (user_id, permissions)
VALUES (1, 'user:read'),
       (1, 'user:write'),
       (1, 'user:delete'),
       (1, 'user:manage'),
       (1, 'admin:read'),
       (1, 'admin:write'),
       (1, 'admin:delete'),
       (1, 'token:usage:read'),
       (1, 'chat:aviation:use'),
       (1, 'chat:generic:use'),
       (2, 'user:read'),
       (2, 'chat:generic:use');


INSERT INTO token_usage_audit (id, user_id, model, provider, prompt_tokens, completion_tokens, total_tokens,
                               cost_in_usd, latency_sec, input_summary, output_summary, created_date)
VALUES (1, 1, 'gpt-4', 'openai', 120, 80, 200, 0.004, 1.1, 'Login request', 'Login success', CURRENT_TIMESTAMP),
       (2, 1, 'gpt-4', 'openai', 220, 140, 360, 0.008, 1.6, 'Generate report', 'Report output', CURRENT_TIMESTAMP),
       (3, 1, 'gpt-3.5', 'openai', 90, 60, 150, 0.002, 0.9, 'Summarize text', 'Short summary', CURRENT_TIMESTAMP),
       (4, 1, 'gpt-4', 'openai', 300, 200, 500, 0.010, 2.0, 'Analyze data', 'Insights', CURRENT_TIMESTAMP),
       (5, 1, 'gpt-3.5', 'openai', 110, 70, 180, 0.003, 1.1, 'Translate text', 'Translated', CURRENT_TIMESTAMP),
       (6, 1, 'gpt-4', 'openai', 200, 150, 350, 0.007, 1.5, 'Search query', 'Search results', CURRENT_TIMESTAMP),
       (7, 1, 'gpt-3.5', 'openai', 130, 90, 220, 0.004, 1.2, 'Chat', 'Response', CURRENT_TIMESTAMP),

       (11, 2, 'gpt-3.5', 'openai', 100, 50, 150, 0.002, 0.8, 'Grammar check', 'Corrected', CURRENT_TIMESTAMP),
       (12, 2, 'gpt-4', 'openai', 350, 250, 600, 0.012, 2.2, 'Email draft', 'Draft', CURRENT_TIMESTAMP);
