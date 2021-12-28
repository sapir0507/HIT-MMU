package com.hit.memory;
import com.hit.Algorithm.*;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.util.DataStat;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class CacheUnit<T> extends java.lang.Object
{
	IAlgoCache<Long, DataModel<T>> algo;
	IDao<Serializable,DataModel<T>> dao; 
	DataStat dataStats;
	private boolean status_update = false;
	
	public CacheUnit(IAlgoCache <Long, DataModel<T>> algo, IDao<Serializable, DataModel<T>> dao)
	{
		this.algo = algo;
		this.dao = dao;
		dataStats = DataStat.getInstance();
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public DataModel<T>[] getDataModels(Long[] ids)
	{
			DataModel<T>[] models = new DataModel[ids.length];
			DataModel dataCache = null;
			DataModel dataFile = null;
			
			for (int i = 0; i < ids.length; i++)
			{
				dataCache = new DataModel(ids[i], algo.getElement(ids[i]));
				if (algo.getElement(ids[i]) != null) 
				{ 
			    	// if the id is in cache, add it to models
					models[i] = dataCache;
					dataStats.addModelsRequest();
				}
				else 
				{
					dataFile = new DataModel(ids[i], dao.find(ids[i]));
					if (dao.find(ids[i]) != null) 
					{ 
					     //if id is in file then add it to models, put it in the cache and delete it from the file
						models[i] = dataFile;
						dataStats.addModelsRequest();
						dataCache = algo.putElement(ids[i], models[i]);
						dao.delete(dao.find(ids[i]));
						
						if(dataCache != null) //swap 
						{
							dataStats.addModelSwap();
							dao.save(dataCache);
							dataStats.addModelSwap();
						}
					} 
				}
			}
			
			if (models.length == 0) 
			      return null;
			else 
			      return models;
	}	

	public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) 
	{ 
		DataModel<T> model;
		for (int i = 0; i < datamodels.length; i++) 
		{
			model = algo.getElement(datamodels[i].getDataModelId());
			//status_update is to see if it was just an update to an existing data, or a swap
			if(model!=null)
				status_update = true;
			else
				status_update = false;
			
			model = algo.putElement(datamodels[i].getDataModelId(), datamodels[i]);
			
			if(model != null && status_update == false) //sees if a swap happened
			{
				dao.save(model);
				dataStats.addModelSwap();
			}
		}
		return datamodels;
	}
	
	public void removeDataModels(Long[] ids) 
	{
		DataModel<T> toRemove;
		for (int i = 0; i < ids.length; i++) 
		{
			algo.removeElement(ids[i]);
		}
		
		for (int i = 0; i < ids.length; i++) 
		{
			DataModel<T> modelToDelete = dao.find(ids[i]); //if exist in file we want to delete it
			if (modelToDelete!=null)
				dao.delete(modelToDelete);
		}
	}
}