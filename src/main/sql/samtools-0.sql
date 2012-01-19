BEGIN;

/**
 * main samtools template
 */
INSERT INTO template VALUES (
    5000, 'samtools', 'Variant Detection', 'Find single nucleotide polymorphisms.'
);

/**
 * combine SAM files template
 */
INSERT INTO template VALUES (
    5001, 'combineSamFiles', NULL, NULL
);

INSERT INTO parent_template_child_template VALUES (
    5002, 5000, 5001
);

/**
 * view template
 */
INSERT INTO template VALUES (
    5003, 'view', NULL, NULL
);

INSERT INTO parent_template_child_template VALUES (
    5004, 5000, 5003
);

/**
 * sort template
 */
INSERT INTO template VALUES (
    5005, 'sort', NULL, NULL
);

INSERT INTO parent_template_child_template VALUES (
    5006, 5000, 5005
);

/**
 * pileup template
 */
INSERT INTO template VALUES (
    5007, 'pileup', NULL, NULL
);

INSERT INTO parent_template_child_template VALUES (
    5008, 5000, 5007
);

/**
 * filter group of templates
 */
INSERT INTO template VALUES (
    5009, 'filterTemplateGroup', NULL, NULL
);

INSERT INTO parent_template_child_template VALUES (
    5010, 5000, 5009
);

/**
 * filter template
 */
INSERT INTO template VALUES (
    5011, 'filter', NULL, NULL
);

INSERT INTO parent_template_child_template VALUES (
    5012, 5009, 5011
);

/**
 * samtools awk template
 */
INSERT INTO template VALUES (
    5013, 'samtoolsAwk', NULL, NULL
);

INSERT INTO parent_template_child_template VALUES (
    5014, 5009, 5013
);

/**
 * convert to vcf template
 */
INSERT INTO template VALUES (
    5015, 'convertToVcf', NULL, NULL
);

INSERT INTO parent_template_child_template VALUES (
    5016, 5009, 5015
);

/**
 * input files property
 */
INSERT INTO property VALUES (
    5016, 'inputFiles', 'Datasets', NULL, TRUE, NULL
);

INSERT INTO property_property_type VALUES (
    5017, 5016, 1004
);

INSERT INTO validator VALUES (
    5018, 'inputFilesValidator', NULL, NULL, TRUE
);

INSERT INTO property_validator VALUES (
    5019, 5016, 5018
);

INSERT INTO rule VALUES (
    5020, 'inputFilesValidatorRule1', NULL, NULL
);

INSERT INTO rule_rule_type VALUES (
    5021, 5020, 2000
);

INSERT INTO validator_rule VALUES (
    5022, 5018, 5020
);

/**
 * result directory property
 */
INSERT INTO property VALUES (
    5023, 'resultDirectory', NULL, NULL, FALSE, NULL
);

INSERT INTO property_property_type VALUES (
    5024, 5023, 1008
);

/**
 * input file URLs property
 */
INSERT INTO property VALUES (
    5025, 'inputFileUrls', NULL, NULL, FALSE, NULL
);

INSERT INTO property_property_type VALUES (
    5026, 5025, 1007
);

/**
 * input file paths property
 */
INSERT INTO property VALUES (
    5027, 'inputFilePaths', NULL, NULL, FALSE, NULL
);

INSERT INTO property_property_type VALUES (
    5028, 5027, 1009
);

/**
 * working directory property
 */
INSERT INTO property VALUES (
    5029, 'workingDirectory', NULL, NULL, FALSE, NULL
);

INSERT INTO property_property_type VALUES (
    5030, 5029, 1008
);

/**
 * combined SAM file path property
 */
INSERT INTO property VALUES (
    5031, 'combinedSamFile', NULL, NULL, FALSE, NULL
);

INSERT INTO property_property_type VALUES (
    5032, 5031, 1008
);

/**
 * view job output file property
 */
INSERT INTO property VALUES (
    5033, 'viewJobOutputFile', NULL, NULL, FALSE, NULL
);

INSERT INTO property_property_type VALUES (
    5034, 5033, 1008
);

/**
 * sort directory path property
 */
INSERT INTO property VALUES (
    5035, 'sortDirectory', NULL, NULL, FALSE, NULL
);

