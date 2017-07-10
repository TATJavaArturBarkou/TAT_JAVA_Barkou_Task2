package by.epam.barkou.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import by.epam.barkou.bean.Book;
import by.epam.barkou.dao.IBookDAO;
import by.epam.barkou.dao.exception.DAOException;
import by.epam.barkou.dao.newUtil.SQLConnection;

public class SQLBookDAO implements IBookDAO {
	
	private final static String SQL_ADD_BOOK = "insert into books (name) values (?)";
	private final static String SQL_UPDATE_BOOK = "update books set name=?, availability=? where id=?";
	private static final String GET_ALL_AVAILABLE_BOOKS = "SELECT id, name, availability FROM books where availability=1";
	
	private SQLConnection sqlConnection = SQLConnection.getInstance();

	@Override
	public void addBook(Book book) throws DAOException {

		Connection connection = sqlConnection.getConnection();
		PreparedStatement ps = null;
		int bookNameIndex = 1;
		
		try {
			ps = connection.prepareStatement(SQL_ADD_BOOK);
			ps.setString(bookNameIndex, book.getName());

			ps.executeUpdate();

		} catch (SQLException e) {

			throw new DAOException(e);

		} finally {
			if (ps != null || connection != null) {
				try {
					ps.close();
					connection.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
		}

	}

	@Override
	public void updateBook(Book book) throws DAOException {

		Connection connection = sqlConnection.getConnection();
		PreparedStatement ps = null;
		int bookNameIndex = 1;
		int bookavailabilityIndex = 2;
		int bookIdIndex = 3;

		try {
			ps = connection.prepareStatement(SQL_UPDATE_BOOK);

			int availability = Integer.parseInt(book.getAvailability());
			int id = Integer.parseInt(book.getIdBook());

			ps.setString(bookNameIndex, book.getName());
			ps.setInt(bookavailabilityIndex, availability);
			ps.setInt(bookIdIndex, id);

			ps.executeUpdate();

		} catch (SQLException e) {

			throw new DAOException(e);

		} finally {
			if (ps != null || connection != null) {
				try {
					ps.close();
					connection.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
		}

	}

	@Override
	public ArrayList<Book> getAllAvailableBooks() throws DAOException {
		Connection connection = sqlConnection.getConnection();
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		ArrayList<Book> booksList = new ArrayList<Book>();
		Book book;
		String bookName = "name";
		
		try {
			ps = connection.prepareStatement(GET_ALL_AVAILABLE_BOOKS);

			resultSet = ps.executeQuery();
			
			while (resultSet.next()) {

				book = new Book(resultSet.getString(bookName));
				booksList.add(book);
			}
			
		} catch (SQLException e) {

			throw new DAOException(e);

		} finally {
			if (ps != null || connection != null) {
				try {
					resultSet.close();
					ps.close();
					connection.close();
				} catch (SQLException e) {
					throw new DAOException(e);
				}
			}
		}
		return booksList;
	}

}
