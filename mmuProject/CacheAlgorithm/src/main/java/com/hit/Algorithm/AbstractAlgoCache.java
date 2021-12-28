package com.hit.Algorithm;

public abstract class AbstractAlgoCache <K,V> implements IAlgoCache<K,V> {

	protected int capacity;

	AbstractAlgoCache(int capacity){
    this.capacity = capacity;
}

	public int getCapacity()
	{
		return capacity;
	}
}
