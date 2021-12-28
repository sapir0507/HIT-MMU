package com.hit.Algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class SecondChance <K,V> extends AbstractAlgoCache<K,V>
{
	private Map<K, V> cacheMap;
	private LinkedList<K> cacheList;
	private LinkedList<K> bitList;

	
	public SecondChance(int capacity)
	{
		super(capacity);
	
    	cacheMap = new HashMap<>();
		cacheList = new LinkedList<>();
		bitList  = new LinkedList<>();
	}

	@Override
	public V getElement(K key) 
	{
		
		if (cacheMap.containsKey(key)) 
		{
			if (!bitList.contains(key))
				bitList.add(key);
						return cacheMap.get(key);
		}
		else return null;
	}
	
	@Override
	public V putElement(K key, V value) 
	{
		V tmpV = null;
		K tmpK;
		int i = 0;

		if ((cacheMap.containsKey(key)) && (!bitList.contains(key)))//???if bitList contains the key, then it gets a second chance
		{ 
			//since it didn't bitList didn't contain the key, it gets removed
			removeElement(key);
		}

		else if (cacheMap.containsKey(key)) 
		{
			tmpV = getElement(key);
			bitList.remove(key);
			cacheMap.put(key, value);
			
		}
		
		
		else if (!bitList.isEmpty())
		{
			if (cacheMap.size() == capacity)
			{
				for (i = 0; i < capacity; i++) 
				{
					if (bitList.contains(cacheList.get(i))) 
					{
						bitList.remove(i);//try
						i++;//why?
					}
					else				
					{
						removeElement(cacheList.get(i));
						cacheMap.put(key, value);
						cacheList.add(key);
					}
				}
			}
		}
		else 
			if (cacheMap.size() == capacity)
		    {
				tmpK = cacheList.removeFirst();
				tmpV = cacheMap.get(tmpK);
				cacheMap.remove(tmpK);
				cacheMap.put(key, value);
				cacheList.add(key);
				
			}
		    else 
		    {
		    	cacheMap.put(key, value); 
	            cacheList.add(key);
		    }			
	return tmpV;
}		
		

	@Override
	public void removeElement(K key)
	{
		if (!cacheMap.isEmpty() && cacheMap.containsKey(key)) 
		{
			cacheList.remove(key);
			//bitList.remove(key);
			cacheMap.remove(key);
		}	
		if(bitList.contains(key))
			bitList.remove(key);
		
	}
}
