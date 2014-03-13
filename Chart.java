import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.data.xy.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import java.io.File;
import java.io.IOException;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import java.text.DateFormat;
import java.text.*;

public class Chart extends ApplicationFrame {

  private static final long serialVersionUID = 1L;

    public Chart(String applicationTitle, String chartTitle) {
        super(applicationTitle);

        DTManager dt = new DTManager();

        int[] data = dt.getDataArray();

        final XYDataset dataset = createDataset(data);
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

        saveChart(chart);

    }
    
    private void saveChart(JFreeChart chart)
    {
        try 
        {
            ChartUtilities.saveChartAsJPEG(new File("/Users/mingcn/Desktop/DT2SM/charts/testChart1.jpg"), chart, 500, 270);
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

    private XYDataset createDataset(int[] array) {
        
        final XYSeries series1 = new XYSeries("First");

        System.out.println("This is the size of the array passed to chart " + array.length);

        for(int i = 0; i < array.length; i++)
        {
            series1.add(i, array[i]);
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
                
        return dataset;
        
    }
    
/**
     * Creates a chart
     */

    private JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Scripps Pier pH Over Time",      // chart title
            "time",                      // x axis label
            "pH",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
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
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());


        DateAxis dateDomainAxis = new DateAxis();
        plot.setDomainAxis(dateDomainAxis);

        final DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        //domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        DateFormat formatter = new SimpleDateFormat("yyyyMMddd");
        //DateTickUnit unit = new DateTickUnit(DateTickUnit.YEAR, 1, formatter);
        domainAxis.setDateFormatOverride(formatter);
        //domainAxis.setRange(0.0,100);
        //domainAxis.setTickUnit(new NumberTickUnit(20));
        //domainAxis.setVerticalTickLabels(true);
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }

       public static void main(String[] args) {
        final Chart demo = new Chart("Scripps Pier", "Line Chart");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
      }
} 