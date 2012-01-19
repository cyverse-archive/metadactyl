BEGIN;

INSERT INTO property_type VALUES (
    1000, 'Number', 'Numeric Value', 'Numeric value.'
);

INSERT INTO property_type VALUES (
    1001, 'Text', 'Arbitrary Text', 'Arbitrary text.'
);

INSERT INTO property_type VALUES (
    1002, 'PHRED', 'PHRED Scale', 'PHRED scale.'
);

INSERT INTO property_type VALUES (
    1003, 'XBasePairs', 'Base Pair Exclusion Range', 'Base pair exclusion range.'
);

INSERT INTO property_type VALUES (
    1004, 'SingleEndReadFiles', 'Single End Read Files', 'List of single end read files.'
);

INSERT INTO property_type VALUES (
    1005, 'ReferenceGenome', 'Reference Genome Database Name', 'Reference genome database name.'
);

INSERT INTO property_type VALUES (
    1006, 'url', 'URL', 'Uniform resource locator.'
);

INSERT INTO property_type VALUES (
    1007, 'urlList', 'List of URLs', 'Uniform resource locator list.'
);

INSERT INTO property_type VALUES (
    1008, 'path', 'Path', 'Path to a file or directory.'
);

INSERT INTO property_type VALUES (
    1009, 'pathList, Path List', 'List of paths to files or directories.'
);

COMMIT;
