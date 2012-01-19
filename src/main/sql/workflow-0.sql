
BEGIN;

/**
 * create a version table w/ a single column named 'version' that has a single 
 * row that indicates the current version of the database schema
 */
CREATE TABLE version (version INTEGER);

/**
 * physical data model for Version 0
 */

/**
 * templates
 */
CREATE TABLE template (
    id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    label VARCHAR(128),
    description VARCHAR(256),
    template_type VARCHAR(256),
    PRIMARY KEY(id)
);

/**
 * associates child templates with their parents
 */
CREATE TABLE parent_template_child_template (
    id BIGINT NOT NULL,
    parent_id BIGINT NOT NULL,
    child_id BIGINT NOT NULL,
    PRIMARY KEY(id)
);

/**
 * property groups
 */
CREATE TABLE property_group (
    id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    label VARCHAR(128),
    description VARCHAR(256),
    property_group_type VARCHAR(256),
    PRIMARY KEY(id)
);

/**
 * associates property groups with templates
 */
CREATE TABLE template_property_group (
    id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    property_group_id BIGINT NOT NULL
);

/**
 * properties
 */
CREATE TABLE property (
    id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    label VARCHAR(128),
    description VARCHAR(256),
    visible BOOLEAN NOT NULL DEFAULT TRUE,
    property_type BIGINT NOT NULL,
    validator BIGINT ,
    value VARCHAR(128),
    PRIMARY KEY(id)
);

/**
 * associates properties with property groups
 */
CREATE TABLE property_group_property (
    id BIGINT NOT NULL,
    property_group_id BIGINT NOT NULL,
    property_id BIGINT NOT NULL
);

/**
 * contracts between property groups
 */
CREATE TABLE contract (
    id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    label VARCHAR(128),
    description VARCHAR(256),
    PRIMARY KEY(id)
);

/**
 * associates contracts with property groups
 */
CREATE TABLE property_group_contract (
    id BIGINT NOT NULL,
    property_group_id BIGINT NOT NULL,
    contract_id BIGINT NOT NULL
);

/**
 * associates properties with contracts
 */
CREATE TABLE contract_property (
    id BIGINT NOT NULL,
    contract_id BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    PRIMARY KEY(id)
);

/**
 * property types
 */
CREATE TABLE property_type (
    id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    label VARCHAR(128),
    description VARCHAR(256),
    PRIMARY KEY(id)
);

/**
 * associates property types with properties
 */
CREATE TABLE property_property_type (
    id BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    property_type_id BIGINT NOT NULL
);

/**
 * property validators
 */
CREATE TABLE validator (
    id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    label VARCHAR(128),
    description VARCHAR(256),
    required BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY(id)
);

/**
 * associates validators with properties
 */
CREATE TABLE property_validator (
    id BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    validator_id BIGINT NOT NULL
);

/**
 * property validator rules
 */
CREATE TABLE rule (
    id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    label VARCHAR(128),
    description VARCHAR(256),
    rule_type BIGINT NOT NULL,
    PRIMARY KEY(id)
);

/**
 * associates rules with validators
 */
CREATE TABLE validator_rule (
    id BIGINT NOT NULL,
    validator_id BIGINT NOT NULL,
    rule_id BIGINT NOT NULL
);

/**
 * rule types
 */
CREATE TABLE rule_type (
    id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    label VARCHAR(128),
    description VARCHAR(256),
    PRIMARY KEY(id)
);

/**
 * associates rule types with rules
 */
CREATE TABLE rule_rule_type (
    id BIGINT NOT NULL,
    rule_id BIGINT NOT NULL,
    rule_type_id BIGINT NOT NULL
);

/**
 * arguments to property validator rules
 */
CREATE TABLE rule_argument (
    id BIGINT NOT NULL,
    rule_id BIGINT NOT NULL,
    argument_value VARCHAR(64) NOT NULL
);

/**
 * after successful completion (at the end of the transaction), a script should
 * be expected to update 'version' (this script will set version = 0, 
 * database-1.sql will set version = 1, etc.)
 */
INSERT INTO version (version)
	VALUES (0);

COMMIT;
