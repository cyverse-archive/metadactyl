
/*** This sql script defines the structure for the transformation services **/

CREATE TABLE transformations (
	id BIGINT NOT NULL,
	name varchar(128) NOT NULL,
	description varchar(255) NOT NULL,
	template_id BIGINT NOT NULL
	);
	
	
CREATE TABLE transformation_property_value (
	transformation_id BIGNINT NOT NULL,
	property_id BIGINT NOT NULL,
	value varchar(200) NOT NULL
	);
	
	
CREATE TABLE transformation_template (
	id BIGINT NOT NULL,
	transformation_id BIGINT NOT NULL,
	template_id BIGINT NOT NULL
	);