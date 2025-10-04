const fs = require('fs');
const path = require('path');
const { GridFSBucket, MongoClient } = require('mongodb');

// Connect to MongoDB
const mongoURI = 'mongodb://root:example@mongodb:27017/myDatabase?authSource=admin'; // Change this to your MongoDB URI

async function insertFile(filePath) {
    try {
        const client = await MongoClient.connect(mongoURI, { useNewUrlParser: true, useUnifiedTopology: true });
        const db = client.db();
        const bucket = new GridFSBucket(db);

        const fileName = path.basename(filePath);
        const readableStream = fs.createReadStream(filePath);

        const uploadStream = bucket.openUploadStream(fileName);
        const id = uploadStream.id;

        readableStream.pipe(uploadStream);

        uploadStream.on('error', (error) => {
            console.log(`Error uploading file ${fileName}: ${error}`);
        });

        uploadStream.on('finish', () => {
            console.log(`File ${fileName} uploaded successfully with id: ${id}`);
            client.close();
        });
    } catch (error) {
        console.error('Error inserting file:', error);
    }
}

// Example usage:
const filePath = './documents/sample.pdf'; // Change this to the path of your file
insertFile(filePath);
