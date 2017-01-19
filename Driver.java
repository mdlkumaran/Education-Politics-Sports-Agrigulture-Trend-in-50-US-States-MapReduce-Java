package com.Kumaran.mapR;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Driver {

	public static void main(String[] args) throws Exception{
		if(args.length !=2){
			System.err.println("Invalid Command");
			System.err.println("specify io path correctly");
			System.exit(0);
		}
		String oPath = args[1];
		System.out.println(args[1]);
		System.out.println(oPath);
		Path outPath = new Path(oPath);
		Configuration conf = new Configuration();
		Job job = new Job(conf, "Driver");
		job.setJarByClass(Driver.class);
		
		job.setNumReduceTasks(1);
		
		job.setMapperClass(FirstMapper.class);
		job.setReducerClass(FirstReducer.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, outPath);
		
		System.exit(job.waitForCompletion(true)?0:1);
	}
}