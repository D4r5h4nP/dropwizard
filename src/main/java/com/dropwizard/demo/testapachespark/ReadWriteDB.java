package com.dropwizard.demo.testapachespark;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

public class ReadWriteDB {

	public static void main(String args[]) {
		
		SparkConf sparkConf = new SparkConf().setAppName("ReadWriteDB").setMaster("local");
		
		SparkSession sparkSession = SparkSession.builder().config(sparkConf)
	//			.config("spark.sql.catalogImplementation","hive")
	//			.enableHiveSupport()
				.getOrCreate();
		StructType employeeSchema = new StructType()
//				.add("id", "int")
				.add("name", "string")
				.add("age", "int")
				.add("email", "string")
				.add("username", "string")
				.add("password", "string")
				.add("isActive", "string");
		
		//Read JSON File
//		Dataset<Row> employeeDFJSON = 
//				sparkSession.read().
//				format("json").load("src/JsonOutput/part-00000-24b893da-1d55-49d6-8545-ace475cd231c-c000.json");
//		employeeDFJSON.printSchema();  // returns structure of table
		
//		employeeDFJSON.show();
//		
//		employeeDFJSON.write()
//		  .format("jdbc")
//		  .mode(SaveMode.Append)
//		  .option("url", "jdbc:h2:tcp://localhost/~/dwcruddemo")
//		  .option("dbtable", "employee")
//		  .option("user", "sa")
//		  .option("password", "")
//		  .save();
		
//		Dataset<Row> employeeDFCSV = 
//				sparkSession.read().
//				option("header", "true")
//				.schema(employeeSchema)
//				.option("mode", "DROPMALFORMED").
//				csv("userdata.csv");
//	
//		employeeDFCSV.write()
//		  .format("jdbc")
//		  .mode(SaveMode.Append)
//		  .option("url", "jdbc:h2:tcp://localhost/~/dwcruddemo")
//		  .option("dbtable", "employee")
//		  .option("user", "sa")
//		  .option("password", "")
//		  .save();
		
	}
}
