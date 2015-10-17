delete from oauth_client_details;

INSERT INTO oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types, authorities, access_token_validity, refresh_token_validity)
VALUES ('dawaaii', 'rest_api', '$2a$11$gxpnezmYfNJRYnw/EpIK5Oe08TlwZDmcmUeKkrGcSGGHXvWaxUwQ2', 'trust,read,write', 'client_credentials,authorization_code,implicit,password,refresh_token', 'ROLE_API_USER', '31104000', '31104000');


