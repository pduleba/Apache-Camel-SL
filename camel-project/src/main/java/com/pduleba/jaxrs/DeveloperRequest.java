package com.pduleba.jaxrs;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.gson.Gson;

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class DeveloperRequest {
	
	private static final Date DATE_2000_01_01 = new GregorianCalendar(2000, 1, 1).getTime();

	private String firstName;
	private String lastName;
	private int age;
	private Date birth;
	private BigDecimal salary;
	private List<String> skills;

	public static void main(String[] args) {
		System.err.println(new Gson().toJson(getRequest()));
	}

	public static DeveloperRequest getRequest() {
		return new DeveloperRequest("Mike", "Johnson", 45, DATE_2000_01_01,
				BigDecimal.TEN, Arrays.asList("Java"));
	}

}
