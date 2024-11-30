package com.coachweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.coachweb.model.PaymentDTO;
import com.coachweb.model.TicketDTO;
import com.coachweb.repository.AdditionRepository;
import com.coachweb.repository.SearchRepository;
import com.coachweb.repository.entity.SeatsSoldEntity;
import com.coachweb.repository.entity.TicketEntity;

import customerexception.DataNotFoundException;

@Repository
public class AdditionRepositoryImpl implements AdditionRepository {

	static final String DB_URL = "jdbc:mysql://localhost:3306/coachticketsales";
	static final String USER = "root";
	static final String PASS = "123456msql";

	@Autowired
	private SearchRepository searchRepository;

	@Override
	public void addTempCustomer(PaymentDTO paymentDTO) {
		StringBuilder sql = new StringBuilder("");
		sql.append("INSERT INTO Tam_ThongTinDatVe (maKH, maXe, ten, sdt, email, diemDi, diemDen, ngayDi, gioDi, tongTien) VALUES\r\n"
						+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

		String customerID = searchRepository.findCustomer(paymentDTO, "KhachHang");
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql.toString())) {
			if (customerID == null || customerID.isEmpty()) {
				String uuid = UUID.randomUUID().toString().substring(0, 8);
				customerID = "KH-" + uuid;
			}
			paymentDTO.setCustomerID(customerID);
			pstm.setString(1, customerID);
			pstm.setString(2, paymentDTO.getCoachID());
			pstm.setString(3, paymentDTO.getName());
			pstm.setString(4, paymentDTO.getPhone());
			pstm.setString(5, paymentDTO.getEmail() != null ? paymentDTO.getEmail() : "");
			pstm.setString(6, paymentDTO.getDeparture());
			pstm.setString(7, paymentDTO.getDestination());
			pstm.setString(8, paymentDTO.getDepartDate());
			pstm.setString(9, paymentDTO.getTimeGo());
			pstm.setDouble(10, 1.0 * paymentDTO.getSeatsID().size() * 150000);

			pstm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}
	}
	
	@Override
	public void addTempSeats(PaymentDTO paymentDTO) {
		StringBuilder sql = new StringBuilder("");
		sql.append("INSERT INTO Tam_KhachHangChoNgoi(maKH, maXe, maCN, ngayDi, gioDi) VALUES\r\n");
		for(int i = 0; i < paymentDTO.getSeatsID().size() - 1; i++) {
			sql.append("(?, ?, ?, ?, ?),\r\n");
		}
		sql.append("(?, ?, ?, ?, ?);");
		
		String customerID = searchRepository.findCustomer(paymentDTO, "khachhang");
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql.toString())) {
			for(int i = 0; i < paymentDTO.getSeatsID().size(); i++) {
				pstm.setString(1+5*i, paymentDTO.getCustomerID());
				pstm.setString(2+5*i, paymentDTO.getCoachID());
				pstm.setInt(3+5*i, paymentDTO.getSeatsID().get(i));
				pstm.setString(4+5*i, paymentDTO.getDepartDate());
				pstm.setString(5+5*i, paymentDTO.getTimeGo());
			}
			pstm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}
		
	}

	public ArrayList<Integer> findSeats(TicketDTO ticket){
		String sql = "";
		sql = "SELECT tcn.maCN FROM Tam_KhachHangChoNgoi tcn WHERE tcn.maKH = ?;\r\n";
		ArrayList<Integer> res = new ArrayList<>();
		
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql)) {
			
				pstm.setString(1, ticket.getCustomerID());
				ResultSet rs = pstm.executeQuery();

				if (!rs.next()) {
					throw new DataNotFoundException("Not found seat.");
				}
				do {
					res.add(rs.getInt("maCN"));
				} while (rs.next());

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}
		return res;
	}
	
	public TicketEntity getTicketEntity(TicketDTO ticket) {
		String sql = "";
		sql = "SELECT * FROM Tam_ThongTinDatVe tt WHERE tt.maKH = ?;\r\n";

		TicketEntity res = new TicketEntity();
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql)) {
				pstm.setString(1, ticket.getCustomerID());
				ResultSet rs = pstm.executeQuery();

				if (!rs.next()) {
					throw new DataNotFoundException("Not found seat.");
				}
				do {
					res.setTicketID(ticket.getTicketID());
					res.setCustomerID(rs.getString("maKH"));
					res.setCoachID(rs.getString("maXe"));
					res.setName(rs.getString("ten"));
					res.setPhone(rs.getString("sdt"));
					res.setEmail(rs.getString("sdt"));
					res.setDeparture(rs.getString("diemDi"));
					res.setDestination(rs.getString("diemDen"));
					res.setDepartDate(rs.getString("ngayDi"));
					res.setTimeGo(rs.getString("gioDi"));
					res.setTotalAmount(rs.getDouble("tongTien"));
					ArrayList<Integer> seats = findSeats(ticket);
					ArrayList<Integer> seatsCopy = new ArrayList<>();
					for (Integer x : seats) {
						seatsCopy.add(x);
					}
					res.setSeatsID(seatsCopy);

				} while (rs.next());

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}
		return res;
	}

	@Override
	public void addNewTicket(TicketDTO ticket) {
		String sql = "INSERT INTO Ve(maVe, maKH, maXe, maTD, ngayDi, gioDi, phuongThucThanhToan, tongTienn, trangThai) VALUES\r\n"
				+ "(?, ?, ?, ?, ?, ?, ?, ?);\r\n";

		TicketEntity te = getTicketEntity(ticket);
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setString(1, ticket.getTicketID());
			pstm.setString(2, ticket.getCustomerID());
			pstm.setString(3, te.getCoachID());
			pstm.setString(4, te.getRouteID());
			pstm.setString(5, te.getDeparture());
			pstm.setString(6, te.getTimeGo());
			pstm.setString(7, ticket.getPaymentChannel());
			pstm.setDouble(8, te.getTotalAmount());
			pstm.setString(9, "Đã thanh toán");
			pstm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}

	}

	public void addCustomerEntity(TicketEntity te) {
		String sql = "INSERT INTO KhachHang(maKH, ten, sdt, email) VALUES\r\n" + "(?, ?, ?, ?);\r\n";

		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql)) {

			pstm.setString(1, te.getCustomerID());
			pstm.setString(2, te.getName());
			pstm.setString(3, te.getPhone());
			pstm.setString(4, te.getEmail());
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}
	}

	public void addCustomerTrip(TicketEntity te) {
		String sql = "INSERT INTO KhachHangTuyenXe(maKH, maXe, maCN, ngayDi, gioDi, maTD) VALUES\r\n";
		int t = te.getSeatsID().size();
		while(--t != 0) {
				sql += "(?, ?, ?, ?, ?, ?),\r\n";
		}
		sql += "(?, ?, ?, ?, ?, ?);";
		
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement pstm = conn.prepareStatement(sql)) {
			for(int i = 0; i < te.getSeatsID().size(); i++) {
				pstm.setString(1+5*i, te.getCustomerID());
				pstm.setString(2+5*i, te.getCoachID());
				pstm.setInt(3+5*i, te.getSeatsID().get(i));
				pstm.setString(4+5*i, te.getDepartDate());
				pstm.setString(5+5*i, te.getTimeGo());
				pstm.setString(6+5*i, te.getRouteID());
			}
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failded...");
		}
	}

	@Override
	public void addNewCustomer(TicketDTO ticket) {
		TicketEntity te = getTicketEntity(ticket);
		addCustomerEntity(te);
		addCustomerTrip(te);

	}
}
