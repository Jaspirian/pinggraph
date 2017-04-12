import java.awt.Color;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class graph {
	int lastColor;
	double[] pingArr = {20,50,100,200};
	colors[] colorArr = {new colors(255,18,13,0), new colors(0,168,255,0), new colors(0,168,107,0), new colors(255,255,133,0), new colors(255,184,133,0), new colors(255,125,115,0)};
	XYSeries data;
	ChartPanel panel;
	JFreeChart chart;
	double trans = 0;
	public ArrayList<log> log = new ArrayList<log>();
	
	/**
	 * Initialize graph
	 */
	public graph(XYSeries data) {
		this.data = data;
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(data);
		
		chart = ChartFactory.createXYLineChart(
				 "Ping", // Title
				 "", // x-axis Label
				 "ms", // y-axis Label
				 dataset, // Dataset
				 PlotOrientation.VERTICAL, // Plot Orientation
				 false, // Show Legend
				 false, // Use tooltips
				 false // Configure chart to generate URLs?
				);
		
		chart.getXYPlot().setRangeGridlinesVisible(false);
		chart.getXYPlot().setDomainGridlinesVisible(false);
		
		chart.getXYPlot().getRenderer().setSeriesPaint(0,Color.BLACK);
		chart.getXYPlot().setBackgroundPaint(Color.WHITE);
		ValueAxis xAxis = chart.getXYPlot().getDomainAxis();
		xAxis.setRange(0,40);
		xAxis.setVisible(false);
		
		panel = new ChartPanel(chart, 200000, 200000, 0, 0, 20000, 20000, false, true, true, true, true, true);
	}
	
	/**
	 * Return ChartPanel
	 */
	public ChartPanel getPane() {
		return panel;
	}
	
	/**
	 * Pinging task, and adds date/time to log
	 */
	public Integer ping(String address) {
		Integer time = null;
		try {
		      InetAddress inet = InetAddress.getByName(address);
		 
		      long finish = 0;
		      long start = new GregorianCalendar().getTimeInMillis();
		 
		      if (inet.isReachable(5000)){
		        finish = new GregorianCalendar().getTimeInMillis();
		        time = (int)(long) (finish - start);
		      } else {
		      }
		    } catch ( Exception e ) {
		    }
		
		String str = "ping failed";
		if(time != null) str = time.toString();
		log.add(new log(new Date(), str, address));
		
		return time;
	}
	
	/**
	 * Add value to chart and knock off earliest
	 */
	public void update(Integer time) {
		data.add(data.getMaxX()+1,time);
		
		color(time);
		
		if(data.getItemCount() >= 40) {
			ValueAxis xAxis = chart.getXYPlot().getDomainAxis();
			xAxis.setRange(data.getMaxX()-40,data.getMaxX());
		}
	}
	
	/**
	 * Handle background colors for the chart
	 */
	public void color(Integer time) {
		int mostPings = 0;
		for(int i=0; i<colorArr.length; i++) {
			colorArr[i].alpha -= 20;
			
			if(colorArr[i].alpha > colorArr[mostPings].alpha) mostPings = i;
			
			if(colorArr[i].alpha > 255) colorArr[i].alpha = 255;
			if(colorArr[i].alpha < 0) colorArr[i].alpha = 0;
		}
		
		if(time==null) {
			colorArr[0].alpha += 255;
		} else if(time<pingArr[0]) {
			colorArr[1].alpha += 40;
		} else if(time<pingArr[1]) {
			colorArr[2].alpha += 40;
		} else if(time<pingArr[2]) {
			colorArr[3].alpha += 40;
		} else if(time<pingArr[3]) {
			colorArr[4].alpha += 40;
		} else if(time>=pingArr[3]) {
			colorArr[5].alpha += 40;
		}
		
		if(colorArr[mostPings].alpha > 255) colorArr[mostPings].alpha = 255;
		if(colorArr[mostPings].alpha < 0) colorArr[mostPings].alpha = 0;
		
		chart.getXYPlot().setBackgroundPaint((new Color(colorArr[mostPings].r,colorArr[mostPings].g,colorArr[mostPings].b,colorArr[mostPings].alpha)));
	}
}
