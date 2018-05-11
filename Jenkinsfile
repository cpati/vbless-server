pipeline {
    agent any
    
    parameters {
        string(name: 'app', defaultValue: 'vbless-server', description: 'vbless docker image')
    }

    stages {
        stage('Build') {
            steps {
            		sh '''
                 /opt/maven/bin/mvn package dockerfile:build -DskipTests;
                 docker login --username=$dockeruserid --password=$dockeruserpw
                 docker tag vblessimg/vbless chidanandapati/vbless-server:v3.2;
                 docker push chidanandapati/vbless-server:v3.2;
                 '''
            }
        }
        stage('Test') {
            steps {
                sh '''
                /opt/maven/bin/mvn test
                '''
            }
        }
        stage('Deploy') {
            steps {
                sh '''
                export KUBERNETES_MASTER=http://127.0.0.1:8001
                kubectl apply -f server-deployment.yaml
                kubectl apply -f server-service.yaml
                kubectl rollout status deployment/vbless-server 
                '''
            }
        }
    }
}
