package app.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.dao.PlacesDAO;
import app.model.Place;



/**
 * Servlet implementation class PlaceServlet
 */
@WebServlet("/")
public class PlaceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PlacesDAO placesDAO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlaceServlet() {
        this.placesDAO = new PlacesDAO();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/add-home":
				showNewForm(request, response);
				break;
				
			case "/insert":
				insertPlace(request, response);
				break;
			
			case "/edit":
				showEditForm(request, response);
				break;
			
			case "/update":
				updatePlace(request, response);
				break;
			
			case "/delete":
				deletePlace(request, response);
				break;
				
			default:
				listPlaces(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}	
		
		
	}
	
	
	
	private void listPlaces(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Place> listPlace = placesDAO.selectAllPlaces();
		request.setAttribute("listPlace", listPlace);
		RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
		dispatcher.forward(request, response);
	}
	
	
	
	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("add-home.jsp");
		dispatcher.forward(request, response);
	}
	
	
	private void insertPlace(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		String name = request.getParameter("name");
		String price = request.getParameter("price");
		String image = request.getParameter("image");
		String rating = request.getParameter("rating");
		Place newPlace = new Place(name, image, price, rating);
		placesDAO.insertPlaces(newPlace);
		response.sendRedirect("home");
	}
	
	
	
	
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Place existingPlace = placesDAO.selectPlace(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("edit-home.jsp");
		request.setAttribute("place", existingPlace);
		dispatcher.forward(request, response);

	}
	
	
	
	private void updatePlace(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String price = request.getParameter("price");
		String image = request.getParameter("image");
		String rating = request.getParameter("rating");
		
		System.out.println(id);
		System.out.println(name);
		System.out.println(price);
		System.out.println(image);
		System.out.println(rating);

		Place book = new Place(id, name, price, image, rating);
		placesDAO.updatePlace(book);
		response.sendRedirect("home");
	}
	
	
	
	
	private void deletePlace(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		placesDAO.deletePlace(id);
		response.sendRedirect("home");

	}
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
