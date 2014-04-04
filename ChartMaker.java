import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.data.xy.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.annotations.XYShapeAnnotation;
import java.io.File;
import java.io.IOException;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.axis.DateAxis;
import java.text.*;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import java.sql.Time;
import org.jfree.data.time.Millisecond;
import java.util.*;
import java.awt.geom.Rectangle2D;
import java.awt.BasicStroke;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.data.time.Day;


public class ChartMaker{

  private static final long serialVersionUID = 1L;

    private String url;
    private String clientName;
    private String channelName;
    private String duration;
    private String chartName;
    private String requestType;
    private String requestStartTime;
    private String saveDirectory;
    private double[] data;
    private double[] times;

    public ChartMaker()
    {
        url = "localhost:3333";
        clientName = "SinkClient";
        channelName = "HelloWorld/IMM/pHEST";
        duration = "900";
        chartName = "Scripps Pier 15 Minutes Chart";
        requestType = "newest";
        requestStartTime = "0";
        saveDirectory = "/Users/mingcn/Dropbox/Public/SIOCharts/TestChart";
    }

    public ChartMaker(String chartName, String requestStartTime, String duration, String requestType, 
        String url, String clientName, String channelName, String saveDirectory) {
        this.url = url;
        this.clientName = clientName;
        this.channelName = channelName;
        this.duration = duration;
        this.chartName = chartName;
        this.requestType = requestType;
        this.requestStartTime = requestStartTime;
        this.saveDirectory = saveDirectory;

    }

    public void execute()
    {
        DTManager dt = new DTManager(requestStartTime, duration, url, clientName, channelName, requestType);

        dt.execute();

        data = dt.getDataArray();
        times = dt.getTimes();

        setChartName();

        final XYDataset dataset = createDataset(data, times);
        final JFreeChart chart = createChartObj(dataset);

        saveChart(chart);

    }
    
    private void saveChart(JFreeChart chart)
    {

        String date = getDateFromDouble("yyyyMMddHHmm", times[times.length - 1]);

        try 
        {
            ChartUtilities.saveChartAsJPEG(new File(saveDirectory + date + ".jpg"), chart, 1000, 540);
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println("Problem occurred creating chart.");
        }
    }

    public String getDateFromDouble(String format, double time)
    {
        
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        long temp = (long)time*(long)1000;
        Date date = new Date(temp);
        return dateFormat.format(date);
    }

    public void setChartName()
    {
        this.chartName = this.chartName + " " + getDateFromDouble("MM/dd/yy", times[0]) + " - " + getDateFromDouble("MM/dd/yy", times[times.length - 1]); 
    }
    
    /**
     * Creates a sample dataset 
     */

    private XYDataset createDataset(double[] array, double[] times) {

        TimeSeries series1 = new TimeSeries("pH");
        TimeSeries series2 = new TimeSeries("Historical pH");
 
        for(int i = 0; i < array.length; i++)
        {
            series1.addOrUpdate(new Millisecond(new Time( (long)times[i]*(long)1000 )), array[i]);
        }

        for(int i = 0; i < array.length; i++)
        {
            series2.addOrUpdate(new Millisecond(new Time( (long)times[i]*(long)1000 )), 8.16);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
       
                
        return dataset;
        
    }
    
    /**
     * Creates a chart
     */

    private JFreeChart createChartObj(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
            chartName,      // chart title
            "time",                      // x axis label
            "pH",                      // y axis label
            dataset,                  // data
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

        //        final StandardLegend legend = (StandardLegend) chart.getLegend();
        //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        //renderer.setSeriesShapesVisible(2, true);
        //renderer.setSeriesShape( 0, new Rectangle2D.Double( -1.0, -1.0, 2.0, 2.0 ) );
        //renderer.setSeriesShape( 1, new Rectangle2D.Double( -1.0, -1.0, 2.0, 2.0 ) );
        //renderer.setSeriesShape( 2, new Rectangle2D.Double( -1.0, -1.0, 2.0, 2.0 ) );
        //renderer.setSeriesPaint(2, Color.BLUE);
        plot.setRenderer(renderer);

      /*  float alpha = (float)0.4;

        final XYDifferenceRenderer renderer = new XYDifferenceRenderer(
            Color.blue, new Color((float)0.0,(float)0.0,(float)0.0, alpha), false
        );
        renderer.setStroke(new BasicStroke(2.0f));
        renderer.setSeriesPaint(2, Color.yellow);
        renderer.setSeriesPaint(3, Color.red);
        renderer.setSeriesShape( 0, new Rectangle2D.Double( -1.0, -1.0, 2.0, 2.0 ) );
        renderer.setSeriesShape( 1, new Rectangle2D.Double( -1.0, -1.0, 2.0, 2.0 ) );
        renderer.setSeriesShape( 2, new Rectangle2D.Double( -1.0, -1.0, 2.0, 2.0 ) );
        renderer.setSeriesPaint(2, Color.BLUE);
        //final XYPlot plot = chart.getXYPlot();
        plot.setRenderer(renderer);
        */
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        DateFormat dateFormat = new SimpleDateFormat("MMM dd EEE HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        axis.setDateFormatOverride(dateFormat);
                
        return chart;
        
    }

       
       public static void main(String[] args) 
       {

        System.setProperty("java.awt.headless","true");

        final ChartMaker demo;

        if(args.length == 7)
        {

            demo = new ChartMaker("Scripps Pier pH", args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            demo.execute();

        }
        else
        {
            System.err.println("There are not enough command line arguments");
            System.exit(1);
        }

      }
} 








