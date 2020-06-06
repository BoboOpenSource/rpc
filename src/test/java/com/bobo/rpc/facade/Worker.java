package com.bobo.rpc.facade;

import java.io.Serializable;

public class Worker implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String name;

	private String dept;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Worker(Integer id, String name, String dept) {
		this.id = id;
		this.name = name;
		this.dept = dept;
	}

	@Override
	public String toString() {
		return "Worker [id=" + id + ", name=" + name + ", dept=" + dept + "]";
	}

}
