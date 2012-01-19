BEGIN;

INSERT INTO property_group VALUES (
    4000, 'SelectReferenceGenome', 'Select Reference Genome', NULL
);

INSERT INTO property VALUES (
    4001, 'ReferenceGenome', 'Reference Genome', NULL, TRUE, NULL
);

INSERT INTO property_group_property VALUES (
    4002, 4000, 4001
);

INSERT INTO property_property_type VALUES (
    4003, 4001, 1005
);

INSERT INTO validator VALUES (
    4004, 'ReferenceGenomeValidator', NULL, NULL, TRUE
);

INSERT INTO rule VALUES (
    4005, 'ReferenceGenomeValidatorRule1', NULL, NULL
);

INSERT INTO validator_rule VALUES (
    4006, 4004, 4005
);

INSERT INTO rule_rule_type VALUES (
    4007, 4005, 2005
);

INSERT INTO property VALUES (
    4008, 'genomeDatabasePath', NULL, NULL, FALSE, NULL
);

INSERT INTO property_group_property VALUES (
    4009, 4000, 4008
);

INSERT INTO property_property_type VALUES (
    4010, 4008, 1008
);

COMMIT;
