RECIPE GENERATOR

per farlo partire, piazzarsi sulla cartella devops ed eseguire il comando

docker-compose up -d

scarica dalla repo l'immagine pubblica oracolo/recipegenerator e scarica anche mysql


DESCRIZIONE DEL PROGETTO
Ci sono solo 3 tabelle (con le rispettive entity in Java): la relazione tra ingrediente e ricetta è many to many (perchè un ingrediente può essere in più ricette,
una ricetta può avere più ingredienti), quindi la terza tabella è la classica bridge table.

Lato java, i POJO che rappresentano le tabelle sono nel package entities, mentre lo script sql che viene eseguito da liquibase una volta che parte l'applicativo è il file src/main/resources/db/creation.sql.

Gli ingredienti vengono inseriti una sola volta per evitare i duplicati.

Mi sono immaginato inoltre 3 possibili errori: la ricetta non viene trovata, viene passato un file di immagine troppo grande e ci sono troppo ingredienti nella ricetta (questi ultimi
sono configurabili tramite variabile d'ambiente.
Ci potevano essere anche altri possibili errori (es. una ricetta senza ingredienti), però non sono implementati.
Ogni errore restituisce un codice che può essere usato in maniera dinamica da un potenziale frontend per tradurre nella lingua dell'utente.

La creazione di una ricetta avviene tramite api POST che accetta una form, molto più semplice da gestire rispetto ad un JSON con il file passato come stringa da decodificare
Le api si trovano nel package rest, dentro RecipeRest.

I dati che passa il client sono trasformati in dati di Business Logic tramite i converter, allo stesso modo per restituirli (architettura esagonale https://medium.com/swlh/hexagonal-architecture-in-java-b980bfc07366).

Per ogni api c'è un test di integrazione. Quarkus semplifica la vita, configurandoti direttamente il database con test container e facendo partire l'applicazione.

COMANDI CURL DI TEST

un po' di comandi curl per test per le varie api. Da dentro /devops/curltests, si possono eseguire i seguenti:

#crea una ricetta come form, in modo da gestire meglio l'upload con la foto della ricetta
curl -X POST -H 'Content-Type: multipart/form-data' -F 'image=@softkitten.jpg' -F 'name=ricettabella' -F 'ingredients=[{\"name\":\"borraccia\"}]' http://localhost:8085/recipes

#prende tutte le ricette che hanno nome e ingredienti. Per il nome la query usa il like, per gli ingredienti il like, creando un or per tutti quelli passati e
un and tra nome e ingredienti
curl -sS 'http://localhost:8085/recipes?recipeName=ric&ingredientNames=fsdfsdsfd&ingredientNames=borrac'

#prende una ricetta per id
curl http://localhost:8085/recipes/1

#l'immagine viene passata tramite url (mi sono immaginato che un possibile frontend semplicemente metta l'url dell'immagine), in modo da restituire un json compiuto.
#L'unico modo per passare un valore binario è fare il Base64 encoding, ma non è efficiente
curl http://localhost:8085/recipes/1/image -o recipe-image.jpg

#una post che fallisce perchè l'immagine è troppo grande. Passa un error code che può essere gestito dal frontend per le traduzioni
#il parametro si può configurare come variabile d'ambiente
curl -X POST -H 'Content-Type: multipart/form-data' -F 'image=@imagetoobig.jpg' -F 'name=failtest' -F 'ingredients=[{\"name\":\"borraccia\"}]' http://localhost:8085/recipes

#una put che modifica gli ingredienti per la ricetta. Semplicemente rimpiazza quelli presenti con quelli dati
curl -X PUT -H "Content-Type: application/json" -d '@ingredients.json' http://localhost:8080/recipes/1

#DELETE, per completezza
curl -X DELETE http://localhost:8085/recipes/1