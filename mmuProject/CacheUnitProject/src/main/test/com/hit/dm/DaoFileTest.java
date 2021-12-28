package com.hit.dm;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.hit.Algorithm.IAlgoCache;
import com.hit.Algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;

class DaoFileTest 
{
	int size = 20;
	public IAlgoCache<Long, DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(size);
	public Long[] ids = new Long[size];
	@SuppressWarnings("unchecked")
	public DataModel<Integer>[] dataModels = new DataModel[size];
	public DataModel<Integer> myModel = null;

	
	@Test	
	void testDaosave()
	{
				
		DaoFileImpl<Integer> dao = new DaoFileImpl<>("DataSource.txt");
		
		for (int i = 0; i < size ; i++)
		{
			ids[i] = Long.valueOf(i);
			myModel = new DataModel<Integer>(Long.valueOf(i), i);
			dataModels[i] = myModel;
			lru.putElement(Long.valueOf(i), myModel);
			dao.save(myModel);
			assertEquals(dataModels[i].getDataModelId(), dao.find(dataModels[i].getDataModelId()).getDataModelId(), "if inserted to the file it should find the current model");
			dao.save(myModel);
		}
	}

	@Test	
	void testDaofind() 
	{
		DaoFileImpl<Integer> dao = new DaoFileImpl<>("DataSource.txt");
		
		for (long i = 0; i < size; i++)
		{	
			assertEquals(dao.find(i), dataModels[(int)i], "if their both there, dao should be able to find it without a problem");
		}
	}

	@Test	
	void testingDaoDelete() 
	{
		DaoFileImpl<Integer> dao = new DaoFileImpl<>("DataSource.txt");
		testDaosave();
		for (int i = 0; i < dataModels.length; i++)
		{
			dao.delete(dataModels[i]);
			assertEquals(null, dao.find(dataModels[i].getDataModelId()), "checking if model was removed");
		}
	}

}
