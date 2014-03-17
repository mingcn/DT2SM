ChartMaker.class: ChartMaker.java
	javac -cp .:jcommon-1.0.22.jar:jfreechart-1.0.17.jar:rbnb.jar ChartMaker.java


dt:
	javac -cp .:rbnb.jar DTManager.java

run:
	java -cp .:jcommon-1.0.22.jar:jfreechart-1.0.17.jar:rbnb.jar ChartMaker
