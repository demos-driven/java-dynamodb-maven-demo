# java-dynamodb-client-maven

## 前置条件

1. 创建Amazon账号并登录 [console](https://console.aws.amazon.com/iam/home?region=us-east-2#/security_credentials)
   ，创建新的访问密钥并下载到本地

2. 安装 AWS CLI

        pip3 install aws

3. 本地部署/启动DynamoDB

       wget https://s3.ap-southeast-1.amazonaws.com/dynamodb-local-singapore/dynamodb_local_latest.tar.gz
       
       tar -zxf dynamodb_local_latest.tar.gz

       java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb

4. 使用 AWS CLI 配置访问密钥（本地开发非必须，但可为后续访问Web DynamoDB）

        aws configure
        
        # region 输入 us-east-2

        # AWSAccessKeyId, AWSSecretKey 输入已下载的密钥值（输入后会保存在  ~/.aws/credentials）

5. 使用 AWS CLI 访问本地 DynamoDB 实例

        aws dynamodb list-tables --endpoint-url http://localhost:8000

   上述命令输出如下，表示tables为空：

        {
            "TableNames": []
        }

## DynamoDB CURD

运行Application main方法:

      mvn clean compile exec:java -Dexec.mainClass="com.example.myproject.Application"