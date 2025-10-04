# Use an official Node.js runtime as a base image
FROM node:21-alpine

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install dependencies
RUN npm install

RUN npm install mongodb

# Copy the remaining application code to the working directory
COPY . .

RUN chmod +x start.sh

# Build the React app
RUN npm run build

# Expose the port that the app will run on
EXPOSE 3000

# Command to run the application
CMD ["./start.sh"]
