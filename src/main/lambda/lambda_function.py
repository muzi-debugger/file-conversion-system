import boto3
import base64
import json

# Initialize S3 client
s3_client = boto3.client('s3', endpoint_url="http://localhost:4566")  # LocalStack endpoint

# Lambda handler
def lambda_handler(event, context):
    bucket_name = "file-storage-bucket"

    # Ensure the bucket exists
    try:
        s3_client.create_bucket(Bucket=bucket_name)
        print(f"Bucket '{bucket_name}' created.")
    except Exception as e:
        print(f"Bucket may already exist or error occurred: {str(e)}")

    for record in event['Records']:
        # Parse SQS message
        body = json.loads(record['body'])
        file_name = body['fileName']
        base64_content = body['base64EncodedContent']

        # Decode the Base64 content
        try:
            file_content = base64.b64decode(base64_content)
        except Exception as decode_error:
            print(f"Failed to decode Base64 content for file {file_name}: {decode_error}")
            continue

        # Upload the decoded content to S3
        try:
            s3_client.put_object(Bucket=bucket_name, Key=file_name, Body=file_content)
            print(f"File '{file_name}' successfully uploaded to bucket '{bucket_name}'.")
        except Exception as upload_error:
            print(f"Failed to upload file {file_name} to S3: {upload_error}")
            continue

    return {"statusCode": 200, "body": "Processing complete"}
