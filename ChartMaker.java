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


public class ChartMaker extends ApplicationFrame {

  private static final long serialVersionUID = 1L;

    private String url;
    private String clientName;
    private String channelName;
    private String duration;
    private String chartName;
    private String chartType;

    public ChartMaker()
    {
        super("Scripps Pier pH Chart");
        url = "localhost:3333";
        clientName = "SinkClient";
        channelName = "HelloWorld/IMM/pHEST";
        duration = "900";
        chartName = "Scripps Pier 15 Minutes Chart";
        chartType = "l";

    }

    public ChartMaker(String chartName, String duration, String url, String clientName, String channelName, String chartType) {
        super("Scripps Pier pH Chart");
        this.url = url;
        this.clientName = clientName;
        this.channelName = channelName;
        this.duration = duration;
        this.chartName = chartName;
        this.chartName = this.chartName + " " + getLastWeek() + " - " + getCurrentDateAs("MM/dd/yy"); 
        this.chartType = chartType;

    }

    public void execute()
    {
        DTManager dt = new DTManager("0", duration, url, clientName, channelName);

        dt.execute();

        int[] data = dt.getDataArray();
        double[] times = dt.getTimes();

        final XYDataset dataset = createDataset(data, times);
        final JFreeChart chart = createChartObj(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

        DateFormat dateFormat = new SimpleDateFormat("EEE dd HH:mm");
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));

        //saveChart(chart);

    }
    
    private void saveChart(JFreeChart chart)
    {

        String date = getCurrentDateAs("yyyyMMddHHmm");

        try 
        {
            ChartUtilities.saveChartAsJPEG(new File("/Users/mingcn/Dropbox/Public/SIOCharts/TestChart" + date + ".jpg"), chart, 500, 270);
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println("Problem occurred creating chart.");
        }
    }

    public String getCurrentDateAs(String format)
    {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public String getLastWeek()
    {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -7);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        return dateFormat.format(c.getTime());
    }
    
/**
     * Creates a sample dataset 
     */

    private XYDataset createDataset(int[] array, double[] times) {

        TimeSeries series1 = new TimeSeries("pH");
        TimeSeries series2 = new TimeSeries("Historical pH");
        TimeSeries series3 = new TimeSeries("Predicted pH");
        TimeSeries series4 = new TimeSeries("Predicted pH two");
 
        for(int i = 0; i < array.length; i++)
        {
            series1.add(new Millisecond(new Time( (long)times[i]*(long)1000 )), 8);
        }

        for(int i = 0; i < array.length; i++)
        {
            series2.add(new Millisecond(new Time( (long)times[i]*(long)1000 )), 8.16);
        }

        for(int i = 0; i < array.length; i++)
        {
            series3.add(new Millisecond(new Time( (long)times[i]*(long)1000 )), 7.6);
        }

        for(int i = 0; i < array.length; i++)
        {
            series4.add(new Millisecond(new Time( (long)times[i]*(long)1000 )), 7.8);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
         dataset.addSeries(series3);
        dataset.addSeries(series4);
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
        //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
       /* final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesShape( 0, new Rectangle2D.Double( -1.0, -1.0, 2.0, 2.0 ) );
        renderer.setSeriesShape( 1, new Rectangle2D.Double( -1.0, -1.0, 2.0, 2.0 ) );
        renderer.setSeriesShape( 2, new Rectangle2D.Double( -1.0, -1.0, 2.0, 2.0 ) );
        renderer.setSeriesPaint(2, Color.BLUE);
        plot.setRenderer(renderer);*/

        float alpha = (float)0.4;

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

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("EEE dd HH:mm"));

       /* final Color c = new Color(255, 60, 24, 63);
        final Marker bst = new IntervalMarker(
            new Day(28, 3, 2004).getFirstMillisecond(), new Day(30, 10, 2004).getFirstMillisecond(),
            c, new BasicStroke(2.0f), null, null, 1.0f
        );*/

                
        return chart;
        
    }

       
       public static void main(String[] args) 
       {

        final ChartMaker demo;

        if(args.length == 5)
        {
            if(args[4].equals("l"))
            {
                demo = new ChartMaker("Scripps Pier pH", args[0], args[1], args[2], args[3], args[4]);
                demo.execute();
                demo.pack();
                RefineryUtilities.centerFrameOnScreen(demo);
                demo.setVisible(true);
            }
            else if(args[4].equals("w"))
            {
                demo = new ChartMaker("Scripps Pier pH", args[0], args[1], args[2], args[3], args[4]);
                demo.execute();
                demo.pack();
                RefineryUtilities.centerFrameOnScreen(demo);
                demo.setVisible(true);
            }
            else
            {
                System.err.println("Place a w or l at the end of command line to specify weekly or live");
                System.exit(1);
            }
        }
        else
        {
            System.err.println("There are not enough command line arguments");
            System.exit(1);
        }

      }
} 








