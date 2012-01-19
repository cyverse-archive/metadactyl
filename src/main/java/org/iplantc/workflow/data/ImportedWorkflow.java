package org.iplantc.workflow.data;

public class ImportedWorkflow
{
	private long hid;
	
	private String importedJson; 
	
	private String analysisIds;
	
	private String dateCreated;
	
	public long getHid()
	{
		return hid;
	}

	public void setHid(long hid)
	{
		this.hid = hid;
	}

	public String getImportedJson()
	{
		return importedJson;
	}

	public void setImportedJson(String importedJson)
	{
		this.importedJson = importedJson;
	}

	public String getAnalysisIds()
	{
		return analysisIds;
	}

	public void setAnalysisIds(String analysisIds)
	{
		this.analysisIds = analysisIds;
	}

	public String getDateCreated()
	{
		return dateCreated;
	}

	public void setDateCreated(String dateCreated)
	{
		this.dateCreated = dateCreated;
	}
	
}
