package com.hit.services;

import com.hit.dm.DataModel;

public class CacheUnitController<T> 
{
	private int capacity;
	private CacheUnitService<T> cacheUnitService = new CacheUnitService<>(); // creating a cache unit service 
	
	public CacheUnitController()
	{
		cacheUnitService.SetChoice(1);
		cacheUnitService.SetCapacity(20);
	}
	
	public void setChoice(int choice)
	{
		cacheUnitService.SetChoice(choice);
	}
	
	public int getChoice() 
	{
		return cacheUnitService.GetChoice();
	}
	
	public boolean delete(DataModel<T>[] dataModels) {
		//deletes all of the data Models from the cache
		return cacheUnitService.delete(dataModels);
	}

	public boolean update(DataModel<T>[] dataModels) {
		//updates all the data models in the cache according to the cache Algorithm chosen
		return cacheUnitService.update(dataModels);
	}
	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		//gets all the data models in the cache
		return cacheUnitService.get(dataModels);
		
	}
	public int getCapacity() 
	{
		return capacity;
	}
	public void setCapacity(int capacity) 
	{
		this.capacity = capacity;
		cacheUnitService.SetCapacity(capacity);
	}

}
	
	
	