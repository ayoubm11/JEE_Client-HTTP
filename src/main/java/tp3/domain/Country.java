package tp3.domain;

public record Country(Integer id, String nom) {
	public Country{
		if(nom == null || nom.isBlank()) {
			throw new IllegalArgumentException("le nom de pays ne peut pas etre vide");
		}
	}
	
	public Country(String nom) {
		this(null, nom);
	}

}
