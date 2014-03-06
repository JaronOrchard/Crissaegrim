package valmanway;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import thunderbrand.TextBlock;

public class ValmanwayLogger {
	
	private static final DateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	private static final File logDirectory = new File("C:/CrissaegrimChunks/logs/");
	private final File logFile;
	private BufferedWriter logWriter;
	
	public ValmanwayLogger() {
		DateFormat dayFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String logFileName = dayFormatter.format(new Date()) + "-log.txt";
		logFile = new File(logDirectory, logFileName);
		try {
			logDirectory.mkdirs();
			logFile.createNewFile(); // Only creates if non-existent
			FileWriter fileWriter = new FileWriter(logFile, true);
			logWriter = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void log(TextBlock tb) {
		log(tb.getMessage());
	}
	
	public void log(String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(timeFormatter.format(new Date()));
		sb.append("] ");
		sb.append(message);
		try {
			logWriter.write(sb.toString());
			logWriter.newLine();
			logWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
