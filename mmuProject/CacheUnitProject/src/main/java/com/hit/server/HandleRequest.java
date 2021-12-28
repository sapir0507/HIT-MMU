package com.hit.server;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;
import com.hit.util.DataStat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Map;

public class HandleRequest<T> implements Runnable
{

    private Socket socket;
	private CacheUnitController<T> unitController;
    private Request<DataModel<T>[]> socketRequest;
    DataStat dataStats;

       
    public HandleRequest(Socket server, CacheUnitController<T> unitController2) 
    {
    	this.socket = server;
    	this.unitController = unitController2;
    	
    	dataStats = DataStat.getInstance();
	}
   
	@Override
    public void run()
    {
        boolean statsRequest = false;
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        Gson gson = new GsonBuilder().create();
        //�-Handle request 
        //�� ��� ����� ������: ��� ����� ���� �� ����� ������ ������ ��� ����� ����� ������ ��������� �� ����� ������ �� �������

        try
        {
        	//gets things from the client
            inputStream = new ObjectInputStream (socket.getInputStream ());
          //write information to the client
            outputStream = new ObjectOutputStream (socket.getOutputStream ());
        } 
        catch (IOException e)
        {
            e.printStackTrace ();
        }
        
        //JSON ����� ��� �����  
        //��� ������ ����� ����� �� �� ����� �� ����� ����� ������ �� 
       
        @SuppressWarnings("unused")
		DataModel<T>[] model = null;
        String inputString;

        try
        {
            //Assume you want to parse a JSON to Java class with Gson library. Now, you have to specifically tell Gson that, how?
        	//this function does exactly that. now JSON will know that we want it to be translated to class Request with DataModel<T>[] objects 
            Type ref = new TypeToken<Request<DataModel<T>[]>>(){}.getType();
            
            inputString = (String) inputStream.readObject();
            
            if(inputString.equals ("stats"))
            {
                statsRequest = true;
            }
            else
            {
            	//de-serialization 
                this.socketRequest = new Gson().fromJson(inputString, ref);
               
             }

          
        } 
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace ();
        }

// ����� ��� ����� ������� �� ������� �� ��
        if(!statsRequest)
        {
        	//action-���� ������ �� ��� �
        	//switch - case ��� �����
        	//����� ���� �������� ���� ���� ������
            @SuppressWarnings("rawtypes")
            String command;
			Map headers = socketRequest.getHeaders();
            command = (String) headers.get("action");
            String s;
            DataModel<T>[] body;
            body = socketRequest.getBody();
            
            switch(command.toLowerCase()) 
            {
            case "get":
            	//get-���� �� �� ������ �� ����� � body -�
            	// ����� �� ��� ��� �-������ ��� unit controller -��
            	
            	 DataModel<T>[] dataModels = unitController.get(body);
                 
                 try
                 {
                	 //converting dataModels into a JSON and saving in gsonString
                	 String gsonString = gson.toJson (dataModels);
                	 //������� �� �� ������� ��������
                	 System.out.println(gsonString);
                	 //������ ���� ��� ����� ���� ��������
                     outputStream.writeObject (gsonString);
                     outputStream.flush ();
                 } 
                 catch (IOException e)
                 {
                     e.printStackTrace ();
                 }
            	break;
            	
            case "delete":
            	//body-���� �������� �� ��� ����� �
            	//delete == true �� ������ �� 
                boolean delete = unitController.delete(body);
                
                //converting delete into a JSON
                 s = gson.toJson (delete);

                try
                {
                	//���� �� ����� ����� ����
                    outputStream.writeObject(s);
                    outputStream.flush ();
                } 
                catch (IOException e)
                {
                    e.printStackTrace ();
                }

            	break;
            	
            	 
            case "update":
                 // ���� ��� ����� update
            	// �� ���� �� ��� ����� ���� ����� ���� ����� ���� ������
            	//���� ��� ����� ���� �������
            	//���� ����� ������ �� ������ ���� �� ����� �� ��
            	 boolean update = unitController.update(body);
            	 
            	 //converting update into a JSON
                  s = gson.toJson (update);

                 try
                 {
                	// converting an object instance into a sequence of bytes 
                     outputStream.writeObject (s);
                     outputStream.flush ();
                 } 
                 catch (IOException e)
                 {
                     e.printStackTrace ();
                 }
            	break;
            
            default:
            	 try
                 {
            		 //����� ������� ������� ������ ���� �� ����� (�� ������)
                     outputStream.writeObject ("Unknown Action");
                     outputStream.flush ();
                 } 
                 catch (IOException e)
                 {
                     e.printStackTrace ();
                 }
            	 break;
            }
        }
        else
        {
        	//�� ����� ����, ����� ����� ������� ����������
        	//dataStats-���� �� ������� ��
        	
            String jsonString = gson.toJson(dataStats);
            try
            {
               outputStream.writeObject (jsonString);
            } 
            catch (IOException e)
            {
                e.printStackTrace ();
            }
        }
        
        try
        {
            outputStream.close ();
            inputStream.close ();

        } 
        catch (IOException e)
        {
            e.printStackTrace ();
        }


    }

}
   
       