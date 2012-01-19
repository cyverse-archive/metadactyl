ALTER TABLE step_precedence_inbound
      ADD COLUMN id uuid NOT NULL,
      DROP CONSTRAINT step_precedence_inbound_pkey,
      ADD CONSTRAINT step_precedence_inbound_pkey PRIMARY KEY (id),
      ADD CONSTRAINT id_step_precedence_id_unique UNIQUE (id, step_precedence_id),
      DROP COLUMN hid;
