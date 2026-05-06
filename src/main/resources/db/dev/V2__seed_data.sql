-- DROP TABLE IF EXISTS token_usage_audit;
-- DROP TABLE IF EXISTS app_user_roles;
-- DROP TABLE IF EXISTS app_user_permissions;
-- DROP TABLE IF EXISTS app_user;

INSERT INTO token_usage_audit (id, user_id, model, provider, prompt_tokens, completion_tokens, total_tokens,
                               cost_in_usd, latency_sec, input_summary, output_summary, created_date)
VALUES (1, NULL, 'gpt-4', 'openai', 120, 80, 200, 0.004, 1.1, 'Login request', 'Login success', CURRENT_TIMESTAMP),
       (2, NULL, 'gpt-4', 'openai', 220, 140, 360, 0.008, 1.6, 'Generate report', 'Report output', CURRENT_TIMESTAMP),
       (3, NULL, 'gpt-3.5', 'openai', 90, 60, 150, 0.002, 0.9, 'Summarize text', 'Short summary', CURRENT_TIMESTAMP),
       (4, NULL, 'gpt-4', 'openai', 300, 200, 500, 0.010, 2.0, 'Analyze data', 'Insights', CURRENT_TIMESTAMP),
       (5, NULL, 'gpt-3.5', 'openai', 110, 70, 180, 0.003, 1.1, 'Translate text', 'Translated', CURRENT_TIMESTAMP),
       (6, NULL, 'gpt-4', 'openai', 200, 150, 350, 0.007, 1.5, 'Search query', 'Search results', CURRENT_TIMESTAMP),
       (7, NULL, 'gpt-3.5', 'openai', 130, 90, 220, 0.004, 1.2, 'Chat', 'Response', CURRENT_TIMESTAMP);
