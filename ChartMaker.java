import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.data.xy.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import java.io.File;
import java.io.IOException;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.axis.DateAxis;
import java.text.*;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import java.sql.Time;
import org.jfree.data.time.Millisecond;

public class ChartMaker extends ApplicationFrame {

  private static final long serialVersionUID = 1L;

    public ChartMaker(String applicationTitle, String chartTitle) {
        super(applicationTitle);

        DTManager dt = new DTManager("0", "900", "localhost:3333", "SinkClient", "HelloWorld/IMM/pHEST");

        dt.execute();

        int[] data = dt.getDataArray();
        double[] times = dt.getTimes();

        final XYDataset dataset = createDataset(data, times);
        final JFreeChart chart = createChartObj(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

        saveChart(chart);

    }
    
    private void saveChart(JFreeChart chart)
    {
        try 
        {
            ChartUtilities.saveChartAsJPEG(new File("/Users/mingcn/Dropbox/Public/SIOCharts/TestChart3.jpg"), chart, 500, 270);
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println("Problem occurred creating chart.");
        }
    }
    
/**
     * Creates a sample dataset 
     */

    private XYDataset createDataset(int[] array, double[] times) {

        TimeSeries series1 = new TimeSeries("pH");
        TimeSeries series2 = new TimeSeries("Historical pH");
        TimeSeries series3 = new TimeSeries("Predicted pH");
 
        for(int i = 0; i < array.length; i++)
        {
            series1.add(new Millisecond(new Time( (long)times[i]*(long)1000 )), array[i]);
        }

        for(int i = 0; i < array.length; i++)
        {
            series2.add(new Millisecond(new Time( (long)times[i]*(long)1000 )), 8.16);
        }

        for(int i = 0; i < array.length; i++)
        {
            series3.add(new Millisecond(new Time( (long)times[i]*(long)1000 )), 7.7);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
                
        return dataset;
        
    }
    
/**
     * Creates a chart
     */

    private JFreeChart createChartObj(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Scripps Pier pH Over Time",      // chart title
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
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesShapesVisible(2, false);
        plot.setRenderer(renderer);

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy-dd HH:mm"));
                
        return chart;
        
    }

       
       public static void main(String[] args) {

        if(args.length == 5)
        {
            final ChartMaker demo = new ChartMaker("Scripps Pier", "Line Chart");
            demo.pack();
            RefineryUtilities.centerFrameOnScreen(demo);
            demo.setVisible(true);
        }
        else
        {
            System.err.println("There are not enough command line arguments");
            System.exit(1);
        }

      }
} 








