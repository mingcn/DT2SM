ChartMaker.class: ChartMaker.java
	javac -cp .:jcommon-1.0.22.jar:jfreechart-1.0.17.jar:rbnb.jar ChartMaker.java


dt:
	javac -cp .:rbnb.jar DTManager.java

run:
	java -cp .:jcommon-1.0.22.jar:jfreechart-1.0.17.jar:rbnb.jar ChartMaker 0 1604800 newest 50.18.112.125:4444 SinkClient IMM/Est_pH_int /Users/mingcn/Dropbox/Public/SIOCharts/TestChart 
