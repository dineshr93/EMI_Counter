package com.din.sw;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

//Sample data to check
// 2015-12 to 2029-12
//169 Total
//47 completed
//122 Pending

//2019-09 to 2022-09
/*Starts     2019-SEPTEMBER
Until      2022-SEPTEMBER
Total EMIs 37
Completed  1
Current    1
Pending    35*/

public class EMIComputer {

	public static void main(String[] args) throws ParseException, IOException { 

		String name = "Dinesh";
		Purpose purpose = Purpose.Insurance;
		YearMonth current = YearMonth.now(ZoneId.of(ZoneOffset.systemDefault().toString()));
		YearMonth start = YearMonth.parse("2019-09");
		YearMonth end = YearMonth.parse("2022-09");


		LinkedHashMap<YearMonth,Integer> map = new LinkedHashMap<>();
		ArrayList<YearMonth> completedList = new ArrayList<YearMonth>();
		ArrayList<YearMonth> pendingList = new ArrayList<YearMonth>();

		YearMonth ym = start ;
		int count = 0;
		while(!ym.isAfter(end)) {
			map.put( ym, ++count );
			ym = ym.plusMonths( 1 ); 
		}

		int totalEMIs = map.size();
		int completed = (map.get(current)-1); 
		
		/*for(YearMonth ymentries : map.keySet()) {
			if(current.)
		}*/
		int currentEMI = 0;
		if(map.get(current)!=null) {
			currentEMI = 1;
		}
		
		//int current = 1;
		int pending = totalEMIs - completed -currentEMI ;

		//Calculation start --------------------------------------------
		//System.out.println("Completed Months");
		ym = start;
		count = 0;
		while(!ym.isAfter(current.minusMonths(1))) {
			completedList.add(ym);
			ym = ym.plusMonths( 1 ); 
		}

		//System.out.println("Pending Months");
		ym = current.plusMonths(1);
		count = 0;
		while(!ym.isAfter(end)) {
			pendingList.add(ym);
			ym = ym.plusMonths( 1 ); 
		}
		//Calculation end----------------------------------------------
		
		PrintStream fileOut = new PrintStream(name+"_"+purpose+"_EMI"+".txt");
		System.setOut(fileOut);
		File file = new File(name+"_"+purpose+"_EMI"+".txt");


		System.out.println(name+"_"+purpose+"_"+"EMI_Report");
		System.out.println("======================");
		System.out.println("Starts     "+ start.getYear()+"-"+start.getMonth());
		System.out.println("Until      "+ end.getYear()+"-"+end.getMonth());
		System.out.println("Total EMIs "+totalEMIs);
		System.out.println("Completed  "+ completed );
		System.out.println("Current    "+1);
		System.out.println("Pending    "+pending);
		System.out.println();
		System.out.println("======================");
		System.out.println("Completed Months");
		System.out.println("======================");
		printList(completedList);
		System.out.println();
		System.out.println("======================");
		System.out.println("Current Month");
		System.out.println("======================");
		System.out.println(currentEMI+" "+ current.getYear()+"-"+current.getMonth());
		System.out.println();
		System.out.println("======================");
		System.out.println("Pending Months");
		System.out.println("======================");
		printList(pendingList);

		
		//Object for json
		EMIData data = new EMIData();
		data.setName(name);
		data.setPurpose(purpose);
		data.setStart(start);
		data.setEnd(end);
		data.setTotalEMIs(totalEMIs);
		data.setCompleted(completed);
		data.setCurrentEMI(currentEMI);
		data.setPending(pending);
		data.setMap(map);
		data.setCompletedList(completedList);
		data.setPendingList(pendingList);
		
		
		
		//To read and write the data
		File jsonFile = new File(name+"_"+purpose+"_EMI"+".json");
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		try {
			String json = mapper.writeValueAsString(data);
			//System.out.println("ResultingJSONstring = " + json);
			//JSON from file to Object
			//EMIData eMIData = mapper.readValue(new File("c:\\user.json"), EMIData.class);

			//JSON from String to Object
			EMIData eMIData = mapper.readValue(json, EMIData.class);
			
			FileUtils.writeStringToFile(jsonFile, json, "ISO-8859-1");
			//System.out.println(eMIData.getStart());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}
		
		//To open the files
		Desktop desktop = Desktop.getDesktop();
        if(file.exists()) desktop.open(file);
        if(jsonFile.exists()) desktop.open(jsonFile);
        
	}
	private static void printList(List<YearMonth> myList) {
		int count = 0;
		for (YearMonth string : myList) {
			System.out.println(++count + " "+ string.getYear()+"-"+string.getMonth());
		}
	}
}
enum Purpose{
	Car,
	Home,
	Mobile,
	Personal,
	Insurance,
	Pension
}
class EMIData {
	String name;
	Purpose purpose;
	int totalEMIs;
	int completed; 
	int currentEMI;
	int pending;
	YearMonth start;
	YearMonth end;
	LinkedHashMap<YearMonth,Integer> map;
	ArrayList<YearMonth> completedList;
	ArrayList<YearMonth> pendingList;
	
	
	public int getCurrentEMI() {
		return currentEMI;
	}
	public void setCurrentEMI(int currentEMI) {
		this.currentEMI = currentEMI;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Purpose getPurpose() {
		return purpose;
	}
	public void setPurpose(Purpose purpose) {
		this.purpose = purpose;
	}
	public int getTotalEMIs() {
		return totalEMIs;
	}
	public void setTotalEMIs(int totalEMIs) {
		this.totalEMIs = totalEMIs;
	}
	public int getCompleted() {
		return completed;
	}
	public void setCompleted(int completed) {
		this.completed = completed;
	}
	public int getPending() {
		return pending;
	}
	public void setPending(int pending) {
		this.pending = pending;
	}
	public YearMonth getStart() {
		return start;
	}
	public void setStart(YearMonth start) {
		this.start = start;
	}
	public YearMonth getEnd() {
		return end;
	}
	public void setEnd(YearMonth end) {
		this.end = end;
	}
	public LinkedHashMap<YearMonth, Integer> getMap() {
		return map;
	}
	public void setMap(LinkedHashMap<YearMonth, Integer> map) {
		this.map = map;
	}
	public ArrayList<YearMonth> getCompletedList() {
		return completedList;
	}
	public void setCompletedList(ArrayList<YearMonth> completedList) {
		this.completedList = completedList;
	}
	public ArrayList<YearMonth> getPendingList() {
		return pendingList;
	}
	public void setPendingList(ArrayList<YearMonth> pendingList) {
		this.pendingList = pendingList;
	}

}
