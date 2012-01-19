package org.iplantc.workflow.data;

import java.sql.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a new DataElement for a given Template within an Analysis.
 * 
 * DataElement is a re-branding of DataObject to indicate that it is a portion of input or output 
 * from a given step (template) within an analysis. 
 * 
 * Some Context: 
 * 
 * This is a table meant to capture and preserve information about analyses added to the system 
 * who have InfoType/FileFormat combinations that are not yet supported.  Currently, we only 
 * support generic types for input/output (type=File).  We need to make this more robust, but do 
 * not have the time required to do this cleanly and robustly.  This table is a "stop-gap" to 
 * help avoid painful retrofitting at a later date.
 *  
 * @author lenards
 *
 */
public class DataElementPreservation
{
    /**
     * The Hibernate generated identifier.
     */
    private long hid;	
	
    private String templateId;
    
    private String fileFormatName;
    
    private String infoTypeName;
    
    /* still a date field, but storing it as timestamp */
    private String dateCreated;

    public DataElementPreservation() {
    	
    }
    
    @Override
	public boolean equals(Object obj)
	{
    	if (obj == null) { 
    		return false; 
    	}
    	if (obj == this) {
    		return true;
    	}
    	if (obj.getClass() != getClass()) {
    		return false;
    	}
    	DataElementPreservation rhs = (DataElementPreservation)obj;
    	return new EqualsBuilder()
    				.append(templateId, rhs.templateId)
    				.append(fileFormatName, rhs.fileFormatName)
    				.append(infoTypeName, rhs.infoTypeName)
    				.isEquals();
	}
    
    @Override
    public int hashCode()
    {
    	return new HashCodeBuilder(17, 37)
    				.append(templateId)
    				.append(fileFormatName)
    				.append(infoTypeName)
    				.toHashCode();
    }
    
    @Override
    public String toString()
    {
    	return new ToStringBuilder(this)
    				.append("TemplateUUID", templateId)
    				.append("InfoType", infoTypeName)
    				.append("FileFormat", fileFormatName)
    				.append("CreatedDate", dateCreated)
    				.toString();
    }
    
	public long getHid()
	{
		return hid;
	}

	public void setHid(long hid)
	{
		this.hid = hid;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getFileFormatName()
	{
		return fileFormatName;
	}

	public void setFileFormatName(String fileFormatName)
	{
		this.fileFormatName = fileFormatName;
	}

	public String getInfoTypeName()
	{
		return infoTypeName;
	}

	public void setInfoTypeName(String infoTypeName)
	{
		this.infoTypeName = infoTypeName;
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
