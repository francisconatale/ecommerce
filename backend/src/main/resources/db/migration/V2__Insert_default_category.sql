INSERT INTO category (id, name, parent_id, is_system, path_names, created_at, updated_at, deleted)
VALUES ('00000000-0000-0000-0000-000000000000', 'Sin categorizar', NULL, TRUE, 'Sin categorizar', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

INSERT INTO category_closure (ancestor_id, descendant_id, depth)
VALUES ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 0);
