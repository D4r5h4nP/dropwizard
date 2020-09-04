package com.dropwizard.demo.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;



@NamedQueries({
	@NamedQuery(
			name = "getAllEmployee",
			query = "from Employee"),
	@NamedQuery(
			name = "findEmployeeByCredentials",
			query = "from Employee where username = :username and password = :password"),
	@NamedQuery(
			name = "findEmployeeByUsername",
			query = "from Employee where username = :username")
//	@NamedQuery(
//			name = "creatEmployee",
//			query = "insert into Employee('name', 'age', 'email') values (:name, :age, :email)"),
//	@NamedQuery(
//			name = "updateEmployee",
//			query = "update Employee List name = :name, age = :age, email = :email where id = :id"),
//	@NamedQuery(
//			name = "findEmployeeById",
//			query = "from Employee where id = :id"
//			),
//	@NamedQuery(
//			name = "deleteEmployee",
//			query = "delete from Employee where id =:id")
})

@Entity
@Table(name = "Employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank @Length(min=2, max=255)
    private String name;
    @NotNull
	private int age;
	@Pattern(regexp=".+@.+\\.[a-z]+")
	private String email;
	
	@NotBlank @Length(min=5, max=20)
	private String username;
	@NotBlank @Length(min=5, max=20)
	private String password;
	
	private boolean isActive;
	
//	@NotNull
//	@Column
//	@ElementCollection
//	private Set<String> role;
//	
//	public Employee() {}
//
//	
//	public Employee(int id, String name, int age, String email, String username, String password){
//		this.id = id;
//		this.name = name;
//		this.age = age;
//		this.email = email;
//		this.username = username;
//		this.password = password;
//	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
//	public Set<String> getRole() {
//		return role;
//	}
//	public void setRole(Set<String> role) {
//		this.role = role;
//	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	
	
}
