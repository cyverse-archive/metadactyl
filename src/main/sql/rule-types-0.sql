BEGIN;

INSERT INTO rule_type VALUES (
    2000, 'NonEmptyArray', NULL, NULL
);

INSERT INTO rule_type VALUES (
    2001, 'DoubleRange', NULL, NULL
);

INSERT INTO rule_type VALUES (
    2002, 'IntRange', NULL, NULL
);

INSERT INTO rule_type VALUES (
    2003, 'IntAbove', NULL, NULL
);

INSERT INTO rule_type VALUES (
    2004, 'GenotypeName', NULL, NULL
);

INSERT INTO rule_type VALUES (
    2005, 'ReferenceGenome', NULL, NULL
);

COMMIT;
