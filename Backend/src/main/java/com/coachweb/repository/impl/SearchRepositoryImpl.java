package com.coachweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.coachweb.model.BookingDTO;
import com.coachweb.model.PaymentDTO;
import com.coachweb.model.TicketDTO;
import com.coachweb.repository.SearchRepository;
import com.coachweb.repository.entity.SeatsSoldEntity;
@Repository
public class SearchRepositoryImpl implements SearchRepository{

	static final String DB_URL = "jdbc:mysql://localhost:3306/coachticketsales";
	static final String USER = "root";
	static final String PASS = "123456msql";
	
	@Override
	public String findCustomer(PaymentDTO paymentDTO, String table) {
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT tb.maKH\r\n"
				+ "FROM " + table + " tb\r\n"
				+ "WHERE tb.ten = ? and tb.sdt = ?;");
		String res = "";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql.toString())) {
			pstm.setString(1, paymentDTO.getName());
			pstm.setString(2, paymentDTO.getPhone());
			ResultSet rs = pstm.executeQuery();
			
			if(rs.next()) {
			res = rs.getString("maKH");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}
		return res;
	}

	@Override
	public List<SeatsSoldEntity> findSeatsSold(BookingDTO bookingDTO, ArrayList<Integer> seatsID, String timeGo) {
	    if (bookingDTO == null || seatsID == null || seatsID.isEmpty()) {
	        throw new IllegalArgumentException("BookingDTO or seatsID cannot be null or empty.");
	    }
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT khtx.maXe, khtx.maCN, khtx.gioDi\r\n"
				+"FROM KhachHangTuyenXe khtx\r\n"
				+ "INNER JOIN TuyenDuong td\r\n"
				+ "ON khtx.maTD = td.maTD\r\n"
				+ "WHERE khtx.ngayDi = ?\r\n"
				+ "AND td.diemDi = ? AND td.diemDen = ? AND khtx.gioDi = ? ");
		sql.append("AND khtx.maCN in (");
		for (int i = 0; i < seatsID.size(); i++) {
		    sql.append("?");
		    if (i < seatsID.size() - 1) {
		        sql.append(", ");
		    }
		}
		sql.append(");");
		
		List<SeatsSoldEntity> res = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql.toString())) {
			
			StringBuilder departDate = new StringBuilder(bookingDTO.getDepartDate().trim());
			departDate.insert(4, '-');
			departDate.insert(7, '-');
			pstm.setString(1, departDate.toString().trim());
			pstm.setString(2, bookingDTO.getDeparture().trim());
			pstm.setString(3, bookingDTO.getDestination().trim());
			pstm.setString(4, timeGo.trim());
			
		    int paramIndex = 5;
		    for (Integer seatID : seatsID) {
		        pstm.setInt(paramIndex++, seatID);
		    }
			ResultSet rs = pstm.executeQuery();
			if(!rs.next()) {
				return res;
			}
			
			do{
				SeatsSoldEntity seatsSold = new SeatsSoldEntity();
				seatsSold.setCoachID(rs.getString("maXe"));
				seatsSold.setSeatID(rs.getInt("maCN"));
				seatsSold.setTimeGo(rs.getString("gioDi"));
				res.add(seatsSold);
			}while(rs.next());

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}

		for(SeatsSoldEntity e: res) {
			System.out.println(e.getSeatID() + " " + e.getTimeGo());
		}
		return res;
	}

}
