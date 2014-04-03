import com.rbnb.sapi.ChannelMap;
import com.rbnb.sapi.Sink;
import com.rbnb.sapi.Client;
import com.rbnb.sapi.SAPIException;
import com.rbnb.sapi.*;
import java.util.*;

public class DTManager 
{

	Sink sink;
	ChannelMap chMap;
	int[] intDataType;
	String[] dataType;

	private double[] requestedData;
	private double[] times;

	private String[] chNames;
	private String[] units;
	private String[] MIMEs;

	private int requestStartTime;
	private int requestDuration;
	private String requestType;
	private String connectionAddress;
	private String clientName;
	private String channelName;

	public void execute()
	{
		connectToDT();
		createChMap();
		addToChMap(channelName);
		requestAndFetch();
	}

	// ----- DT Client & Source related operation
	public DTManager() 
	{
		this.requestStartTime = 0; //System.getCurrentMilli();
		this.requestDuration = 1000;
		this.connectionAddress = "localhost:3333";
		this.clientName = "SinkClient";
		this.channelName = "HellowWorld/IMM/pHEST";
		this.requestType = "newest";
	}

	public DTManager(String requestStartTime, String requestDuration,
		String connectionAddress, String clientName, String channelName, String requestType)
	{
		this.requestStartTime = Integer.parseInt(requestStartTime);
		this.requestDuration = Integer.parseInt(requestDuration);
		this.connectionAddress = connectionAddress;
		this.clientName = clientName;
		this.channelName = channelName;
		this.requestType = requestType;
	}

	public void connectToDT() 
	{
		this.connectToDT(connectionAddress, clientName);
	}

	public void connectToDT(String address, String name) 
	{
		sink=new Sink();
      	try
      	{
      		sink.OpenRBNBConnection(address, name);
      	}
      	catch(SAPIException se)
      	{ 
      		se.printStackTrace();
      		sink.CloseRBNBConnection();
      		System.exit(1);
      	}
	}

	public void detachSink() 
	{
		if (this.sink != null)
			//this.sink.Detach();
		this.sink = null;
	}

	public void closeRBNBConnection() 
	{
		if (this.sink != null)
			this.sink.CloseRBNBConnection();
	}

	public void clear() 
	{
		this.sink = null;
	}

	public boolean isDTConnectionAlive() 
	{
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
			this.sink.Request(this.chMap, requestStartTime, requestDuration, requestType);
			chMap = sink.Fetch(-1,chMap); 
	    	requestedData = (chMap.GetDataAsFloat64(0));
			times = chMap.GetTimes(0);
	    }
	    catch(SAPIException se)
	    {
	    	se.printStackTrace();
      		this.sink.CloseRBNBConnection();
      		System.exit(1);	
	    }

	}

	public double[] getDataArray()
	{
		return requestedData;
	}

	public double[] getTimes()
	{
		return times;
	}

}