package com.dropwizard.demo.testapachespark;

import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

public class ReadWriteCSV {

	public static void main(String[] args) {

		//	System.setProperty("hadoop.home.dir", "c:\\winutils\\");
		
		SparkConf sparkConf = new SparkConf().setAppName("JavSparkSqlDemo").setMaster("local");
		
		SparkSession sparkSession = SparkSession.builder().config(sparkConf)
		//		.config("spark.sql.catalogImplementation","hive")
		//		.enableHiveSupport()
				.getOrCreate();
		
		StructType employeeSchema = new StructType()
		//		.add("id", "int")
				.add("name", "string")
				.add("age", "int")
				.add("email", "string")
				.add("username", "string")
				.add("password", "string")
				.add("isActive", "string");
		
		
		//// Read CSV File
		Dataset<Row> employeeDFCSV = 
				sparkSession.read().
				option("header", "true")
				.schema(employeeSchema)
				.option("mode", "DROPMALFORMED").
				csv("userdata.csv");
		
		employeeDFCSV.show();
		
		Properties connection = new Properties();
		connection.put("driver",  "org.h2.Driver");
		connection.put("user", "sa");
		connection.put("password", ""); 
	
		Dataset<Row> updatedEmpDF = sparkSession.read().jdbc("jdbc:h2:tcp://localhost/~/dwcruddemo", "employee", connection);
		updatedEmpDF.write().mode(SaveMode.Append).option("filename", "updatedEmployee.csv").csv("src/CsvOutput/");
	}
}
