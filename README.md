# Client HTTP pour API REST

## Prérequis
 **Serveur d'application** compatible Jakarta EE 11 (Tomcat 11+)
 
 **déployé JEE-Servlets-REST-API** https://github.com/ayoubm11/JEE-Servlets-REST-API.git et accessible (API REST)

## Dépendances nécessaires

Téléchargez et ajoutez dans `WEB-INF/lib/` :

1. **Jakarta JSON Processing API** (jakarta.json-api-2.1.3.jar)
2. **Parsson Implementation** (parsson-1.1.7.jar)

### Liens de téléchargement Maven Central:
- Jakarta JSON API: https://mvnrepository.com/artifact/jakarta.json/jakarta.json-api/2.1.3
- Parsson: https://mvnrepository.com/artifact/org.eclipse.parsson/parsson/1.1.7

## Structure du projet

```
TP3/
├── src/main/
   ├── java/
   │   ├── tp3/domain/
   │   │   └── Country.java
   │   ├── tp3/dao/
   │   │   ├── JsonClient.java
   │   │   └── CountryDAO.java
   │   └── tp3/web/
   │       ├── CountryServlet.java
   │       └── CharacterEncodingFilter.java
   └── webapp/
       ├── WEB-INF/
          ├── web.xml
          └── lib/
              ├── jakarta.json-api-2.1.3.jar
              └── parsson-1.1.7.jar
       

```

## Configuration

### 1. Modifier l'URL de l'API REST

Dans `CountryServlet.java`, modifiez la méthode `init()` :

```java
@Override
public void init() throws ServletException {
    // Adaptez cette URL selon votre configuration
    String apiBaseUrl = "http://localhost:8080/JEE-Servlets-REST-API/api/countries";
    dao = new CountryDAO(apiBaseUrl);
}
```

**Important**: Remplacez `/JEE-Servlets-REST-API/api/countries` par l'URL exacte de votre API REST.

### 2. Vérifier le port du serveur

Assurez-vous que :
- Le JEE-Servlets-REST-API est déployé et accessible
- Le port correspond à votre configuration Tomcat (par défaut 8080)

## Déploiement

### Option 1: Avec Eclipse

1. Clic droit sur le projet → **Run As** → **Run on Server**
2. Sélectionnez votre serveur (Tomcat, etc.)
3. Cliquez sur **Finish**

### Option 2: Avec Maven

```bash
mvn clean package
# Le fichier WAR sera généré dans target/TP3.war
# Déployez-le manuellement sur votre serveur
```

### Option 3: Manuel

1. Exportez le projet en fichier WAR
2. Copiez le WAR dans le dossier `webapps` de votre serveur
3. Démarrez le serveur

## Utilisation

### Accéder à l'application

Ouvrez votre navigateur et accédez à :

```
http://localhost:8080/...
```

### Fonctionnalités disponibles

1. **Liste des pays** : `/countries`
   - Affiche tous les pays dans un tableau HTML
   - Liens pour modifier ou supprimer chaque pays

2. **Ajouter un pays** : `/countries/add`
   - Formulaire pour ajouter un nouveau pays
   - Validation du nom

3. **Modifier un pays** : `/countries/edit?id=X`
   - Formulaire pré-rempli avec le nom actuel
   - Modification du nom

4. **Supprimer un pays** : `/countries/delete?id=X`
   - Suppression directe avec confirmation JavaScript
   - Redirection vers la liste

## Points importants

### Gestion des erreurs

- Si l'API REST n'est pas accessible, les méthodes DAO retournent `null` ou des listes vides
- Des messages d'erreur sont affichés dans la console du serveur
- L'interface affiche des messages d'erreur appropriés

### Encodage

Le filtre `CharacterEncodingFilter` garantit l'encodage UTF-8 pour :
- Les requêtes entrantes
- Les réponses sortantes
- Les noms de pays avec des caractères spéciaux

### Sécurité

- Échappement HTML pour éviter les injections XSS
- Validation des paramètres d'URL
- Confirmation JavaScript avant suppression

## Troubleshooting

### Problème: "Pays non trouvé" ou liste vide

**Solution**: Vérifiez que :
1. Le TP2 est déployé et accessible
2. L'URL de l'API est correcte dans `CountryServlet.init()`
3. La base de données Sakila contient des données

### Problème: Erreur 404

**Solution**: 
- Vérifiez que l'URL correspond au pattern de la servlet
- Utilisez `/countries` et non `/country`

### Problème: Caractères spéciaux mal affichés

**Solution**:
- Vérifiez que le `CharacterEncodingFilter` est actif
- Ajoutez `<meta charset='UTF-8'>` dans vos pages HTML

### Problème: Connexion refusée

**Solution**:
1. Assurez-vous que le JEE-Servlets-REST-API est démarré
2. Vérifiez le port dans l'URL de l'API
3. Testez l'API directement avec Postman

## Test de l'application

### Test complet

1. **Lister** : Accédez à `/countries` → tous les pays s'affichent
2. **Ajouter** : Cliquez sur "Ajouter" → remplissez le formulaire → validez
3. **Vérifier** : Le nouveau pays apparaît dans la liste
4. **Modifier** : Cliquez sur "Modifier" → changez le nom → validez
5. **Vérifier** : Le nom est mis à jour
6. **Supprimer** : Cliquez sur "Supprimer" → confirmez
7. **Vérifier** : Le pays a disparu de la liste

### Tests avec différents navigateurs

Testez sur :
- Chrome / Edge
- Firefox ...


## Notes

- Le projet utilise des **Records Java** (Java 14+) pour les objets immutables
- Compatible avec **Jakarta EE 11** et **Java 17**
- Suit le pattern **MVC** (Model-View-Controller)
- Suit le pattern **DAO** (Data Access Object)
