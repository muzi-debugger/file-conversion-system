# file-conversion-system

## Purpose

Health, finance and legal enterprises among others make use of pdf documents. Since files can be in different file formats these documents are therefore required to be standardised 
This application converts all incoming .docx documents into PDFs to meet these requirements. Additionally, the PDF versions should maintain the integrity of the original formatting and allow for features like watermarking, encryption, and digital signatures.

## Technologies 
The application makes use of an SFTP server for secure uploads of files. The files are then detected by a file watcher in the Java application which then converts files to PDF and stores the converted files in a database using MySQL with all the files metadata. All converted files are placed in an SQS queue which then stores the file to an AWS S3 bucket for the end user to view the uploaded file. Each file has a category i.e. health, finance or legal this separation by category simplifies which enterprise should view the uploaded file.


## Running the application
This application has been created using JHipster, a popular Java framework that simplifies creating a Spring Boot application with a UI.
To run the application run the following commands:

First clean and build a Maven-based Java project


```mvn clean install```


Once the build is complete you can run the back-end 


```mvn -P dev -Dmaven.test.skip=true spring-boot:run```


Starting the frontend

install all the dependencies listed in the package.json file for a Node.js project, build and start the front-end
```npm install```
```npm run webapp:build```
```npm start```

## Docker components
This application uses docker for the following components
- SFTP server
- AWS Services: SQS, S3 and Lambda

Using the docker file to enable connection to the SFTP server (ensure Docker desktop is running)    
Navigate to sftp\dockerfile and build the Docker image ```docker run -d --name sftp-container -p 2222:22 my-sftp```
Start a container from the image:

```docker run -d --name sftp-container -p 2222:22 sftp-server```
You can give your container a different name as you wish.

Now that the SFTP server is running you need to connect to the server. It is not ideal to use username and password to connect to the server, ideally a private key is what you would use to connect to the server but for simplicity this app uses username and password. Connect to the server using an application like FileZilla. Open FileZilla -> click file -> Site Manager -> new Site -> Protocol: SFTP - SSH File Transfer Protocol -> Host: localhost -> port: 22 -> user: user -> password: password
This will establish a new SFTP connection. A directory called upload is already created thanks to the Dockerfile. This is the directory in which you will be uploading your docx files to.

## Starting AWS Components
For the sake of making this application cost free I have chosen LocalStack to simulate the AWS services which the application uses. The AWS services have been configured in a docker-compose file.
Navigate to ```docker\localstack``` and start the container using ```docker-compose up -d```
This starts all the required AWS services for the application. The final step in the setup is to create a bucket, an SQS queue, import the lambda and create a resource mapping for the lambda to store our converted files in the S3 bucket.
For simplicity you can do this manually in a few clicks. Go to https://app.localstack.cloud/dashboard (create an account if you don't have one) and navigate to resource browser. You will see free services S3, Lambda and SQS. Make sure you're in us-west-1 region as this is where these services have been defined and create the SQS queue with the name file-generic-queue, an S3 bucket muzi-file-storage-bucket and a lambda function named lambda_function. Ensure that the handler is lambda_function.lambda_handler, role ```arn:aws:iam::000000000000:role/execution-role``` runtime runtime is python 3.13, timeout 30 seconds, drag and drop the lambda function in src\main\lambda\lambda_function.zip . Finally, let's create the resource mapping so that our lambda automatically triggers when there's a message in the SQS queue it will decode the base64 encoded string, decode the base64 encoded string and store the pdf file in an S3 bucket.

```awslocal lambda create-event-source-mapping --event-source-arn arn:aws:sqs:us-west-1:000000000000:generic-file-queue --function-name lambda_function ```


Verify the source mapping by running:
```awslocal lambda list-event-source-mappings --function-name lambda_function```


## Testing the application to simulate a file upload
Now that your application is running its time to test that our application works. Open FileZilla and upload a docx file to the 'upload' directory. This triggers the SFTPMonitor class that is polling the directory for new files. Once a file is detected it is converted to pdf and saved to the downloads directory (src\main\resources\downloads). The files are then placed on the SQS queue, you can verify that the files are in the SQS queue by checking the SQS queue for new messages and all the file's metadata should be on the SQS quueue. Finally, check the S3 bucket if a file has been stored in the S3 bucket. To verify that the file exists click on the bucket and you can download the file.

This sums up the file conversion application. I hope you enjoyed this tutorial!



