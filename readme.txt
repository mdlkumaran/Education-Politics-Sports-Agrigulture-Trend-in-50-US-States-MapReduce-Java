In putty, add the .pem file for opening the clusters.

Details of the clusters:

Manangatti Dharman Lekha-HadoopNameNode          ec2-52-11-45-149.us-west-2.compute.amazonaws.com  10.0.0.229
Manangatti Dharman Lekha-HadoopSecondaryNameNode ec2-52-41-84-113.us-west-2.compute.amazonaws.com  10.0.0.231
Manangatti Dharman Lekha-HadoopDataNode          ec2-52-41-101-119.us-west-2.compute.amazonaws.com 10.0.0.230
Manangatti Dharman Lekha-HadoopSecondDataNode    ec2-52-37-203-72.us-west-2.compute.amazonaws.com  10.0.0.179

In the unix terminal,

issue the following commands in Home Directory,
 
eval 'ssh-agent'
ssh-add HadoopEC2Cluster.pem
hdfs dfs -rm -r -f /test/out*
hadoop jar mapR-0.0.1-SNAPSHOT.jar com.Kumaran.mapR.Driver /test/states/ /test/output

To view the output:

hdfs dfs -copyToLocal /test/output/part-r-00000 /home/ubuntu/
vi part-r-00000