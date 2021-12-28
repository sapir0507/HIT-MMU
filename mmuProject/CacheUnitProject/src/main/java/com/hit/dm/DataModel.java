package com.hit.dm;

import java.io.Serializable;
import java.util.Objects;



public class DataModel<T> implements Serializable 
{
	private static final long serialVersionUID = 1L;
	private long dataModelId = 1;
	private T content;
	
	
	public DataModel(long id, T content)
	{
	    setDataModelId(id);
	    setContent(content);
	  
	}
	
	public long getDataModelId() 
	{
		return this.dataModelId;		
	}
	
	public void setDataModelId(long id) 
	{
		this.dataModelId = id;		
	}
	
	public T getContent() 
	{
		return this.content;		
	}
	
	public void setContent(T content) 
	{
		this.content = content;		
	}
	
	public String toString()
	{ 
		  return this.dataModelId + " " + content;  
	}
	
    public int hashCode()
    {
        return Objects.hash(dataModelId, content);
    }
	
	 public boolean equals(Object obj) 
	 {
		    if (!(obj instanceof DataModel)) 
		    {
		      return false;
		    }
		    if (obj == this) 
		    {
		      return true;
		    }
		    
			@SuppressWarnings("unchecked")
			DataModel <T> data = (DataModel<T>)obj;
		    return this.getDataModelId() == data.dataModelId && Objects.equals(this.getDataModelId(), this.getContent());	    	
		    	
		    }
	 }

