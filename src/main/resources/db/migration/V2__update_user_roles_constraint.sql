-- Drop existing constraint
ALTER TABLE app_user_roles
DROP
CONSTRAINT chk_user_roles;

-- Add updated constraint
ALTER TABLE app_user_roles
    ADD CONSTRAINT chk_user_roles
        CHECK (
            role IN (
                     'ROLE_USER',
                     'ROLE_ANALYST',
                     'ROLE_MANAGER',
                     'ROLE_ADMIN'
                )
            );