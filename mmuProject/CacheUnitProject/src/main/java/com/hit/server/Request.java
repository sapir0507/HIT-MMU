
package com.hit.server;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Request<T> implements Serializable
{

    private java.util.Map<java.lang.String,java.lang.String> headers;
    private T body;

    public Request(java.util.Map<java.lang.String,java.lang.String> headers, T body)
    {
        this.headers = headers;
        this.body = body;
    }

    public T getBody()
    {
        return this.body;
    }

    public java.util.Map<java.lang.String,java.lang.String> getHeaders()
    {
        return this.headers;
    }

    public void	setBody(T body)
    {
        this.body = body;
    }

    public void	setHeaders(java.util.Map<java.lang.String,java.lang.String> headers)
    {
        this.headers = headers;
    }

    @Override
    public java.lang.String toString()
    {
        return "Request{" + "headers=" + headers.toString() + ", body=" + body.toString() +'}';
    }
}