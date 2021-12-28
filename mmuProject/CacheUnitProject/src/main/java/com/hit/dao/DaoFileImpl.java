package com.hit.dao;

import com.hit.dm.DataModel;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> 
{
    private final String filePath;
    private Map<Long, DataModel<T>> daoMap;
    private boolean flag;
    private ObjectOutputStream write;
    private ObjectInputStream read;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public DaoFileImpl(String filePath)
    {
        this.filePath = filePath;
        this.daoMap = new HashMap();
        this.flag = true;
    }

    @Override
    public void save(DataModel<T> entity) 
    {
        try 
        {
        	if(flag)
        		writeFile();
        	   	readFile();
        	
        	if(entity != null)
        	{
        		daoMap.put(entity.getDataModelId(), entity);
        		writeFile();
        	}
        }
        catch(IOException | ClassNotFoundException  e) 
        {
        	e.printStackTrace();
        }
    }

    @Override
    public void delete(DataModel<T> entity)
    {
        try 
        {
        	if(flag)
        		writeFile();
        		readFile();
        	
        	if(entity!=null)
        	{
        		daoMap.remove(entity.getDataModelId(), entity);
        		writeFile();
        	}
        	
        }
        catch(IOException | ClassNotFoundException e)
        {
        	e.printStackTrace();
        }
    }

    @Override
    public DataModel<T> find(Long id) 
    {
    	DataModel <T> result = null;
    	try {
        	if(flag)
        		writeFile();
        		readFile();
        	result = daoMap.get(id);
        	
        }
        catch(IOException | ClassNotFoundException e) 
    	{
        	e.printStackTrace();
        }
     	return result;
    }

    
    private void writeFile() throws IOException
    {
        try
        {
        	write = new ObjectOutputStream(new FileOutputStream(filePath,false));
        	
        	if(flag)
        		flag = false;
        		write.writeObject(daoMap);
        }
        catch(IOException e) 
        {
        	e.getMessage();
        }
    }

    @SuppressWarnings("unchecked")
	private void readFile() throws IOException, ClassNotFoundException 
    {
    	try 
    	{
            read =new ObjectInputStream(new FileInputStream(filePath));
            daoMap = (HashMap<Long, DataModel<T>>)read.readObject();
    	}
    	catch(IOException e)
    	{
        	e.getMessage();
        }
    }
}