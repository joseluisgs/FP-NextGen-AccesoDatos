conn = new Mongo();
// Nos vamos a la base de datos test,
// si no lo haría por defecto en la BD definida en el docker-compose.yml MONGO_INITDB_DATABASE
db = conn.getDB("test");
// Borramos todas las colecciones
// db.collection.drop();

// Y en la colección prueba creamos un índice e insertamos
db.prueba.createIndex({"address.zip": 1}, {unique: false});

db.prueba.insert({"address": {"city": "Paris", "zip": "123"}, "name": "Mike", "phone": "1234"});
db.prueba.insert({"address": {"city": "Marsel", "zip": "321"}, "name": "Helga", "phone": "4321"});

db = conn.getDB("tenistas");
db.dropDatabase();

// db = conn.getDB("blog");

// Si queremos crear los datos a mano

// db.category.insert({ "_id": parseInt(1), "texto": "General"});
// db.category.insert({ "_id": parseInt(2), "texto": "Dudas"});
// db.category.insert({ "_id": parseInt(3), "texto": "Evaluación"});
// db.category.insert({ "_id": parseInt(4), "texto": "Pruebas" });

// // Secuencia de los numeros
// db.hibernate_sequences.insert({ "_id": "hibernate_sequence", "seq": 5 });
