ALTER TABLE isolation_points
    ADD COLUMN created_platform VARCHAR(200),
    ADD COLUMN modified_platform VARCHAR(200),
    ADD COLUMN created_platform_type VARCHAR(30),
    ADD COLUMN modified_platform_type VARCHAR(30);

ALTER TABLE image_annotation
    ADD COLUMN created_platform VARCHAR(200),
    ADD COLUMN modified_platform VARCHAR(200),
    ADD COLUMN created_platform_type VARCHAR(30),
    ADD COLUMN modified_platform_type VARCHAR(30);

ALTER TABLE editable_images
    ADD COLUMN created_platform VARCHAR(200),
    ADD COLUMN modified_platform VARCHAR(200),
    ADD COLUMN created_platform_type VARCHAR(30),
    ADD COLUMN modified_platform_type VARCHAR(30);