INSERT INTO property_property_type VALUES (
    5036, 5035, 1008
);

/**
 * sort job output file path property
 */
INSERT INTO property VALUES (
    5037, 'sortJobOutputFile', NULL, NULL, FALSE, NULL
);

INSERT INTO property_property_type VALUES (
    5038, 5037, 1008
);

/**
 * pileup output file path property
 */
INSERT INTO property VALUES (
    5039, 'pileupOutputFile', NULL, NULL, FALSE, NULL
);

INSERT INTO property_property_type VALUES (
    5040, 5039, 1008
);

/**
 * filter job output file path property
 */
INSERT INTO property VALUES (
    5041, 'filterJobOutputFile', NULL, NULL, FALSE, NULL
);

INSERT INTO property_property_type VALUES (
    5042, 5041, 1008
);

/**
 * awk job output file path property
 */
INSERT INTO property VALUES (
    5043, 'awkJobOutputFile', NULL, NULL, FALSE, NULL
);

INSERT INTO property_property_type VALUES (
    5044, 5043, 1008
);

/**
 * theta parameter property
 */
INSERT INTO property VALUES (
    5045, 'thetaParameter',
    'Theta Parameter (error dependency coefficient).<br>Enter a number between 0 and 1',
    NULL, TRUE, '.85'
);

INSERT INTO property_property_type VALUES (
    5046, 5045, 1000
);

INSERT INTO validator VALUES (
    5047, 'thetaParameterValidator', NULL, NULL, TRUE
);

INSERT INTO property_validator VALUES (
    5048, 5045, 5047
);

INSERT INTO rule VALUES (
    5049, 'thetaParameterValidatorRule1', NULL, NULL
);

INSERT INTO validator_rule VALUES (
    5050, 5047, 5049
);

INSERT INTO rule_rule_type VALUES (
    5051, 5049, 2001
);

INSERT INTO rule_argument VALUES (
    5052, 5049, '0.0'
);

INSERT INTO rule_argument VALUES (
    5053, 5049, '1.0'
);

/**
 * number of haplotypes property
 */
INSERT INTO property VALUES (
    5054, 'numberOfHaplotypes', 'Number of haplotypes in sample', NULL, TRUE, '2'
);

INSERT INTO property_property_type VALUES (
    5055, 5054, 1000
);

INSERT INTO validator VALUES (
    5056, 'numberOfHaplotypesValidator', NULL, NULL, TRUE
);

INSERT INTO property_validator VALUES (
    5057, 5054, 5056
);

INSERT INTO rule VALUES (
    5058, 'numberOfHaplotypesValidatorRule1', NULL, NULL
);

INSERT INTO validator_rule VALUES (
    5059, 5056, 5058
);

INSERT INTO rule_rule_type VALUES (
    5060, 5058, 2002
);

INSERT INTO rule_argument VALUES (
    5061, 5058, '1'
);

INSERT INTO rule_argument VALUES (
    5062, 5058, '2'
);

/**
 * indel probability property
 */
INSERT INTO property VALUES (
    5063, 'indelProbability', 'Probability of an indel in sequencing (PHRED scale)', NULL, TRUE, '0'
);

INSERT INTO property_property_type VALUES (
    5064, 5063, 1002
);

/**
 * minimum read depth
 */
INSERT INTO property VALUES (
    5065, 'minReadDepth', 'Minimum read depth', NULL, TRUE, '3'
);

INSERT INTO property_property_type VALUES (
    5066, 5065, 1000
);

INSERT INTO validator VALUES (
    5067, 'minReadDepthValidator', NULL, NULL, TRUE 
);

INSERT INTO property_validator VALUES (
    5068, 5065, 5067
);

INSERT INTO rule VALUES (
    5069, 'minReadDepthValidatorRule1', NULL, NULL
);

INSERT INTO validator_rule VALUES (
    5070, 5067, 5069
);

INSERT INTO rule_rule_type VALUES (
    5071, 5069, 2002
);

INSERT INTO rule_argument VALUES (
    5072, 5069, '1'
);

INSERT INTO rule_argument VALUES (
    5073, 5069, '100'
);

