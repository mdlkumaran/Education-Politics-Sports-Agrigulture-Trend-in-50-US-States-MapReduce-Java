package com.Kumaran.mapR;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.ArrayList;

public class FirstReducer extends Reducer<Text,IntWritable, Text, Text>{
	Path path;
	TreeMap<String, Integer> tFullMap = new TreeMap<String, Integer>(); 
	TreeMap<String, Integer> tdomMap = new TreeMap<String, Integer>(); 
	TreeMap<Integer, String> tFullRevMap = new TreeMap<Integer, String>(Collections.reverseOrder()); 
	List<HashMap<String, ArrayList<String>>> hList = new ArrayList<HashMap<String, ArrayList<String>>>();
	HashMap<ArrayList<String>, ArrayList<String>> hMapSeq = new HashMap<ArrayList<String>, ArrayList<String>>();
	@Override
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException,InterruptedException
	{
		int count =0;
		for(IntWritable value:values)
		{
			count +=value.get();
		}
		
		tFullMap.put(key.toString(), count);
//		context.write(key, new IntWritable(count));
	}

	@Override
	protected void cleanup(Context context)
			throws IOException, InterruptedException {

		String [] arr;
		String statesFirst = "";
		String fieldFirst = "";
		String statesNext = "";
		String fieldNext = "";
		String education = "Education";
		String politics = "Politics";
		String sports = "Sports";
		String agriculture = "Agriculture";
		String states = "";
		String field = "";
		String state = "";
		int educationCount = 0;
		int politicsCount = 0;
		int sportsCount = 0;
		int agricultureCount = 0;
		int i = 0;
		int k=0;
		int l=0;
		String hMapState = "";
		ArrayList<String> arrListHmap = new ArrayList<String>();
		String strListHMap = "";
		ArrayList<String> arrListHmapSeq = new ArrayList<String>();
		ArrayList<String> hMapSeqState = new ArrayList<String>();
		String strListHMapSeq = "";
		ArrayList<String> arrList = new ArrayList<String>();
		ArrayList<String> arrListInt = new ArrayList<String>();
		ArrayList<String> stateListInt = new ArrayList<String>();
		HashMap<String, ArrayList<String>> hMap = new HashMap<String, ArrayList<String>>();
		

		for(Map.Entry<String, Integer> entry : tFullMap.entrySet())
		{
			String Key = entry.getKey();
			int value	= entry.getValue();
			arr = Key.split(":");
			statesFirst = arr[0];
			fieldFirst =statesFirst+"::"+arr[1];
			field = arr[1];
			tFullRevMap.put(value,fieldFirst);
			break;
		}

		for (Map.Entry<String, Integer> entry : tFullMap.entrySet()) {
			if(i==0)
			{
				i++;
				continue;
			}

			String Key = entry.getKey();
			int value	= entry.getValue();
			arr = Key.split(":");
			statesNext = arr[0];
			fieldNext =arr[1];
			states = arr[0];
			field = arr[1];

			if(statesFirst.equalsIgnoreCase(statesNext))
			{
				fieldNext = statesNext +"::"+ fieldNext;
				tFullRevMap.put(value,fieldNext);
			}
			else if(!statesFirst.equalsIgnoreCase(statesNext))
			{
				for (Map.Entry<Integer, String> word : tFullRevMap.entrySet()) {
					if(word.getValue().toLowerCase().contains(education.toLowerCase())){
						educationCount++;
					}
					else if(word.getValue().toLowerCase().contains(politics.toLowerCase())){
						politicsCount++;
					}
					else if(word.getValue().toLowerCase().contains(sports.toLowerCase())){
						sportsCount++;
					}
					else if(word.getValue().toLowerCase().contains(agriculture.toLowerCase())){
						agricultureCount++;
					}
					break;
				}
			
				for(Map.Entry<Integer, String> entry1 : tFullRevMap.entrySet())
				{
					int Key1 = entry1.getKey();
					String value1	= entry1.getValue();
					arr = value1.split("::");
					states = arr[0];
					field = arr[1];
					arrList.add(field);
				}
				hMap.put(states, arrList);
				arrList = new ArrayList<String>();
				tFullRevMap.clear();

				fieldNext = statesNext +"::"+ fieldNext;

				tFullRevMap.put(value,fieldNext);
			}
			statesFirst = statesNext;

		}

		for (Map.Entry<Integer, String> word : tFullRevMap.entrySet()) {

			for(Map.Entry<Integer, String> entry1 : tFullRevMap.entrySet())
			{
				int Key1 = entry1.getKey();
				String value1	= entry1.getValue();
				arr = value1.split("::");
				states = arr[0];
				field = arr[1];
				arrList.add(field);
			}

			if(word.getValue().toLowerCase().contains(education.toLowerCase())){
				educationCount++;
			}
			else if(word.getValue().toLowerCase().contains(politics.toLowerCase())){
				politicsCount++;
			}
			else if(word.getValue().toLowerCase().contains(sports.toLowerCase())){
				sportsCount++;
			}
			else if(word.getValue().toLowerCase().contains(agriculture.toLowerCase())){
				agricultureCount++;
			}
			break;
		}
		hMap.put(states, arrList);

		tdomMap.put(education, educationCount);
		tdomMap.put(politics, politicsCount);
		tdomMap.put(sports, sportsCount);
		tdomMap.put(agriculture, agricultureCount);
		

		for (Map.Entry<String, ArrayList<String>> entry : hMap.entrySet()) {
			state = entry.getKey();
			arrListInt = entry.getValue();
			stateListInt.add(state);
			hMapSeq.put(arrListInt, stateListInt);
			break;
		}
		for (Map.Entry<String, ArrayList<String>> entry : hMap.entrySet()) {
			if(l==0)
			{
				l++;
				continue;
			}
			hMapState = entry.getKey();
			arrListHmap = entry.getValue();
			strListHMap = String.join(",", arrListHmap);
			for (Map.Entry<ArrayList<String>, ArrayList<String>> entry1 : hMapSeq.entrySet()) {
				arrListHmapSeq = entry1.getKey();
				hMapSeqState = entry1.getValue();
				strListHMapSeq = String.join(",", arrListHmapSeq);

				if(strListHMap.equals(strListHMapSeq))
				{
					k =1;
					break;
				}
				k=2;
			}
			if(k==1)
			{
				hMapSeqState = new ArrayList<String>();
				hMapSeqState=hMapSeq.get(arrListHmapSeq);
				hMapSeqState.add(hMapState);
				hMapSeq.put(arrListHmap, hMapSeqState);
			}
			else if(k==2)
			{
				hMapSeqState = new ArrayList<String>();
				hMapSeqState.add(hMapState);
				hMapSeq.put(arrListHmap, hMapSeqState);
			}
		}
		System.out.println(tdomMap);
		System.out.println(hMapSeq);
			
		for (Entry<String, Integer> result1 : tdomMap.entrySet()) {
			Text key = new Text(result1.getKey());
			Text value = new Text(result1.getValue().toString());
			context.write(key, value);
			
		}

		for (Entry<ArrayList<String>, ArrayList<String>> result2 : hMapSeq.entrySet()) {
			strListHMapSeq = String.join(",", result2.getKey());
			strListHMap = String.join(",", result2.getValue());
			Text key = new Text(strListHMapSeq);
			Text value = new Text(strListHMap);
			context.write(key, value);
		}
		
		super.cleanup(context);
	}
}