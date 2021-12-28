package com.hit.Algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class RandomAlgoCacheImpl<K,V> extends AbstractAlgoCache<K,V>
{
	private Map<K, V> cacheMap;
	private LinkedList<K> cacheList;
	
	public RandomAlgoCacheImpl(int capacity) 
	{
		super(capacity);
		cacheMap = new HashMap<>();
		cacheList = new LinkedList<>();
	}

	@Override
	public V getElement(K key)
	{
		if (cacheMap.containsKey(key))
			return cacheMap.get(key);
		else return null;
	}

	@Override
	public V putElement(K key, V value) 
	{
		V tmpV = null;
		K tmpK;
		ArrayList<K> keysAsArray = new ArrayList<K>(cacheMap.keySet());
		Random r = new Random();
		int temp = 0;
				
		if (cacheMap.containsKey(key)) 
		{ 
			//if exists - remove from current location and add to the top of the list
			removeElement(key);
		}		
		else 
			if (cacheMap.size() == capacity)
		    { 
		    	temp = r.nextInt(keysAsArray.size());
		    	tmpK = cacheList.remove(temp);
		    	tmpV = cacheMap.remove(tmpK);
		    	cacheMap.put(key, value);
	    		cacheList.add(key);
		    }
	    	else 
		    {
		        cacheMap.put(key, value); //if does not exist or was removed - add to map
		        cacheList.add(key); //add key to list
		    }
		return tmpV;
		
	}

	@Override
	public void removeElement(K key)
	{ 
		//Removes the mapping for the specified key from this map if present.
		if (cacheMap.containsKey(key))
		{
			cacheList.remove(key);
			cacheMap.remove(key);
		}
	}
}
