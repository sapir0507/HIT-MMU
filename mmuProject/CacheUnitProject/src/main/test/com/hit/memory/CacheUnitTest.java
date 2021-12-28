package com.hit.memory;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.hit.Algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dm.DataModel;
import org.junit.After;
import org.junit.Before;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

@SuppressWarnings("unused")
public class CacheUnitTest
{
   
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
    public void getDataModels() throws IOException
    {
        LRUAlgoCacheImpl<Long, DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(25);
        DaoFileImpl<Integer> daoFile = new DaoFileImpl<>("DataSource.txt");
		CacheUnit<Integer> cacheUnit = new CacheUnit(lru, daoFile);

		//create some data in the memory & a seperate dao
        for (int i = 0; i < 25; i++)
        {
            lru.putElement(Long.valueOf(i),new DataModel(Long.valueOf(i),i));
            daoFile.save(new DataModel(Long.valueOf(i), i));
        }

     
        Long[] ids = {Long.valueOf(5), Long.valueOf(17), Long.valueOf(7), Long.valueOf(11), Long.valueOf(4), Long.valueOf(24)};
        DataModel<Integer>[] dataModels = null;

        dataModels = cacheUnit.getDataModels(ids);
        for(int i = 0; i < dataModels.length; i++) 
        {
           System.out.println("Page ID = " + dataModels[i].getDataModelId() + " , Page content = "+ dataModels[i].getContent());
           //assertEquals(daoFile.find(ids[i]).getContent(), dataModels[i].getContent());
        }

    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void updateFile(DataModel<Integer> model1) 
    {
    	
    	 LRUAlgoCacheImpl<Long, DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(25);
         DaoFileImpl<Integer> daoFile = new DaoFileImpl<>("DataSource.txt");
         CacheUnit<Integer> cacheUnit = new CacheUnit(lru, daoFile);

         for (int i = 0; i < 25; i++)
         {
           lru.putElement(Long.valueOf(i), new DataModel(Long.valueOf(i),i));
         }

         for (int i = 0; i < 25; i++)
         {
            int integer = i+5;
            lru.putElement(Long.valueOf(i),new DataModel(Long.valueOf(i),integer));
            daoFile.save(new DataModel(Long.valueOf(i), integer));
         }
         
         for (long i = 0; i < 25; i++)
         {
        //	assertEquals(daoFile.find(i), lru.getElement(i));
         	System.out.println("element of lru[i] is "+ lru.getElement(i)+ "element of dao[i] is " +daoFile.find(i)+ "if equal then it was updated\n");
         }
    	
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked", "null" })
    @Test
    public DataModel<Integer>[] putDataModels(DataModel<Integer>[] datamodels)
    {
    	LRUAlgoCacheImpl<Long, DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(25);
        DaoFileImpl<Integer> daoFile = new DaoFileImpl<>("DataSource.txt");

		CacheUnit<Integer> cacheUnit = new CacheUnit(lru, daoFile);

        for (int i = 0; i < 25; i++)
        {
          lru.putElement(Long.valueOf(i),datamodels[i]);
          daoFile.save(datamodels[i]);
        }

        DataModel<Integer>[] dataModels = null;
        for(long i=0; i<25;i++)
        {
            dataModels[(int) i] = lru.getElement(i);
        }
    	return dataModels;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void removeDataModels(Long[] ids) 
    {
    	LRUAlgoCacheImpl<Long, DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(25);
        DaoFileImpl<Integer> daoFile = new DaoFileImpl<>("DataSource.txt");
		CacheUnit<Integer> cacheUnit = new CacheUnit(lru, daoFile);

        for (int i = 0; i < 25; i++)
        {
          lru.putElement(Long.valueOf(i), new DataModel(Long.valueOf(i),i));
          daoFile.save(new DataModel(Long.valueOf(i), i));
        }

        Long[] ids1 = {Long.valueOf(5), Long.valueOf(15), Long.valueOf(7), Long.valueOf(11), Long.valueOf(4), Long.valueOf(24)};
        DataModel<Integer>[] dataModels = null;

         cacheUnit.removeDataModels(ids1);
         dataModels = cacheUnit.getDataModels(ids1);
        // assertEquals(null, dataModels);
         if(dataModels == null)
        	 System.out.println("files were deleted!\\n");
         else 
        	 System.out.println("files were not deleted!\\n");
    }
    
    
    

}