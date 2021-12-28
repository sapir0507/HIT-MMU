package com.hit.server;

public class Response<T> extends java.lang.Object
{
	private java.util.Map<java.lang.String,java.lang.String> headers;
	private T body;
	
	Response(java.util.Map<java.lang.String,java.lang.String> headers, T body) 
	{
		this.headers = headers;
        this.body = body;
	}
	public java.util.Map<java.lang.String,java.lang.String> getHeaders()
	{
		return this.headers;
		
	}
	public void setHeaders(java.util.Map<java.lang.String,java.lang.String> headers) 
	{
		this.headers = headers;
	}
	public T getBody() {
		return this.body;
	}
	public void setBody(T body)
	{
		this.body = body;
	}
	@Override
	public java.lang.String toString()
	{
		 return "Response{" + "headers=" + headers.toString () + ", body=" + body.toString () +'}';
		
	}
}
