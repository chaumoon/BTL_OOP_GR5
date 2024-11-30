package com.coachweb.repository.impl;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Hashtable;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.coachweb.model.BookingDTO;
import com.coachweb.repository.BookingRepository;
import com.coachweb.repository.entity.BookingEntity;
import com.coachweb.repository.entity.SeatEntity;
import com.coachweb.repository.entity.SeatsSoldEntity;

import customerexception.DataNotFoundException;

@Repository
public class BookingRepositoryImpl implements BookingRepository {

	static final String DB_URL = "jdbc:mysql://localhost:3306/coachticketsales";
	static final String USER = "root";
	static final String PASS = "123456msql";

	@Override
	public List<SeatsSoldEntity> findSeatsSold(BookingDTO bookingDTO) {
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT khtx.maXe, khtx.maCN, khtx.gioDi\r\n"
				+ "FROM KhachHangTuyenXe khtx\r\n"
				+ "INNER JOIN TuyenDuong td\r\n"
				+ "ON td.maTD = khtx.maTD\r\n"
				+ "WHERE khtx.ngayDi = ?\r\n"
				+ "AND td.diemDi = ? AND td.diemDen = ? ;");
		
		List<SeatsSoldEntity> res = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql.toString())) {
			StringBuilder departDate = new StringBuilder(bookingDTO.getDepartDate().trim());
			departDate.insert(4, '-');
			departDate.insert(7, '-');
			pstm.setString(1, departDate.toString());
			pstm.setString(2, bookingDTO.getDeparture().trim());
			pstm.setString(3, bookingDTO.getDestination().trim());

			ResultSet rs = pstm.executeQuery();
			if(!rs.next()) {
				return res;
			}
			do {
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

		return res;
	}
	
	
	
	@Override
	public List<SeatEntity> findSeats(){
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ChoNgoi;");
		
		List<SeatEntity> res = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery(sql.toString());){
			if(!rs.next()) {
				return res;
			}
			
			do {
				SeatEntity se = new SeatEntity(rs.getInt("maCN"), rs.getDouble("giaTien"), 0);
				res.add(se);
			}while(rs.next());
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}
		
		return res;
	}
	
	@Override
	public List<BookingEntity> findAll(BookingDTO bookingDTO) {
		
		String sql = "SELECT lt.maXe, kg.gioDi, kg.gioDen\r\n"
				+ "FROM KhungGio kg\r\n"
				+ "INNER JOIN TuyenDuong td\r\n"
				+ "ON kg.maTD = td.maTD\r\n"
				+ "INNER JOIN LichTrinh lt\r\n"
				+ "ON kg.maKG = lt.maKG\r\n"
				+ "WHERE td.diemDi = ? AND td.diemDen = ? ";
		
		LocalTime currentTime = LocalTime.now();
		String timeNow = currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond();
		LocalDate currentDate = LocalDate.now();
		String dateNow = currentDate.getYear() + "-" + currentDate.getMonthValue() + "-" + currentDate.getDayOfMonth();
		StringBuilder departDate = new StringBuilder(bookingDTO.getDepartDate());
		departDate.insert(4, '-');
		departDate.insert(7, '-');
		String condition = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			LocalDate dateN = LocalDate.parse(dateNow.trim(), formatter);
			LocalDate dDate = LocalDate.parse(departDate.toString().trim(), formatter);
			if(dDate.isAfter(dateN)) {
				condition = "and 1 = 1;";
			}
			else if(dDate.isBefore(dateN)) {
				throw new DataNotFoundException("Not found trip");
			}
			else {
				condition = "and kg.gioDi > ?;";
			}
			
		}catch(DateTimeParseException e) {
			System.out.println("May be invalid date format.");
			e.printStackTrace();
		}
		
		
		List<BookingEntity> res = new ArrayList<>();
		sql += condition;
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql.toString())) {
			pstm.setString(1, bookingDTO.getDeparture().replaceAll("-", " ").trim());
			pstm.setString(2, bookingDTO.getDestination().replaceAll("-", " ").trim());
			
			if(!condition.equals("and 1 = 1;")) {
				pstm.setString(3, timeNow);
			}
			ResultSet rs = pstm.executeQuery();
			
			if(!rs.next()) {
				throw new DataNotFoundException("Not found data");
			}
			do {
				BookingEntity booking = new BookingEntity();
				booking.setCoachID(rs.getString("maXe"));
				booking.setTimeGo(rs.getString("gioDi"));
				booking.setTimeCome(rs.getString("gioDen"));
				booking.setSeatsLeft(28);
				res.add(booking);
			}while (rs.next());

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}
		return res;
	}


}
