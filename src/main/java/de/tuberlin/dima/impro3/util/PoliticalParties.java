package de.tuberlin.dima.impro3.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PoliticalParties {
	
	private static List<String> parties = null;
	
	public static List<String> getParties() {
		if (parties == null) {
			Scanner scanner = null;
			try {
				scanner = new Scanner(new File(ResourceManager.getResourcePath("/parties/german_parties.txt")));
				
				parties = new ArrayList<String>();
				
				while (scanner.hasNext()) {
					String party = scanner.nextLine();
					parties.add(party);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				scanner.close();
			}
		}
		
		return parties;
	}
	
}
