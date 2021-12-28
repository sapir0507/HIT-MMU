package com.hit.Algorithm;

public interface IAlgoCache <K, V> {

	public abstract V getElement(K key);
	public abstract V putElement(K key, V value);
	public abstract void removeElement(K key);
	
	
}