/**
 * maximum read depth
 */
INSERT INTO property VALUES (
    5074, 'maxReadDepth', 'Maximum read depth', NULL, TRUE, '100'
);

INSERT INTO property_property_type VALUES (
    5075, 5074, 1000
);

INSERT INTO validator VALUES (
    5075, 'minReadDepthValidator', NULL, NULL, TRUE 
);

INSERT INTO property_validator VALUES (
    5076, 5074, 5075
);

INSERT INTO rule VALUES (
    5077, 'minReadDepthValidatorRule1', NULL, NULL
);

INSERT INTO validator_rule VALUES (
    5078, 5075, 5077
);

INSERT INTO rule_rule_type VALUES (
    5079, 5077, 2002
);

INSERT INTO rule_argument VALUES (
    5080, 5077, '1'
);

INSERT INTO rule_argument VALUES (
    5081, 5077, '100'
);

/**
 * base pair gap exclusion window
 */
INSERT INTO property VALUES (
    5082, 'basePairGapExclusionWindow', 'SNPs within X base pairs around a gab should be excluded', NULL, TRUE, '10'
);

INSERT INTO property_property_type VALUES (
    5083, 5082, 1003
);

INSERT INTO validator VALUES (
    5084, 'basePairGapExclusionWindowValidator', NULL, NULL, TRUE 
);

INSERT INTO property_validator VALUES (
    5085, 5082, 5084
);

INSERT INTO rule VALUES (
    5086, 'basePairGapExclusionWindowValidatorRule1', NULL, NULL
);

INSERT INTO validator_rule VALUES (
    5087, 5084, 5086
);

INSERT INTO rule_rule_type VALUES (
    5088, 5086, 2003
);

INSERT INTO rule_argument VALUES (
    5089, 5086, '0'
);

/**
 * window size for filtering dense SNPs
 */
INSERT INTO property VALUES (
    5090, 'windowSizeForFilteringDenseSnps', 'Window size for filtering dense SNPs', NULL, TRUE, '10'
);

INSERT INTO property_property_type VALUES (
    5091, 5090, 1000
);

INSERT INTO validator VALUES (
    5092, 'windowSizeForFilteringDenseSnpsValidator', NULL, NULL, TRUE 
);

INSERT INTO property_validator VALUES (
    5093, 5090, 5092
);

INSERT INTO rule VALUES (
    5094, 'windowSizeForFilteringDenseSnpsValidatorRule1', NULL, NULL
);

INSERT INTO validator_rule VALUES (
    5095, 5092, 5094
);

INSERT INTO rule_rule_type VALUES (
    5096, 5094, 2003
);

INSERT INTO rule_argument VALUES (
    5097, 5094, '0'
);

/**
 * maximum number of SNPs in a window
 */
INSERT INTO property VALUES (
    5098, 'maxSnpsInWindow', 'Maximum number of SNPs in a window', NULL, TRUE, '2'
);

INSERT INTO property_property_type VALUES (
    5099, 5098, 1000
);

INSERT INTO validator VALUES (
    5100, 'maxSnpsInWindowValidator', NULL, NULL, TRUE 
);

INSERT INTO property_validator VALUES (
    5101, 5098, 5100
);

INSERT INTO rule VALUES (
    5102, 'maxSnpsInWindowValidatorRule1', NULL, NULL
);

INSERT INTO validator_rule VALUES (
    5103, 5100, 5102
);

INSERT INTO rule_rule_type VALUES (
    5104, 5102, 2003
);

INSERT INTO rule_argument VALUES (
    5105, 5102, '0'
);

/**
 * window size for filtering adjacent gaps
 */
INSERT INTO property VALUES (
    5106, 'windowSizeForFilteringAdjacentGaps', 'Window size for filtering adjacent gaps', NULL, TRUE, '30'
);

INSERT INTO property_property_type VALUES (
    5107, 5106, 1000
);

INSERT INTO validator VALUES (
    5108, 'windowSizeForFilteringAdjacentGapsValidator', NULL, NULL, TRUE 
);

INSERT INTO property_validator VALUES (
    5109, 5106, 5108
);

