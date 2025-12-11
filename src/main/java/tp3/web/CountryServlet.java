package tp3.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tp3.dao.CountryDAO;
import tp3.domain.Country;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = {"/countrise", "/countrise/add", "/countries/edit" , "/countries/delet"})
public class CountryServlet extends HttpServlet {
	private CountryDAO dao;
	
	@Override
	public void init() throws ServletException{
		String apiBaseUrl = "http://localhost:8081/TP2-JEE/api/countries";
		dao = new CountryDAO(apiBaseUrl);
	}
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/countries":
                afficherListePays(request, response);
                break;
            case "/countries/add":
                afficherFormulaireAjout(request, response);
                break;
            case "/countries/edit":
                afficherFormulaireModification(request, response);
                break;
            case "/countries/delete":
                supprimerPays(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request,  HttpServletResponse response)
    						throws ServletException, IOException{
    	String path = request.getServletPath();
    	
        switch (path) {
	        case "/countries/add":
	            ajouterPays(request, response);
	            break;
	        case "/countries/edit":
	            modifierPays(request, response);
	            break;
	        default:
	            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }   	
    }
    
    private void afficherListePays(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        List<Country> countries = dao.get();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Liste des Pays</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println("h1 { color: #333; }");
        out.println(".container { max-width: 900px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        out.println(".btn { display: inline-block; padding: 10px 20px; margin: 10px 5px; text-decoration: none; border-radius: 4px; font-weight: bold; }");
        out.println(".btn-primary { background-color: #007bff; color: white; }");
        out.println(".btn-primary:hover { background-color: #0056b3; }");
        out.println(".btn-success { background-color: #28a745; color: white; }");
        out.println(".btn-success:hover { background-color: #218838; }");
        out.println(".btn-warning { background-color: #ffc107; color: black; }");
        out.println(".btn-warning:hover { background-color: #e0a800; }");
        out.println(".btn-danger { background-color: #dc3545; color: white; }");
        out.println(".btn-danger:hover { background-color: #c82333; }");
        out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        out.println("th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
        out.println("th { background-color: #007bff; color: white; }");
        out.println("tr:hover { background-color: #f1f1f1; }");
        out.println(".actions { display: flex; gap: 10px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Liste des Pays</h1>");
        
        out.println("<a href='countries/add' class='btn btn-success'>➕ Ajouter un nouveau pays</a>");
        
        if (countries.isEmpty()) {
            out.println("<p>Aucun pays trouvé.</p>");
        } else {
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Nom du Pays</th>");
            out.println("<th>Actions</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            
            for (Country country : countries) {
                out.println("<tr>");
                out.println("<td>" + country.id() + "</td>");
                out.println("<td>" + escapeHtml(country.nom()) + "</td>");
                out.println("<td class='actions'>");
                out.println("<a href='countries/edit?id=" + country.id() + "' class='btn btn-warning'>✏️ Modifier</a>");
                out.println("<a href='countries/delete?id=" + country.id() + "' class='btn btn-danger' onclick='return confirm(\"Êtes-vous sûr de vouloir supprimer ce pays ?\");'>🗑️ Supprimer</a>");
                out.println("</td>");
                out.println("</tr>");
            }
            
            out.println("</tbody>");
            out.println("</table>");
        }
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    
    
    private void afficherFormulaireAjout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Ajouter un Pays</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        out.println("h1 { color: #333; }");
        out.println(".container { max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        out.println(".form-group { margin-bottom: 15px; }");
        out.println("label { display: block; margin-bottom: 5px; font-weight: bold; }");
        out.println("input[type='text'] { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }");
        out.println(".btn { display: inline-block; padding: 10px 20px; margin: 10px 5px; text-decoration: none; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; }");
        out.println(".btn-success { background-color: #28a745; color: white; }");
        out.println(".btn-success:hover { background-color: #218838; }");
        out.println(".btn-secondary { background-color: #6c757d; color: white; }");
        out.println(".btn-secondary:hover { background-color: #5a6268; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Ajouter un Nouveau Pays</h1>");
        
        out.println("<form method='post' action='add'>");
        out.println("<div class='form-group'>");
        out.println("<label for='nom'>Nom du pays :</label>");
        out.println("<input type='text' id='nom' name='nom' required>");
        out.println("</div>");
        
        out.println("<button type='submit' class='btn btn-success'>💾 Enregistrer</button>");
        out.println("<a href='../countries' class='btn btn-secondary'>↩️ Retour</a>");
        out.println("</form>");
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    
    
    private void afficherFormulaireModification(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect("../countries");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            Country country = dao.get(id);
            
            if (country == null) {
                out.println("<h1>Erreur : Pays non trouvé</h1>");
                out.println("<a href='../countries'>Retour à la liste</a>");
                return;
            }
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Modifier un Pays</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
            out.println("h1 { color: #333; }");
            out.println(".container { max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
            out.println(".form-group { margin-bottom: 15px; }");
            out.println("label { display: block; margin-bottom: 5px; font-weight: bold; }");
            out.println("input[type='text'] { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }");
            out.println(".btn { display: inline-block; padding: 10px 20px; margin: 10px 5px; text-decoration: none; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; }");
            out.println(".btn-warning { background-color: #ffc107; color: black; }");
            out.println(".btn-warning:hover { background-color: #e0a800; }");
            out.println(".btn-secondary { background-color: #6c757d; color: white; }");
            out.println(".btn-secondary:hover { background-color: #5a6268; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>Modifier le Pays</h1>");
            
            out.println("<form method='post' action='edit'>");
            out.println("<input type='hidden' name='id' value='" + country.id() + "'>");
            
            out.println("<div class='form-group'>");
            out.println("<label for='nom'>Nom du pays :</label>");
            out.println("<input type='text' id='nom' name='nom' value='" + escapeHtml(country.nom()) + "' required>");
            out.println("</div>");
            
            out.println("<button type='submit' class='btn btn-warning'>💾 Modifier</button>");
            out.println("<a href='../countries' class='btn btn-secondary'>↩️ Retour</a>");
            out.println("</form>");
            
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            
        } catch (NumberFormatException e) {
            response.sendRedirect("../countries");
        }
    }
    
    
    private void ajouterPays(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String nom = request.getParameter("nom");
        
        if (nom == null || nom.trim().isEmpty()) {
            response.sendRedirect("add");
            return;
        }
        
        Integer id = dao.add(nom.trim());
        
        if (id != null) {
            response.sendRedirect("../countries");
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<h1>Erreur lors de l'ajout du pays</h1>");
            out.println("<a href='add'>Réessayer</a>");
        }
    }
    
    
    private void modifierPays(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String idStr = request.getParameter("id");
        String nom = request.getParameter("nom");
        
        if (idStr == null || nom == null || nom.trim().isEmpty()) {
            response.sendRedirect("../countries");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            boolean success = dao.edit(id, nom.trim());
            
            if (success) {
                response.sendRedirect("../countries");
            } else {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<h1>Erreur lors de la modification du pays</h1>");
                out.println("<a href='edit?id=" + id + "'>Réessayer</a>");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("../countries");
        }
    }
    
    
    
    private void supprimerPays(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect("../countries");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            boolean success = dao.delete(id);
            
            response.sendRedirect("../countries");
            
        } catch (NumberFormatException e) {
            response.sendRedirect("../countries");
        }
    }
    
    
    
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }
    
    
}


























