set -e

java -cp target/components-dev-SNAPSHOT.jar org.iplantc.metadata.components.tools.SaveDeployedComponents /Users/raygoza/Documents/iplant/data/components.csv

java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.JsonToTemplateMapper /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/tool_integration/new/cufflinks.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.JsonToTemplateMapper /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/tool_integration/contrastconfig.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.JsonToTemplateMapper /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/tool_integration/cace.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.JsonToTemplateMapper /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/tool_integration/dace.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.JsonToTemplateMapper /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/tool_integration/new/fastx_barcode_splitter.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.JsonToTemplateMapper /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/tool_integration/new/fastx_groomer.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.JsonToTemplateMapper /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/tool_integration/new/fastx_quality_filter.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.JsonToTemplateMapper /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/tool_integration/new/fastx_clipper.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.JsonToTemplateMapper /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/tool_integration/new/fastx_trimmer.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.JsonToTemplateMapper /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/tool_integration/new/tnrs.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.JsonToTemplateMapper /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/tool_integration/bowtie/bowtie_0_12_7-pair-end.json




java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/analysis_authoring/independent_contrast.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/analysis_authoring/cufflinks_analysis.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/analysis_authoring/caceanalysis.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/analysis_authoring/daceanalysis.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/analysis_authoring/fastx_trimmer.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/analysis_authoring/fastx_barcode.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/analysis_authoring/fastx_groomer.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/analysis_authoring/fastx_clipper.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/analysis_authoring/fastx_quality_filter.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/analysis_authoring/tnrs.json
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis /Users/raygoza/Documents/workspace/de-space/metadata/metadata_json/in_progress/analysis_authoring/bowtie/bowtie_0_12_7_pair_end.json

java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveAnalysis ~/Documents/iplant/data/json/analisys/fake_analysis.json



java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.ExecuteSqlScript ~/Documents/iplant/data/sql/remove_template_group.sql
java -cp target/workflow-dev-SNAPSHOT.jar org.iplantc.workflow.tools.SaveTemplateGroupings ~/Documents/iplant/data/json/ui/template_grouping.json








