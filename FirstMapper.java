package com.Kumaran.mapR;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class FirstMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
	
	
	
	@Override	
	public void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException
	{
		
		String line =value.toString();
		String education = "Education";
		String politics = "Politics";
		String sports = "Sports";
		String agriculture = "Agriculture";
		
		
		
		FileSplit fileSplit = (FileSplit)context.getInputSplit();
		String states = fileSplit.getPath().getName();
		
//		System.out.println("filename:"+filename);
		
		for(String word : line.split(" ")){
			if(word.toLowerCase().contains(education.toLowerCase())){
				context.write(new Text(states+":"+education), new IntWritable(1));
			}
			else if(word.toLowerCase().contains(politics.toLowerCase())){
				context.write(new Text(states+":"+politics), new IntWritable(1));
			}
			else if(word.toLowerCase().contains(sports.toLowerCase())){
				context.write(new Text(states+":"+sports), new IntWritable(1));
			}
			else if(word.toLowerCase().contains(agriculture.toLowerCase())){
				context.write(new Text(states+":"+agriculture), new IntWritable(1));
			}
		}
	}
}

