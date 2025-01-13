import boto3
import base64
import json
import logging
from botocore.exceptions import ClientError

# Initialize S3 client
s3_client = boto3.client('s3')

# Initialize logger
logger = logging.getLogger()
logger.setLevel(logging.INFO)

def lambda_handler(event, context):
    bucket_name = "muzi-file-storage-bucket"  

    # Process each record in the event
    for record in event.get('Records', []):
        try:
            # Parse the body from the record
            body = json.loads(record['body'])
            file_name = body.get('fileName')
            base64_content = body.get('base64EncodedContent')

            # Validate fields
            if not file_name or not base64_content:
                logger.warning(f"Missing fileName or base64EncodedContent in message: {body}")
                continue

            # Decode Base64 content
            try:
                file_content = base64.b64decode(base64_content)
            except Exception as decode_error:
                logger.error(f"Failed to decode Base64 content for file '{file_name}': {decode_error}")
                continue

            # Upload to S3
            try:
                s3_client.put_object(Bucket=bucket_name, Key=file_name, Body=file_content)
                logger.info(f"File '{file_name}' successfully uploaded to bucket '{bucket_name}'.")
            except ClientError as e:
                logger.error(f"Failed to upload file '{file_name}' to S3: {e}")
                continue

        except Exception as e:
            logger.error(f"Error processing record: {e}")
            continue

    return {
        "statusCode": 200,
        "body": json.dumps({"message": "Processing complete"})
    }
