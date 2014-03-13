import com.rbnb.sapi.ChannelMap;
import com.rbnb.sapi.Sink;
import com.rbnb.sapi.Client;
import com.rbnb.sapi.SAPIException;
import com.rbnb.sapi.*;
import java.util.*;

public class DTManager {

	Sink sink;
	ChannelMap chMap;
	int[] intDataType;
	String[] dataType;

	private int[] requestedData;

	private String[] chNames;
	private String[] units;
	private String[] MIMEs;

	// ----- DT Client & Source related operation
	public DTManager() 
	{
		connectToDT();

		createChMap();

		addToChMap("HelloWorld/IMM/pHEST");

		requestAndFetch();
	}

	public void connectToDT() {
		String address = "localhost:3333";
		String clientName = "SinkClient";
		this.connectToDT(address, clientName);
	}

	public void connectToDT(String address, String name) {
		sink=new Sink();
      	try
      	{
      		sink.OpenRBNBConnection("localhost:3333", "SinkClient");
      	}
      	catch(SAPIException se)
      	{ 
      		se.printStackTrace();
      		sink.CloseRBNBConnection();
      		System.exit(1);
      	}
	}

	public void detachSink() {
		if (this.sink != null)
			//this.sink.Detach();
		this.sink = null;
	}

	public void closeRBNBConnection() {
		if (this.sink != null)
			this.sink.CloseRBNBConnection();
	}

	public void clear() {
		this.sink = null;
	}

	public boolean isDTConnectionAlive() {
		return this.sink.VerifyConnection();
	}

	/*
	 * // ----- DT ChannelMap related operations
	 * 
	 * // this method creates the following: // a new channel map with proper
	 * cheannel names // an array of int for data type defined by
	 * com.rbnb.sapi.ChannelMap // // "int8", "int16", "int32", "int64"
	 * "float32", "float64", // "string", "bytearray", or "unknown". // Assume
	 * that the chNames and the dataTypes have the same length
	 */
	public void createChMap()
	{

		this.chMap = new ChannelMap();

		//this.chNames = chNames;
		//this.units = units;
		//this.MIMEs = MIMEs;
		//String[] chNames, String[] dTypes, String[] units, String[] MIMEs

	}

	public void addToChMap(String path)
	{
		try
      	{
      		this.chMap.Add(path);
      	}
      	catch(SAPIException se)
      	{ 
      		se.printStackTrace();
      		this.sink.CloseRBNBConnection();
      		System.exit(1);
      	}
	}

	public void requestAndFetch()
	{
		try
		{
			this.sink.Request(this.chMap, 200, 0, "newest");
			chMap = sink.Fetch(-1,chMap); 

			System.out.println(chMap.GetDataAsInt32(0).length);

	    	requestedData = chMap.GetDataAsInt32(0);

			double[] times = chMap.GetTimes(0);

			/*for(int i = 0; i < chMap.GetDataAsInt32(0).length; i++)
	        {
	        	System.out.println("Retrieved \""
	                   +chMap.GetDataAsInt32(0)[i]
	                   +"\" from server." + times[i]);
	    	}*/

	    }
	    catch(SAPIException se)
	    {
	    	se.printStackTrace();
      		this.sink.CloseRBNBConnection();
      		System.exit(1);	
	    }
	}

	public int[] getDataArray()
	{
		return requestedData;
	}

	public void findpHValues()
  	{
    	try 
    	{
      	Sink sink=new Sink();
      	sink.OpenRBNBConnection("localhost:3333", "SinkClient");
 
       // Pull data from the server:
      	ChannelMap rMap = new ChannelMap();

      	rMap.Add("HelloWorld/IMM/pHEST");
      	sink.Request(rMap, 0, 900, "newest");

        rMap = sink.Fetch(-1,rMap); 

        System.out.println(rMap.GetChannelList());
        System.out.println(rMap.GetChannelList().length);

        System.out.println(rMap.GetDataAsInt32(0));
        
        for(int i = 0; i < 10; i++)
        {
        	System.out.println("Retrieved \""
                   +rMap.GetDataAsInt32(0)[i]
                   +"\" from server.");
    	}

    	System.out.println(rMap.GetDataAsInt32(0).length);


        
       } 
     catch(SAPIException se) 
     { 
      se.printStackTrace(); 
     }
  }

}