package valmanway;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import textblock.TextBlock;
import thunderbrand.Thunderbrand;

public class ValmanwayLogger {
	
	private static final DateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	private static final DateFormat dayFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private static final File logDirectory = new File((Thunderbrand.isLinuxBuild() ? "./" : "C:/") + "CrissaegrimLogs/");
	private String currentDay;
	private File logFile;
	private BufferedWriter logWriter;
	
	public ValmanwayLogger() {
		currentDay = dayFormatter.format(new Date());
		logFile = new File(logDirectory, currentDay + "-log.txt");
		try {
			logDirectory.mkdirs();
			logFile.createNewFile(); // Only creates if non-existent
			FileWriter fileWriter = new FileWriter(logFile, true);
			logWriter = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void close() {
		try {
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void log(TextBlock tb) {
		log(tb.getMessage());
	}
	
	public synchronized void log(String message) {
		makeNewFileIfNecessary();
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
	
	private synchronized void makeNewFileIfNecessary() {
		String today = dayFormatter.format(new Date());
		if (!currentDay.equals(today)) {
			close();
			currentDay = today;
			logFile = new File(logDirectory, currentDay + "-log.txt");
			try {
				logDirectory.mkdirs();
				logFile.createNewFile(); // Only creates if non-existent
				FileWriter fileWriter = new FileWriter(logFile, true);
				logWriter = new BufferedWriter(fileWriter);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
