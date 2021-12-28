package com.hit.services;
import com.hit.Algorithm.LRUAlgoCacheImpl;
import com.hit.Algorithm.RandomAlgoCacheImpl;
import com.hit.Algorithm.SecondChance;
import com.hit.dao.DaoFileImpl;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;
import javax.xml.crypto.Data;
import java.io.IOException;

@SuppressWarnings("unused")
public class CacheUnitService<T>
{
	private CacheUnit<T> cacheUnit;
	private int choice = 1;
	private int capacity = 20;
	private final DaoFileImpl<T> daoFile = new DaoFileImpl<>("out.txt");
	@SuppressWarnings("unchecked")
	public CacheUnitService()
    {
		// DaoFileImpl<T> daoFile = new DaoFileImpl<>("out.txt");
		 
		if(GetChoice()==1)
		{
			LRUAlgoCacheImpl<T, DataModel<T>>cacheAlgo = new LRUAlgoCacheImpl<>(GetCapacity());
			cacheUnit = new CacheUnit(cacheAlgo, daoFile);
		}
		else if(GetChoice()==3)
		{
			RandomAlgoCacheImpl<T, DataModel<T>> cacheAlgo = new RandomAlgoCacheImpl<>(GetCapacity());
			cacheUnit = new CacheUnit(cacheAlgo, daoFile);
		}
		else 
		{
			SecondChance<T, DataModel<T>> cacheAlgo = new SecondChance<>(GetCapacity());
			cacheUnit = new CacheUnit(cacheAlgo, daoFile);
		}
	
    }
	public void SetChoice(int choice)
	{
		this.choice = choice;
		if(cacheUnit == null)
		{
			System.out.println("warning, all data in cache has been deleted/n");
			ChooseAlgo(choice);
		}
		else ChooseAlgo(choice);
	}
	
	public void ChooseAlgo(int choice) 
	{
		CacheUnit<T> cacheUTemp;
		//DaoFileImpl<T> daoFile = new DaoFileImpl<>("out.txt");
		if(GetChoice()==1)
		{
			LRUAlgoCacheImpl<T, DataModel<T>>cacheAlgo = new LRUAlgoCacheImpl<>(GetCapacity());
			
			cacheUTemp = new CacheUnit(cacheAlgo, daoFile);
		}
		else if(GetChoice()==3)
		{
			RandomAlgoCacheImpl<T, DataModel<T>> cacheAlgo = new RandomAlgoCacheImpl<>(GetCapacity());
			cacheUTemp = new CacheUnit(cacheAlgo, daoFile);
		}
		else 
		{
			SecondChance<T, DataModel<T>> cacheAlgo = new SecondChance<>(GetCapacity());
			cacheUTemp = new CacheUnit(cacheAlgo, daoFile);
		}
		
			cacheUnit = cacheUTemp;
		
	}
	
	public int GetChoice() 
	{
		return this.choice;
	}
	
	public int GetCapacity() 
	{
		return this.capacity;
	}
	
	public void SetCapacity(int capacity)
	{
		this.capacity = capacity;
		ChooseAlgo(GetChoice());
	}
	
    public CacheUnit<T> getCacheUnit()
    {
		return cacheUnit;
	}

	public void setCacheUnit(CacheUnit<T> cacheUnit) 
	{
		this.cacheUnit = cacheUnit;
	}

	public boolean delete(DataModel<T>[] dataModels)
    {
    	boolean isDelete = false;
    	
        DataModel<T>[] returnModels = null;
        Long[] ids = new Long[dataModels.length];

        for (int i = 0; i < dataModels.length ; i++)
        {
            ids[i] = dataModels[i].getDataModelId ();
        }

        returnModels = cacheUnit.getDataModels(ids); //returns all the models that are in cache or in file

        for(DataModel<T> model: returnModels)
        {
            if(model.getContent() != null)
            	{
            	   // model.setContent(null);
            	    cacheUnit.removeDataModels(ids);
            	}
        }
        
        if(returnModels.length > 0)        
        {
            isDelete = true;
        }

        return isDelete;
    }

    
	public boolean update(DataModel<T>[] dataModels)
    {
    	boolean isUpdate = false;
    	 
        DataModel<T>[] returnModels = null;
        Long[] ids = new Long[dataModels.length];

        for (int i = 0; i < dataModels.length ; i++)
        {
            ids[i] = dataModels[i].getDataModelId ();
        }
        //returns the data models with those ids, that already exists in the cache or the file 
        //if ids are not matching anything in cache or file, return a null in returnModels[0]
        returnModels = cacheUnit.getDataModels(ids);

        for (int i = 0; i <dataModels.length ; i++)
        {
            for (int j = 0; j <returnModels.length ; j++)
            {
            	if((returnModels[j] != null) && ((long)dataModels[i].getDataModelId())==((long)returnModels[j].getDataModelId()))
                {
            		//checks if data model in place i is already exists in return models
            		//if so, it adds changes the content inside it to dataModel's content.
                    returnModels[j].setContent(dataModels[i].getContent());
                    //exits from the loop to look for the next data model 
                    j = returnModels.length+1;
                }
            }
        }
        
        returnModels = cacheUnit.putDataModels(dataModels);
         
        
        if(dataModels.length > 0)
        {
            isUpdate = true;
        }
        

        return isUpdate;
    }

   
	public DataModel<T>[] get(DataModel<T>[] dataModels)
    {
        Long[] ids = new Long[dataModels.length];
        DataModel<T>[] models = null;

        for (int i = 0; i < dataModels.length; i++)
        {
            ids[i] = dataModels[i].getDataModelId ();
        }

        models = cacheUnit.getDataModels(ids);

        return models;
    }
}