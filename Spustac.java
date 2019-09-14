import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Spustac {
	
	public static  Map<String, Integer> map = new HashMap<>();
	
	public static String[] getArrayFromFile(File subor) {
		String[] pole = null;
		Scanner scannerCount = null;
		Scanner scannerFile = null;
		try {
			scannerCount = new Scanner(subor);

			int i = 0;
			int pocet = 0;
			while (scannerCount.hasNext()) {
				pocet++;
				scannerCount.next();
			}
			pole = new String[pocet];
			scannerFile = new Scanner(subor);
			while (scannerFile.hasNext()) {
				pole[i] = scannerFile.next();
				i++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("Spracovanie prerušené");
			System.exit(1);
			;
		} finally {
			if (scannerFile != null)
				scannerFile.close();
			if (scannerCount != null)
				scannerCount.close();
		}
		return pole;
	}

	public static String get_filepath_of_file() {
		FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
		dialog.setMode(FileDialog.LOAD);
		dialog.setVisible(true);
		String filepath = dialog.getDirectory();
		if (filepath != null) {
			filepath = filepath + dialog.getFile();
		}
		return filepath;

	}

	public static void write_to_console(Map<String, Integer> mapOfData) {
		
		System.out.println("------------Nový výpis--------------");
		for (String keyName : mapOfData.keySet()) {
			String key = keyName.toString();
			String value = mapOfData.get(keyName).toString();
			if (value.equals("0")) {
				continue;
			}
			System.out.println(key + " " + value);
		}
	}

	public static Map<String, Integer> convert_array_to_map(String[] array) {
		Map<String, Integer> convertedMap = new HashMap<>();
		for (int i = 0; i < array.length; i++) {

			if (i != array.length - 1 && i % 2 == 0) {

				if (convertedMap.containsKey(array[i])) {
					convertedMap.put(array[i], convertedMap.get(array[i])
							+ Integer.valueOf(array[i + 1]));
				} else {
					convertedMap.put(array[i], Integer.valueOf(array[i + 1]));
				}
			}
		}
		return convertedMap;
	}
	
	public static void thread_of_listing() {
		while(true){
			try {
				Thread.sleep(60000);
				write_to_console(map);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void nacitajDataZoSuboru() {
		String path = get_filepath_of_file();
		if ( !( path == null )) {
			File file = new File(path);
			if (file != null) {
				String[] arrayOfData = getArrayFromFile(file);
				map = convert_array_to_map(arrayOfData);
				write_to_console(map);		
			}
		}
		
	}
	
	public static void spustiVlaknoVypisu() {
		ExecutorService service = Executors.newFixedThreadPool(2);
	    service.submit(new Runnable() {
	        public void run() {
	            thread_of_listing();
	        }
	    });
	}
	
	public static void citajDataZKonzoly() {
		
		boolean isKey = true;
		String actKey = null;
		String actValue = "";
		Scanner scannerConsole = new Scanner(System.in);
		while (true) {
			if (scannerConsole.hasNext()) {
				actValue = scannerConsole.next();
			}
			
			if (actValue.equals("quit")) {
				System.exit(1);
			}
			
			if (isKey == true) {
				actKey = actValue;
				if (actKey.length() != 3) {
					System.out.println("Program ukonèený! Zadali ste menu v nesprávnom formáte!");
					System.exit(1);
				}
				isKey = false;
			} else {
				try {
					if (map.containsKey(actKey)) {
						
						map.put(actKey, map.get(actKey) + Integer.valueOf(actValue));
					} else {
						map.put(actKey, Integer.valueOf(actValue));
					}
					isKey = true;
				} catch (NumberFormatException e) {
					System.out.println("Program ukonèený! Zadali ste èiastku v chybnom formáte!");
					System.exit(1);
				}
				
			}

		}
		
	}

	public static void main(String[] args) {
		
		nacitajDataZoSuboru();
		
		spustiVlaknoVypisu( );
		
		citajDataZKonzoly( );
		
	}


}
