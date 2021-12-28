package com.hit.dao;

public interface IDao <ID extends java.io.Serializable, T>
{
	public T find(ID id) throws IllegalArgumentException;
	public void delete(T entity) throws IllegalArgumentException;
	public void save(T entity);
}
