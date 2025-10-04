function insertFile(filePath) {
  const fs = require('fs');
  const path = require('path');
  const { GridFSBucket, MongoClient } = require('mongodb');

  const mongoURI = 'mongodb://localhost:27017/myDatabase'; // Change this to your MongoDB URI

  MongoClient.connect(mongoURI, { useNewUrlParser: true, useUnifiedTopology: true }, (err, client) => {
      if (err) {
          console.error('Error connecting to MongoDB:', err);
          return;
      }

      const db = client.db();
      const bucket = new GridFSBucket(db);

      const fileName = path.basename(filePath);
      const readableStream = fs.createReadStream(filePath);

      const uploadStream = bucket.openUploadStream(fileName);
      const id = uploadStream.id;

      readableStream.pipe(uploadStream);

      uploadStream.on('error', (error) => {
          console.error(`Error uploading file ${fileName}: ${error}`);
          client.close();
      });

      uploadStream.on('finish', () => {
          console.log(`File ${fileName} uploaded successfully with id: ${id}`);
          client.close();
      });
  });
}
 // Create or use a specific database
  var dbName = "demo";
  var db = db.getSiblingDB(dbName);
  // Create a collection
  var collectionName = "roles";
  db.createCollection(collectionName);

  // Insert test data into the collection
  db.roles.insertMany([
    { name: "ROLE_USER" },
    { name: "ROLE_MODERATOR" },
    { name: "ROLE_ADMIN" },
  ])

  // insertFile('./files/sample.pdf');
  // insertFile('./files/sample.txt');
  print("Sample data inserted successfully.");
