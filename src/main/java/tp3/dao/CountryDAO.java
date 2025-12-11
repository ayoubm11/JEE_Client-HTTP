package tp3.dao;

import jakarta.json.*;
import tp3.domain.Country;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class CountryDAO {
    
    private final String baseUrl;
    private final JsonClient client;
    

    public CountryDAO(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = new JsonClient();
    }
    
    /**
     * Récupère tous les pays
     * @return Liste de tous les pays
     */
    public List<Country> get() {
        List<Country> countries = new ArrayList<>();
        
        try {
            String response = client.get(baseUrl);
            
            try (JsonReader jsonReader = Json.createReader(new StringReader(response))) {
                JsonArray jsonArray = jsonReader.readArray();
                
                for (JsonValue value : jsonArray) {
                    JsonObject jsonObject = value.asJsonObject();
                    Integer id = jsonObject.getInt("id");
                    String nom = jsonObject.getString("pays");
                    countries.add(new Country(id, nom));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des pays: " + e.getMessage());
            e.printStackTrace();
        }
        
        return countries;
    }
    
    /**
     * Récupère un pays par son ID
     * @param id Identifiant du pays
     * @return Le pays trouvé ou null
     */
    public Country get(Integer id) {
        try {
            String response = client.get(baseUrl + "/" + id);
            
            try (JsonReader jsonReader = Json.createReader(new StringReader(response))) {
                JsonObject jsonObject = jsonReader.readObject();
                
                // Vérifier si c'est une erreur
                if (jsonObject.containsKey("erreur")) {
                    return null;
                }
                
                Integer countryId = jsonObject.getInt("id");
                String nom = jsonObject.getString("pays");
                return new Country(countryId, nom);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du pays " + id + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Ajoute un nouveau pays
     * @param nom Nom du pays à ajouter
     * @return L'ID du pays créé ou null en cas d'erreur
     */
    public Integer add(String nom) {
        try {
            // Créer l'objet JSON à envoyer
            JsonObject jsonInput = Json.createObjectBuilder()
                    .add("pays", nom)
                    .build();
            
            String response = client.post(baseUrl, jsonInput.toString());
            
            try (JsonReader jsonReader = Json.createReader(new StringReader(response))) {
                JsonObject jsonObject = jsonReader.readObject();
                
                // Vérifier si c'est une erreur
                if (jsonObject.containsKey("erreur")) {
                    System.err.println("Erreur API: " + jsonObject.getString("erreur"));
                    return null;
                }
                
                return jsonObject.getInt("id");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout du pays: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Modifie un pays existant
     * @param id Identifiant du pays
     * @param nouveauNom Nouveau nom du pays
     * @return true si la modification a réussi, false sinon
     */
    public boolean edit(Integer id, String nouveauNom) {
        try {
            // Créer l'objet JSON à envoyer
            JsonObject jsonInput = Json.createObjectBuilder()
                    .add("id", id)
                    .add("pays", nouveauNom)
                    .build();
            
            String response = client.put(baseUrl, jsonInput.toString());
            
            try (JsonReader jsonReader = Json.createReader(new StringReader(response))) {
                JsonObject jsonObject = jsonReader.readObject();
                
                // Vérifier si c'est une erreur
                if (jsonObject.containsKey("erreur")) {
                    System.err.println("Erreur API: " + jsonObject.getString("erreur"));
                    return false;
                }
                
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la modification du pays: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Supprime un pays
     * @param id Identifiant du pays à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean delete(Integer id) {
        try {
            String response = client.delete(baseUrl + "/" + id);
            
            try (JsonReader jsonReader = Json.createReader(new StringReader(response))) {
                JsonObject jsonObject = jsonReader.readObject();
                
                // Vérifier si c'est une erreur
                if (jsonObject.containsKey("erreur")) {
                    System.err.println("Erreur API: " + jsonObject.getString("erreur"));
                    return false;
                }
                
                // Vérifier si la suppression est confirmée
                return jsonObject.getBoolean("supprimé", false);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du pays: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}