INSERT INTO rule VALUES (
    5110, 'windowSizeForFilteringAdjacentGapsValidatorRule1', NULL, NULL
);

INSERT INTO validator_rule VALUES (
    5111, 5108, 5110
);

INSERT INTO rule_rule_type VALUES (
    5112, 5110, 2003
);

INSERT INTO rule_argument VALUES (
    5113, 5110, '0'
);

/**
 * minimum SNP quality
 */
INSERT INTO property VALUES (
    5114, 'minSnpQuality', 'Minimum SNP quality (PHRED based):', NULL, TRUE, '30'
);

INSERT INTO property_property_type VALUES (
    5115, 5114, 1002
);

/**
 * sample name
 */
INSERT INTO property VALUES (
    5116, 'sampleName', 'Enter genotype (sample) name', NULL, TRUE, 'samplename'
);

INSERT INTO property_property_type VALUES (
    5117, 5116, 1001
);

INSERT INTO validator VALUES (
    5118, 'sampleNameValidator', NULL, NULL, TRUE 
);

INSERT INTO property_validator VALUES (
    5119, 5116, 5118
);

INSERT INTO rule VALUES (
    5120, 'sampleNameValidatorRule1', NULL, NULL
);

INSERT INTO validator_rule VALUES (
    5121, 5118, 5120
);

INSERT INTO rule_rule_type VALUES (
    5122, 5120, 2004
);

/**
 * reference genome property group
 */
INSERT INTO template_property_group VALUES (
    5123, 5000, 4000
);

/**
 * generated values property group
 */
INSERT INTO property_group VALUES (
    5124, 'samtoolsGeneratedValues', NULL, NULL
);

INSERT INTO template_property_group VALUES (
    5125, 5000, 5124
);

INSERT INTO property_group_property VALUES (
    5126, 5124, 5023
);

INSERT INTO property_group_property VALUES (
    5127, 5124, 5025
);

INSERT INTO property_group_property VALUES (
    5128, 5124, 5027
);

INSERT INTO property_group_property VALUES (
    5129, 5124, 5029
);

INSERT INTO property_group_property VALUES (
    5130, 5124, 5031
);

INSERT INTO property_group_property VALUES (
    5131, 5124, 5033
);

INSERT INTO property_group_property VALUES (
    5132, 5124, 5035
);

INSERT INTO property_group_property VALUES (
    5133, 5124, 5037
);

INSERT INTO property_group_property VALUES (
    5134, 5124, 5039
);

INSERT INTO property_group_property VALUES (
    5135, 5124, 5041
);

INSERT INTO property_group_property VALUES (
    5136, 5124, 5043
);

/**
 * select input files property group
 */
INSERT INTO property_group VALUES (
    5137, 'selectInputFiles', 'Select Files for Input', NULL
);

INSERT INTO template_property_group VALUES (
    5138, 5000, 5137
);

INSERT INTO property_group_property VALUES (
    5139, 5137, 5016
);

/**
 * samtools combine sam files property group
 */
INSERT INTO property_group VALUES (
    5140, 'samtoolsCombineSamFiles', 'Combine SAM Files', NULL
);

INSERT INTO template_property_group VALUES (
    5141, 5001, 5140
);

INSERT INTO property_group_property VALUES (
    5142, 5140, 5023
);

INSERT INTO property_group_property VALUES (
    5143, 5140, 5027
);

/**
 * samtools view property group
 */
INSERT INTO property_group VALUES (
    5144, 'samtoolsView', 'Samtools View', NULL
);

INSERT INTO template_property_group VALUES (
    5145, 5003, 5144
);

INSERT INTO property_group_property VALUES (
    5146, 5144, 5023
);

INSERT INTO property_group_property VALUES (
    5147, 5144, 5031
);

INSERT INTO property_group_property VALUES (
    5148, 5144, 4008
);

/**
 * samtools sort property group
 */
INSERT INTO property_group VALUES (
    5149, 'samtoolsSort', 'Samtools Sort', NULL
);

INSERT INTO template_property_group VALUES (
    5150, 5005, 5149
);

INSERT INTO property_group_property VALUES (
    5151, 5149, 5031
);

INSERT INTO property_group_property VALUES (
    5152, 5149, 5033
);

