ALTER TABLE transformation_activity ADD COLUMN guid uuid;

-- Copy the existing ids over.  Note that this only assumes that the uuids in 
-- the id field are of incorrect length - if they contain invalid characters
-- you'll probably just have to generate new ids.
UPDATE transformation_activity 
SET    transformation_activity_uuid = CAST(substr(regexp_replace(id, '-', '', 'g'), 0, 33) AS uuid);

ALTER TABLE transformation_activity ALTER COLUMN guid SET NOT NULL;

-- To drop the hid column on transformation activity we have to break the
-- foreign key constraints and relate them back to the guid column in the
-- transformation activity table.

--------------------------
-- template_group_template
--------------------------
ALTER TABLE template_group_template ADD COLUMN transformation_activity_id uuid;

UPDATE template_group_template
SET    transformation_activity_id = transformation_activity.guid
FROM   transformation_activity
WHERE  transformation_activity.hid = template_group_template.template_id;

ALTER TABLE template_group_template ALTER COLUMN transformation_activity_id SET NOT NULL;
ALTER TABLE template_group_template DROP CONSTRAINT template_group_template_template_id_fkey;

-----------------------------------
-- transformation_activity_mappings
-----------------------------------
ALTER TABLE transformation_activity_mappings 
      DROP CONSTRAINT transformation_activity_mappings_pkey,
      DROP CONSTRAINT transformation_activity_mapping_transformation_activity_id_fkey;
ALTER TABLE transformation_activity_mappings RENAME COLUMN transformation_activity_id TO old_transformation_activity_id;
ALTER TABLE transformation_activity_mappings ADD COLUMN transformation_activity_id uuid;

UPDATE transformation_activity_mappings
SET    transformation_activity_id = transformation_activity.guid
FROM   transformation_activity
WHERE  transformation_activity.hid = transformation_activity_mappings.old_transformation_activity_id;

ALTER TABLE transformation_activity_mappings 
      DROP COLUMN old_transformation_activity_id,
      ALTER COLUMN transformation_activity_id SET NOT NULL;

ALTER TABLE transformation_activity_mappings ADD CONSTRAINT transformation_activity_mappings_pkey
      PRIMARY KEY (transformation_activity_id, hid);

--------------------------------------
-- transformation_task_step_precedence
--------------------------------------
ALTER TABLE transformation_task_step_precedence ADD COLUMN transformation_activity_id uuid;

UPDATE transformation_task_step_precedence
SET    transformation_activity_id = transformation_activity.guid
FROM   transformation_activity
WHERE  transformation_activity.hid = transformation_task_step_precedence.transformation_task_id;

ALTER TABLE transformation_task_step_precedence 
      ALTER COLUMN transformation_activity_id SET NOT NULL,
      DROP CONSTRAINT transformation_task_step_precedence_transformation_task_id_fkey;

ALTER TABLE transformation_task_step_precedence 
      DROP CONSTRAINT transformation_task_step_precedence_pkey;

ALTER TABLE transformation_task_step_precedence DROP COLUMN transformation_task_id;

----------------------------
-- transformation_task_steps
----------------------------
ALTER TABLE transformation_task_steps ADD COLUMN transformation_activity_id uuid;

UPDATE transformation_task_steps
SET    transformation_activity_id = transformation_activity.guid
FROM   transformation_activity
WHERE  transformation_activity.hid = transformation_task_steps.transformation_task_id;

ALTER TABLE transformation_task_steps
      DROP CONSTRAINT transformation_task_steps_transformation_task_id_fkey,
      DROP CONSTRAINT transformation_task_steps_pkey;

ALTER TABLE transformation_task_steps
      DROP COLUMN transformation_task_id,
      ADD CONSTRAINT transformation_task_steps_pkey PRIMARY KEY (hid, transformation_activity_id);

-------------------------
-- Deal with foreign keys
-------------------------

-- We have dropped all foreign keys on the hid column of transformation_activity.
-- We can now drop that column and remove the primary key on it.
ALTER TABLE transformation_activity 
      DROP CONSTRAINT transformation_activity_pkey,
      DROP COLUMN hid,
      ADD CONSTRAINT transformation_activity_pkey PRIMARY KEY (guid);

-- Now recreate all of the foreign keys
ALTER TABLE template_group_template ADD CONSTRAINT transformation_activity_id_fkey
      FOREIGN KEY (transformation_activity_id) REFERENCES transformation_activity (guid) MATCH FULL;

ALTER TABLE transformation_activity_mappings ADD CONSTRAINT transformation_activity_id_fkey
      FOREIGN KEY (transformation_activity_id) REFERENCES transformation_activity (guid) MATCH FULL;

ALTER TABLE transformation_task_step_precedence ADD CONSTRAINT transformation_activity_id_fkey
      FOREIGN KEY (transformation_activity_id) REFERENCES transformation_activity (guid) MATCH FULL;

ALTER TABLE transformation_task_steps ADD CONSTRAINT transformation_activity_id_fkey
      FOREIGN KEY (transformation_activity_id) REFERENCES transformation_activity (guid) MATCH FULL;

-- Now finish changes the transformation_activity
ALTER TABLE transformation_activity
      DROP COLUMN id,
      RENAME COLUMN guid TO id;