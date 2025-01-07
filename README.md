# file-conversion-system

## Purpose

Health, finance and legal enterprises among others make use of pdf documents. Since files can be in different file formats these documents are therefore required to be standardised 
This application converts all incoming .docx documents into PDFs to meet these requirements. Additionally, the PDF versions should maintain the integrity of the original formatting and allow for features like watermarking, encryption, and digital signatures.

## Technologies 
The application makes use of an SFTP server for secure uploads of files. The files are then detected by a file watcher in the Java application which then converts files to PDF and stores the converted files in a database using MySQL with all the files metadata. All converted files are placed in an SQS queue which then stores the file to an AWS S3 bucket for the end user to view the uploaded file. Each file has a category i.e. health, finance or legal this separation by category simplifies which enterprise should view the uploaded file.


## Running the application
This application has been created using JHipster, a popular Java framework that simplifies creating a Spring Boot application with a UI.
To run the application run the following commands

Starting the frontend


```npm run webapp```
```npm start```

Starting the backend


```mvn spring-boot:run```

Testing the application to simulate a file upload


