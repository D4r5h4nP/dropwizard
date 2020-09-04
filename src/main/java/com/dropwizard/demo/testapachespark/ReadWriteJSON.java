package com.dropwizard.demo.testapachespark;

import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

public class ReadWriteJSON {
	
	public static void main(String args[]) {

		SparkConf sparkConf = new SparkConf().setAppName("JavSparkSqlDemo").setMaster("local");
		
		SparkSession sparkSession = SparkSession.builder().config(sparkConf)
	//			.config("spark.sql.catalogImplementation","hive")
	//			.enableHiveSupport()
				.getOrCreate();
		
		//Read JSON File
		Dataset<Row> employeeDFJSON = 
				sparkSession.read().json("src/JsonOutput/part-00000-24b893da-1d55-49d6-8545-ace475cd231c-c000.json");
		employeeDFJSON.show();
		
		Properties connection = new Properties();
		connection.put("driver",  "org.h2.Driver");
		connection.put("user", "sa");
		connection.put("password", ""); 
	
		Dataset<Row> updatedEmpDF = sparkSession.read().jdbc("jdbc:h2:tcp://localhost/~/dwcruddemo", "employee", connection);
		updatedEmpDF.write().mode(SaveMode.Append).option("filename", "updatedEmployee.json").json("src/JsonOutput/");
		
	}
	
}
