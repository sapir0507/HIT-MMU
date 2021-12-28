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
        //ל-Handle request 
        //יש שתי מטרות עקריות: דבר ראשון נקבל את הבקשה מהסרבר ונבדוק האם מדובר בבקשה לבדיקת סטטיסטיקה או ביצוע פעולות על הזיכרון

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
        
        //JSON הבקשה היא בצורת  
        //לכן המחלקה צריכה לתרגם את מה שרשום שם למשהו שנוכל להשתמש בו 
       
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

// נבדוק האם מדובר בפעולות על הזיכרון אם כן
        if(!statsRequest)
        {
        	//action-ניקח מהבקשה את סוג ה
        	//switch - case ואז בעזרת
        	//נעביר אותו לפונקציה הקטע הקוד המתאים
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
            	//get-רשום על מי מבצעים את פעולת ה body -ב
            	// מחזיר את הדף לפי ה-אינדקס שלו unit controller -וה
            	
            	 DataModel<T>[] dataModels = unitController.get(body);
                 
                 try
                 {
                	 //converting dataModels into a JSON and saving in gsonString
                	 String gsonString = gson.toJson (dataModels);
                	 //מדפיסים את מה שקיבלנו מהזיכרון
                	 System.out.println(gsonString);
                	 //שולחים אותו למי שביקש אותו מלכתחילה
                     outputStream.writeObject (gsonString);
                     outputStream.flush ();
                 } 
                 catch (IOException e)
                 {
                     e.printStackTrace ();
                 }
            	break;
            	
            case "delete":
            	//body-מוחק מהזיכרון את הדף שכתוב ב
            	//delete == true אם הצלחנו אז 
                boolean delete = unitController.delete(body);
                
                //converting delete into a JSON
                 s = gson.toJson (delete);

                try
                {
                	//נשלח את סטטוס הבקשה חזרה
                    outputStream.writeObject(s);
                    outputStream.flush ();
                } 
                catch (IOException e)
                {
                    e.printStackTrace ();
                }

            	break;
            	
            	 
            case "update":
                 // עושה שני דברים update
            	// אם קיים אז הוא מעדכן אותו לנתון החדש שקיבל בגוף ההודעה
            	//אחרת הוא מוסיף אותו לזיכרון
            	//מקבל תשובה חיובית או שלילית לגבי אם הצליח או לא
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
            		 //במידה שהפעולה שאחנחנו אמורים לבצע לא קיימת (לא מוגדרת)
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
        	//אם הגענו לכאן, מדובר בבקשה לנתונים בסטטיסטיקה
        	//dataStats-נשלח את הנתונים שב
        	
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
   
       