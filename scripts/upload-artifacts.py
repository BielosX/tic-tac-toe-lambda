import os
import boto3
import sys
import re

def main():
    s3 = boto3.client('s3')
    args = len(sys.argv)
    argv = sys.argv
    if args < 4:
        print("Please specify Artifacts directory, S3 Bucket and suffix")
        exit(255)
    artifacts = argv[1]
    bucket = argv[2]
    suffix = argv[3]
    compiled = re.compile(r'(.*)((\.zip$)|(\.jar$))')
    for filename in os.listdir(artifacts):
        match_result = compiled.match(filename)
        if match_result:
            no_extension = match_result.group(1)
            extension = match_result.group(2)
            s3_key = f"{no_extension}-{suffix}{extension}"
            print(f"Uploading {filename} as {s3_key} to {bucket}")
            s3.upload_file(os.path.join(artifacts, filename), bucket, s3_key)


if __name__ == '__main__':
    main()