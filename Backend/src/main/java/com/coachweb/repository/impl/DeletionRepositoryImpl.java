package com.coachweb.repository.impl;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.coachweb.model.PaymentDTO;
import com.coachweb.model.TicketDTO;
import com.coachweb.repository.DeletionRepository;
import com.coachweb.repository.SearchRepository;
@Repository
public class DeletionRepositoryImpl implements DeletionRepository{
	
	@Autowired
	private SearchRepository searchRepository;
	
	static final String DB_URL = "jdbc:mysql://localhost:3306/coachticketsales";
	static final String USER = "root";
	static final String PASS = "123456msql";
	
	@Override
	public void removeCustomer(TicketDTO ticketDTO, String table) {
		StringBuilder sql = new StringBuilder("");
		sql.append("DELETE " + table + " WHERE maKH = ?;");
		
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql.toString())) {
			pstm.setString(1, ticketDTO.getCustomerID());
			
			pstm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}
	}

}
