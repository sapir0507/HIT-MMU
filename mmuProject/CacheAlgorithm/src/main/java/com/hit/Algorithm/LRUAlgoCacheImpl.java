package com.hit.Algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUAlgoCacheImpl<K, V> extends AbstractAlgoCache<K,V> 
{
	private Map<K, V> cacheMap;
	private LinkedList<K> cacheList;

	public LRUAlgoCacheImpl(int capacity) 
	{
		super(capacity);
		cacheMap = new HashMap<>();
		cacheList = new LinkedList<>();
	}

	@Override
	public V getElement(K key)
	{ 
		//Returns the value to which the specified key is mapped, or null if this cache contains no mapping for the key.
		if (cacheMap.containsKey(key))
		{
			cacheList.remove(key);
			cacheList.addFirst(key);
			return cacheMap.get(key);
		}
		return null;
	}

	@Override
	public V putElement(K key, V value)
	{
		V tmpV = null;
		K tmpK;
		
		if (cacheMap.containsKey(key)) 
		{ 
			//if exists - remove from current location and add to the top of the list
			removeElement(key);
		}
		
		else if (cacheMap.size() == capacity)
		{
			//if does not exist and the list is full 
			tmpK = cacheList.removeLast(); 
			tmpV = cacheMap.remove(tmpK); 
		}

		cacheMap.put(key, value); //if does not exist or was removed - add to map
		cacheList.addFirst(key); 
		
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