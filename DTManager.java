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

	private int requestStartTime = 0;
	private int requestDuration = 0;
	private String connectionAddress = "localhost:3333";
	private String clientName = "SinkClient";
	private String channelName = "HelloWorld/IMM/pHEST";

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
		this.clientName = "DTManager";
		this.channelName = "something";
	}

	public DTManager(String requestStartTime, String requestDuration,
		String connectionAddress, String clientName, String channelName)
	{
		this.requestStartTime = Integer.parseInt(requestStartTime);
		this.requestDuration = Integer.parseInt(requestDuration);
		this.connectionAddress = connectionAddress;
		this.clientName = clientName;
		this.channelName = channelName;
	}

	public void connectToDT() {
		this.connectToDT(connectionAddress, clientName);
	}

	public void connectToDT(String address, String name) {
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
		try {
			this.sink.RequestRegistration(this.chMap);
			this.sink.Fetch(-1,this.chMap);
			System.out.println(this.chMap.GetTimeStart(0));
			System.out.println(this.chMap.GetTimeDuration(0));

		}
		catch(SAPIException se)
		{
			se.printStackTrace();
			this.sink.CloseRBNBConnection();
			System.exit(1);
		}


		try
		{
			this.sink.Request(this.chMap, requestStartTime, requestDuration, "newest");

			chMap = sink.Fetch(-1,chMap); 

	    	requestedData = chMap.GetDataAsInt32(0);

			double[] times = chMap.GetTimes(0);

	    	System.out.println(chMap.GetDataAsInt32(0).length);

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

	public static void main(String[] args)
	{
		//DTManager dm = new DTManager(0, 300, "localhost:3333", "SinkClient", "HelloWorld/IMM/pHEST");
		if(args.length == 5)
		{
			DTManager dm = new DTManager(args[0], args[1], args[2], args[3], args[4]);
			dm.execute();
		}
		else
		{
			System.err.println("There are not enough command line arguments");
			System.exit(1);
		}

	}

}