INSERT INTO property_group_property VALUES (
    5153, 5149, 5035
);

/**
 * pileup property group
 */
INSERT INTO property_group VALUES (
    5154, 'pileup', 'Pileup', NULL
);

INSERT INTO template_property_group VALUES (
    5155, 5007, 5154
);

INSERT INTO property_group_property VALUES (
    5156, 5154, 5031
);

INSERT INTO property_group_property VALUES (
    5157, 5154, 4008
);

INSERT INTO property_group_property VALUES (
    5158, 5154, 5037
);

INSERT INTO property_group_property VALUES (
    5159, 5154, 5045
);

INSERT INTO property_group_property VALUES (
    5160, 5154, 5054
);

INSERT INTO property_group_property VALUES (
    5161, 5154, 5063
);

/**
 * filter group property group
 */
INSERT INTO property_group VALUES (
    5162, 'filter group', NULL, NULL
);

INSERT INTO template_property_group VALUES (
    5163, 5009, 5162
);

INSERT INTO property_group_property VALUES (
    5164, 5162, 5031
);

INSERT INTO property_group_property VALUES (
    5165, 5176, 4008
);

INSERT INTO property_group_property VALUES (
    5166, 5162, 5039
);

INSERT INTO property_group_property VALUES (
    5167, 5162, 5041
);

INSERT INTO property_group_property VALUES (
    5168, 5162, 5043
);

INSERT INTO property_group_property VALUES (
    5169, 5162, 5065
);

INSERT INTO property_group_property VALUES (
    5170, 5162, 5074
);

INSERT INTO property_group_property VALUES (
    5171, 5162, 5082
);

INSERT INTO property_group_property VALUES (
    5172, 5162, 5090
);

INSERT INTO property_group_property VALUES (
    5173, 5162, 5098
);

INSERT INTO property_group_property VALUES (
    5174, 5162, 5106
);

INSERT INTO property_group_property VALUES (
    5175, 5162, 5114
);

INSERT INTO property_group_property VALUES (
    5176, 5162, 5116
);

/**
 * awk property group
 */
INSERT INTO property_group VALUES (
    5177, 'filterPropertyGroup', 'Filtering', NULL
);

INSERT INTO template_property_group VALUES (
    5178, 5011, 5177
);

INSERT INTO property_group_property VALUES (
    5179, 5177, 5031
);

INSERT INTO property_group_property VALUES (
    5180, 5177, 4008
);

INSERT INTO property_group_property VALUES (
    5181, 5177, 5039 
);

INSERT INTO property_group_property VALUES (
    5182, 5177, 5041
);

INSERT INTO property_group_property VALUES (
    5183, 5177, 5043
);

INSERT INTO property_group_property VALUES (
    5184, 5177, 5065
);

INSERT INTO property_group_property VALUES (
    5185, 5177, 5074 
);

INSERT INTO property_group_property VALUES (
    5186, 5177, 5082
);

INSERT INTO property_group_property VALUES (
    5187, 5177, 5090
);

INSERT INTO property_group_property VALUES (
    5188, 5177, 5098
);

INSERT INTO property_group_property VALUES (
    5189, 5177, 5106
);

INSERT INTO property_group_property VALUES (
    5190, 5177, 5114
);

/**
 * samtools awk property group
 */
INSERT INTO property_group VALUES (
    5191, 'samtoolsAwk', NULL, NULL
);

INSERT INTO template_property_group VALUES (
    5192, 5013, 5191
);

INSERT INTO property_group_property VALUES (
    5193, 5192, 5031
);

INSERT INTO property_group_property VALUES (
    5194, 5192, 5041
);

/**
 * convert to VCF property group
 */
INSERT INTO property_group VALUES (
    5195, 'convertToVcf', NULL, NULL
);

INSERT INTO template_property_group VALUES (
    5196, 5015, 5195
);

INSERT INTO property_group_property VALUES (
    5197, 5195, 5031
);

INSERT INTO property_group_property VALUES (
    5198, 5195, 4008
);

INSERT INTO property_group_property VALUES (
    5199, 5195, 5043
);

INSERT INTO property_group_property VALUES (
    5200, 5195, 5116
);

COMMIT;
