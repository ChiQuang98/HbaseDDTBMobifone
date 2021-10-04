package com.mobifone.bigdata.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int department_id;

	private int role_id;

	private String username;

	private String password;

	private String fullname;

	private String email;

	private String phone;

	private int login_type;

	private int status;

	private Date created_at;

	private String created_by;

	private Date updated_at;

	private String updated_by;

}
